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

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error creating HouseProfile: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse)!;
        }

        public async Task<HouseProfileDto> UpdateAsync(UpdateHouseProfileDto houseProfile)
        {
            var convertedHouseProfile = JsonConvert.SerializeObject(houseProfile);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseProfile);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseProfile/UpdateHouseProfile/{houseProfile.Id}", byteContent);
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error updating HouseProfile: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseProfile/DeleteHouseProfile/{id}");
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error deleting HouseProfile: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        }

        public async Task<HouseProfileDto> GetSingleAsync(int id, bool includeOwner)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseProfile/GetHouseProfile/{id}?includeOwner={includeOwner}");

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error getting a single HouseProfile: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseProfileDto>(jsonResponse);
        }

        public IQueryable<HouseProfileDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllHouseProfiles?includeOwner=true").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseProfiles: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseProfile = JsonConvert.DeserializeObject<List<HouseProfileDto>>(jsonResponse);

            return houseProfile!.AsQueryable();
        }
        
        public IQueryable<HouseProfileDto> GetAllByOwner(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseProfile/GetProfilesByOwner/OwnerId?ownerId={ownerId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseProfiles by owner id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseProfile = JsonConvert.DeserializeObject<List<HouseProfileDto>>(jsonResponse);

            return houseProfile!.AsQueryable();
        }
        
        public IQueryable<string> GetAllChores()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllChores").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseProfile chores: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills!.AsQueryable();
        }
        
        public IQueryable<string> GetAllRules()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllRules").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseProfile rules: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills!.AsQueryable();
        }
        
        public IQueryable<string> GetAllAmenities()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseProfile/GetAllAmenities").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseProfile amenities: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var skills = JsonConvert.DeserializeObject<List<string>>(jsonResponse);

            return skills!.AsQueryable();
        }
}