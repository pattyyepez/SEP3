using System.Net.Http.Headers;
using DTOs.HouseListing;
using HousePalClient.ServiceContracts;
using Newtonsoft.Json;

namespace HousePalClient.Services;

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

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error creating HouseListing: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert
                .DeserializeObject<HouseListingDto>(jsonResponse)!;
        }
    

        public async Task DeleteAsync(int id)
        {
            using HttpResponseMessage response = await _httpClient.DeleteAsync($"https://localhost:7134/api/HouseListing/DeleteHouseListing/{id}");
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error deleting HouseListing: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
        }

        public async Task<HouseListingDto> UpdateAsync(UpdateHouseListingDto houseListing)
        {
            var convertedListing = JsonConvert.SerializeObject(houseListing);
            var buffer = System.Text.Encoding.UTF8.GetBytes(convertedListing);
            var byteContent = new ByteArrayContent(buffer);
            byteContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            
            using HttpResponseMessage response = await _httpClient.PutAsync($"https://localhost:7134/api/HouseListing/UpdateHouseListing", byteContent);
            
            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error updating HouseListing: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
            
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseListingDto>(jsonResponse);
        }

        public async Task<HouseListingDto> GetSingleAsync(int id, bool details)
        {
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetHouseListing/{id}?includeProfile={details}");

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting single HouseListing: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseListingDto>(jsonResponse);
        }

        public async Task<HouseListingDto> GetViewListing(int id, int? sitterId)
        {
            
            using HttpResponseMessage response = await _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetViewListing/{id}{(sitterId.HasValue ? $"?sitterId={sitterId}" : "")}");

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting single detailed HouseListing: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }
    
            var jsonResponse = await response.Content.ReadAsStringAsync();
            Console.WriteLine($"{jsonResponse}\n");
            return JsonConvert.DeserializeObject<HouseListingDto>(jsonResponse);
        }

        public IQueryable<HouseListingDto> GetAll()
        {
            HttpResponseMessage response = _httpClient.GetAsync("https://localhost:7134/api/HouseListing/GetAllHouseListings?IncludeProfile=true").Result;

                        if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseListings: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }

        public IQueryable<HouseListingDto> GetAllDetailedByOwner(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetAllDetailedByOwner/{ownerId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseListings: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }

        public IQueryable<HouseListingDto> GetConfirmedStaysHo(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetConfirmedStaysHo/{ownerId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting confirmed stays (HouseListings) for HouseOwner by owner id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }

        public IQueryable<HouseListingDto> GetPastStaysHo(int ownerId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetPastStaysHo/{ownerId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting past stays (HouseListings) for HouseOwner by owner id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }
        
        public IQueryable<HouseListingDto> GetConfirmedStaysHs(int sitterId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetConfirmedStaysHs/{sitterId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting confirmed stays (HouseListings) for HouseSitter by sitter id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }

        public IQueryable<HouseListingDto> GetPastStaysHs(int sitterId)
        {
            HttpResponseMessage response = _httpClient.GetAsync($"https://localhost:7134/api/HouseListing/GetPastStaysHs/{sitterId}").Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting past stays (HouseListings) for HouseSitter by sitter id: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }

        public IQueryable<HouseListingDto> GetBrowseListings(FilteredHouseListingsDto? filter)
        {
            string uri =
                "https://localhost:7134/api/HouseListing/GetBrowseListingsHs?";

            if (filter != null)
            {
                if(!string.IsNullOrWhiteSpace(filter.Region))
                    uri += $"Region={filter.Region}&";
            
                if(!string.IsNullOrWhiteSpace(filter.City))
                    uri += $"City={filter.City}&";
            
                if(filter.StartDay.HasValue)
                    uri += $"StartDay={filter.StartDay}&StartMonth={filter.StartMonth}&StartYear={filter.StartYear}&";
            
                if(filter.EndDay.HasValue)
                    uri += $"EndDay={filter.EndDay}&EndMonth={filter.EndMonth}&EndYear={filter.EndYear}&";
            
                if(filter.Chores.Count != 0)
                    foreach (var chore in filter.Chores)
                    {
                        uri += $"Chores={chore}&";
                    }
            
                if(filter.Amenities.Count != 0)
                    foreach (var amenity in filter.Amenities)
                    {
                        uri += $"Amenities={amenity}&";
                    }
            }
            
            uri = uri.TrimEnd('&');
            HttpResponseMessage response = _httpClient.GetAsync(uri).Result;

            if (!response.IsSuccessStatusCode)
            {
                var errorContent = response.Content.ReadAsStringAsync().Result;
                Console.WriteLine($"Error getting all HouseListings for browsing w/ filter: {errorContent}");
                throw new HttpRequestException($"API error: {errorContent}");
            }

            var jsonResponse = response.Content.ReadAsStringAsync().Result;
            Console.WriteLine($"{jsonResponse}\n");

            var houseListing = JsonConvert.DeserializeObject<List<HouseListingDto>>(jsonResponse);

            return houseListing!.AsQueryable();
        }

}