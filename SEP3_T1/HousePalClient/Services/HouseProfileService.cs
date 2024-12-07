using System.Net.Http.Headers;
using DTOs.HouseProfile;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services;

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
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/HouseProfile/CreateHouseProfile", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public async Task<HouseProfileDto> UpdateAsync(UpdateHouseProfileDto houseProfile)
        {
            var convertedHouseProfile = JsonConvert.SerializeObject(houseProfile);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseProfile);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseProfile/UpdateHouseProfile/{houseProfile.Id}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseProfile/DeleteHouseProfile/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseProfileDto> GetSingleAsync(int id, bool includeOwner)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseProfile/GetHouseProfile/{id}?includeOwner={includeOwner}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public IQueryable<HouseProfileDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllHouseProfiles?includeOwner=true").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseProfile = JsonConvert.DeserializeObject<List<HouseProfileDto>>(jsonResponse);

            return houseProfile.AsQueryable();
        }
        
        public IQueryable<HouseProfileDto> GetAllByOwner(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseProfile/GetProfilesByOwner/OwnerId?ownerId={ownerId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseProfile = JsonConvert.DeserializeObject<List<HouseProfileDto>>(jsonResponse);

            return houseProfile.AsQueryable();
        }
        
        public IQueryable<string> GetAllChores()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllChores").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills.AsQueryable();
        }
        
        public IQueryable<string> GetAllRules()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllRules").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills.AsQueryable();
        }
        
        public IQueryable<string> GetAllAmenities()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllAmenities").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills.AsQueryable();
        }
}