package org.example.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.receiver.WorkSpaceManager;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
class FileNode {
    String name;
    List<FileNode> children;

    FileNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }
    void addChild(FileNode child) {
        children.add(child);
    }
}

@Data
public class ListFolder {
    public FileNode root = new FileNode();
    public StringBuilder print=new StringBuilder();
    public void list_cur_work_folder() {
        // 获取当前工作目录
        String currentDirectory = System.getProperty("user.dir");

        // 创建根节点
        FileNode root = new FileNode(currentDirectory);
        print=new StringBuilder();

        // 构建目录树
        buildDirectoryTree(root, new File(currentDirectory));

        print_tree(root,"");
        System.out.println("hint: ignore .git .idea target .gitignore run_time_files");
        System.out.print(print);
    }

    private void buildDirectoryTree(FileNode parentNode, File directory) {
        if (directory.isDirectory())
        {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileNode childNode = new FileNode(file.getName());
                    if(!childNode.getName().equals(".git") && !childNode.getName().equals(".gitignore") && !childNode.getName().equals(".idea") && !childNode.getName().equals("target") && !childNode.getName().equals("run_time_files"))
                    {
                        parentNode.addChild(childNode);
                        buildDirectoryTree(childNode, file);
                    }
                }
            }
        }
    }

    public void print_tree(FileNode fileNode, String prefix){
        StringBuilder temp = new StringBuilder();
        if(fileNode.children.size()>0){
            for(int i=0;i<fileNode.children.size()-1;i++){
                FileNode child = fileNode.children.get(i);
                temp = new StringBuilder();
                temp.append(prefix).append("├── ").append(child.name);
                if (check_if_opened(child.name)) {
                    temp.append(" * ");
                }
                temp.append("\n");
                print.append(temp);
                print_tree(child,prefix+"│   ");
            }
            FileNode last_child = fileNode.children.get(fileNode.children.size()-1);
            temp=new StringBuilder();
            temp.append(prefix).append("└── ").append(last_child.name);
            if (check_if_opened(last_child.name)) {
                temp.append(" * ");
            }
            temp.append("\n");
            print.append(temp);
            print_tree(last_child,prefix+"    ");
        }
    }

    public boolean check_if_opened(String file_name){
        WorkSpaceManager workSpaceManager  = WorkSpaceManager.getInstance();
        for (WorkSpace workSpace : workSpaceManager.workSpaceList){
            if(file_name.equals(FileSystems.getDefault().getPath(workSpace.document.file_path).getFileName().toString())){
                return true;
            }
        }
        return false;
    }
}

