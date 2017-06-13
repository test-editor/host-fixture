package org.testeditor.fixture.host.s3270;

import java.util.List;

/**
 * Represents the result of an s3270 / ws3270 command.
 */
public class Result {

    private final List<String> dataLines;
    private final String statusString;
    private Status status;

    public Result(final List<String> dataLines, final String statusString) {
        this.dataLines = dataLines;
        this.statusString = statusString;
    }

    public List<String> getDataLines() {
        return this.dataLines;
    }

    public String getStatusString() {
        return this.statusString;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
