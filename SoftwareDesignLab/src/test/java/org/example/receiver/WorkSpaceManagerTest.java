package org.example.receiver;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;

import static org.example.shell.TestCaseLab1.clean_num;
import static org.example.utils.FileUtils.recent_workspace_path;
import static org.example.utils.TestShell.readFileToString;
import static org.testng.Assert.*;

public class WorkSpaceManagerTest {

    @Test
    public void testLoad() {
        clean();
        WorkSpaceManager.getInstance().load("test.md");
        assertEquals(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).workspace_name,"test");
        assertEquals(FileSystems.getDefault().getPath(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.file_path).getFileName().toString(),"test.md");
        assertFalse(WorkSpaceManager.getInstance().load("test.md"));
    }

    @Test
    public void testOpen() {
        clean();
        assertFalse(WorkSpaceManager.getInstance().open("test"));
        WorkSpaceManager.getInstance().load("test1.md");
        WorkSpaceManager.getInstance().load("test2.md");
        assertTrue(WorkSpaceManager.getInstance().open("test1"));
        assertTrue(WorkSpaceManager.getInstance().open("test2"));
        assertEquals(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).workspace_name,"test2");
    }

    @Test
    public void testClose() {
        clean();
        assertFalse(WorkSpaceManager.getInstance().open("test"));
        WorkSpaceManager.getInstance().load("test1.md");
        WorkSpaceManager.getInstance().load("test2.md");
        assertTrue(WorkSpaceManager.getInstance().open("test1"));
        assertTrue(WorkSpaceManager.getInstance().open("test2"));
        assertEquals(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).workspace_name,"test2");
        WorkSpaceManager.getInstance().close();
        assertFalse(WorkSpaceManager.getInstance().open("test2"));
        assertTrue(WorkSpaceManager.getInstance().open("test1"));
    }

    @Test
    public void testExit() {
        clean();
        WorkSpaceManager.getInstance().load("test1.md");
        WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.insert(1, Arrays.asList("#","hello"));
        WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.insert(2, Arrays.asList("##","world"));

        WorkSpaceManager.getInstance().load("test2.md");
        WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.insert(1, Arrays.asList("#","software"));
        WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.insert(2, Arrays.asList("##","design"));

        String simulatedInput = "Y"; // 模拟输入的数据

        InputStream originalInput = System.in; // 保存原始输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream); // 将输入重定向为模拟输入流

        WorkSpaceManager.getInstance().exit();
        String res_1 = readFileToString(recent_workspace_path);

        assertEquals(res_1,"{\"workSpaceList\":[{\"document\":{\"doc_lines\":[[\"#\",\"hello\"],[\"##\",\"world\"]],\"modified\":true,\"file_path\":\"C:\\\\Users\\\\21714\\\\Desktop\\\\Code\\\\SoftwareDesign\\\\lab1\\\\SoftwareDesignLab1\\\\test1.md\",\"edit_lines\":[[\"insert\",\"1\",\"#\",\"hello\"],[\"insert\",\"2\",\"##\",\"world\"]],\"record_edit\":true},\"workspace_name\":\"test1\"},{\"document\":{\"doc_lines\":[[\"#\",\"software\"],[\"##\",\"design\"]],\"modified\":true,\"file_path\":\"C:\\\\Users\\\\21714\\\\Desktop\\\\Code\\\\SoftwareDesign\\\\lab1\\\\SoftwareDesignLab1\\\\test2.md\",\"edit_lines\":[[\"insert\",\"1\",\"#\",\"software\"],[\"insert\",\"2\",\"##\",\"design\"]],\"record_edit\":true},\"workspace_name\":\"test2\"}],\"currentWorkspace\":1}");
        // 恢复原始输入流
        System.setIn(originalInput);

        WorkSpaceManager.getInstance().workSpaceList = WorkSpaceManager.load_recent().workSpaceList;
        WorkSpaceManager.getInstance().currentWorkspace = WorkSpaceManager.load_recent().currentWorkspace;

        assertEquals(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).workspace_name,"test2");
        assertEquals(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.doc_lines.get(0).get(1),"software");
        assertEquals(readFileToString("test2.md"),"");
    }

    @Test
    public void testList_workspace() {
        clean();
        WorkSpaceManager.getInstance().load("test1.md");
        WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.insert(1, Arrays.asList("#","hello"));
        WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.insert(2, Arrays.asList("##","world"));

        WorkSpaceManager.getInstance().load("test2.md");

        String expect = """
                  test1 *
                ->test2 \s
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
            WorkSpaceManager.getInstance().list_workspace();

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput,expect);
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testLs() {
        clean();
        WorkSpaceManager.getInstance().load("test1.md");

        String expect = """
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
                └── test1.md *\s
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
            WorkSpaceManager.getInstance().ls();

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput.replaceAll("[\\r\\n]", ""),expect.replaceAll("[\\r\\n]", ""));
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
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
    }
}