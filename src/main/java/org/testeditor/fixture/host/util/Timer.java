/*******************************************************************************
 * Copyright (c) 2012 - 2018 Signal Iduna Corporation and others.
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

package org.testeditor.fixture.host.util;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;

/**
 * A {@code Timer} represents an utility for measuring time durations. The
 * elapsed time (in milliseconds) can be received for further processing.
 * 
 *
 */
public class Timer {

    private TimeUnit timeUnit;

    public Timer(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    Stopwatch sw;
    long elapsedTime;

    public void startTimer() {
        sw = Stopwatch.createStarted();
    }

    public void stopTimer() {
        if (sw == null) {
            throw new RuntimeException("Timer not started yet");
        }
        elapsedTime = sw.elapsed(timeUnit);
    }

    /**
     * @return the elapsedMillis
     */
    public long getElapsedTime() {
        return elapsedTime;
    }
}
