package org.testeditor.fixture.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostDriverFixture {

    Logger logger = LoggerFactory.getLogger(HostDriverFixture.class);

    public void connect() {
        logger.info("Connection accomplished");
    }

    public void disconnect() {
        logger.info("Connection closed");
    }

    public void getStatus() {
        logger.info("get Status ...");
    }

    public void getTextOnPosition() {
        logger.info("get Text Position ...");
    }

    public boolean isTextOnScreen() {
        logger.info("get Text On Screen ...");
        return false;
    }

    private boolean isPositionReached() {
        logger.info("Position reached ...");
        return false;
    }

    public void sendKeys() {
        logger.info("Send Key ...");
    }

    public void setCursorPosition(int row, int col) {
        logger.info("Set cursor position ...");
    }

    public void typeInto(String value, int row, int col) {
        logger.info("Set cursor position ...");

    }

}
