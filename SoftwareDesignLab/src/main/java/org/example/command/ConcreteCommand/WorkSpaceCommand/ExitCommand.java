package org.example.command.ConcreteCommand.WorkSpaceCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

public class ExitCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    public ExitCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.exit();
    }
}
