using System.Net;
using HousePalClient.Components;
using Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

if (builder.Environment.IsDevelopment())
{
    var httpClientHandler = new HttpClientHandler();
    httpClientHandler.ServerCertificateCustomValidationCallback = (message, cert, chain, sslPolicyErrors) => true;
    
    builder.Services.AddHttpClient<IHouseOwnerService, HouseOwnerService>(client => new HttpClient(httpClientHandler)
    {
        BaseAddress = new Uri("https://localhost:7134/")
    });
}
else
{
    // For production, add HttpClient without bypassing SSL validation
    builder.Services.AddHttpClient<IHouseOwnerService, HouseOwnerService>(client =>
    {
        client.BaseAddress = new Uri("https://production-url.com/");
    });
}

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseStaticFiles();
app.UseAntiforgery();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();