using DTOs.Application;
using Newtonsoft.Json;
using System.Net.Http.Headers;


namespace Services;

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

            try
            {
                response.EnsureSuccessStatusCode();

            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                Console.WriteLine(ex.InnerException);
                Console.WriteLine(ex.StackTrace);
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
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public async Task DeleteAsync(int listingId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/Application/DeleteApplication/{listingId}/{sitterId}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<ApplicationDto> GetSingleAsync(int listingId, int sitterId)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/Application/GetApplication/{listingId}/{sitterId}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public IQueryable<ApplicationDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/Application/GetAllApplications").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }

        public IQueryable<ApplicationDto> GetApplicationsByListing(int listingId, bool includeSitter)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/Application/GetApplication/{listingId}?includeSitter={includeSitter}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }

        //https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}
        public IQueryable<ApplicationDto> GetApplicationsByUserStatus(int userId, string status)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/Application/GetApplicationsByUser/{userId}/{status}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }
}