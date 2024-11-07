using HousePalClient.Auth;
using HousePalClient.Components;
using Microsoft.AspNetCore.Components.Authorization;
using Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Configura la autenticación y autorización
builder.Services.AddScoped<AuthenticationStateProvider, SimpleAuthProvider>();
builder.Services.AddAuthorizationCore();

if (builder.Environment.IsDevelopment())
{
    var httpClientHandler = new HttpClientHandler();
    httpClientHandler.ServerCertificateCustomValidationCallback = (message, cert, chain, sslPolicyErrors) => true;

    builder.Services.AddHttpClient<IHouseOwnerService, HouseOwnerService>()
        .ConfigurePrimaryHttpMessageHandler(() => httpClientHandler);
    builder.Services.AddHttpClient<IHouseSitterService, HouseSitterService>()
        .ConfigurePrimaryHttpMessageHandler(() => httpClientHandler);
}
else
{
    var baseAddress = builder.Configuration.GetValue<string>("ApiSettings:BaseUrl");
    
    builder.Services.AddHttpClient<IHouseOwnerService, HouseOwnerService>(client =>
    {
        client.BaseAddress = new Uri(baseAddress);
    });
    
    builder.Services.AddHttpClient<IHouseSitterService, HouseSitterService>(client =>
    {
        client.BaseAddress = new Uri(baseAddress);
    });
}

// Construye la aplicación después de registrar todos los servicios
var app = builder.Build();

// Configure the HTTP request pipeline.
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