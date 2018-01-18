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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerMillis() {

        // given
        long low = 998;
        long high = 1002;
        final int TIME_TO_WAIT = 1000001;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Timer timer = new Timer(timeUnit);

        // when
        timer.startTimer();
        methodToBeTimed(TIME_TO_WAIT, TimeUnit.MICROSECONDS);
        timer.stopTimer();
        long elapsedTime = timer.getElapsedTime();
        System.out.println(elapsedTime);

        // then
        Assert.assertThat(elapsedTime, allOf(greaterThan(low), lessThan(high)));
    }

    @Test
    public void testTimerSeconds() {

        // given
        final long ELAPSED_TIME = 1;
        final int TIME_TO_WAIT = 1200;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        Timer timer = new Timer(timeUnit);

        // when
        timer.startTimer();
        methodToBeTimed(TIME_TO_WAIT, TimeUnit.MILLISECONDS);
        timer.stopTimer();
        long elapsedTime = timer.getElapsedTime();

        // then
        Assert.assertEquals(ELAPSED_TIME, elapsedTime);
    }

    @Test
    public void testTimerMicroSeconds() {

        // given
        final long ELAPSED_TIME = 1;
        final int TIME_TO_WAIT = 1932000;
        final TimeUnit timeUnitToWait = TimeUnit.MICROSECONDS;

        TimeUnit timeUnitToMeasure = TimeUnit.SECONDS;
        Timer timer = new Timer(timeUnitToMeasure);

        // when
        timer.startTimer();
        methodToBeTimed(TIME_TO_WAIT, timeUnitToWait);
        timer.stopTimer();
        long elapsedTime = timer.getElapsedTime();

        // then
        Assert.assertEquals(ELAPSED_TIME, elapsedTime);
    }

    @Test
    public void testTwoTimerStarts() {

        // given
        final long ELAPSED_TIME = 1;
        final int TIME_TO_WAIT = 1932000;
        final TimeUnit timeUnitToWait = TimeUnit.MICROSECONDS;

        TimeUnit timeUnitToMeasure = TimeUnit.SECONDS;
        Timer timer = new Timer(timeUnitToMeasure);

        // when
        timer.startTimer();
        methodToBeTimed(TIME_TO_WAIT, timeUnitToWait);
        timer.stopTimer();
        long elapsedTime = timer.getElapsedTime();

        timer.startTimer();
        methodToBeTimed(TIME_TO_WAIT, timeUnitToWait);
        timer.stopTimer();
        long elapsedTime2 = timer.getElapsedTime();

        // then
        Assert.assertEquals(ELAPSED_TIME, elapsedTime2);
    }

    private void methodToBeTimed(int i, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
