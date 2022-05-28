package vn.metamon.app.utils;

import java.io.Closeable;


public final class IoUtils {

    private IoUtils() {
        throw new IllegalStateException("Cannot instantiate object of utility class");
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignored
            }
        }
    }
}