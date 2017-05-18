package org.testeditor.fixture.host.exceptions;

public class StatusNotFoundException extends RuntimeException {

  /**
   * Indicates that the name of the state could not be resolved.
   */
  private static final long serialVersionUID = 8794743317555774095L;

  private String state;

  public StatusNotFoundException(final String state) {
    super(state);
    this.state = state;
  }

  /**
   * Returns the name of the state that could not be resolved.
   */
  public String getState() {
    return state;
  }

}
