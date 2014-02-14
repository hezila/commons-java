package com.numb3r3.common.io;

import com.google.common.io.PatternFilenameFilter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

    public static void touch_dir(String path) throws IOException {
        File theDir = new File(path);
        // if the directory does not exist, create it
        if (!theDir.exists()) {

            boolean success = (new File(path)).mkdirs();
            if (!success) {
                throw new IOException("Unable to create the fold " + path);
            }
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
    public static ArrayList<File> find(String path, Boolean recursive) {
        return find(new File(path), recursive);
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

    // readLines
    //-----------------------------------------------------------------------
    /**
     * Get the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input  the <code>InputStream</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static List<String> readLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        return readLines(reader);
    }

    /**
     * Get the contents of an <code>InputStream</code> as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     *
     * @param input  the <code>InputStream</code> to read from, not null
     * @param encoding  the encoding to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        if (encoding == null) {
            return readLines(input);
        } else {
            InputStreamReader reader = new InputStreamReader(input, encoding);
            return readLines(reader);
        }
    }

    /**
     * Get the contents of a <code>Reader</code> as a list of Strings,
     * one entry per line.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedReader</code>.
     *
     * @param input  the <code>Reader</code> to read from, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List list = new ArrayList();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    /**
     * Reads file completely and returns the contents as a string.
     * @param fileName
     * @return file contents
     * @throws IOException
     */
    public static String readToString(String fileName) throws IOException {
        File f =  new File(fileName);
        byte[] bytes = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);

        int tot = 0;
        while(tot < bytes.length) {
            tot += fis.read(bytes, tot, bytes.length - tot);
        }

        fis.close();
        return new String(bytes);
    }


    public static boolean deleteFolder(File folder) {
        return deleteFolderContents(folder) && folder.delete();
    }

    public static boolean deleteFolderContents(File folder) {
        System.out.println("Deleting content of: " + folder.getAbsolutePath());
        File[] files = folder.listFiles();
        if (files == null) return true;
        for (File file : files) {
            if (file.isFile()) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                if (!deleteFolder(file)) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void writeBytesToFile(byte[] bytes, String destination) {
        try {
            FileChannel fc = new FileOutputStream(destination).getChannel();
            fc.write(ByteBuffer.wrap(bytes));
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
