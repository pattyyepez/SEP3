using System.Net.Http.Headers;
using DTOs.HouseOwner;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services
{
    public class HouseOwnerService : IHouseOwnerService
    {
        private readonly HttpClient _httpClient;

        public HouseOwnerService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<HouseOwnerDto> AddAsync(CreateHouseOwnerDto houseOwner)
        {
            var convertedHouseOwner = JsonConvert.SerializeObject(houseOwner);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseOwner);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");

            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/HouseOwner", byteContent);

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error creating HouseOwner: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<HouseOwnerDto>(jsonResponse)!;
        }


        public async Task<HouseOwnerDto> UpdateAsync(UpdateHouseOwnerDto houseOwner)
        {
            var convertedHouseOwner = JsonConvert.SerializeObject(houseOwner);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseOwner);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseOwner/{houseOwner.UserId}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseOwnerDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseOwner/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseOwnerDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseOwner/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseOwnerDto>(jsonResponse);
            // return await httpClient.GetAsync("api/H").Result.;
        }

        public IQueryable<HouseOwnerDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseOwner").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseOwner = JsonConvert.DeserializeObject<List<HouseOwnerDto>>(jsonResponse);

            return houseOwner.AsQueryable();
        }
        
    }
    
}