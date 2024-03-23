package org.example.utils;

import org.example.command.Command;
import org.example.command.ConcreteCommand.EditCommand.*;
import org.example.command.ConcreteCommand.ShowCommand.*;
import org.example.command.ConcreteCommand.WorkSpaceCommand.*;
import org.example.command.ConcreteCommand.FileCommand.SaveCommand;
import org.example.command.ConcreteCommand.LogCommand.HistoryCommand;
import org.example.command.ConcreteCommand.LogCommand.StatsCommand;
import org.example.command.ConcreteCommand.EditCommand.NothingCommand;
import org.example.observer.ConcreteObserver.HistoryLog;
import org.example.observer.ConcreteObserver.StatsLog;
import org.example.receiver.WorkSpaceManager;

import java.util.List;

public class GetCommand {
    public static Command getCommand(String cmd, List<String> cmd_args, WorkSpaceManager workSpaceManager, HistoryLog historyLog, StatsLog statsLog) {
        Command command;
        if((workSpaceManager.currentWorkspace==-1) && (!cmd.equals("load")) && (!cmd.equals("list-workspace")) && (!cmd.equals("open")) && (!cmd.equals("ls")) && (!cmd.equals("exit"))){
            System.out.println("暂未加载任何文件或工作区！");
            command = new NothingCommand();
            return command;
        }
        switch (cmd) {
            case "load" -> {
                if (cmd_args.size() > 0) {
                    command = new LoadCommand(cmd_args, workSpaceManager);
                } else {
                    command = new NothingCommand();
                    System.out.println(cmd + "参数不能为空");
                }
            }
            case "open" -> {
                if (cmd_args.size() > 0) {
                    command = new OpenCommand(cmd_args,workSpaceManager);
                } else {
                    command = new NothingCommand();
                    System.out.println(cmd + "参数不能为空");
                }

            }
            case "insert" -> {
                if (cmd_args.size() > 0) {
                    command = new InsertCommand(cmd_args,workSpaceManager);
                } else {
                    command = new NothingCommand();
                    System.out.println(cmd + "参数不能为空");
                }
            }
            case "append-head" -> {
                if (cmd_args.size() > 0) {
                    command = new AppendHeadCommand(cmd_args,workSpaceManager);
                } else {
                    command = new NothingCommand();
                    System.out.println(cmd + "参数不能为空");
                }
            }
            case "append-tail" -> {
                if (cmd_args.size() > 0) {
                    command = new AppendTailCommand(cmd_args, workSpaceManager);
                } else {
                    command = new NothingCommand();
                    System.out.println(cmd + "参数不能为空");
                }
            }
            case "delete" -> {
                if (cmd_args.size() > 0) {
                    command = new DeleteCommand(cmd_args, workSpaceManager);
                } else {
                    command = new NothingCommand();
                    System.out.println(cmd + "参数不能为空");
                }
            }
            case "undo" -> command = new UndoCommand(workSpaceManager);
            case "redo" -> command = new RedoCommand(workSpaceManager);
            case "list" -> command = new ListCommand(workSpaceManager);
            case "list-tree" -> command = new ListTreeCommand(workSpaceManager);
            case "dir-tree" -> command = new DirTreeCommand(cmd_args,workSpaceManager);
            case "ls" -> command = new LsCommand(workSpaceManager);
            case "history" -> command = new HistoryCommand(cmd_args, historyLog);
            case "stats" -> command = new StatsCommand(cmd_args, statsLog);
            case "exit" -> command = new ExitCommand(workSpaceManager);
            case "close" -> command = new CloseCommand(workSpaceManager);
            case "save" -> command = new SaveCommand(workSpaceManager);
            case "list-workspace" -> command = new ListWorkspaceCommand(workSpaceManager);
            default -> {
                System.out.println(cmd + " 不是有效命令");
                command = new HelpCommand();
            }
        }
        return command;
    }
}
