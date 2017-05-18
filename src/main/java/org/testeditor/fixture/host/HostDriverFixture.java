package org.testeditor.fixture.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testeditor.fixture.core.interaction.FixtureMethod;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;

/**
 * HostDriverFixture provides convenience methods for automating mainframe
 * functional tests. It consists of an API to deal with mainframe fields in
 * screens. As a test driver, we use the X3270 framework to communicate with the
 * mainframe which should be tested.
 *
 * @see <a href="http://x3270.bgp.nu/">http://x3270.bgp.nu/</a>
 */
public class HostDriverFixture {
  private static final Logger logger = LoggerFactory.getLogger(HostDriverFixture.class);
  private Connection connection;

  /**
   * The Connection is responsible for the subprocess of the s3270/ws3270
   * application. The whole communication will be done through this subprocess.
   *
   * @param s3270Path
   *          The path to the s3270/ws3270 application.
   * @param hostname
   *          The hostname to be connected to.
   * @param port
   *          The port of the host to be connected to.
   */
  @FixtureMethod
  public void connect(String s3270Path, String hostname, int port) {
    logger.info("Host-Fixture starting ...");
    TerminalType type = TerminalType.TYPE_3279;
    TerminalMode mode = TerminalMode.MODE_24x80;
    CharacterSet charSet = CharacterSet.CHAR_GERMAN_EURO;

    connection = new Connection();
    connection.connect(s3270Path, hostname, port, type, mode, charSet);
    boolean connected = connection.isConnected();
    if (connected) {
      logger.info("successfully connected to host='{}', port='{}'", hostname, port);
    } else {
      throw new RuntimeException("The connection to host '" + hostname + "' on port '" + port
          + "' could not be established.");
    }
  }

  /**
   * Disconnects from host.
   * {@link org.testeditor.fixture.host.net.Connection#disconnect()}
   *
   */
  @FixtureMethod
  public void disconnect() {
    logger.info("Connection closing ...");
    connection.disconnect();
  }

  /**
   * Provides the Status Objekt which will be returned when an input or action
   * is executed.
   *
   * @return {@link org.testeditor.fixture.host.s3270.Status}
   */
  @FixtureMethod
  public Status getStatus() {
    logger.info("get Status ...");
    return connection.getStatus();
  }

  /**
   * Provides a possibility to type a value into a specified field through the
   * parameters row an column.
   * <p>
   * Attention! The input field has to be unprotected and not hidden. If they
   * are, the s3270 emulation will lock further actions.
   *
   * @param value
   *          the value to be typed into the input field of a mainframe window.
   * @param row
   *          the row of the input field
   * @param col
   *          the column of the input field.
   */
  @FixtureMethod
  public void typeInto(String value, int row, int col) {
    setCursorPosition(row, col);
    connection.doCommand("String(\"" + value + "\")");
    connection.doCommand("ascii"); // just to see if typed in successfully.
  }

  private void setCursorPosition(int row, int col) {
    connection.doCommand("MoveCursor(" + row + "," + col + ")");
  }

}
