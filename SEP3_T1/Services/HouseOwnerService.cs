using Microsoft.AspNetCore.Components;
using System.Collections.Generic;
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

        public async Task<HouseOwner> GetHouseOwner(int id)
        {
            using HttpResponseMessage response = await httpClient.GetAsync($"https://localhost:7134/api/HouseOwner/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseOwner>(jsonResponse);
            // return await httpClient.GetAsync("api/H").Result.;
        }
    }
}