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
                    "https://localhost:7134/api/HouseListing/CreateHouseListing", byteContent);

            response.EnsureSuccessStatusCode();

            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert
                .DeserializeObject<HouseListingDto>(jsonResponse)!;
        }
    

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseListing/DeleteHouseListing/{id}");
            
            response.EnsureSuccessStatusCode();
        }

        public async Task<HouseListingDto> GetSingleAsync(int id, bool details)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetHouseListing/{id}?includeProfile={details}");

            response.EnsureSuccessStatusCode();
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseListingDto>(jsonResponse);
        }

        public IQueryable<HouseListingDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseListing/GetAllHouseListings?IncludeProfile=true").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
    
        public IQueryable<HouseListingDto> GetAllByProfile(int profileId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetListingsByProfile/ProfileId?profileId={profileId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
        
        public IQueryable<HouseListingDto> GetAllByOwner(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetListingsByOwner/OwnerId?ownerId={ownerId}").Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
        
        //  https://localhost:7134/api/HouseListing/GetFilteredListings
        // ?Region=s&City=s&Rating=1&StartDay=1&StartMonth=1&StartYear=1&EndDay=1&EndMonth=1&EndYear=1
        // &Amenities=string&Chores=Water%20plants&Chores=Take%20out%20trash  &StartDay=1&StartMonth=1&StartYear=1
        public IQueryable<HouseListingDto> GetFilteredListings(FilteredHouseListingsDto filter)
        {
            string uri =
                "https://localhost:7134/api/HouseListing/GetFilteredListings?";
            
            if(!string.IsNullOrWhiteSpace(filter.Region))
                uri += $"Region={filter.Region}&";
            
            if(!string.IsNullOrWhiteSpace(filter.City))
                uri += $"City={filter.City}&";
            
            if(filter.StartDay.HasValue)
                uri += $"StartDay={filter.StartDay}&StartMonth={filter.StartMonth}&StartYear={filter.StartYear}&";
            
            if(filter.EndDay.HasValue)
                uri += $"EndDay={filter.EndDay}&EndMonth={filter.EndMonth}&EndYear={filter.EndYear}&";
            
            uri = uri.TrimEnd('&');
            
            HttpResponseMessage response = _httpClient.GetAsync(uri).Result;

            response.EnsureSuccessStatusCode();

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing.AsQueryable();
        }
}