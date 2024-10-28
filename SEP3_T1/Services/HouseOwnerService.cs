using Microsoft.AspNetCore.Components;
using System.Collections.Generic;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Entities;
using Newtonsoft.Json;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace Services
{
    public class HouseOwnerService : IHouseOwnerService
    {
        private readonly HttpClient httpClient;

        public HouseOwnerService(HttpClient httpClient)
        {
            this.httpClient = httpClient;
        }

        public async Task<HouseOwner> AddAsync(HouseOwner houseOwner)
        {
            var convertedHouseOwner = JsonConvert.SerializeObject(houseOwner);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseOwner);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await httpClient.PostAsync("https://localhost:7134/api/HouseOwner", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseOwner>(jsonResponse);
            // return await httpClient.GetAsync("api/H").Result.;
        }

        public async Task<HouseOwner> UpdateAsync(HouseOwner houseOwner)
        {
            var convertedHouseOwner = JsonConvert.SerializeObject(houseOwner);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedHouseOwner);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await httpClient.PutAsync($"https://localhost:7134/api/HouseOwner/{houseOwner.OwnerId}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseOwner>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await httpClient.DeleteAsync($"https://localhost:7134/api/HouseOwner/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseOwner> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await httpClient.GetAsync($"https://localhost:7134/api/HouseOwner/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseOwner>(jsonResponse);
            // return await httpClient.GetAsync("api/H").Result.;
        }

        public IQueryable<HouseOwner> GetAll()
        {
            throw new NotImplementedException();
        }
    }
}