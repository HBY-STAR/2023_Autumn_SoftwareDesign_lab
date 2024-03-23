package org.example.observer.ConcreteObserver;

import org.example.receiver.WorkSpaceManager;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.example.observer.ConcreteObserver.StatsLog.stats_file_path;
import static org.example.shell.TestCaseLab1.clean_num;
import static org.example.shell.TestCaseLab2.string_format_date;
import static org.example.utils.TestShell.readFileToString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class StatsLogTest {

    @Test
    public void testSes_start_log() {
        String expect = "session start at yyyyMMdd HH:mm:ss\n";
        clean();
        StatsLog statsLog = new StatsLog();

        statsLog.ses_start_log();

        String actual = string_format_date(readFileToString(stats_file_path));
        assertEquals(actual,expect);

        clean();
    }

    @Test
    public void testSes_end_log() throws InterruptedException {
        clean();
        String expect = "session start at yyyyMMdd HH:mm:ss\n" + "./test.md 1 秒 \n";
        StatsLog statsLog = new StatsLog();
        WorkSpaceManager.getInstance().load("test.md");

        statsLog.ses_start_log();
        statsLog.file_start_log();
        Thread.sleep(1000);
        statsLog.ses_end_log();

        String actual = string_format_date(readFileToString(stats_file_path));
        assertEquals(actual,expect);

        clean();
    }

    @Test
    public void testFile_start_log() {
        clean();

        StatsLog statsLog = new StatsLog();
        WorkSpaceManager.getInstance().load("test.md");
        statsLog.file_start_log();
        assertNotNull(statsLog.current_file);
        assertNotNull(statsLog.current_file_start);
        clean();
    }

    @Test
    public void testFile_end_log() throws InterruptedException {
        clean();
        String expect = "./test.md 2 秒 \n";
        StatsLog statsLog = new StatsLog();

        WorkSpaceManager.getInstance().load("test.md");
        statsLog.file_start_log();
        Thread.sleep(2000);
        statsLog.file_end_log();

        String actual = string_format_date(readFileToString(stats_file_path));
        assertEquals(actual,expect);
        clean();
    }

    @Test
    public void testStats_current() throws InterruptedException {
        clean();
        String expect = "在当前工作目录下创建文件...\n./test.md 2 秒 \n";
        StatsLog statsLog = new StatsLog();



        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            WorkSpaceManager.getInstance().load("test.md");
            statsLog.file_start_log();
            Thread.sleep(2000);
            statsLog.stats_current();

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput.replaceAll("[\\r\\n]", ""),  expect.replaceAll("[\\r\\n]", ""));
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
        clean();
    }

    @Test
    public void testStats_all() throws InterruptedException {
        clean();
        String expect = """
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                session start at yyyyMMdd HH:mm:ss
                ./test1.md 1 秒\s
                ./test2.md 2 秒\s
                """;
        StatsLog statsLog = new StatsLog();

        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            statsLog.ses_start_log();
            WorkSpaceManager.getInstance().load("test1.md");
            statsLog.file_start_log();
            Thread.sleep(1000);
            statsLog.file_end_log();
            WorkSpaceManager.getInstance().load("test2.md");
            statsLog.file_start_log();
            Thread.sleep(2000);
            statsLog.stats_all();
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
        WorkSpaceManager.getInstance().workSpaceList=new ArrayList<>();
        WorkSpaceManager.getInstance().currentWorkspace=-1;
        File check_file = new File("run_time_files/stats.log");
        if(check_file.exists()){
            check_file.delete();
        }
        check_file = new File("test.md");
        if(check_file.exists()){
            check_file.delete();
        }
        for (int i=1;i<10;i++){
            clean_num(i);
        }
    }
}