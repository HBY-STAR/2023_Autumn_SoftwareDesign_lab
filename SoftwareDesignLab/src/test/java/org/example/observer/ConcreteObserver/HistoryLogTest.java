package org.example.observer.ConcreteObserver;

import org.example.receiver.WorkSpaceManager;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.shell.TestCaseLab1.clean_num;
import static org.example.shell.TestCaseLab2.string_format_date;
import static org.example.utils.TestShell.readFileToString;
import static org.example.utils.TestShell.test_commands;
import static org.testng.Assert.*;

public class HistoryLogTest {

    @Test
    public void testSes_start_log() {
        String expect = "workspace start at yyyyMMdd HH:mm:ss\n";

        HistoryLog historyLog = new HistoryLog();
        historyLog.cur_file_path = "run_time_files/default_history.log";

        clean();
        historyLog.ses_start_log();
        String actual = string_format_date(readFileToString(historyLog.cur_file_path));
        assertEquals(actual,expect);
        clean();
    }

    @Test
    public void testCmd_log() {
        String expect = "yyyyMMdd HH:mm:ss insert # hello\n";
        HistoryLog historyLog = new HistoryLog();
        historyLog.cur_file_path = "run_time_files/default_history.log";

        clean();
        historyLog.cmd_log("insert", Arrays.asList("#","hello"));
        String actual = string_format_date(readFileToString(historyLog.cur_file_path));
        assertEquals(actual,expect);
        clean();
    }

    @Test
    public void testHistory_by_num() {
        String expect = """
                yyyyMMdd HH:mm:ss save
                yyyyMMdd HH:mm:ss list
                """;

        HistoryLog historyLog = new HistoryLog();
        historyLog.cur_file_path = "run_time_files/default_history.log";

        clean();
        historyLog.ses_start_log();
        historyLog.cmd_log("load", List.of("test.md"));
        historyLog.cmd_log("insert", Arrays.asList("#","hello"));
        historyLog.ses_start_log();
        historyLog.cmd_log("open", List.of("test"));
        historyLog.cmd_log("list", new ArrayList<>());
        historyLog.cmd_log("save", new ArrayList<>());

        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            historyLog.history_by_num(2);

            // 捕获输出并转换为字符串
            String capturedOutput = string_format_date(outputStreamCaptor.toString());
            assertEquals(capturedOutput.replaceAll("[\\r\\n]", ""),  expect.replaceAll("[\\r\\n]", ""));
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }

        clean();
    }

    @Test
    public void testHistory_all() {
        String expect = """
                yyyyMMdd HH:mm:ss save
                yyyyMMdd HH:mm:ss list
                yyyyMMdd HH:mm:ss open test
                yyyyMMdd HH:mm:ss insert # hello
                yyyyMMdd HH:mm:ss load test.md
                """;

        HistoryLog historyLog = new HistoryLog();
        historyLog.cur_file_path = "run_time_files/default_history.log";

        clean();
        historyLog.ses_start_log();
        historyLog.cmd_log("load", List.of("test.md"));
        historyLog.cmd_log("insert", Arrays.asList("#","hello"));
        historyLog.ses_start_log();
        historyLog.cmd_log("open", List.of("test"));
        historyLog.cmd_log("list", new ArrayList<>());
        historyLog.cmd_log("save", new ArrayList<>());

        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            historyLog.history_all();

            // 捕获输出并转换为字符串
            String capturedOutput = string_format_date(outputStreamCaptor.toString());
            assertEquals(capturedOutput.replaceAll("[\\r\\n]", ""),  expect.replaceAll("[\\r\\n]", ""));
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }

        clean();
    }

    public void clean(){
        File check_file = new File("run_time_files/default_history.log");
        if(check_file.exists()){
            check_file.delete();
        }
    }
}