package org.example.command.ConcreteCommand.ShowCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

public class ListCommand implements Command {
    private final WorkSpaceManager workSpaceManager;

    public ListCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.list();
    }
}
