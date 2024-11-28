using DTOs.Report;
using Grpc.Core;
using Grpc.Net.Client;
using RepositoryContracts;

namespace DatabaseRepositories;

public class ReportRepository : IReportRepository
{
    private readonly ReportService.ReportServiceClient _client;

    public ReportRepository()
    {
        GrpcChannel channel = GrpcChannel.ForAddress("http://localhost:9090", new GrpcChannelOptions
        {  
            Credentials = ChannelCredentials.Insecure,
        });
        _client = new ReportService.ReportServiceClient(channel);
    }
    
    public Task<ReportDto> AddAsync(CreateReportDto report)
    {
        ReportResponse reply = _client.CreateReport(new CreateReportRequest
        {
            ReportingId = report.ReportingId,
            ReportedId = report.ReportedId,
            AdminId = report.AdminId,
            Comment = report.Comment
        });
        
        return Task.FromResult(new ReportDto
        {
            Id = reply.Id,
            ReportingId = report.ReportingId,
            ReportedId = report.ReportedId,
            AdminId = report.AdminId,
            Comment = report.Comment,
            Status = reply.Status,
            Date = new DateTime(1970, 1, 2).AddMilliseconds(reply.Date)
        });
    }

    public Task<ReportDto> UpdateAsync(int id, UpdateReportDto report)
    {
        ReportResponse reply = _client.UpdateReport(new UpdateReportRequest()
        {
            Id = id,
            Status = report.Status
        });
        
        return Task.FromResult(new ReportDto
        {
            Id = reply.Id,
            ReportingId = reply.ReportingId,
            ReportedId = reply.ReportedId,
            AdminId = reply.AdminId,
            Comment = reply.Comment,
            Status = reply.Status,
            Date = new DateTime(1970, 1, 2).AddMilliseconds(reply.Date)
        });
    }

    public Task DeleteAsync(int id)
    {
        _client.DeleteReport(new ReportRequest()
        {
            Id = id
        });
        
        return Task.CompletedTask;
    }

    public Task<ReportDto> GetSingleAsync(int id)
    {
        ReportResponse reply = _client.GetReport(new ReportRequest()
        {
            Id = id
        });
        return Task.FromResult(new ReportDto
        {
            Id = reply.Id,
            ReportingId = reply.ReportingId,
            ReportedId = reply.ReportedId,
            AdminId = reply.AdminId,
            Comment = reply.Comment,
            Status = reply.Status,
            Date = new DateTime(1970, 1, 2).AddMilliseconds(reply.Date)
        });
    }

    public IQueryable<ReportDto> GetAll()
    {
        AllReportsResponse reply = _client.GetAllReports(new AllReportsRequest());
        var reportResponses = reply.Reports.ToList();
        var reports = new List<ReportDto>();

        foreach (var report in reportResponses)
        {
            reports.Add(new ReportDto
            {
                Id = report.Id,
                ReportingId = report.ReportingId,
                ReportedId = report.ReportedId,
                AdminId = report.AdminId,
                Comment = report.Comment,
                Status = report.Status,
                Date = new DateTime(1970, 1, 2).AddMilliseconds(report.Date)
            });
        }

        return reports.AsQueryable();
    }
}