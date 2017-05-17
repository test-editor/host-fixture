package org.testeditor.fixture.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;

public class HostDriverFixture {
  Connection con;

  Logger logger = LoggerFactory.getLogger(HostDriverFixture.class);

  /**
   *
   * @param s3270Path
   * @param hostname
   * @param port
   */
  public void connect(String s3270Path, String hostname, int port) {
    logger.info("Host-Fixture starting ...");
    TerminalType type = TerminalType.TYPE_3279;
    TerminalMode mode = TerminalMode.MODE_24x80;
    CharacterSet charSet = CharacterSet.CHAR_GERMAN_EURO;

    con = new Connection();
    con.connect(s3270Path, hostname, port, type, mode, charSet);
    boolean connected = con.isConnected();
    if (connected) {
      logger.info("Host-Fixture ready to rock ;O)");
    } else {
      logger.info("Host-Fixture is not ready to rock :O(");
    }
  }

  public void disconnect() {
    logger.info("Connection closing ...");
    con.disconnect();
  }

  public void getStatus() {
    logger.info("get Status ...");
    con.getStatus();
  }

  public void typeInto(String value, int row, int col) {
    logger.info("Set cursor position ...");
    con.typeInto(value, new Locator(col, row));

  }

}
