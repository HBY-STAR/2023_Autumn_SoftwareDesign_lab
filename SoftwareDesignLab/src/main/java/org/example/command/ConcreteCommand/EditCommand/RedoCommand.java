package org.example.command.ConcreteCommand.EditCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

public class RedoCommand implements Command {
    private final WorkSpaceManager workSpaceManager;

    public RedoCommand(WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute() {
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.redo();
    }
}
