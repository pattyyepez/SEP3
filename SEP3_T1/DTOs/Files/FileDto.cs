namespace DTOs;

public class FileDto
{
    public string? FileName { get; set; }
    public string? StoredFileName { get; set; }
    public int ErrorCode { get; set; }
    public bool Uploaded { get; set; }
}