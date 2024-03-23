package org.example.shell;

import org.example.receiver.WorkSpaceManager;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.shell.TestCaseLab1.clean_num;
import static org.example.utils.TestShell.readFileToString;
import static org.example.utils.TestShell.test_commands;
import static org.testng.Assert.assertEquals;

@Test
public class TestCaseLab2 {
    @Test
    public void test_case1(){
        String commands_1 = """
                load test1.md
                load test2.md
                load test3.md
                load test4.md
                load test5.md
                list-workspace
                open test1
                list-workspace
                open test2
                list-workspace
                close
                list-workspace
                open test3
                list-workspace
                """;
        String expect = """
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                  test1 \s
                  test2 \s
                  test3 \s
                  test4 \s
                ->test5 \s
                切换到工作区 test1 。
                ->test1 \s
                  test2 \s
                  test3 \s
                  test4 \s
                  test5 \s
                切换到工作区 test2 。
                  test1 \s
                ->test2 \s
                  test3 \s
                  test4 \s
                  test5 \s
                工作区 test2 已关闭
                  test1 \s
                  test3 \s
                  test4 \s
                  test5 \s
                切换到工作区 test3 。
                  test1 \s
                ->test3 \s
                  test4 \s
                  test5 \s""";
        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            clean();
            test_commands(commands_1);

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput.replaceAll("[\\r\\n]", ""),expect.replaceAll("[\\r\\n]", ""));
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
        clean();
    }

    @Test
    public void test_case2(){
        String commands_2 = """
                load test1.md
                load test2.md
                list-workspace
                open test1
                list-workspace
                insert # hello
                list-workspace
                close
                list-workspace
                open test2
                list-workspace
                insert # world
                list-workspace
                """;
        String expect = """
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                  test1 \s
                ->test2 \s
                切换到工作区 test1 。
                ->test1 \s
                  test2 \s
                ->test1 *
                  test2 \s
                Do you want to save the current workspace [Y\\N] ？
                保存成功！
                工作区 test1 已关闭
                  test2 \s
                切换到工作区 test2 。
                ->test2 \s
                ->test2 *
                """;

        String simulatedInput = "Y"; // 模拟输入的数据
        InputStream originalInput = System.in; // 保存原始输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream); // 将输入重定向为模拟输入流
        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            clean();
            test_commands(commands_2);

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput.replaceAll("[\\r\\n]", ""),expect.replaceAll("[\\r\\n]", ""));
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
        clean();

        // 恢复原始输入流
        System.setIn(originalInput);

    }

    @Test
    public void test_case3(){
        String commands_3 = """
                load test1.md
                load test2.md
                list-workspace
                open test1
                list-workspace
                insert # hello
                list-workspace
                save
                list-workspace
                insert ## world
                list-workspace
                list
                """;
        String expect = """
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                  test1 \s
                ->test2 \s
                切换到工作区 test1 。
                ->test1 \s
                  test2 \s
                ->test1 *
                  test2 \s
                保存成功！
                ->test1 \s
                  test2 \s
                ->test1 *
                  test2 \s
                # hello\s
                ## world\s""";

        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            clean();
            test_commands(commands_3);

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
    public void test_case4(){
        String commands_4= """
                load test1.md
                insert # hello
                load test2.md
                insert # world
                open test1
                list
                save
                """;
        String expect_1 = """
                workspace start at yyyyMMdd HH:mm:ss
                yyyyMMdd HH:mm:ss load test1.md
                yyyyMMdd HH:mm:ss insert # hello
                workspace start at yyyyMMdd HH:mm:ss
                yyyyMMdd HH:mm:ss open test1
                yyyyMMdd HH:mm:ss list
                yyyyMMdd HH:mm:ss save
                """;
        String expect_2 = """
                workspace start at yyyyMMdd HH:mm:ss
                yyyyMMdd HH:mm:ss load test2.md
                yyyyMMdd HH:mm:ss insert # world
                """;

        clean();
        test_commands(commands_4);
        String actual_1 = string_format_date(readFileToString("run_time_files/test1_history.log"));
        String actual_2 = string_format_date(readFileToString("run_time_files/test2_history.log"));
        assertEquals(actual_1,expect_1);
        assertEquals(actual_2,expect_2);
        clean();
    }

    @Test
    public void test_case5(){
        String commands_5= """
                load test1.md
                insert # hello
                load test2.md
                insert # world
                open test1
                open test2
                open test1
                list
                save
                """;
        String expect = """
                session start at yyyyMMdd HH:mm:ss
                ./test1.md 0 秒\s
                ./test2.md 0 秒\s
                ./test1.md 0 秒\s
                ./test2.md 0 秒\s
                """;


        clean();
        test_commands(commands_5);
        String actual = string_format_date(readFileToString("run_time_files/stats.log"));
        assertEquals(actual,expect);
        clean();
    }

    @Test
    public void test_case6(){
        String commands_6= """
                load test1.md
                append-head # 旅行清单
                append-tail ## 亚洲
                append-tail 1. 中国
                append-tail 2. 日本
                load test2.md
                append-head # 书籍推荐
                append-tail ## 编程
                append-tail * 《设计模式的艺术》
                append-tail * 《云原生：运用容器、函数计算和数据构建下一代应用》
                append-tail * 《深入理解Java虚拟机》
                list-tree
                list-workspace
                save
                list-workspace
                open test1
                list-workspace
                """;
        String expect = """
                在当前工作目录下创建文件...
                在当前工作目录下创建文件...
                └── 书籍推荐
                    └── 编程
                        ├── *《设计模式的艺术》
                        ├── *《云原生：运用容器、函数计算和数据构建下一代应用》
                        └── *《深入理解Java虚拟机》
                  test1 *
                ->test2 *
                保存成功！
                  test1 *
                ->test2 \s
                切换到工作区 test1 。
                ->test1 *
                  test2 \s
                """;

        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            clean();
            test_commands(commands_6);

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
    public void test_case7(){
        String commands_7_1= """
                load test3.md
                insert ## 程序设计
                append-head # 我的资源
                append-tail ### 软件设计
                load test4.md
                append-head # 大学合集
                append-tail 1. 复旦大学
                append-tail 2. 上海交通大学
                close
                open test3
                list-workspace
                """;
        String commands_7_2= """
                exit
                """;
        String expect_1 = """
             在当前工作目录下创建文件...
             在当前工作目录下创建文件...
             Do you want to save the current workspace [Y\\N] ？
             保存成功！
             工作区 test4 已关闭
             切换到工作区 test3 。
             ->test3 *
             
                """;

        clean();

        String simulatedInput = "Y"; // 模拟输入的数据
        InputStream originalInput = System.in; // 保存原始输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream); // 将输入重定向为模拟输入流
        System.out.println("================TEST_CASE_7_START==================");
        test_commands(commands_7_1);
        System.out.println("================TEST_CASE_7_END====================");
        System.setIn(originalInput);

        //        测试退出程序可以取消该注释

//           simulatedInput = "Y"; // 模拟输入的数据
//                inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
//                System.setIn(inputStream); // 将输入重定向为模拟输入流
//                test_commands(commands_7_2);
//                System.setIn(originalInput);

        clean();
    }

    @Test
    public void test_case8(){
        String commands_8= """
                load test5.md
                append-head # 书籍推荐
                append-tail * 《深入理解计算机系统》
                ls
                """;
        String expect = """
                在当前工作目录下创建文件...
                hint: ignore .git .idea target .gitignore run_time_files
                ├── pom.xml
                ├── src
                │   ├── main
                │   │   ├── java
                │   │   │   └── org
                │   │   │       └── example
                │   │   │           ├── command
                │   │   │           │   ├── Command.java
                │   │   │           │   └── ConcreteCommand
                │   │   │           │       ├── EditCommand
                │   │   │           │       │   ├── AppendHeadCommand.java
                │   │   │           │       │   ├── AppendTailCommand.java
                │   │   │           │       │   ├── DeleteCommand.java
                │   │   │           │       │   ├── InsertCommand.java
                │   │   │           │       │   ├── NothingCommand.java
                │   │   │           │       │   ├── RedoCommand.java
                │   │   │           │       │   └── UndoCommand.java
                │   │   │           │       ├── FileCommand
                │   │   │           │       │   └── SaveCommand.java
                │   │   │           │       ├── LogCommand
                │   │   │           │       │   ├── HistoryCommand.java
                │   │   │           │       │   └── StatsCommand.java
                │   │   │           │       ├── ShowCommand
                │   │   │           │       │   ├── DirTreeCommand.java
                │   │   │           │       │   ├── HelpCommand.java
                │   │   │           │       │   ├── ListCommand.java
                │   │   │           │       │   ├── ListTreeCommand.java
                │   │   │           │       │   └── LsCommand.java
                │   │   │           │       └── WorkSpaceCommand
                │   │   │           │           ├── CloseCommand.java
                │   │   │           │           ├── ExitCommand.java
                │   │   │           │           ├── ListWorkspaceCommand.java
                │   │   │           │           ├── LoadCommand.java
                │   │   │           │           └── OpenCommand.java
                │   │   │           ├── invoker
                │   │   │           │   └── Invoker.java
                │   │   │           ├── observer
                │   │   │           │   ├── ConcreteObserver
                │   │   │           │   │   ├── HistoryLog.java
                │   │   │           │   │   └── StatsLog.java
                │   │   │           │   └── Observer.java
                │   │   │           ├── receiver
                │   │   │           │   ├── Document.java
                │   │   │           │   └── WorkSpaceManager.java
                │   │   │           ├── Shell.java
                │   │   │           └── utils
                │   │   │               ├── FileUtils.java
                │   │   │               ├── GetCommand.java
                │   │   │               ├── ListFolder.java
                │   │   │               ├── ListTree.java
                │   │   │               ├── TestShell.java
                │   │   │               └── WorkSpace.java
                │   │   └── resources
                │   └── test
                │       └── java
                │           └── org
                │               └── example
                │                   ├── observer
                │                   │   └── ConcreteObserver
                │                   │       ├── HistoryLogTest.java
                │                   │       └── StatsLogTest.java
                │                   ├── receiver
                │                   │   ├── DocumentTest.java
                │                   │   └── WorkSpaceManagerTest.java
                │                   └── shell
                │                       ├── TestCaseLab1.java
                │                       └── TestCaseLab2.java
                └── test5.md *\s
                
                """;


        // 保存当前System.out
        PrintStream originalOut = System.out;
        try {
            // 创建一个字节数组输出流
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStreamCaptor);

            // 将System.out重定向到我们的输出流
            System.setOut(printStream);

            //执行测试
            clean();
            test_commands(commands_8);

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
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
        File check_file = new File("test.md");
        if(check_file.exists()){
            check_file.delete();
        }
        for(int i=1;i<=10;i++){
            clean_num(i);
        }
        check_file = new File("run_time_files/recent_workspace.json");
        if(check_file.exists()){
            check_file.delete();
        }
        check_file = new File("run_time_files/stats.log");
        if(check_file.exists()){
            check_file.delete();
        }
    }


    public static String string_format_date(String input) {
        String output =  input;

        // 使用正则表达式提取所有时间字符串
        List<String> timeStrings = extractAllTimeStrings(input);

        // 解析并比较每个时间字符串
        for (String timeStr : timeStrings) {
            output = output.replace(timeStr,"yyyyMMdd HH:mm:ss");
        }

        return output;
    }

    // 使用正则表达式提取所有时间字符串
    private static List<String> extractAllTimeStrings(String input) {
        List<String> timeStrings = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{4}\\d{2}\\d{2} \\d{2}:\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            timeStrings.add(matcher.group());
        }
        return timeStrings;
    }
}
