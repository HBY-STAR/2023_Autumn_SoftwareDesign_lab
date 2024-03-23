package org.example.shell;

import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.io.File;

import static org.example.utils.TestShell.readFileToString;
import static org.example.utils.TestShell.test_commands;
import static org.testng.Assert.assertEquals;


public class TestCaseLab1 {
    private static final String commands_1 = """
                load test1.md
                insert ## 程序设计
                append-head # 我的资源
                append-tail ### 软件设计
                append-tail #### 设计模式
                append-tail 1. 观察者模式
                append-tail 3. 单例模式
                insert 6 2. 策略模式
                delete 单例模式
                append-tail 3. 组合模式
                list-tree
                append-tail ## ⼯具箱
                append-tail ### Adobe
                list-tree
                save""";

    private static final String result_1 = """
            # 我的资源\s
            ## 程序设计\s
            ### 软件设计\s
            #### 设计模式\s
            1. 观察者模式\s
            2. 策略模式\s
            3. 组合模式\s
            ## ⼯具箱\s
            ### Adobe\s
            """;
    private static final String commands_2 = """
                load test2.md
                append-head # 旅⾏清单
                append-tail ## 亚洲
                append-tail 1. 中国
                append-tail 2. ⽇本
                delete 亚洲
                undo
                redo
                list-tree
                save""";
    private static final String result_2= """
            # 旅⾏清单\s
            1. 中国\s
            2. ⽇本\s
            """;
    private static final String commands_3 = """
                load test3.md
                append-head # 书籍推荐
                append-tail * 《深入理解计算机系统》
                undo
                append-tail ## 编程
                append-tail * 《设计模式的艺术》
                redo
                list-tree
                append-tail * 《云原⽣：运⽤容器、函数计算和数据构建下⼀代应⽤》
                append-tail * 《深入理解Java虚拟机》
                undo
                redo
                list-tree
                save""";

    private static final String result_3= """
            # 书籍推荐\s
            ## 编程\s
            * 《设计模式的艺术》\s
            * 《云原⽣：运⽤容器、函数计算和数据构建下⼀代应⽤》\s
            * 《深入理解Java虚拟机》\s
            """;

    private static final String commands_4 = """
                load test4.md
                append-head # 旅⾏清单
                append-tail ## 亚洲
                save
                append-tail 1. 中国
                append-tail 2. ⽇本
                append-tail ## 欧洲
                load test3.md
                list-tree
                load test4.md
                list-tree""";

    private static final String result_4 = """
            # 旅⾏清单\s
            ## 亚洲\s
            """;

    private static final String commands_5 = """
                load test5.md
                append-head # 旅⾏清单
                append-tail ## 欧洲
                insert 2 ## 亚洲
                insert 3 1. 中国
                insert 4 2. ⽇本
                save
                undo
                list-tree
                delete 亚洲
                list-tree
                history 2
                undo
                list-tree
                redo
                list-tree
                redo
                list-tree
                save""";

    private static final String result_5 = """
            # 旅⾏清单\s
            1. 中国\s
            ## 欧洲\s
            """;

    @Test
    public void test_case1(){
        clean_num(1);
        test_commands(commands_1);
        assertEquals(readFileToString("test1.md"),result_1);
    }

    @Test
    public void test_case2(){
        clean_num(2);
        test_commands(commands_2);
        assertEquals(readFileToString("test2.md"),result_2);
    }

    @Test
    public void test_case3(){
        clean_num(3);
        test_commands(commands_3);
        assertEquals(readFileToString("test3.md"),result_3);
    }

    @Test
    public void test_case4(){
        clean_num(4);
        test_commands(commands_4);
        assertEquals(readFileToString("test4.md"),result_4);
    }

    @Test
    public void test_case5(){
        clean_num(5);
        test_commands(commands_5);
        assertEquals(readFileToString("test5.md"),result_5);
    }

    @Ignore
    public static void clean_num(int test_case) {
        File check_file = new File("test"+test_case+".md");
        if(check_file.exists()){
            check_file.delete();
        }
        check_file = new File("run_time_files/test"+test_case+"_history.log");
        if(check_file.exists()) {
            check_file.delete();
        }
    }
}
