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
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/Application/UpdateApplication/{application.ListingId}/{application.SitterId}", byteContent);
            
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

        public IQueryable<ApplicationDto> GetApplicationsByListing(int listingId, string status, bool includeSitter)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/Application/GetApplicationByListing/{listingId}/{status}?includeSitter={includeSitter}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting Applications by listing id, status: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }

        //https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}
        public IQueryable<ApplicationDto> GetApplicationsByUserStatus(int userId, string status, bool includeListings, bool includeProfiles)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}?includeListings={includeListings}&includeProfiles={includeProfiles}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all Applications by user id, status: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }
}