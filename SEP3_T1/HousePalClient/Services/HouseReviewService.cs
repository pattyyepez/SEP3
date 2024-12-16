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
            
                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error creating HouseReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
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
            
                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error updating HouseReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }

        public async Task DeleteAsync(int profileId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseReview/Delete/{profileId}/{sitterId}");
            
                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error deleting HouseReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        }

        public async Task<HouseReviewDto> GetSingleAsync(int profileId, int sitterId, bool includeProfile, bool includeSitter)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseReview/Get/{profileId}/{sitterId}?includeProfile={includeProfile}&includeSitter={includeSitter}");

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting single HouseReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseReviewDto>(jsonResponse);
        }

        public IQueryable<HouseReviewDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseReview/GetAll").Result;

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseReviews: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseReview = JsonConvert.DeserializeObject<List<HouseReviewDto>>(jsonResponse);

            return houseReview.AsQueryable();
        }
}