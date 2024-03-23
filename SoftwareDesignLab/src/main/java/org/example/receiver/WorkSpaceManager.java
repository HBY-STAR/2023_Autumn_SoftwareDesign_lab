package org.example.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.utils.ListFolder;
import org.example.utils.WorkSpace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.utils.FileUtils.getNameFromPath;
import static org.example.utils.FileUtils.recent_workspace_path;

@Data
public class WorkSpaceManager {
    private static WorkSpaceManager instance;
    public List<WorkSpace> workSpaceList;
    public int currentWorkspace;

    private WorkSpaceManager() {
        workSpaceList = new ArrayList<>();
        currentWorkspace = -1;
    }

    public static WorkSpaceManager getInstance() {
        if (instance == null) {
            synchronized (WorkSpaceManager.class) {
                if (instance == null) {
                    instance = load_recent();
                }
            }
        }
        return instance;
    }

    public boolean load(String file_path){
        int find = find_document(file_path);
        if(find>-1){
            System.out.println("文件已在工作区 "+WorkSpaceManager.getInstance().workSpaceList.get(find).workspace_name+" 打开");
        }else {
            WorkSpace workSpace = new WorkSpace();
            boolean success = workSpace.document.load(file_path);
            if(success){
                workSpace.workspace_name=getNameFromPath(workSpace.document.file_path);
                WorkSpaceManager.getInstance().workSpaceList.add(workSpace);
                WorkSpaceManager.getInstance().currentWorkspace = WorkSpaceManager.getInstance().workSpaceList.indexOf(workSpace);
                return true;
            }
        }
        return false;
    }

    public boolean open(String workspace_name){
        int find = find_workspace(workspace_name);
        if(find>-1){
            WorkSpaceManager.getInstance().currentWorkspace = find;
            System.out.println("切换到工作区 "+WorkSpaceManager.getInstance().workSpaceList.get(find).workspace_name+" 。");
            return true;
        }
        System.out.println("工作区 " + workspace_name+" 不存在。");
        return false;
    }

    public boolean close(){
        if(WorkSpaceManager.getInstance().workSpaceList.size()<1||WorkSpaceManager.getInstance().currentWorkspace==-1){
            return false;
        }
        if(WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.modified){
            doc_save_reminder();
        }
        System.out.println("工作区 "+WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).workspace_name+" 已关闭");
        WorkSpaceManager.getInstance().workSpaceList.remove(WorkSpaceManager.getInstance().currentWorkspace);
        WorkSpaceManager.getInstance().currentWorkspace=-1;

        return true;
    }



    public boolean exit(){
        boolean modified=false;
        for (WorkSpace workSpace : WorkSpaceManager.getInstance().workSpaceList){
            if(workSpace.document.modified){
                modified=true;
            }
        }
        if(modified){
            workspace_save_reminder();
        }else {
            clean_recent_workspace();
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(recent_workspace_path),WorkSpaceManager.getInstance());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static void clean_recent_workspace() {
        File check_file= new File(recent_workspace_path);
        if(check_file.exists()){
            check_file.delete();
        }
    }

    public boolean list_workspace(){
        StringBuilder list=new StringBuilder();
        for (int i=0;i<WorkSpaceManager.getInstance().workSpaceList.size();i++){
            if(i==WorkSpaceManager.getInstance().currentWorkspace){
                list.append("->");
            }else {
                list.append("  ");
            }
            list.append(WorkSpaceManager.getInstance().workSpaceList.get(i).workspace_name);
            if(WorkSpaceManager.getInstance().workSpaceList.get(i).document.modified){
                list.append(" *");
            }else {
                list.append("  ");
            }
            list.append("\n");
        }
        System.out.print(list);
        return true;
    }

    private int find_document(String file_path){
        String absolute_path;
        File check_file= new File(file_path);
        if(check_file.exists()){
            absolute_path = check_file.getAbsolutePath();
            for (WorkSpace workSpace : WorkSpaceManager.getInstance().workSpaceList){
                if(absolute_path.equals(workSpace.document.file_path)){
                    return WorkSpaceManager.getInstance().workSpaceList.indexOf(workSpace);
                }
            }
        }
        return -1;
    }

    private int find_workspace(String workspace_name){
        for (WorkSpace workSpace : WorkSpaceManager.getInstance().workSpaceList){
            if(workSpace.workspace_name.equals(workspace_name)){
                return WorkSpaceManager.getInstance().workSpaceList.indexOf(workSpace);
            }
        }
        return -1;
    }

    private static void doc_save_reminder() {
        System.out.println("Do you want to save the current workspace [Y\\N] ？");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(answer.equals("Y")){
            WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.save();
        }else if(!answer.equals("N")){
            System.out.println("错误输入，已默认不保存并继续。");
        }
    }

    private static void workspace_save_reminder() {
        System.out.println("Do you want to save the unsaved workspace [Y\\N] ？");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(answer.equals("Y")){
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(recent_workspace_path),WorkSpaceManager.getInstance());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(!answer.equals("N")){
            System.out.println("错误输入，已默认不保存并继续。");
            clean_recent_workspace();
        }else {
            clean_recent_workspace();
        }
    }

    public static WorkSpaceManager load_recent(){
        WorkSpaceManager recent = new WorkSpaceManager();
        File check_file= new File(recent_workspace_path);
        if(check_file.exists()){
            try {
                ObjectMapper mapper = new ObjectMapper();
                recent = mapper.readValue(new File(recent_workspace_path),WorkSpaceManager.class);
                System.out.println("\n已加载上次保存的工作区");
                System.out.print(">>>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return recent;
    }

    public boolean ls() {
        ListFolder listFolder = new ListFolder();
        listFolder.list_cur_work_folder();
        return true;
    }
}
