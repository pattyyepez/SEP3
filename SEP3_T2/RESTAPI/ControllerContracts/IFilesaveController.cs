using DTOs;
using DTOs.Files;
using Microsoft.AspNetCore.Mvc;

namespace RESTAPI.ControllerContracts;

public interface IFilesaveController
{
    Task<ActionResult<IList<FileDto>>> PostFile(
        [FromForm] IEnumerable<IFormFile> files);

    IActionResult GetImage(string filename, string extension);

    Task<IActionResult> DeleteFile(string filename,
        string extension);
}