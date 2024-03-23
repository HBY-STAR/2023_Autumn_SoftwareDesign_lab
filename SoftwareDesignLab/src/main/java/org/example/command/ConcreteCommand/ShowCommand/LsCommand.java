package org.example.command.ConcreteCommand.ShowCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

public class LsCommand implements Command {
    private final WorkSpaceManager workSpaceManager;

    public LsCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.ls();
    }
}
