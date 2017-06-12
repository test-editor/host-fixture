package org.testeditor.fixture.host.s3270.options;

public enum CharacterSet {

  /**
   * The EBCDIC-Character Set which the mainframe page uses. The list can be
   * reviewed under
   *
   * <a href=
   * "http://x3270.bgp.nu/ws3270-man.html#Character-Sets">http://x3270.bgp.nu/</a>
   * <ul>
   * <li>german-euro = 1141</li>
   * <li>german = 273</li>
   * <li>us-euro = 1140</li>
   * <li>US international = 037</li>
   * </ul>
   *
   */
  CHAR_GERMAN_EURO("german-euro"), CHAR_GERMAN("german"), CHAR_US_EURO("us-euro"), CHAR_US_INTL("us-intl");
  private String charSet;

  private CharacterSet(String charSet) {
    this.charSet = charSet;
  }

  public String getCharSet() {
    return charSet;
  }

}
