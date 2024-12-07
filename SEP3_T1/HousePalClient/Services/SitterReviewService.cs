using System.Net.Http.Headers;
using DTOs.SitterReview;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services;

public class SitterReviewService : ISitterReviewService
{
      private readonly HttpClient _httpClient;

        public SitterReviewService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<SitterReviewDto> AddAsync(CreateSitterReviewDto sitterReview)
        {
            var convertedSitterReview = JsonConvert.SerializeObject(sitterReview);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedSitterReview);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/SitterReview/Create", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse)!;
        }

        public async Task<SitterReviewDto> UpdateAsync(UpdateSitterReviewDto sitterReview)
        {
            var convertedSitterReview = JsonConvert.SerializeObject(sitterReview);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedSitterReview);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/SitterReview/Update", byteContent);
            
            response.EnsureSuccessStatusCode();
        
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public async Task DeleteAsync(int ownerId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/SitterReview/Delete/{ownerId}/{sitterId}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<SitterReviewDto> GetSingleAsync(int ownerId, int sitterId, bool includeOwner, bool includeSitter)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/Get/{ownerId}/{sitterId}?includeOwner={includeOwner}&includeSitter={includeSitter}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public IQueryable<SitterReviewDto> GetAll(bool includeOwner, bool includeSitter)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/GetAll?includeOwner={includeOwner}&includeSitter={includeSitter}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var sitterReview = JsonConvert.DeserializeObject<List<SitterReviewDto>>(jsonResponse);

            return sitterReview.AsQueryable();
        }

        public IQueryable<SitterReviewDto> GetAllReviewsForSitter(int sitterId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/GetAllForSitter/{sitterId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var sitterReview = JsonConvert.DeserializeObject<List<SitterReviewDto>>(jsonResponse);

            return sitterReview.AsQueryable();
        }
}