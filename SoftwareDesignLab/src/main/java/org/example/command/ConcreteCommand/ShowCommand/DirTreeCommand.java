package org.example.command.ConcreteCommand.ShowCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

import java.util.List;

public class DirTreeCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    private final List<String> text;

    public DirTreeCommand(List<String> cmd_args, WorkSpaceManager workSpaceManager){
        this.text=cmd_args;
        this.workSpaceManager = workSpaceManager;
    }
    @Override
    public boolean execute(){
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.dir_tree(text);
    }
}
