using DTOs.Application;
using DTOs.HouseListing;
using DTOs.HouseProfile;
using DTOs.HouseSitter;
using DTOs.SitterReview;
using Microsoft.AspNetCore.Mvc;
using RepositoryContracts;
using RESTAPI.ControllerContracts;

namespace RESTAPI.Controllers;

[ApiController]
[Route("api/[controller]/[action]")]
public class HouseListingController : ControllerBase, IHouseListingController
{
    private readonly IHouseListingRepository _repo;

    public HouseListingController(IHouseListingRepository repo)
    {
        _repo = repo;
    }

    // Get: api/HouseListing?includeProfile=true
    [HttpGet]
    public async Task<IActionResult> GetAllHouseListings(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfile)
    {
        var response = _repo.GetAll();
        if (!includeProfile) return Ok(response);

        var toReturn = new List<HouseListingDto>();
        foreach (var listing in response)
        {
            listing.Profile =
                await profileRepo.GetSingleAsync(listing.ProfileId);
            toReturn.Add(listing);
        }

        return Ok(toReturn.AsQueryable());
    }

    [HttpGet("{ownerId}")]
    public async Task<IActionResult> GetAllDetailedByOwner(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IApplicationRepository appRepo, 
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromServices] ISitterReviewRepository reviewRepo, 
        [FromRoute] int ownerId)
    {
        var response = _repo.GetAll().Where(l => l.Status == "Open" && l.StartDate >= DateOnly.FromDateTime(DateTime.Today));

        var toReturn = new List<HouseListingDto>();

        foreach (var listing in response)
        {
            var tempProfile = await profileRepo.GetSingleAsync(listing.ProfileId);
            listing.Profile = new HouseProfileDto
            {
                OwnerId = tempProfile.OwnerId,
                Title = tempProfile.Title,
                Pictures = new List<string>(){tempProfile.Pictures[0]},
            };

            listing.Applications = appRepo.GetAll()
                .Where(a => a.ListingId == listing.Id && a.Status != "Confirmed").ToList();

            foreach (var app in listing.Applications)
            {
                var tempSitter = await sitterRepo.GetSingleAsync(app.SitterId);
                app.Sitter = new HouseSitterDto
                {
                    UserId = tempSitter.UserId,
                    Name = tempSitter.Name,
                    ProfilePicture = tempSitter.ProfilePicture,
                };
                
                app.Sitter.Reviews = reviewRepo.GetAll()
                    .Where(r => r.SitterId == app.SitterId)
                    .Select(r => new SitterReviewDto { Rating = r.Rating })
                    .ToList();
            }

            toReturn.Add(listing);
        }
        return Ok(toReturn.Where(l => l.Profile.OwnerId == ownerId).AsQueryable());
    }

    // GET: api/HouseListing/{id}?includeProfile=true
    [HttpGet("{id}")]
    public async Task<IActionResult> GetHouseListing(int id,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfile)
    {
        var response = await _repo.GetSingleAsync(id);

        if (includeProfile)
            response.Profile =
                await profileRepo.GetSingleAsync(response.ProfileId);

        return Ok(response);
    }

    [HttpGet ("{ownerId}")]
    public async Task<IActionResult> GetConfirmedStaysHo(
        [FromServices] IApplicationRepository appRepo,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        int? ownerId)
    {
        var response = _repo.GetAll().Where(l => l.Status == "Closed" && l.EndDate > DateOnly.FromDateTime(DateTime.Today));

        var toReturn = new List<HouseListingDto>();

        foreach (var listing in response)
        {
            var tempProfile = await profileRepo.GetSingleAsync(listing.ProfileId);
            listing.Profile = new HouseProfileDto
            {
                OwnerId = tempProfile.OwnerId,
                Title = tempProfile.Title,
                Pictures = new List<string>(){tempProfile.Pictures[0]},
            };

            listing.Applications = appRepo.GetAll()
                .Where(a => a.ListingId == listing.Id && a.Status == "Confirmed").ToList();

            foreach (var app in listing.Applications)
            {
                var tempSitter = await sitterRepo.GetSingleAsync(app.SitterId);
                app.Sitter = new HouseSitterDto
                {
                    Name = tempSitter.Name,
                    ProfilePicture = tempSitter.ProfilePicture,
                    Email = tempSitter.Email,
                    Phone = tempSitter.Phone,
                };

            }

            toReturn.Add(listing);
        }
        return Ok(toReturn.Where(l => l.Profile.OwnerId == ownerId).AsQueryable());
    }

    [HttpGet("{ownerId}")]
    public async Task<IActionResult> GetPastStaysHo(
        [FromServices] IApplicationRepository appRepo,
        [FromServices] IHouseProfileRepository profileRepo, 
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromServices] ISitterReviewRepository reviewRepo, 
        [FromRoute] int? ownerId)
    {
        var response = _repo.GetAll().Where(l => l.Status == "Closed" && l.EndDate < DateOnly.FromDateTime(DateTime.Today));

        var toReturn = new List<HouseListingDto>();

        foreach (var listing in response)
        {
            var tempProfile = await profileRepo.GetSingleAsync(listing.ProfileId);
            listing.Profile = new HouseProfileDto
            {
                OwnerId = tempProfile.OwnerId,
                Title = tempProfile.Title,
                Pictures = new List<string>(){tempProfile.Pictures[0]},
            };

            listing.Applications = appRepo.GetAll()
                .Where(a => a.ListingId == listing.Id && a.Status == "Confirmed")
                .Select(a => new ApplicationDto{SitterId = a.SitterId})
                .ToList();

            foreach (var app in listing.Applications)
            {
                var tempSitter = await sitterRepo.GetSingleAsync(app.SitterId);
                app.Sitter = new HouseSitterDto
                {
                    Name = tempSitter.Name,
                    ProfilePicture = tempSitter.ProfilePicture,
                };

                try
                {
                    var tempReview = await reviewRepo.GetSingleAsync(tempProfile.OwnerId, app.SitterId);
                    tempReview.Editable = DateOnly.FromDateTime(tempReview.Date) < listing.EndDate;
                    app.Sitter.Reviews = new List<SitterReviewDto>()
                    {
                        tempReview
                    };
                }
                catch (Exception e)
                {
                    Console.WriteLine($"Did not find review for composite id {tempProfile.OwnerId}, {app.SitterId} when trying to GetPastStaysHo for owner {ownerId}");
                }
            }

            toReturn.Add(listing);
        }
        return Ok(toReturn.Where(l => l.Profile.OwnerId == ownerId).AsQueryable());
    }

    [HttpGet("ProfileId")]
    public async Task<IActionResult> GetListingsByProfile(
        [FromQuery] int? profileId)
    {
        IQueryable<HouseListingDto> listings = _repo.GetAll();

        if (profileId.HasValue)
        {
            listings = listings.Where(l => l.ProfileId == profileId.Value);
        }

        return Ok(listings);
    }


    [HttpGet("OwnerId")]
    public async Task<IActionResult> GetListingsByOwner(int ownerId,
        [FromServices] IHouseProfileRepository profileRepo)
    {
        var response = _repo.GetAll();

        var toReturn = new List<HouseListingDto>();
        foreach (var listing in response)
        {
            listing.Profile =
                await profileRepo.GetSingleAsync(listing.ProfileId);
            toReturn.Add(listing);
        }

        return Ok(toReturn.AsQueryable()
            .Where(l => l.Profile.OwnerId == ownerId));
    }

    // GET api/HouseListing/GetListingsByOwnerStatus/{ownerId}/{status}?includeApplications=true&includeProfiles=true

    [HttpGet("{ownerId:int}/{status}")]
    public async Task<IActionResult> GetListingsByOwnerStatus(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromServices] IApplicationRepository applicationRepo,
        [FromServices] IHouseSitterRepository sitterRepo,
        [FromQuery] bool includeApplications,
        [FromQuery] bool includeProfiles,
        int ownerId, string status)
    {
        var response = _repo.GetAll();

        var toReturn = new List<HouseListingDto>();

        foreach (var listing in response)
        {
            if (includeProfiles)
            {
                listing.Profile =
                    await profileRepo.GetSingleAsync(listing.ProfileId);
            }

            if (includeApplications)
            {
                listing.Applications = applicationRepo.GetAll()
                    .Where(a => a.ListingId == listing.Id)
                    .Select(a => new ApplicationDto
                    {
                        Status = a.Status,
                        Sitter = new HouseSitterDto
                        {
                            Name = sitterRepo.GetSingleAsync(a.SitterId).Result
                                .Name
                        }
                    })
                    .ToList();
            }

            toReturn.Add(listing);
        }

        return Ok(ownerId == 0
            ? response.Where(l => l.Status == status)
            : toReturn.AsQueryable().Where(l =>
                l.Profile.OwnerId == ownerId && l.Status == status));
    }

    [HttpGet("{sitterId}")]
    public async Task<IActionResult> GetWaitingForReviewBySitter(
        [FromServices] IHouseReviewRepository reviewRepo,
        [FromServices] IApplicationRepository applicationRepo,
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] bool includeProfiles,
        int sitterId)
    {
        var confirmedApps = applicationRepo.GetAll()
            .Where(a => a.SitterId == sitterId && a.Status == "Confirmed")
            .Select(a => a.ListingId)
            .ToList();

        var reviews = reviewRepo.GetAll()
            .Where(r => r.SitterId == sitterId)
            .Select(r => r.ProfileId)
            .ToList();

        var response = _repo.GetAll().Where(l =>
            confirmedApps.Contains(l.Id) && !reviews.Contains(l.ProfileId) &&
            l.EndDate < DateOnly.FromDateTime(DateTime.Now));

        if (!includeProfiles) return Ok(response.ToList());

        foreach (var listing in response)
        {
            var profile = await profileRepo.GetSingleAsync(listing.ProfileId);
            listing.Profile = new HouseProfileDto
            {
                Title = profile.Title,
                Pictures = profile.Pictures,
            };
        }

        return Ok(response.ToList());
    }

    // POST: api/HouseListing
    [HttpPost]
    public async Task<IActionResult> CreateHouseListing(
        [FromBody] CreateHouseListingDto createDto)
    {
        var response = await _repo.AddAsync(createDto);
        return Ok(response);
    }

    // PUT: api/HouseListing
    [HttpPut]
    public async Task<IActionResult> UpdateHouseListing(
        [FromBody] UpdateHouseListingDto updateDto)
    {
        var response = await _repo.UpdateAsync(updateDto);
        return Ok(response);
    }

    // DELETE: api/HouseListing/{id}
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteHouseListing(int id)
    {
        await _repo.DeleteAsync(id);
        return Ok();
    }

    [HttpGet]
    public async Task<IActionResult> GetFilteredListings(
        [FromServices] IHouseProfileRepository profileRepo,
        [FromQuery] FilteredHouseListingsDto filter)
    {
        var listings = _repo.GetAll().Where(l => l.Status == "Open");
        foreach (var listing in listings)
        {
            listing.Profile =
                await profileRepo.GetSingleAsync(listing.ProfileId);
        }

        if (!string.IsNullOrWhiteSpace(filter.Region))
            listings = listings
                .Where(l => l.Profile.Region == filter.Region);

        if (!string.IsNullOrWhiteSpace(filter.City))
            listings = listings
                .Where(l => l.Profile.City == filter.City);

        if (filter.StartDay.HasValue)
            listings = listings
                .Where(l => Math.Abs(
                    l.StartDate.ToDateTime(new TimeOnly())
                        .Subtract(new DateTime(filter.StartYear.Value,
                            filter.StartMonth.Value, filter.StartDay.Value))
                        .TotalDays) < 6);

        if (filter.EndDay.HasValue)
            listings = listings
                .Where(l => Math.Abs(
                    l.EndDate.ToDateTime(new TimeOnly())
                        .Subtract(new DateTime(filter.EndYear.Value,
                            filter.EndMonth.Value, filter.EndDay.Value))
                        .TotalDays) < 6);

        if (filter.Amenities.Any())
            listings = listings
                .Where(l =>
                    l.Profile.Amenities.Intersect(filter.Amenities).Any());

        if (filter.Chores.Any())
            listings = listings
                .Where(l => l.Profile.Chores.Intersect(filter.Chores).Any());

        return Ok(listings.ToList());
    }
}