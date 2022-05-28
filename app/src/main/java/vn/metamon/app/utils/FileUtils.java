package vn.metamon.app.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public final class FileUtils {

    public static final String DIRECTORY_METAMON = "metamon";

    public static final String ZIP_EXTENSION = ".zip";

    private FileUtils() {
        throw new IllegalStateException("Cannot instantiate object of utility class");
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath;
        if (PermissionUtils.hasExternalStoragePermission(context)) {
            // Check if media is mounted or storage is built-in, if so, try and use external cache dir
            // otherwise use internal cache dir
            final boolean isMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
            final boolean useExternal = isMounted || !isExternalStorageRemovable();
            cachePath = useExternal ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (PlatformUtils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (PlatformUtils.hasFroyo()) {
            return context.getExternalCacheDir();
        }
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }


    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (PlatformUtils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    public static String getFilePath(File file) {
        return file == null ? "" : file.getPath();
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /*  Create Directory where screenshot will save for sharing screenshot  */
    public static File getFolderStorageDir(String folderName) {
        // Get the directory for the user's public files directory.
        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!file.mkdirs()) {
            Log.e("FileUtils", "Directory not created");
        }
        return file;
    }

    /*  Store taken screenshot into above created path  */
    public static File store(@NonNull Bitmap bitmap, String fileName, File saveFilePath, int quality) {
        if (!isExternalStorageWritable() || bitmap.isRecycled()) {
            return null;
        }
        File dir = new File(saveFilePath.getAbsolutePath());
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
        }
        File file = new File(saveFilePath.getAbsolutePath(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return file;
    }



    public static List<File> getDirectoriesInPath(File dir) {
        List<File> subDirectories = new ArrayList<>();

        File[] listFiles = dir.listFiles();

        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    subDirectories.add(file);
                }
            }
        }

        return subDirectories;
    }

    public static List<File> getDirectoriesInPath(String path) {
        return getDirectoriesInPath(new File(path));
    }

    private static byte[] createChecksum(String filename) {
        MessageDigest complete;
        InputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            return complete.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IoUtils.safeClose(fis);
        }
    }

    public static String getMD5ChecksumOfFile(String filename) {
        byte[] b = createChecksum(filename);
        if (b == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < b.length; i++) {
            sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static boolean downloadFile(String downloadUrl, String filePath) {
        final String TAG = "GiftStory";
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        File zipFile = new File(filePath);

        try {
            File parentFile = zipFile.getParentFile();
            if (!parentFile.exists()) {
                boolean canMkdirs = parentFile.mkdirs();
                if (!canMkdirs) {
                    return false;
                }
            }

            URL url = new URL(downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            int contentLength = connection.getContentLength();
            if (contentLength > getUsableSpace(parentFile)) {
                return false;
            }

            is = connection.getInputStream();
            os = new FileOutputStream(zipFile);

            byte[] data = new byte[4096];
            int count;

            while ((count = is.read(data)) != -1) {
                os.write(data, 0, count);
            }

            os.flush();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        } finally {
            IoUtils.safeClose(is);
            IoUtils.safeClose(os);

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static boolean extractZipFile(String zipFilePath, String destFilePath) {
        final String TAG = "GiftStory";

        InputStream is = null;
        ZipInputStream zis = null;

        try {
            File destFile = new File(destFilePath);
            if (!destFile.exists()) {
                destFile.mkdirs();
            }

            is = new FileInputStream(zipFilePath);
            zis = new ZipInputStream(is);

            ZipEntry ze;

            byte[] buffer = new byte[4096];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getSize() > getUsableSpace(destFile.getParentFile())) {
                    zis.closeEntry();
                    return false;
                }

                String name = ze.getName();

                if (ze.isDirectory()) {
                    new File(destFile, name).mkdirs();
                    continue;
                }

                FileOutputStream fos = new FileOutputStream(new File(destFilePath, name));

                while ((count = zis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }

                IoUtils.safeClose(fos);
                zis.closeEntry();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            IoUtils.safeClose(is);
            IoUtils.safeClose(zis);
        }
    }

    public static String readTextFile(File fileToRead) {
        StringBuilder sb = new StringBuilder();

        if (fileToRead.exists() && !fileToRead.isDirectory()) {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(fileToRead);
                br = new BufferedReader(fr);
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IoUtils.safeClose(br);
            }
        }

        return sb.toString();
    }

    public static String readTextFile(String filePath) {
        return readTextFile(new File(filePath));
    }

    public static boolean writeTextFile(File fileToWrite, String text) {
        File parentFile = fileToWrite.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            if (!fileToWrite.exists()) {
                boolean canCreateFile = fileToWrite.createNewFile();
                if (!canCreateFile) {
                    parentFile.delete();
                    return false;
                }
            }

            fw = new FileWriter(fileToWrite, false);
            bw = new BufferedWriter(fw);

            bw.write(text);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            parentFile.delete();
            return false;
        } finally {
            IoUtils.safeClose(bw);
        }
    }

    public static boolean writeTextFile(String filePath, String text) {
        return writeTextFile(new File(filePath), text);
    }
}
