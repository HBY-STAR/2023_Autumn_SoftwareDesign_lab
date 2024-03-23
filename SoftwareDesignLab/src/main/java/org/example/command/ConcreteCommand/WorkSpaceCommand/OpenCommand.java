package org.example.command.ConcreteCommand.WorkSpaceCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

import java.util.List;

public class OpenCommand implements Command {
    private String workspace_name;
    private final WorkSpaceManager workSpaceManager;
    public OpenCommand(List<String> cmd_args, WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
        if(cmd_args.size()>1){
            System.out.println("仅支持单个工作区");
        } else{
            this.workspace_name=cmd_args.get(0);
        }
    }
    @Override
    public boolean execute(){
        if(workspace_name==null){
            return false;
        }
        return workSpaceManager.open(workspace_name);
    }
}
