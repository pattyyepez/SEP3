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
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/Application", byteContent);

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
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/Application/{application.ListingId}/{application.SitterId}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/Application/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<ApplicationDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/Application/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ApplicationDto>(jsonResponse);
        }

        public IQueryable<ApplicationDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/Application").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var application = JsonConvert.DeserializeObject<List<ApplicationDto>>(jsonResponse);

            return application.AsQueryable();
        }
        
    
}