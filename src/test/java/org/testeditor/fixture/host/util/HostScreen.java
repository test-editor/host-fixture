/*******************************************************************************
 * Copyright (c) 2012 - 2017 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 * itemis AG
 *******************************************************************************/
package org.testeditor.fixture.host.util;

import java.util.ArrayList;
import java.util.List;

public class HostScreen {

    /**
     * The x3270 application starts at the first left corner with the position
     * value (0;0) for the start column and start row. So the maximum row and
     * column for a standard Terminal Mode 24x80 screen size is (23;79).
     * 
     * @return List of a template mainframe screen for testing purposes.
     */
    public List<String> createHostScreen() {

        List<String> lines = new ArrayList<String>();
        lines.add("data:  01/01/2000 14:27:36        AMD MENUE FOR ABCDEFGHIJ          Panelid  - PAN1234");
        lines.add("data:  Coroneradmin = TROLL                                         Terminal - TM12345");
        lines.add("data:  Menukey     = KANONE      Printkey  = NONE                   Modell   - 3278-00");
        lines.add("data:  Hitkey       = PF6         Printer   = LPT1234               System   - DONOTIN");
        lines.add("data:  Cmdprefix    = $                                             Node     - ABCDEFG");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:     Session     PF-Key    Description                                           ");
        lines.add("data:                                                                                 ");
        lines.add("data: _ ABC         PF 1      ABC                                                     ");
        lines.add("data: _ DEF         PF 2      DEF                                                     ");
        lines.add("data: _ GHI         PF 3      GHI                                                     ");
        lines.add("data: _ JKL         PF 4      JKL                                                     ");
        lines.add("data: _ MNO         PF 5      MNO                                                     ");
        lines.add("data: _ PQR         PF 6      PQR                                                     ");
        lines.add("data: _ STY         PF 7      STY                                                     ");
        lines.add("data: _ ZKI         PF 8      ZKI                                                     ");
        lines.add("data: _ CBA         PF 9      CBA                                                     ");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:  COMMAND  ===> @K                                                               ");
        lines.add("data:   PF1=RIGHT  PF2=LEFT  PF2=KEYS   PF3=FORWARD   PF4=BACK  PF5=HELP              ");
        return lines;
    }

    /**
     * The x3270 application starts at the first left corner with the position
     * value (0;0) for the start column and start row. So the maximum row and
     * column for a standard Terminal Mode 24x80 screen size is (23;79).
     * 
     * @return List of a template mainframe screen as Hex-Values for testing
     *         purposes.
     */
    public List<String> createHostScreenAsHex() {

        List<String> lines = new ArrayList<String>();
        lines.add(
                "data: SF(c0=f0) c3b6 30 31 2f 30 31 2f 32 30 30 30 20 31 34 3a 32 37 3a 33 36 SF(c0=f0) 20 20 20 SF(c0=f0) 41 4d 44 20 4d 45 4e 55 45 20 46 4f 52 20 41 42 43 44 45 46 47 48 49 4a SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 SF(c0=f0) 50 61 6e 65 6c 69 64 SF(c0=f0) 20 20 SF(c0=f0) 2d 20 50 41 4e 31 32 33 34 ");
        lines.add(
                "data: SF(c0=f0) 20 43 6f 72 6f 6e 65 72 61 64 6d 69 6e 20 3d 20 54 52 4f 4c 4c SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 SF(c0=f0) 54 65 72 6d 69 6e 61 6c 20 SF(c0=f0)2d 20 54 4d 31 32 33 34 35 ");
        lines.add(
                "data: SF(c0=f0) 4f 4e 45 20 20 20 20 20 20 SF(c0=f0) 50 72 69 6e 74 6b 65 79 SF(c0=f0) 20 20 SF(c0=f0) 3d 20 4e 4f 4e 45 SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 SF(c0=f0) 4d 6f 64 65 6c 6c 20 20 20 SF(c0=f0) 2d 20 33 32 37 38 2d 30 30 ");
        lines.add(
                "data: SF(c0=f0) 20 43 6d 64 70 72 65 66 69 78 SF(c0=f0) 20 20 20 20 SF(c0=f0) 3d 20 24 SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 SF(c0=f0) 4e 6f 64 65 20 20 20 20 20 SF(c0=f0) 2d 20 41 42 43 44 45 46 47 ");
        lines.add(
                "data: SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        lines.add(
                "data: SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        lines.add(
                "data: SF(c0=f0) 20 20 20 20 20 53 65 73 73 69 6f 6e SF(c0=f0) 20 20 20 20 20 SF(c0=f0) 50 46 2d 4b 65 79 SF(c0=f0) 20 20 20 20 SF(c0=f0) 44 65 73 63 72 69 70 74 69 6f 6e SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        lines.add(
                "data: SF(c0=f0) 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        lines.add(
                "data: SF(c0=f0) 5f 20 41 42 43 SF(c0=f0) 20 20 20 20 20 20 20 20 20 SF(c0=f0) 50 46 SF(c0=f0) 20 31 SF(c0=f0) 20 20 20 20 20 20 SF(c0=f0) 41 42 43 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        lines.add(
                "data: SF(c0=f0) 5f 20 44 45 46 SF(c0=f0) 20 20 20 20 20 20 20 20 20 SF(c0=f0) 50 46 SF(c0=f0) 20 32 SF(c0=f0) 20 20 20 20 20 20 SF(c0=f0) 44 45 46 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        lines.add(
                "data: SF(c0=f0) 5f 20 47 48 49 SF(c0=f0) 20 20 20 20 20 20 20 20 20 SF(c0=f0) 50 46 SF(c0=f0) 20 33 SF(c0=f0) 20 20 20 20 20 20 SF(c0=f0) 47 48 49 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 ");
        return lines;
    }

    public List<String> createHostScreenWithStatusLine() {

        List<String> lines = new ArrayList<String>();
        lines.add("data:  01/01/2000 14:27:36        AMD MENUE FOR ABCDEFGHIJ          Panelid  - PAN1234");
        lines.add("data:  Coroneradmin = TROLL                                         Terminal - TM12345");
        lines.add("data:  Menukey     = KANONE      Printkey  = NONE                   Modell   - 3278-00");
        lines.add("data:  Hitkey       = PF6         Printer   = LPT1234               System   - DONOTIN");
        lines.add("data:  Cmdprefix    = $                                             Node     - ABCDEFG");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:     Session     PF-Key    Description                                           ");
        lines.add("data:                                                                                 ");
        lines.add("data: _ ABC         PF 1      ABC                                                     ");
        lines.add("data: _ DEF         PF 2      DEF                                                     ");
        lines.add("data: _ GHI         PF 3      GHI                                                     ");
        lines.add("data: _ JKL         PF 4      JKL                                                     ");
        lines.add("data: _ MNO         PF 5      MNO                                                     ");
        lines.add("data: _ PQR         PF 6      PQR                                                     ");
        lines.add("data: _ STY         PF 7      STY                                                     ");
        lines.add("data: _ ZKI         PF 8      ZKI                                                     ");
        lines.add("data: _ CBA         PF 9      CBA                                                     ");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:                                                                                 ");
        lines.add("data:  COMMAND  ===> @K                                                               ");
        lines.add("data:   PF1=RIGHT  PF2=LEFT  PF2=KEYS   PF3=FORWARD   PF4=BACK  PF5=HELP              ");
        lines.add("U F U C(tn3270si.hh.signal-iduna.de) I 2 24 80 2 17 0x0 -");
        lines.add("ok");
        return lines;
    }

}
