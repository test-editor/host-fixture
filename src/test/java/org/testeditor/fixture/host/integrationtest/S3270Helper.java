/*******************************************************************************
 * Copyright (c) 2012 - 2017 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 * itemis AG
 *******************************************************************************/
package org.testeditor.fixture.host.integrationtest;

import java.io.File;
import org.apache.commons.lang3.SystemUtils;

public class S3270Helper {

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
