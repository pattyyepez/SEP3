using System.Net.Http.Headers;
using DTOs.HouseReview;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services;

public class HouseReviewService : IHouseReviewService
{
      private readonly HttpClient _httpClient;

        public HouseReviewService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<HouseReviewDto> AddAsync(CreateHouseReviewDto houseReview)
        {
            var convertedHouseReview = JsonConvert.SerializeObject(houseReview);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseReview);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/HouseReview/CreateHouseReview", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }

        // public async Task<HouseReviewDto> UpdateAsync(HouseReviewDto houseReview)
        // {
        //     var convertedHouseReview = JsonConvert.SerializeObject(houseReview);
        //     var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseReview);
        //     var byteContent = new ByteArrayContent(buffer);
        //     byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
        //     
        //     using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseReview/{houseReview.Id}", byteContent);
        //     
        //     response.EnsureSuccessStatusCode();
        //
        //     var jsonResponse = await response.Content.ReadAsStringAsync();
        //     Console.WriteLine($"{jsonResponse}\n");
        //     return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        // }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseReview/DeleteHouseReview/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseReviewDto> GetSingleAsync(int id, bool includeProfile, bool includeSitter)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseReview/GetHouseReview/{id}?includeProfile={includeProfile}&includeSitter={includeSitter}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }

        public IQueryable<HouseReviewDto> GetAllReviewsForProfile(int profileId)
        { 
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseReview/GetAllReviewsForProfile/{profileId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseReview = JsonConvert.DeserializeObject<List<HouseReviewDto>>(jsonResponse);

            return houseReview.AsQueryable();
        }

        public IQueryable<HouseReviewDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseReview/GetAllHouseReviews").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseReview = JsonConvert.DeserializeObject<List<HouseReviewDto>>(jsonResponse);

            return houseReview.AsQueryable();
        }
        
    
}