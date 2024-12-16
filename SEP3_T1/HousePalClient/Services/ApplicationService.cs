using System.Net.Http.Headers;
using DTOs.Application;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services;

public class ApplicationService : IApplicationService
{
      private readonly HttpClient _httpClient;

        public ApplicationService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<ApplicationDto> AddAsync(CreateApplicationDto application)
        {
            var convertedApplication = JsonConvert.SerializeObject(application);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedApplication);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/Application/CreateApplication", byteContent);

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error creating Application: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public async Task<ApplicationDto> UpdateAsync(UpdateApplicationDto application)
        {
            var convertedApplication = JsonConvert.SerializeObject(application);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedApplication);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PatchAsync($"https://localhost:7134/api/Application/UpdateApplication", byteContent);
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error updating Application: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public async Task DeleteAsync(int listingId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/Application/DeleteApplication/{listingId}/{sitterId}");
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error deleting Application: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        }

        public async Task<ApplicationDto> GetSingleAsync(int listingId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/Application/GetApplication/{listingId}/{sitterId}");

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"Error getting single Application: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public IQueryable<ApplicationDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/Application/GetAllApplications").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all Application: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }

        public IQueryable<ApplicationDto> GetMyApplicationsSitter(int sitterId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/Application/GetMyApplicationsSitter/{sitterId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting MyApplications for HouseSitter by sitter id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }
}