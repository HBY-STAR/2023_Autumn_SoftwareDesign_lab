package org.example.command.ConcreteCommand.EditCommand;

import org.example.command.Command;
import org.example.receiver.Document;
import org.example.receiver.WorkSpaceManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements Command {
    private final WorkSpaceManager workSpaceManager;
    private int row;
    private List<String> text =new ArrayList<>();

    boolean use_row;

    public DeleteCommand(List<String> cmd_args, WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
        String str_row = cmd_args.get(0);
        try{
            row = Integer.parseInt(str_row);
            use_row = true;
        }catch(NumberFormatException e)
        {
            text = cmd_args;
            use_row =false;
        }
    }
    @Override
    public boolean execute(){
        if(use_row){
            return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.delete_row(row);
        }else{
            return workSpaceManager.workSpaceList.get(workSpaceManager.currentWorkspace).document.delete_text(text);
        }
    }
}
