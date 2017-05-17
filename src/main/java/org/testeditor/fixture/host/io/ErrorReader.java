package org.testeditor.fixture.host.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ErrorReader extends Thread {

  private String message = null;
  private BufferedReader err = null;
  private Process process;

  public ErrorReader(Process proc) {
    this.process = proc;
    run();
  }

  @Override
  public void run() {

    err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    try {
      while (true) {
        String msg = err.readLine();
        if (msg == null) {
          break;
        }
        setMessage(msg);
      }
    } catch (final IOException ex) {
      // ignore
    }
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message
   *          the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

}
