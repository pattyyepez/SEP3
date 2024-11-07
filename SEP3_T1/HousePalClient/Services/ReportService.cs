using System.Net.Http.Headers;
using DTOs.Report;
using Newtonsoft.Json;

namespace Services;

public class ReportService : IReportService
{
    
      private readonly HttpClient _httpClient;

        public ReportService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<ReportDto> AddAsync(ReportDto report)
        {
            var convertedReport = JsonConvert.SerializeObject(report);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedReport);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PostAsync("https://localhost:7134/api/Report", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ReportDto>(jsonResponse);
        }

        public async Task<ReportDto> UpdateAsync(ReportDto report)
        {
            var convertedReport = JsonConvert.SerializeObject(report);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedReport);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseOwner/{report.Id}", byteContent);
            
            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ReportDto>(jsonResponse);
        }

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/Report/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<ReportDto> GetSingleAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/Report/{id}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<ReportDto>(jsonResponse);
        }

        public IQueryable<ReportDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/Report").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var report = JsonConvert.DeserializeObject<List<ReportDto>>(jsonResponse);

            return report.AsQueryable();
        }
        
    
}