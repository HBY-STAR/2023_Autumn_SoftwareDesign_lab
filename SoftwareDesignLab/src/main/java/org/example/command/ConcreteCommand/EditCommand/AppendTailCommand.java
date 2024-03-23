package org.example.command.ConcreteCommand.EditCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

import java.util.ArrayList;
import java.util.List;

public class AppendTailCommand implements Command {
    private int row;
    private List<String> text =new ArrayList<>();
    private final WorkSpaceManager workSpaceManager;

    public AppendTailCommand(List<String> cmd_args, WorkSpaceManager workSpaceManager){
        this.workSpaceManager=workSpaceManager;
        row = this.workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.doc_lines.size()+1;
        text = cmd_args;
    }
    @Override
    public boolean execute() {
        return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.insert(row, text);
    }
}
