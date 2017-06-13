package org.testeditor.fixture.host;

import org.testeditor.fixture.core.interaction.FixtureMethod;
import org.testeditor.fixture.host.net.Connection;
import org.testeditor.fixture.host.s3270.Status;
import org.testeditor.fixture.host.s3270.options.CharacterSet;
import org.testeditor.fixture.host.s3270.options.TerminalMode;
import org.testeditor.fixture.host.s3270.options.TerminalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public HostDriverFixture() {
        this.connection = new Connection();
    }

    public HostDriverFixture(Connection connection) {
        this.connection = connection;
    }

    /**
     * The Connection is responsible for the subprocess of the s3270/ws3270
     * application. The whole communication will be done through this
     * subprocess.
     *
     * @param s3270Path
     *            The path to the s3270/ws3270 application.
     * @param hostname
     *            The hostname to be connected to.
     * @param port
     *            The port of the host to be connected to.
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

    /**
     * Disconnects from host.
     * {@link org.testeditor.fixture.host.net.Connection#disconnect()}
     *
     */
    @FixtureMethod
    public boolean disconnect() {
        logger.info("Connection closing ...");
        return connection.disconnect();
    }

    /**
     * Provides the Status Objekt which will be returned when an input or action
     * is is
     *
     * @return {@link org.testeditor.fixture.host.s3270.Status}
     */
    @FixtureMethod
    public Status getStatus() {
        logger.info("get Status ...");
        return connection.getStatus();
    }

    @FixtureMethod
    public void typeInto(String value, int row, int col) {
        logger.info("Set cursor position ...");
        connection.typeInto(value, new Locator(col, row));

    }

}
