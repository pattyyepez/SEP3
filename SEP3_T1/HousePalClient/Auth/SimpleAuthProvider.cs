
using DTOs.Login;
using Microsoft.JSInterop;

namespace HousePalClient.Auth;

using Microsoft.AspNetCore.Components.Authorization;
using System.Security.Claims;
using System.Text.Json;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

public class SimpleAuthProvider : AuthenticationStateProvider
{
    private readonly HttpClient _httpClient;
    private readonly IJSRuntime _jsRuntime;

    public SimpleAuthProvider(HttpClient httpClient, IJSRuntime jsRuntime)
    {
        _httpClient = httpClient;
        _jsRuntime = jsRuntime;
    }
    
    public override async Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        string userAsJson = "";
        try
        {
            userAsJson = await _jsRuntime.InvokeAsync<string>("localStorage.getItem", "currentUser");
        }
        catch (InvalidOperationException e)
        {
            return new AuthenticationState(new());
        }

        if (string.IsNullOrEmpty(userAsJson))
        {
            return new AuthenticationState(new());
        }

        UserDto userDto = JsonSerializer.Deserialize<UserDto>(userAsJson)!;
        List<Claim> claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, userDto.Name),
            new Claim(ClaimTypes.Email, userDto.Email),
            new Claim("Id", userDto.UserId.ToString()),
            new Claim(ClaimTypes.MobilePhone, userDto.Phone),
            new Claim("ProfilePicture", userDto.ProfilePicture),
            new Claim("CPR", userDto.CPR),
            new Claim("IsVerified", userDto.IsVerified.ToString()),
            new Claim("Biography", userDto.Biography)
        };
    
        if (userDto.Address != null)
        {
            claims.Add(new Claim("Address", userDto.Address));
            claims.Add(new Claim(ClaimTypes.Role, "HouseOwner"));
        }
    
        else
        {
            claims.Add(new Claim("Experiences", userDto.Experience));
            claims.Add(new Claim("Pictures", JsonSerializer.Serialize(userDto.Pictures)));
            claims.Add(new Claim("Skills", JsonSerializer.Serialize(userDto.Skills)));
            claims.Add(new Claim(ClaimTypes.Role, "HouseSitter"));
        }
        ClaimsIdentity identity = new ClaimsIdentity(claims, "apiauth");
        ClaimsPrincipal claimsPrincipal = new ClaimsPrincipal(identity);
        return new AuthenticationState(claimsPrincipal);
    }

    public async Task Login(string email, string password)
    {
        var response = await _httpClient.PostAsJsonAsync("auth/login",
            new LoginRequest
            {
                Email = email, 
                Password = password
            });

        if (!response.IsSuccessStatusCode)
        {
            throw new Exception("Invalid login attempt");
        }

        var userDto = await response.Content.ReadFromJsonAsync<UserDto>(new JsonSerializerOptions()
        {
            PropertyNameCaseInsensitive = true
        });

        string serialisedData = JsonSerializer.Serialize(userDto);
        await _jsRuntime.InvokeVoidAsync("localStorage.setItem", "currentUser", serialisedData);
    }

    public async void Logout()
    {
        await _jsRuntime.InvokeVoidAsync("localStorage.setItem", "currentUser", "");
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(new())));
    }
}
