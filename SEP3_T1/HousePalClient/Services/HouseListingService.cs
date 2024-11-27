using System.Net.Http.Headers;
using DTOs.HouseListing;
using DTOs.HouseSitter;
using Newtonsoft.Json;

namespace Services;

public class HouseListingService : IHouseListingService
{
      private readonly HttpClient _httpClient;

        public HouseListingService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<HouseListingDto> AddAsync(
            CreateHouseListingDto houseListing)
        {
            var convertedHouseListing =
                JsonConvert.SerializeObject(houseListing);
            var buffer =
                System.Text.Encoding.UTF8.GetBytes(convertedHouseListing);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType =
                new MediaTypeHeaderValue("application/json");

            using HttpResponseMessage response =
                await _httpClient.PostAsync(
                    "https://localhost:7134/api/HouseListing", byteContent);

            response.EnsureSuccessStatusCode();

            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert
                .DeserializeObject<HouseListingDto>(jsonResponse)!;
        }

        public async Task<HouseListingDto> UpdateAsync(UpdateHouseListingDto houseListing)
        {
            var convertedHouseListing = JsonConvert.SerializeObject(houseListing);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseListing);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseListing/{houseListing.Id}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseListingDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseListing/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseListingDto> GetSingleAsync(int id, bool details)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/{id}?includeProfile={details}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseListingDto>(jsonResponse);
        }

        public IQueryable<HouseListingDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseListing?IncludeProfile=true").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
    
        public IQueryable<HouseListingDto> GetAllByProfile(int profileId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/ProfileId?profileId={profileId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
        
        public IQueryable<HouseListingDto> GetAllByOwner(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/OwnerId?ownerId={ownerId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
}