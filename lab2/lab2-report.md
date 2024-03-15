<center><h1>lab2-report</h1></center>

<h2>一、功能完成度列表</h2>

<ol>
&ensp;

<li>load 文件路径（new） &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>save（new） &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>list-workspace &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>open 工作区 &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>close &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>ls &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>exit（new） &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>工作区恢复 &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font>

&ensp;

<li>JUint 测试 &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<font color=Green>&#x2714;</font><br><br>
</ol>

<h2>二、使用说明</h2>
<ol>
&ensp;
<li>项目采用maven进行管理，使用IDEA进行开发。源代码在src/main中，测试代码在src/test中，运行Shell.java启动程序。

&ensp;

<li>在lab1的基础上进行了重构：将打印树形结构实现为组合模式（之前为直接递归遍历）。将日志实现为观察者模式，invoker在成功调用命令后调用通知观察者。并且增加了WorkSpaceManager管理工作区以适应lab2的需求。

&ensp;

<li>分层对lab2进行测试。在receiver层面上，对Doucment和WorkSpaceManager进行单元测试。在observer层面上，对StatsLog和HistoryLog进行单元测试。在Shell层面上，进行集成测试，除了lab1已有的测试用例外，增加了对lab2中增加的功能的测试。

&ensp;

<li>下面对TestCaseLab2进行介绍：例如对于其中的test_case_2，执行以下命令：

```shell
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
```

测试中模拟终端输入Y并且捕获终端输出，则应得到如下输出（通过clean()在每次测试时都检测并删除测试文件）：

```shell
在当前工作目录下创建文件...
在当前工作目录下创建文件...
    test1 s
->test2 s
切换到工作区 test1 。
->test1 s
    test2 s
->test1 *
    test2 s
Do you want to save the current workspace [Y\N] ？
保存成功！
工作区 test1 已关闭
    test2 s
切换到工作区 test2 。
->test2 s
->test2 *
```

&ensp;

<li>TestCaseLab2覆盖了除工作区恢复之外的所有新增功能。下面对工作区恢复进行演示，因为工作区恢复涉及退出和重进程序，不便进行测试。<br><br> 
假设第一次运行程序输入了以下内容：

```shell
Hello! Input help to get some information
>>>load test1.md
在当前工作目录下创建文件...
>>>insert # hello
>>>list-tree
└── hello
>>>load test2.md
在当前工作目录下创建文件...
>>>insert # world
>>>list-tree
└── world
>>>undo
>>>list-tree
>>>list-workspace
  test1 *
->test2 *
>>>exit
Do you want to save the unsaved workspace [Y\N] ？
Y
Bye~
```

在第二次运行时进行以下命令：

```shell
Hello! Input help to get some information
>>>
已加载上次保存的工作区
>>>list-workspace
  test1 *
->test2 *
>>>list-tree
>>>redo
>>>list-tree
└── world
>>>open test1
切换到工作区 test1 。
>>>list-tree
└── hello
>>>save
保存成功！
>>>close
工作区 test1 已关闭
>>>open test2
切换到工作区 test2 。
>>>save
保存成功！
>>>exit
Bye~
```

可以看到，在第二次重进程序时首先提示已加载上次保存的工作区。然后执行list-workspace可以看到之前工作区编辑的内容依然存在，并且可以正常redo之前工作区undo的内容。这是通过在exit输入Y时将单例WorkSpaceManager写入run_time_files/recent_workspace.json来实现的。最后看到当对工作区的所有内容都进行保存后，再进行exit则会直接退出。

&ensp;

<li>以下为运行所有测试的截图：</br>

![Alt text](<屏幕截图 2023-11-27 111358.png>)
![Alt text](<屏幕截图 2023-11-27 111441.png>)
![Alt text](<屏幕截图 2023-11-27 111507.png>)
![Alt text](<屏幕截图 2023-11-27 111539.png>)
![Alt text](<屏幕截图 2023-11-27 111607.png>)
![Alt text](<屏幕截图 2023-11-27 111639.png>)

&ensp;

</ol>

<h2>三：关于第二次提交的补充</h2>
由于第一次提交时还未给出测试用例，故在此按照给出的测试用例进行测试并修改部分实现。<br>
按照给出的测试用例，在TestCaseLab2中增加了测试用例 6，7，8，分别对应给出的 lab2 的第 1，2，3个测试用例。以下是运行截图：（第7个测试用例也涉及退出程序，可在test_case_7所在代码中取消注释来进行完整测试）

![Alt text](<屏幕截图 2023-11-29 201858.png>)
第四个测试用例涉及退出程序，下面给出在命令行中运行的测试结果：
![Alt text](<屏幕截图 2023-11-29 201447.png>)
第二次进入程序并输入以下命令：
![Alt text](<屏幕截图 2023-11-29 202818.png>)


