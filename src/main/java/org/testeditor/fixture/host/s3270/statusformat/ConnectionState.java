package org.testeditor.fixture.host.s3270.statusformat;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;

/**
 *
 * The ConnectionState is defined as follows:
 * <ul>
 * <li>C(hostname) - If connected to a host.</li>
 * <li>N - otherwise.</li>
 * </ul>
 *
 * See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">ConnectionState<a/>
 *
 */
public enum ConnectionState {
  CONNECTED("C"), NOT_CONNECTED("N");
  private String state;

  private ConnectionState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }

  public static ConnectionState getConnectionState(String input) {
    ConnectionState[] values = ConnectionState.values();
    for (ConnectionState connectionState : values) {
      if (connectionState.getState().equals(input)) {
        return connectionState;
      }
    }
    throw new StatusNotFoundException("FieldProtection state " + input + " is unknown!");
  }

}
