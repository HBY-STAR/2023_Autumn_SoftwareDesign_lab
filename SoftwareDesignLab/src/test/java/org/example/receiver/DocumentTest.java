package org.example.receiver;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.shell.TestCaseLab1.clean_num;
import static org.testng.Assert.assertEquals;

@Test
public class DocumentTest {
    public  static Document document = new Document();

    @Test
    public void testLoad() {
        clean();
        document.load("test.md");
        assertEquals(FileSystems.getDefault().getPath(document.file_path).getFileName().toString(),"test.md");
    }

    @Test
    public void testSave() {
        clean();
        document.load("test.md");
        int before = document.doc_lines.size();
        List<String> insert = new ArrayList<>();
        insert.add("#");
        insert.add("Test_save");
        document.insert(1,insert);
        document.save();
        document.load("test.md");
        int after = document.doc_lines.size();
        assertEquals(after,before+1);
    }

    @Test
    public void testInsert() {
        clean();
        document.load("test.md");
        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("Test_header");
        document.insert(1,insert1);
        assertEquals(insert1,document.doc_lines.get(0));

        List<String> insert2 = new ArrayList<>();
        insert2.add("*");
        insert2.add("Test_text");
        document.insert(2,insert2);
        assertEquals(insert2,document.doc_lines.get(1));

        List<String> insert3 = new ArrayList<>();
        insert3.add("1.");
        insert3.add("Test_list");
        document.insert(3,insert3);
        assertEquals(insert3,document.doc_lines.get(2));
    }

    @Test
    public void testDelete_row() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("Test_delete_row_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("Test_delete_row_2");
        document.insert(1,insert2);

        document.delete_row(1);

        assertEquals(document.doc_lines.get(0),insert1);
    }

    @Test
    public void testDelete_text() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("Test_delete_row_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("Test_delete_row_2");
        document.insert(1,insert2);

        List<String> check = new ArrayList<>();
        check.add("Test_delete_row_2");
        document.delete_text(check);

        assertEquals(document.doc_lines.get(0),insert1);
    }

    @Test
    public void testUndo() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("Test_redo_row_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("Test_redo_row_2");
        document.insert(1,insert2);

        document.undo();

        assertEquals(document.doc_lines.get(0),insert1);
    }
    @Test
    public void testRedo() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("Test_redo_row_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("Test_redo_row_2");
        document.insert(1,insert2);

        document.undo();
        document.redo();

        assertEquals(document.doc_lines.get(0),insert2);
    }

    @Test
    public void testList() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("text_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("text_2");
        document.insert(1,insert2);

        String expect = """
                ## text_2\s
                # text_1\s
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
            document.list();

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput,expect);
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testDir_tree() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("text_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("text_2");
        document.insert(2,insert2);

        List<String> insert3 = new ArrayList<>();
        insert3.add("###");
        insert3.add("text_3");
        document.insert(3,insert3);

        List<String> insert4 = new ArrayList<>();
        insert4.add("###");
        insert4.add("text_4");
        document.insert(4,insert4);

        String expect = """
                └── text_2
                    ├── text_3
                    └── text_4
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
            document.dir_tree(Collections.singletonList("text_2"));

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput,expect);
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testList_tree() {
        clean();
        document.load("test.md");

        List<String> insert1 = new ArrayList<>();
        insert1.add("#");
        insert1.add("text_1");
        document.insert(1,insert1);

        List<String> insert2 = new ArrayList<>();
        insert2.add("##");
        insert2.add("text_2");
        document.insert(2,insert2);

        List<String> insert3 = new ArrayList<>();
        insert3.add("###");
        insert3.add("text_3");
        document.insert(3,insert3);

        List<String> insert4 = new ArrayList<>();
        insert4.add("###");
        insert4.add("text_4");
        document.insert(4,insert4);

        List<String> insert5 = new ArrayList<>();
        insert5.add("#");
        insert5.add("text_5");
        document.insert(5,insert5);

        List<String> insert6 = new ArrayList<>();
        insert6.add("##");
        insert6.add("text_6");
        document.insert(6,insert6);

        String expect = """
              ├── text_1
              │   └── text_2
              │       ├── text_3
              │       └── text_4
              └── text_5
                  └── text_6
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
            document.list_tree();

            // 捕获输出并转换为字符串
            String capturedOutput = outputStreamCaptor.toString();
            assertEquals(capturedOutput,expect);
        } finally {
            // 恢复System.out
            System.setOut(originalOut);
        }
    }

    private void clean() {
        File check_file = new File("test.md");
        if(check_file.exists()){
            check_file.delete();
        }
        for(int i=1;i<=10;i++){
            clean_num(i);
        }
    }
}