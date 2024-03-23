package org.example.command.ConcreteCommand.WorkSpaceCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

public class ListWorkspaceCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    public ListWorkspaceCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.list_workspace();
    }
}
