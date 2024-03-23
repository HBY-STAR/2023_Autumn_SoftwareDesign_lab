package org.example.command.ConcreteCommand.FileCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

public class SaveCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    public SaveCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }

    @Override
    public boolean execute(){
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.save();
    }
}
