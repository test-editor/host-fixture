package org.testeditor.fixture.host.s3270.statusformat;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;

/**
 * The ScreenFormatting is defined as follows:
 * <ul>
 * <li>F - If the screen is formatted.</li>
 * <li>U - If unformatted or in NVT mode.</li>
 * </ul>
 *
 * See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">ScreenFormatting<a/>
 */
public enum ScreenFormatting {
  FORMATTED("F"), UNFORMATTED("U");
  private String formatting;

  private ScreenFormatting(String formatting) {
    this.formatting = formatting;
  }

  public String getFormatting() {
    return formatting;
  }

  public static ScreenFormatting getScreenFormatting(String input) {
    ScreenFormatting[] values = ScreenFormatting.values();
    for (ScreenFormatting screenFormatting : values) {

      if (screenFormatting.getFormatting().equals(input)) {
        return screenFormatting;
      }
    }
    throw new StatusNotFoundException("ScreenFormatting format " + input + " is unknown!");
  }

}
