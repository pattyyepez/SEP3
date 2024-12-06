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
            
            response.EnsureSuccessStatusCode();
    
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
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseSitterDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseSitter/DeleteHouseSitter/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseSitterDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseSitter/GetHouseSitter/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseSitterDto>(jsonResponse);
        }

        public IQueryable<HouseSitterDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseSitter/GetAllHouseSitters").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseSitter = JsonConvert.DeserializeObject<List<HouseSitterDto>>(jsonResponse);

            return houseSitter.AsQueryable();
        }
        
        public IQueryable<String> GetAllSkills()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseSitter/GetAllSkills").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills.AsQueryable();
        }
}