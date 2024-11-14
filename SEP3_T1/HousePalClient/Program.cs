using HousePalClient.Auth;
using HousePalClient.Components;
using Microsoft.AspNetCore.Components.Authorization;
using Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddScoped(sp => new HttpClient
    {
        BaseAddress = new Uri("https://localhost:7134")
    }
);

builder.Services.AddScoped<IHouseOwnerService, HouseOwnerService>();
builder.Services.AddScoped<IHouseSitterService, HouseSitterService>();
builder.Services.AddScoped<IHouseProfileService, HouseProfileService>();

builder.Services.AddScoped<AuthenticationStateProvider, SimpleAuthProvider>();

// builder.Services.AddAuthentication();
// builder.Services.AddAuthenticationCore();

// builder.Services.AddScoped<AuthenticationStateProvider, SimpleAuthProvider>();
// builder.Services.AddAuthorizationCore();

// if (builder.Environment.IsDevelopment())
// {
//     var httpClientHandler = new HttpClientHandler();
//     httpClientHandler.ServerCertificateCustomValidationCallback = (message, cert, chain, sslPolicyErrors) => true;
//
//     builder.Services.AddHttpClient<IHouseOwnerService, HouseOwnerService>()
//         .ConfigurePrimaryHttpMessageHandler(() => httpClientHandler);
//     builder.Services.AddHttpClient<IHouseSitterService, HouseSitterService>()
//         .ConfigurePrimaryHttpMessageHandler(() => httpClientHandler);
// }
// else
// {
//     var baseAddress = builder.Configuration.GetValue<string>("ApiSettings:BaseUrl");
//     
//     builder.Services.AddHttpClient<IHouseOwnerService, HouseOwnerService>(client =>
//     {
//         client.BaseAddress = new Uri(baseAddress);
//     });
//     
//     builder.Services.AddHttpClient<IHouseSitterService, HouseSitterService>(client =>
//     {
//         client.BaseAddress = new Uri(baseAddress);
//     });
// }

var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseAntiforgery();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();