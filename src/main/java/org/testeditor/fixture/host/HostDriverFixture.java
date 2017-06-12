package org.testeditor.fixture.host;

import org.testeditor.fixture.core.interaction.FixtureMethod;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostDriverFixture {
    private static final Logger logger = LoggerFactory.getLogger(HostDriverFixture.class);

    private Connection connection;

    public HostDriverFixture() {
        this.connection = new Connection();
    }

    public HostDriverFixture(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @param s3270Path
     * @param hostname
     * @param port
     */
    @FixtureMethod
    public boolean connect(String s3270Path, String hostname, int port) {
        logger.info("Host-Fixture connecting ...");
        TerminalType type = TerminalType.TYPE_3279;
        TerminalMode mode = TerminalMode.MODE_24x80;
        CharacterSet charSet = CharacterSet.CHAR_GERMAN_EURO;
        connection.connect(s3270Path, hostname, port, type, mode, charSet);
        boolean connected = connection.isConnected();
        if (connected) {
            logger.info("successfully connected to host='{}', port='{}'", hostname, port);
            return true;
        } else {
            logger.info("The connection to host '" + hostname + "' on port '" + port + "' could not be established.");
            return false;
        }
    }

    @FixtureMethod
    public boolean disconnect() {
        logger.info("Connection closing ...");
        return connection.disconnect();
    }

    @FixtureMethod
    public void getStatus() {
        logger.info("get Status ...");
        connection.getStatus();
    }

    @FixtureMethod
    public void typeInto(String value, int row, int col) {
        logger.info("Set cursor position ...");
        connection.typeInto(value, new Locator(col, row));

    }

}
