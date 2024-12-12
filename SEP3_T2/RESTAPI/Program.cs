using DatabaseRepositories;
using Microsoft.OpenApi.Models;
using RepositoryContracts;
using RESTAPI.ExceptionHandling;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<IHouseOwnerRepository, HouseOwnerRepository>();
builder.Services.AddScoped<IHouseSitterRepository, HouseSitterRepository>();
builder.Services.AddScoped<IHouseProfileRepository, HouseProfileRepository>();
builder.Services.AddScoped<IHouseListingRepository, HouseListingRepository>();
builder.Services.AddScoped<IApplicationRepository, ApplicationRepository>();
builder.Services.AddScoped<IHouseReviewRepository, HouseReviewRepository>();
builder.Services.AddScoped<ISitterReviewRepository, SitterReviewRepository>();
builder.Services.AddScoped<IReportRepository, ReportRepository>();

builder.Services.AddTransient<GlobalExceptionHandlerMiddleware>();
builder.Services.AddSwaggerGen(c =>
    {
        c.SwaggerDoc("v1", new OpenApiInfo { Title = "LearnWebAPI", Version = "v1" });
    });

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
    app.UseSwagger();
    app.UseSwaggerUI(c =>
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "LearnWebAPI v1"));
}

app.UseRouting();
app.UseMiddleware<GlobalExceptionHandlerMiddleware>();
app.UseAuthorization();
app.UseEndpoints(endpoints => { endpoints.MapControllers(); });

app.UseHttpsRedirection();

app.MapControllers();

app.Run();