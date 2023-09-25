package ie.futurecollars.invoicing.db.file;

import ie.futurecollars.invoicing.utils.FilesService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class IdService {

  private final Path idFilePath;
  private final FilesService filesService;
  private int nextId = 1;

  public IdService(Path idFilePath, FilesService filesService) {
    this.idFilePath = idFilePath;
    this.filesService = filesService;

    try {
      List<String> lines = filesService.readAllLines(idFilePath);
      if (lines.isEmpty()) {
        filesService.writeToFile(idFilePath, "1");
      } else {
        nextId = Integer.parseInt(lines.get(0));
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialize id database", e);
    }
  }

  public int getNextIdAndIncrement() {
    try {
      filesService.writeToFile(idFilePath, String.valueOf(nextId + 1));
      return nextId++;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read id file", e);
    }
  }
}
