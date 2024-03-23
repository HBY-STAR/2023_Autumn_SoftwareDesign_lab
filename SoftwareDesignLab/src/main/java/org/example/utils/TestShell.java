package org.example.utils;

import org.example.command.Command;
import org.example.invoker.Invoker;
import org.example.observer.ConcreteObserver.HistoryLog;
import org.example.observer.ConcreteObserver.StatsLog;
import org.example.receiver.WorkSpaceManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.utils.GetCommand.getCommand;

public class TestShell {
    public static void test_commands(String commands) {
        Command command;
        Invoker invoker = new Invoker();
        WorkSpaceManager workSpaceManager = WorkSpaceManager.getInstance();
        HistoryLog historyLog = new HistoryLog(invoker);
        StatsLog statsLog = new StatsLog(invoker);

        Scanner scanner = new Scanner(commands);
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
                invoker.setCommand(command, cmd,cmd_args);
                invoker.execCommand();
            }
        } while (scanner.hasNextLine());
    }

    public static String readFileToString(String filePath) {
        Path path = Paths.get(filePath);

        try {
            // 读取文件内容并转换为字符串
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 或者抛出异常，根据需要处理错误
        }
    }
}
