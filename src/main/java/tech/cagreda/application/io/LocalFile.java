package tech.cagreda.application.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalFile {

  /*private static final FileAttribute<Set<PosixFilePermission>> FILE_ATTRIBUTE =
      PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
*/
  public static void appendLines(Path directory, String content) throws IOException {
    Files.writeString(directory, content, StandardOpenOption.APPEND);

  }

  public static Path addHeader(String key, Path pathBody, String content) throws IOException {
    Path pathHeaderFile = initPaymentFile(key);
    Path pathFinalFile = initPaymentFile(key);
    appendLines(pathHeaderFile, content);
    try (OutputStream out =
                 Files.newOutputStream(pathFinalFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
      Files.copy(pathHeaderFile, out);
      Files.copy(pathBody, out);
    }
    if(!System.getProperty("os.name").toLowerCase().contains("windows")){
      Files.delete(pathHeaderFile);
      Files.delete(pathBody);
    }

    return pathFinalFile;
  }

  public static Path initPaymentFile(String key) throws IOException {
    var directoryName = String.format("%s/%s", System.getProperty("java.io.tmpdir"), key);
    Path directory = Files.createDirectories(Path.of(directoryName));
    return Files.createFile(directory.resolve(UUID.randomUUID().toString()));
  }
}
