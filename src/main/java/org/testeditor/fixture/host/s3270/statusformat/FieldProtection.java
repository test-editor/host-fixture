package org.testeditor.fixture.host.s3270.statusformat;

import org.testeditor.fixture.host.exceptions.StatusNotFoundException;

/**
 * The EmulatorMode is defined as follows:
 * <ul>
 * <li>P - if the field containing the cursor is protected.</li>
 * <li>U - if unprotected or unformatted.</li>
 * </ul>
 *
 * See <a href=
 * "http://x3270.bgp.nu/x3270-script.html#Status-Format">FieldProtection<a/>
 *
 */
public enum FieldProtection {
  PROTECTED("P"), UNPROTECTED("U");
  private String protection;

  private FieldProtection(String protection) {
    this.protection = protection;
  }

  public String getProtection() {
    return protection;
  }

  public static FieldProtection getFieldProtection(String input) {
    FieldProtection[] values = FieldProtection.values();
    for (FieldProtection fieldProtection : values) {
      if (fieldProtection.getProtection().equals(input)) {
        return fieldProtection;
      }
    }
    throw new StatusNotFoundException("FieldProtection state " + input + " is unknown!");
  }
}
