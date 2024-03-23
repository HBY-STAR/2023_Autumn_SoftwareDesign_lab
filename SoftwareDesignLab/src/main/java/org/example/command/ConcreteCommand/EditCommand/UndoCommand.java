package org.example.command.ConcreteCommand.EditCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

//TODO
public class UndoCommand implements Command{
    private final WorkSpaceManager workSpaceManager;

    public UndoCommand(WorkSpaceManager workSpaceManager){ this.workSpaceManager = workSpaceManager; }
    @Override
    public boolean execute() {
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.undo();
    }
}
