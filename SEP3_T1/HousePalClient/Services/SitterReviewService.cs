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
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error creating SitterReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
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
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error updating SitterReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public async Task DeleteAsync(int ownerId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/SitterReview/Delete/{ownerId}/{sitterId}");
            
                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error deleting SitterReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        }

        public async Task<SitterReviewDto> GetSingleAsync(int ownerId, int sitterId, bool includeOwner, bool includeSitter)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/Get/{ownerId}/{sitterId}?includeOwner={includeOwner}&includeSitter={includeSitter}");

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting single SitterReview: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<SitterReviewDto>(jsonResponse);
        }

        public IQueryable<SitterReviewDto> GetAll(bool includeOwner, bool includeSitter)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/GetAll?includeOwner={includeOwner}&includeSitter={includeSitter}").Result;

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all SitterReviews: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var sitterReview = JsonConvert.DeserializeObject<List<SitterReviewDto>>(jsonResponse);

            return sitterReview!.AsQueryable();
        }

        public IQueryable<SitterReviewDto> GetAllReviewsForSitter(int sitterId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/SitterReview/GetAllForSitter/{sitterId}").Result;

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all SitterReviews by sitter id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var sitterReview = JsonConvert.DeserializeObject<List<SitterReviewDto>>(jsonResponse);

            return sitterReview!.AsQueryable();
        }
}