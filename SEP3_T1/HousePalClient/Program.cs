using HousePalClient.Auth;
using HousePalClient.Components;
using HousePalClient.ServiceContracts;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Components.Authorization;
using Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddHttpClient();
builder.Services.AddBlazorBootstrap();

builder.Services.AddScoped(sp => new HttpClient() { BaseAddress = new Uri("https://localhost:7134") });

builder.Services.AddScoped<IHouseOwnerService, HouseOwnerService>();
builder.Services.AddScoped<IHouseSitterService, HouseSitterService>();
builder.Services.AddScoped<IHouseProfileService, HouseProfileService>();
builder.Services.AddScoped<IHouseListingService, HouseListingService>();
builder.Services.AddScoped<IApplicationService, ApplicationService>();
builder.Services.AddScoped<ISitterReviewService, SitterReviewService>();
builder.Services.AddScoped<IHouseReviewService, HouseReviewService>();
builder.Services.AddScoped<IReportService, ReportService>();

builder.Services.AddScoped<AuthenticationStateProvider, SimpleAuthProvider>();

builder.Services.AddAuthorization();
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme).
    AddCookie(options =>
    {
        options.Cookie.Name = "auth_cookie";
        options.LoginPath = "/login";
        // options.LogoutPath = "/logout";
        options.Cookie.MaxAge = TimeSpan.FromMinutes(90);
        options.AccessDeniedPath = "/";
    });

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