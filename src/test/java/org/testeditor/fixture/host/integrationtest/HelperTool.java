package org.testeditor.fixture.host.integrationtest;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

/**
 * The HelperTool is just a container for convenience methods to proof
 * prerequisites for different testcases.
 */
public class HelperTool {

    /**
     * @return true if the path to s3270 driver is not null and available, false
     *         otherwise.
     */
    public static boolean isS3270DriverPresent(String s3270Path) {
        if (s3270Path != null && !s3270Path.isEmpty()) {
            return new File(s3270Path).exists();
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

}
