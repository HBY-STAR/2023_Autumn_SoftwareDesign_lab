package org.example.command.ConcreteCommand.EditCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

import java.util.ArrayList;
import java.util.List;

public class AppendHeadCommand implements Command {
    private List<String> text =new ArrayList<>();
    private final WorkSpaceManager workSpaceManager;

    public AppendHeadCommand(List<String> cmd_args, WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
        text = cmd_args;
    }
    @Override
    public boolean execute(){
            return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.insert(1, text);
        }
}
