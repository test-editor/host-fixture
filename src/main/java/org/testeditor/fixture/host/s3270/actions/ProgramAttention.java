package org.testeditor.fixture.host.s3270.actions;

import org.testeditor.fixture.host.exceptions.UnknownCommandException;

public enum ProgramAttention {

    PA_1("PA(1)"), PA_2("PA(2)"), PA_3("PA(3)");

    private String command;

    private ProgramAttention(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static ProgramAttention getControlCommand(String input) {
        ProgramAttention[] values = ProgramAttention.values();
        for (ProgramAttention attentionCommand : values) {
            if (attentionCommand.getCommand().equals(input)) {
                return attentionCommand;
            }
        }
        throw new UnknownCommandException("ProgramAttention " + input + " is unknown!");
    }

}
