package org.example.receiver;

import lombok.Data;
import org.example.utils.ListTree;

import java.io.*;
import java.util.*;

@Data
public class Document {
    public  List<List<String>> doc_lines = new ArrayList<>();
    public boolean modified=false;
    public  String file_path = null;
    public  Stack<List<String>> edit_lines = new Stack<>();
    public boolean record_edit = true;
    public boolean load(String filePath) {
        try{
            File check_file= new File(filePath);
            //检测是否已加载文件
            if(file_path != null){
                //System.out.println("文件: "+file_path+" 已关闭，开始加载新文件...");
                file_path=null;
                doc_lines=new ArrayList<>();
                return load(filePath);
            }
            //检测文件是否存在
            if(check_file.exists()){
                file_path=check_file.getAbsolutePath();
            } else {
                //检测父路径
                //如果父路径为空则自动创建文件
                if(check_file.getParentFile()==null){
                    System.out.println("在当前工作目录下创建文件...");
                    if(!check_file.createNewFile()){
                        System.out.println("异常，文件创建失败！");
                        System.exit(-1);
                    }
                    file_path=check_file.getAbsolutePath();

                    modified=false;
                    return true;
                }
                //若父路径不为空但不为目录
                else if(!check_file.getParentFile().isDirectory()){
                    System.out.println("路径: "+check_file.getParent()+" 不存在");
                    return false;
                }
                //父路径正常
                else {
                    if(!check_file.createNewFile()){
                        System.out.println("异常，文件创建失败！");
                        System.exit(-1);
                    }
                    file_path=check_file.getAbsolutePath();

                    //清空栈
                    edit_lines=new Stack<>();

                    return true;
                }
            }
            // 构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file_path));
            String s;
            // 使用readLine方法，一次读一行
            while((s = br.readLine())!=null){
                List<String> add = getListStrings(s);
                doc_lines.add(add);
            }
            br.close();

            //清空栈
            edit_lines=new Stack<>();

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean save(){
        try{
            if(file_path == null){
                System.out.println("暂未加载任何文件！");
                return false;
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file_path));
            StringBuilder strBd = new StringBuilder();
            for(List<String> docLine : doc_lines){
                for (String docElement: docLine){
                    strBd.append(docElement).append(" ");
                }
                strBd.append("\n");
            }
            bw.write(strBd.toString());
            bw.flush();
            bw.close();
            System.out.println("保存成功！");

            //清空栈
            modified=false;

            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean insert(int row, List<String> text) {
        try{
            if(file_path == null){
                System.out.println("暂未加载任何文件！");
                return false;
            }
            if(row<=0){
                System.out.println("若指定行号，则行号应为一个自然数");
                return false;
            }
            else {
                //检查格式
                if(text.size()!=2){
                    System.out.println("插入格式错误");
                    return false;
                }
                String check = text.get(0);
                if(!check.equals("#")&&!check.equals("##")&&!check.equals("###")&&!check.equals("####")&&
                        !check.equals("#####")&&!check.equals("######")&&!check.equals("*")&&!check.endsWith(".")){
                    System.out.println("插入格式错误");
                    return false;
                }

                if(row-1<=doc_lines.size()){
                    doc_lines.add(row-1,text);
                }
                else {
                    for(int i=0;i<row-1-doc_lines.size();i++){
                        doc_lines.add(getListStrings(""));
                    }
                    doc_lines.add(doc_lines.size(),text);
                }

                //记录编辑命令
                if(record_edit){
                    record_edit("insert",row, text);
                }

                modified=true;

                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean delete_row(int row) {
        if(row<=0){
            System.out.println("若指定行号，则行号应为一个自然数");
            return false;
        }
        else {
            if(row-1<=doc_lines.size()){

                //记录编辑命令
                if(record_edit){
                    record_edit("delete",row, doc_lines.get(row-1));
                }
                
                doc_lines.remove(row-1);
                modified=true;
                return true;
            }
            else {
                System.out.println("行号超出文件范围");
                modified=true;
                return false;
            }
        }
    }
    public boolean delete_text(List<String> text) {
        if(text.size()==0){
            System.out.println("指定的字符串为空");
            return false;
        }else{
            int flag=0;
            for (int i=0;i<doc_lines.size();i++){
               if(doc_lines.get(i).get(1).equals(text.get(0))){

                   //记录编辑命令
                   if(record_edit){
                       record_edit("delete",i+1, doc_lines.get(i));
                   }

                   doc_lines.remove(doc_lines.get(i));
                   flag++;
                   break;
               }
            }
            if(flag==0){
                System.out.println("未查找到指定字符串");
                return false;
            }
            else{
                modified=true;
                return true;
            }
        }
    }
    public boolean list() {
        try{
            if(file_path == null){
                System.out.println("暂未加载任何文件！");
            }else {
                for (List<String> docLine : doc_lines) {
                    for (String docElement : docLine){
                        System.out.print(docElement);
                        System.out.print(" ");
                    }
                    System.out.print("\n");
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean dir_tree(List<String> text){
        ListTree listTree = new ListTree();
        if(text.size()==0){
            listTree.PrintAll(doc_lines);
            return true;
        }else{
            int find=-1;
            for (int i=0;i<doc_lines.size();i++){
                if(doc_lines.get(i).get(1).equals(text.get(0))){
                    find=i;
                }
            }
            if(find==-1){
                System.out.println("未查找到指定字符串");
                return false;
            }else {
                listTree.PrintDir(doc_lines,find);
            }
        }
        return true;
    }
    public boolean list_tree() {
        ListTree listTree = new ListTree();
        listTree.PrintAll(doc_lines);
        return true;
    }
    public boolean redo() {
        if(edit_lines.size()>=1){
            List<String> edit_line = edit_lines.lastElement();
            if(edit_line.get(0).equals("undo")){
                String cmd = edit_line.get(1);
                int row = Integer.parseInt(edit_line.get(2));
                List<String> text = new ArrayList<>();
                text.add(edit_line.get(3));
                text.add(edit_line.get(4));

                record_edit=false;
                if(cmd.equals("insert")){
                    insert(row,text);
                }else{
                    delete_row(row);
                }
                record_edit=true;

                edit_lines.pop();

                return true;
            }else {
                System.out.println("redo的上一条指令只可为undo");
                return false;
            }
        }else {
            System.out.println("没有可重做的指令");
            return false;
        }
    }
    public boolean undo() {
        if(edit_lines.size()>=1){
            List<String> edit_line;
            int skip = 1;
            do{
                edit_line = edit_lines.elementAt(edit_lines.size()-skip);
                skip++;
            }while (edit_line!=null && edit_line.get(0).equals("undo"));

            if(edit_line!=null){
                if(edit_line.size()==4){
                    String cmd = edit_line.get(0);
                    int row = Integer.parseInt(edit_line.get(1));
                    List<String> text = new ArrayList<>();
                    text.add(edit_line.get(2));
                    text.add(edit_line.get(3));

                    record_edit=false;
                    if(cmd.equals("insert")){
                        delete_row(row);
                    }else{
                        insert(row,text);
                    }
                    record_edit=true;

                    List<String> undo_line = new ArrayList<>();
                    undo_line.add("undo");
                    undo_line.add(cmd);
                    undo_line.add(Integer.toString(row));
                    undo_line.add(text.get(0));
                    undo_line.add(text.get(1));
                    edit_lines.push(undo_line);

                    return true;
                }
            }else {
                System.out.println("没有可撤销的指令");
                return false;
            }
        }else {
            System.out.println("没有可撤销的指令");
            return false;
        }
        return false;
    }
    private void record_edit(String cmd,int row, List<String> text) {
        List<String> edit_line = new ArrayList<>();
        edit_line.add(cmd);
        edit_line.add(Integer.toString(row));
        edit_line.add(text.get(0));
        edit_line.add(text.get(1));
        edit_lines.push(edit_line);
    }
    private static List<String> getListStrings(String s) {
        List<String> add = new ArrayList<>();
        Scanner line_scanner = new Scanner(s);
        while (line_scanner.hasNext()){
            add.add(line_scanner.next());
        }
        return add;
    }
}
