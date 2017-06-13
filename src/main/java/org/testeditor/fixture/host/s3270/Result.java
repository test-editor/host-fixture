package org.testeditor.fixture.host.s3270;

import java.util.List;

/**
 * Represents the result of an s3270 / ws3270 command.
 */
public class Result {

<<<<<<< HEAD
  private final List<String> data;
  private final String statusString;
  private Status status;

  public Result(final List<String> data, final String statusString) {
    this.data = data;
    this.statusString = statusString;
  }

  public List<String> getData() {
    return this.data;
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
=======
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
>>>>>>> feature/newStructure
}
