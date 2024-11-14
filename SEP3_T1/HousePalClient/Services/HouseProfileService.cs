using System.Net.Http.Headers;
using DTOs.HouseProfile;
using Newtonsoft.Json;

namespace Services;

public class HouseProfileService : IHouseProfileService
{
      private readonly HttpClient _httpClient;

        public HouseProfileService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<HouseProfileDto> AddAsync(CreateHouseProfileDto houseProfile)
        {
            var convertedHouseProfile = JsonConvert.SerializeObject(houseProfile);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseProfile);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/HouseProfile", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public async Task<HouseProfileDto> UpdateAsync(HouseProfileDto houseProfile)
        {
            var convertedHouseProfile = JsonConvert.SerializeObject(houseProfile);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseProfile);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseProfile/{houseProfile.Id}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseProfile/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseProfileDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseProfile/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public IQueryable<HouseProfileDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseProfile = JsonConvert.DeserializeObject<List<HouseProfileDto>>(jsonResponse);

            return houseProfile.AsQueryable();
        }
        
    
}