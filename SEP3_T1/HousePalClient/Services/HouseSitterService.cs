using System.Net.Http.Headers;
using DTOs.HouseSitter;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services;

public class HouseSitterService : IHouseSitterService
{
      private readonly HttpClient _httpClient;

        public HouseSitterService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<HouseSitterDto> AddAsync(CreateHouseSitterDto houseSitter)
        {
            var convertedHouseSitter = JsonConvert.SerializeObject(houseSitter);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseSitter);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/HouseSitter/CreateHouseSitter", byteContent);
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error creating HouseSitter: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseSitterDto>(jsonResponse);
        }

        public async Task<HouseSitterDto> UpdateAsync(UpdateHouseSitterDto houseSitter)
        {
            var convertedHouseSitter = JsonConvert.SerializeObject(houseSitter);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseSitter);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseSitter/UpdateHouseSitter/{houseSitter.UserId}", byteContent);
            
                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error updating HouseSitter: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseSitterDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseSitter/DeleteHouseSitter/{id}");
            
                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error deleting HouseSitter: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        }

        public async Task<HouseSitterDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseSitter/GetHouseSitter/{id}");

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting single HouseSitter: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseSitterDto>(jsonResponse);
        }

        public IQueryable<HouseSitterDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseSitter/GetAllHouseSitters").Result;

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseSitters: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseSitter = JsonConvert.DeserializeObject<List<HouseSitterDto>>(jsonResponse);

            return houseSitter.AsQueryable();
        }
        
        public IQueryable<String> GetAllSkills()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseSitter/GetAllSkills").Result;

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseSitter skills: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills.AsQueryable();
        }
}