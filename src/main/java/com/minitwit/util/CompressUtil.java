package com.minitwit.util;

import org.zeroturnaround.zip.FileSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CompressUtil {

    public static void zipFolder(String zipFilePath, String folderPath) {

        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new RuntimeException("Not a folder:" + folderPath);
        }

        File zipFile = checkTargetZipOutput(zipFilePath);

        ZipUtil.pack(folder, zipFile);
    }

    private static File checkTargetZipOutput(String zipFilePath) {
        if (!zipFilePath.endsWith(".zip")) {
            throw new RuntimeException("Must be a zip extension:" + zipFilePath);
        }

        File zipFile = new File(zipFilePath);
        if (zipFile.isDirectory()) {
            throw new RuntimeException("Not a valid file:" + zipFilePath);
        }
        return zipFile;
    }

    // Create a default zip name "folder.zip"
    public static void zipFolder(String folderPath) {
        // e.g /user/folder or /user/folder/ --> /user/folder.zip
        String targetFolder = folderPath.replaceAll("\\*$|/*$", "");

        zipFolder(folderPath, targetFolder + ".zip");
    }


    // Notice: All file name must be unique
    public static void zip(
            String zipFilePath,
            String filePath,
            String... moreFilePaths) {

        File zipFile = checkTargetZipOutput(zipFilePath);

        String[] pathsToZip = concat(filePath, moreFilePaths);

        Set<String> uniqueFileNames = Arrays.stream(pathsToZip).
                map(
                    path ->
                        new File(path).getName()).
                collect(Collectors.toSet());

        if (uniqueFileNames.size() != pathsToZip.length) {
            throw new RuntimeException("Files to zip should NOT be have the same file name");
        }

        ZipEntrySource[] entries = new ZipEntrySource[pathsToZip.length];
        for (int i = 0; i < pathsToZip.length; i++) {
            File theFileMustExist = new File(pathsToZip[i]);
            if (!theFileMustExist.exists()) {
                throw new RuntimeException("File not exist:" + pathsToZip[i]);
            }
            if (!theFileMustExist.isFile()) {
                throw new RuntimeException("File may be a folder:" + pathsToZip[i]);
            }
            entries[i] = new FileSource(theFileMustExist.getName(), theFileMustExist);
        }

        ZipUtil.pack(entries, zipFile);
    }


    @SuppressWarnings("unchecked")
    private static<T> T[] concat(T atleastOne, T[] others) {

        T[] result = (T[])Array.newInstance(atleastOne.getClass(), others.length + 1);

        result[0] = atleastOne;
        System.arraycopy(others, 0, result, 1, others.length);

        return result;
    }

    public static void main(String[] args) {
        zip("/Users/fw/Downloads/塗り絵.zip",
            "/Users/fw/Downloads/塗り絵/pr52.pdf",
            "/Users/fw/Downloads/塗り絵/pr53.pdf");
    }
}
