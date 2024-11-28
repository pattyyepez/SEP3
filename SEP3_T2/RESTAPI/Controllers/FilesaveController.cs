using DTOs;

namespace RESTAPI.Controllers;

using System.Net;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("[controller]")]
public class FilesaveController(
    IHostEnvironment env, ILogger<FilesaveController> logger) 
    : ControllerBase
{
    
    [HttpPost]
    public async Task<ActionResult<IList<FileDto>>> PostFile(
        [FromForm] IEnumerable<IFormFile> files)
    {
        Console.Write("got request");
        var maxAllowedFiles = 3;
        var filesProcessed = 0;
        var resourcePath = new Uri($"{Request.Scheme}://{Request.Host}/");
        List<FileDto> uploadResults = [];

        foreach (var file in files)
        {
            var uploadResult = new FileDto();
            string trustedFileNameForFileStorage;
            var untrustedFileName = file.FileName;
            var extension = Path.GetExtension(untrustedFileName);
            uploadResult.FileName = untrustedFileName;
            var trustedFileNameForDisplay =
                WebUtility.HtmlEncode(untrustedFileName);

            if (filesProcessed < maxAllowedFiles)
            {
                if (file.Length == 0)
                {
                    Console.Write("error 1");
                    logger.LogInformation("{FileName} length is 0 (Err: 1)",
                        trustedFileNameForDisplay);
                    uploadResult.ErrorCode = 1;
                }
                else
                {
                    try
                    {
                        Console.Write("processing");
                        trustedFileNameForFileStorage = Path.GetRandomFileName();
                        trustedFileNameForFileStorage =
                            trustedFileNameForFileStorage.Split('.')[0] +
                            extension;
                        var path = Path.Combine(env.ContentRootPath,
                            env.EnvironmentName, "unsafe_uploads",
                            trustedFileNameForFileStorage);

                        await using FileStream fs = new(path, FileMode.Create);
                        await file.CopyToAsync(fs);

                        logger.LogInformation("{FileName} saved at {Path}",
                            trustedFileNameForDisplay, path);
                        uploadResult.Uploaded = true;
                        uploadResult.StoredFileName = trustedFileNameForFileStorage;
                        Console.Write(trustedFileNameForFileStorage);
                    }
                    catch (IOException ex)
                    {
                        logger.LogError("{FileName} error on upload (Err: 3): {Message}",
                            trustedFileNameForDisplay, ex.Message);
                        uploadResult.ErrorCode = 3;
                    }
                }

                filesProcessed++;
            }
            else
            {
                logger.LogInformation("{FileName} not uploaded because the " +
                    "request exceeded the allowed {Count} of files (Err: 4)",
                    trustedFileNameForDisplay, maxAllowedFiles);
                uploadResult.ErrorCode = 4;
            }

            uploadResults.Add(uploadResult);
        }

        Console.WriteLine(resourcePath);
        return new CreatedResult(resourcePath, uploadResults);
    }
    
    [HttpGet("{filename}/{extension}")]
    public IActionResult GetImage(string filename, string extension)
    {
        filename = filename + "." + extension;
        filename = $"Development/unsafe_uploads/{filename}";

        if (!System.IO.File.Exists(filename))
        {
            return NotFound("Image not found.");
        }

        var imageBytes = System.IO.File.ReadAllBytes(filename);
        return File(imageBytes, $"image/{extension}");
    }
    
    [HttpDelete("{filename}/{extension}")]
    public async Task<IActionResult> DeleteHouseProfile(string filename, string extension)
    {
        try
        {
            filename = filename + "." + extension;
            filename = $"Development/unsafe_uploads/{filename}";
            if (Path.Exists(filename))
            {
                System.IO.File.Delete(filename);
                return Ok();
            }
            return NotFound();
        }
        catch (Exception ex)
        {
            return StatusCode(500,
                $"Error deleting HouseProfile: {ex.Message}");
        }
    }
}