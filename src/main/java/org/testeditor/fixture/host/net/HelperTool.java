package org.testeditor.fixture.host.net;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

/**
 * The HelperTool is just a container for convenience methods to proof
 * prerequisites for different testcases.
 */
public class HelperTool {

    private static String pathS3270;

    /**
     * @return true if the path to s3270 driver is not null and available, false
     *         otherwise.
     */
    public static boolean isS3270DriverPresent(String s3270Path) {
        if (s3270Path != null && !s3270Path.isEmpty()) {
            pathS3270 = s3270Path;
            return new File(pathS3270).exists();
        } else {
            return false;
        }
    }

    /**
     * @return true if OS = WINDOWS, false otherwise
     */
    public static boolean isOsWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    /**
     * @return String reperesenting the path to s3270 driver.exe
     */
    public static String getS3270DriverPath() {
        return pathS3270;
    }

}
