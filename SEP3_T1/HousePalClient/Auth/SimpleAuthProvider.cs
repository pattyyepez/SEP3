
using DTOs.Login;

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
    private ClaimsPrincipal _currentClaimsPrincipal = new(new ClaimsIdentity());

    public SimpleAuthProvider(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public override Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        return Task.FromResult(new AuthenticationState(_currentClaimsPrincipal));
    }

    public async Task Login(string email, string password)
    {
        var response = await _httpClient.PostAsJsonAsync("auth/login", new LoginRequest { Email = email, Password = password });
        
        if (!response.IsSuccessStatusCode)
        {
            throw new Exception("Invalid login attempt");
        }

        var userDto = await response.Content.ReadFromJsonAsync<UserDto>();

        var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, userDto.Email),
            new Claim("Id", userDto.UserId.ToString())
        };
        
        var identity = new ClaimsIdentity(claims, "apiauth");
        _currentClaimsPrincipal = new ClaimsPrincipal(identity);

        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_currentClaimsPrincipal)));
    }

    public void Logout()
    {
        _currentClaimsPrincipal = new ClaimsPrincipal(new ClaimsIdentity());
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_currentClaimsPrincipal)));
    }
}
