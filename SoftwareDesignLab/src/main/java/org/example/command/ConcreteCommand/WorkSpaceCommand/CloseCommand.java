package org.example.command.ConcreteCommand.WorkSpaceCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

import java.util.List;

public class CloseCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    public CloseCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.close();
    }
}
