package org.example.command.ConcreteCommand.ShowCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

public class ListTreeCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    public ListTreeCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.list_tree();
    }
}
