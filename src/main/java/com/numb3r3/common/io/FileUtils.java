package com.numb3r3.common.io;

import com.google.common.io.PatternFilenameFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * Get the text after the final dot (.) in the file name.
     *
     * @param f The file to get the extension of.
     * @return The extension of File f.
     */
    public static String getExtension(File f) {
        String ext = null;

        String name = f.getName();
        int i = name.lastIndexOf('.');

        if (i > 0 && i < name.length() - 1) {
            ext = name.substring(i + 1).toLowerCase();
        }

        return ext;
    }

    /**
     * Writes all lines read from the reader.
     *
     * @param reader The source reader
     * @param writer The destination writer
     * @throws IOException
     */
    public static void pipe(Reader reader, Writer writer) throws IOException {
        pipe(reader, writer, 4092);
    }

    /**
     * Writes all lines read from the reader.
     *
     * @param reader     the source reader
     * @param writer     the destination writer
     * @param buffersize size of the buffer to use
     * @throws IOException
     */
    public static void pipe(Reader reader, Writer writer, int buffersize) throws IOException {
        char[] buffer = new char[buffersize];
        while (reader.read(buffer) != -1) {
            writer.write(buffer);
        }
    }

    /**
     * Writes all lines read from the reader.
     *
     * @param is The input stream
     * @param os The output stream
     * @throws IOException
     */
    public static void pipe(InputStream is, OutputStream os) throws IOException {
        pipe(is, os, 4092);
    }

    /**
     * Writes all lines read from the reader.
     *
     * @param is         The input stream
     * @param os         The output stream
     * @param buffersize size of the buffer to use
     * @throws IOException
     */
    public static void pipe(InputStream is, OutputStream os, int buffersize) throws IOException {
        byte[] buffer = new byte[buffersize];
        while (is.read(buffer) != -1) {
            os.write(buffer);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            OutputStream out = openOutputStream(file);
            out.close();
        }
        boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    public static String combine(final String... paths) {
        if (paths == null || paths.length == 0) {
            return null;
        }

        File file = new File(paths[0]);
        final int len = paths.length;
        for (int i = 1; i < len; i++) {
            file = new File(file, paths[i]);
        }

        return file.getPath();
    }

    /**
     * Return all files beneath path.
     *
     * @param path      the path to search
     * @param recursive iff true, search subdirectories too.
     * @return
     */
    public static ArrayList<File> find(File path, Boolean recursive) {
        ArrayList<File> files = new ArrayList<File>();
        find(files, path, recursive);
        return files;
    }

    /**
     * A private helper function for building the results for public Find.
     *
     * @param files
     * @param path
     * @param recursive
     */
    private static void find(List<File> files, File path, Boolean recursive) {
        if (path.isDirectory()) {
            // iterate over files
            for (File file : path.listFiles()) {
                if (recursive || !file.isDirectory()) {
                    find(files, file, recursive);
                }
            }
        } else {
            // process the file
            files.add(path);
        }
    }

    public static String backtrackToFile(String startDir, String targetFile) {
        FilenameFilter filenameFilter = new PatternFilenameFilter(targetFile);
        File currentDir = new File(startDir).getAbsoluteFile();

        while (currentDir.getParentFile() != null) {
            String[] files = currentDir.list(filenameFilter);
            if (files.length == 1) {
                return currentDir.getAbsolutePath();
            } else {
                currentDir = currentDir.getParentFile();
            }
        }

        return null;
    }
}
