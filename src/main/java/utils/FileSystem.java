package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public final class FileSystem {
    private static final String ZIPFILEEXTENSION = ".zip";
    private static String sourceFolder;
    private List<String> fileList;

    public FileSystem() {
    }

    public FileSystem(String sourceFolder) {
        this.fileList = new ArrayList();
        FileSystem.sourceFolder = sourceFolder;
    }

    public static String getZipFileExtension() {
        return ".zip";
    }

    public static File zipAllFilesAndFolders(String folderPath) {
        return zipAllFilesAndFolders(folderPath, folderPath + File.separator + (new File(folderPath)).getName());
    }

    public static File zipAllFilesAndFolders(String folderPath, String outputZipFile) {
        FileSystem zip = new FileSystem(folderPath);
        zip.generateFileList(new File(sourceFolder));
        File compressedFile = zip.zipIt(outputZipFile + ".zip");
        return compressedFile;
    }

    public static boolean isFileExist(String path) {
        return (new File(path)).exists();
    }

    public static String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(46));
    }

    public static String getResourcePath(String resourcePath) throws URISyntaxException {
        System.out.println(resourcePath);
        URL resource = FileSystem.class.getClassLoader().getResource(resourcePath);
        return resource.toURI().getPath();
    }

    public static void copy(String srcPath, String destPath) throws IOException {
        File src = new File(srcPath);
        File dest = new File(destPath);
        int length;
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdirs();
            }

            String[] files = src.list();
            String[] var5 = files;
            int var6 = files.length;

            for(length = 0; length < var6; ++length) {
                String file = var5[length];
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copy(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];

            while((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }

    }

    public static void copyResourceFile(String srcPath, String destPath) throws Exception {
        InputStream in = FileSystem.class.getResourceAsStream(srcPath);
        File outFile = new File(destPath);
        File parentDir = outFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        OutputStream out = new FileOutputStream(new File(destPath));
        byte[] buffer = new byte[1024];

        int length;
        while((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }

    public static void createDirectory(String path) {
        (new File(path)).mkdir();
    }

    public static void flushDirectory(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                return;
            }

            String[] files = file.list();
            String[] var3 = files;
            int var4 = files.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String eachFile = var3[var5];
                if ((new File(file, eachFile)).isDirectory()) {
                    flushDirectory(file + File.separator + eachFile);
                } else {
                    delete((new File(file, eachFile)).getAbsolutePath());
                }
            }
        }

    }

    public static void emptyDirectory(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                return;
            }

            String[] files = file.list();
            String[] var3 = files;
            int var4 = files.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String eachFile = var3[var5];
                delete((new File(file, eachFile)).getAbsolutePath());
            }
        }

    }

    public static void clearFolder(String path) {
        try {
            File file = new File(path);
            Log.info("Clearing files and folders in filepath: '" + file.getAbsolutePath() + "'");
            FileUtils.cleanDirectory(file);
        } catch (Exception var2) {
            Log.info(var2.getMessage());
        }

    }

    public static void delete(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                String[] files = file.list();
                String[] var3 = files;
                int var4 = files.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String eachFile = var3[var5];
                    delete((new File(file, eachFile)).getAbsolutePath());
                }

                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }

    }

    public static void writeToFile(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

    public static void appendToFile(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
        writer.write(content);
        writer.close();
    }

    public static String rename(String oldFilePath, String newName) throws IOException {
        File oldfile = new File(oldFilePath);
        File newfile = new File(oldfile.getParent() + File.separator + newName);
        oldfile.renameTo(newfile);
        return newfile.getAbsolutePath();
    }

    public static int getNumberOfLinesInAFile(String file) throws IOException {
        LineNumberReader lineNoReader = new LineNumberReader(new FileReader(new File(file)));
        lineNoReader.skip(9223372036854775807L);
        int numbeOfLines = lineNoReader.getLineNumber();
        lineNoReader.close();
        return numbeOfLines;
    }

    public static void writeToFile(String filePath, String content, Charset encoding) throws IOException {
        String s = new String(content.getBytes(), encoding);
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(s);
        writer.close();
    }

    public static String readFromFile(String path) throws IOException {
        return readFromFile(path, StandardCharsets.UTF_8);
    }

    public static String readFromFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

    public static String readLineFromFile(String path, int line) throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(path));
        String firstLine = null;

        for(int i = 0; i < line; ++i) {
            firstLine = brTest.readLine();
        }

        brTest.close();
        return firstLine;
    }

    public static String readFromResourceFile(String path) throws IOException {
        return readFromResourceFile(path, StandardCharsets.UTF_8);
    }

    public static String readFromResourceFile(String path, Charset encoding) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(FileSystem.class.getClassLoader().getResourceAsStream(path), encoding));
        StringBuffer sb = new StringBuffer();

        int num;
        while((num = br.read()) != -1) {
            sb.append((char)num);
        }

        br.close();
        return sb.toString();
    }

    public static void replaceFileContent(String filePath, String find, String replace) throws IOException {
        replaceFileContent(filePath, find, replace, StandardCharsets.UTF_8);
    }

    public static void replaceFileContent(String filePath, String find, String replace, Charset encoding) throws IOException {
        String content = readFromFile(filePath, encoding);
        content = content.replaceAll(find, replace);
        writeToFile(filePath, content, encoding);
    }

    public static void move(String oldPath, String newPath) throws IOException {
        copy(oldPath, newPath);
        delete(oldPath);
    }

    public static File searchFileWithContent(String folderPath, String content, String fileType) throws IOException {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        File[] var5 = listOfFiles;
        int var6 = listOfFiles.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            File file = var5[var7];
            if (file.isFile()) {
                String ext = FilenameUtils.getExtension(file.getAbsolutePath());
                if ((fileType.equals("*") || ext.equalsIgnoreCase(fileType)) && readFromFile(file.getAbsolutePath()).contains(content)) {
                    return file;
                }
            } else if (file.isDirectory()) {
                return searchFileWithContent(file.getAbsolutePath(), content, fileType);
            }
        }

        return null;
    }

    public static File[] getFileList(String folderPath) throws IOException {
        return (new File(folderPath)).listFiles();
    }

    public static String convertToByteArrayString(File file) {
        String fileContent = null;

        try {
            byte[] byteArray = FileUtils.readFileToByteArray(file);
            fileContent = new String(Base64.encodeBase64(byteArray));
        } catch (IOException var3) {
            var3.printStackTrace(System.err);
            System.exit(-1);
        }

        return fileContent;
    }

    public File zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        String source = "";
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            try {
                source = sourceFolder.substring(sourceFolder.lastIndexOf(File.separator) + 1, sourceFolder.length());
            } catch (Exception var27) {
                source = sourceFolder;
            }

            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            Log.info("Output to Zip : " + zipFile);
            FileInputStream in = null;
            Iterator var7 = this.fileList.iterator();

            while(var7.hasNext()) {
                String file = (String)var7.next();
                Log.info("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);

                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);

                    int len;
                    while((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            Log.info("Folder successfully compressed");
        } catch (IOException var29) {
            var29.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException var26) {
                var26.printStackTrace();
            }

        }

        return new File(zipFile);
    }

    public void generateFileList(File node) {
        if (node.isFile()) {
            this.fileList.add(this.generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            String[] var3 = subNote;
            int var4 = subNote.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String filename = var3[var5];
                this.generateFileList(new File(node, filename));
            }
        }

    }

    private String generateZipEntry(String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
}
