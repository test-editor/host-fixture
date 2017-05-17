package org.testeditor.fixture.host.s3270;

import java.util.List;

/**
 * Represents the result of an s3270 / ws3270 command.
 */
public class Result {

  private final List<String> data;
  private final String status;

  public Result(final List<String> data, final String status) {
    this.data = data;
    this.status = status;
  }

  public List<String> getData() {
    return data;
  }

  public String getStatus() {
    return status;
  }
}
