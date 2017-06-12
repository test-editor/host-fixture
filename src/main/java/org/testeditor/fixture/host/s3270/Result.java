package org.testeditor.fixture.host.s3270;

import java.util.List;

/**
 * Represents the result of an s3270 / ws3270 command.
 */
public class Result {

    private final List<String> dataLines;
    private final String status;

    public Result(final List<String> dataLines, final String status) {
        this.dataLines = dataLines;
        this.status = status;
    }

    public List<String> getDataLines() {
        return dataLines;
    }

    public String getStatus() {
        return status;
    }
}
