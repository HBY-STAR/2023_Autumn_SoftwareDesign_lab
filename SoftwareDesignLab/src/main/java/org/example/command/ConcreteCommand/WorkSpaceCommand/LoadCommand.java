package org.example.command.ConcreteCommand.WorkSpaceCommand;

import org.example.command.Command;
import org.example.receiver.WorkSpaceManager;

import java.util.List;

public class LoadCommand implements Command {
    private String file_path;
    private final WorkSpaceManager workSpaceManager;
    public LoadCommand(List<String> cmd_args, WorkSpaceManager workSpaceManager){
        this.workSpaceManager = workSpaceManager;
        if(cmd_args.size()>1){
            System.out.println("一次仅支持打开单个文件");
        } else{
            this.file_path=cmd_args.get(0);
        }
    }
    @Override
    public boolean execute(){
        if(file_path==null){
            return false;
        }
        return workSpaceManager.load(file_path);
    }
}
