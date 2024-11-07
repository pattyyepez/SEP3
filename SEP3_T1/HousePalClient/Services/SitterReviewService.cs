using System.Net.Http.Headers;
using DTOs.SitterReview;
using Newtonsoft.Json;

namespace Services;

public class SitterReviewService : ISitterReviewService
{
      private readonly HttpClient _httpClient;

        public SitterReviewService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<SitterReviewDto> AddAsync(SitterReviewDto sitterReview)
        {
            var convertedSitterReview = JsonConvert.SerializeObject(sitterReview);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedSitterReview);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/SitterReview", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public async Task<SitterReviewDto> UpdateAsync(SitterReviewDto sitterReview)
        {
            var convertedSitterReview = JsonConvert.SerializeObject(sitterReview);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedSitterReview);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/SitterReview/{sitterReview.Id}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/SitterReview/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<SitterReviewDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public IQueryable<SitterReviewDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/SitterReview").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var sitterReview = JsonConvert.DeserializeObject<List<SitterReviewDto>>(jsonResponse);

            return sitterReview.AsQueryable();
        
    }
}