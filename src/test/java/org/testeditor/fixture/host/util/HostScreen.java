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
