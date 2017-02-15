package org.cresst.sb.irp.zip;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;

/**
 * Module for handling Zip files.
 */
public class IrpZipUtils {
    /**
     * 
     * @param paths
     *            existing paths before extracting from `zipFile`. All `Path`'s
     *            from `zipFile` will be added to `paths`.
     * @param tmpDir
     *            temporary directory for files from zip to be extracted to.
     * @param zipFile
     *            target zip file
     * @throws IOException
     */
    public static void extractFilesFromZip(final List<Path> paths, final Path tmpDir, Path zipFile) throws IOException {
        // Find all XML documents in the ZIP file and add them to paths
        final URI uri = URI.create("jar:file:" + zipFile.toUri().getPath());
        try (FileSystem fs = FileSystems.newFileSystem(uri, new HashMap<String, Object>())) {
            for (Path root : fs.getRootDirectories()) {

                // Walk the virtual ZIP filesystem looking for XML documents
                Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                    private final PathMatcher matcher = fs.getPathMatcher("glob:*.xml");

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (attrs.isRegularFile() && matcher.matches(file.getFileName()) && !file.getFileName().toString().startsWith(".")) {
                            // Copy the XML file from the ZIP file to the temp directory
                            Path destFile = Paths.get(tmpDir.toString(), file.getFileName().toString());
                            Path copiedFile = Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);

                            // Add the copied XML file to paths
                            paths.add(copiedFile);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException ex) {
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }
    }
}
