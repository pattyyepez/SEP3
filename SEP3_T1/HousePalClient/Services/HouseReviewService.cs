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
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/HouseReview/Create", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }
        
        public async Task<HouseReviewDto> UpdateAsync(UpdateHouseReviewDto houseReview)
        {
            var convertedHouseReview = JsonConvert.SerializeObject(houseReview);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseReview);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync("https://localhost:7134/api/HouseReview/Update", byteContent);
            
            response.EnsureSuccessStatusCode();
        
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }

        public async Task DeleteAsync(int profileId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseReview/Delete/{profileId}/{sitterId}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseReviewDto> GetSingleAsync(int profileId, int sitterId, bool includeProfile, bool includeSitter)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseReview/Get/{profileId}/{sitterId}?includeProfile={includeProfile}&includeSitter={includeSitter}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }

        public IQueryable<HouseReviewDto> GetAllReviewsForProfile(int profileId)
        { 
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseReview/GetAllForProfile/{profileId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseReview = JsonConvert.DeserializeObject<List<HouseReviewDto>>(jsonResponse);

            return houseReview.AsQueryable();
        }

        public IQueryable<HouseReviewDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseReview/GetAll").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseReview = JsonConvert.DeserializeObject<List<HouseReviewDto>>(jsonResponse);

            return houseReview.AsQueryable();
        }
        
    
}