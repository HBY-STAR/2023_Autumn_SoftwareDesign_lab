package org.example;

import org.example.command.Command;
import org.example.invoker.Invoker;
import org.example.observer.ConcreteObserver.HistoryLog;
import org.example.observer.ConcreteObserver.StatsLog;
import org.example.receiver.WorkSpaceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.utils.GetCommand.getCommand;

public class Shell {
    public static void main(String[] args) {
        System.out.println("Hello! Input help to get some information");
        System.out.print(">>>");

        //初始化命令，调用者，接收者
        Command command;
        Invoker invoker = new Invoker();
        WorkSpaceManager workSpaceManager = WorkSpaceManager.getInstance();

        //日志及统计模块
        HistoryLog historyLog = new HistoryLog(invoker);
        StatsLog statsLog = new StatsLog(invoker);

        Scanner scanner = new Scanner(System.in);
        do {
            String cmd;
            List<String> cmd_args = new ArrayList<>();

            Scanner cmd_scanner = new Scanner(scanner.nextLine());
            if (cmd_scanner.hasNext()) {
                cmd = cmd_scanner.next();
                while (cmd_scanner.hasNext()) {
                    cmd_args.add(cmd_scanner.next());
                }
                command = getCommand(cmd, cmd_args, workSpaceManager, historyLog, statsLog);
                invoker.setCommand(command,cmd,cmd_args);
                invoker.execCommand();
            } else {
                System.out.println("输入为空!");
            }
            System.out.print(">>>");
        }
        while (scanner.hasNextLine());
    }
}
