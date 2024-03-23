package org.example.observer.ConcreteObserver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.invoker.Invoker;
import org.example.observer.Observer;
import org.example.receiver.WorkSpaceManager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.FileUtils.check_and_create_file_if_not_exist;

@AllArgsConstructor
@NoArgsConstructor
public class HistoryLog extends Observer {
    public String cur_file_dir = "run_time_files/";
    public String cur_work_space;
    public String cur_file_path ="run_time_files/default_history.log";
    public HistoryLog(Invoker invoker){
        this.invoker = invoker;
        this.invoker.attach(this);
        cur_work_space = "default";
    }
    @Override
    public void update() {
        if(WorkSpaceManager.getInstance().currentWorkspace>-1){
            cur_work_space = WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).workspace_name;
            cur_file_path = cur_file_dir+cur_work_space+"_history.log";
        }
        if(this.invoker.getCmd_type().equals("load")||this.invoker.getCmd_type().equals("open")){
            ses_start_log();
        }
        if(!this.invoker.getCmd_type().equals("exit")){
            cmd_log(this.invoker.getCmd_type(),this.invoker.getCmd_args());
        }
    }

    public void ses_start_log(){
        LocalDateTime ses_start = LocalDateTime.now();
        check_and_create_file_if_not_exist(cur_file_path);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(cur_file_path,true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        stringBuilder.append("workspace start at ").append(fmt.format(ses_start)).append('\n');
        try {
            bw.write(stringBuilder.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void cmd_log(String cmd, List<String> cmd_args){
        LocalDateTime cmd_start = LocalDateTime.now();
        check_and_create_file_if_not_exist(cur_file_path);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(cur_file_path,true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        stringBuilder.append(fmt.format(cmd_start)).append(' ').append(cmd);
        for(String cmd_arg:cmd_args){
            stringBuilder.append(" ").append(cmd_arg);
        }
        stringBuilder.append('\n');
        try {
            bw.write(stringBuilder.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean history_by_num(int num){
        try{
            List<String> history_lines=new ArrayList<>();
            if(num<0){
                System.out.println("history参数错误");
                return false;
            } else{
                BufferedReader br = new BufferedReader(new FileReader(cur_file_path));
                String s;
                // 使用readLine方法，一次读一行
                while((s = br.readLine())!=null){
                    if(s.charAt(0)!='s'){
                        history_lines.add(s);
                    }
                }
                br.close();
                for(int i=history_lines.size()-1;i>=history_lines.size()-num;i--){
                    System.out.println(history_lines.get(i));
                }
                br.close();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean history_all(){
        try{
            List<String> history_lines=new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(cur_file_path));
            String s;
            // 使用readLine方法，一次读一行
            while((s = br.readLine())!=null){
                if(s.charAt(0)!='s'){
                    history_lines.add(s);
                }
            }
            br.close();
            for(int i=history_lines.size()-1;i>=0;i--){
                if(history_lines.get(i).charAt(0) !='w'){
                    System.out.println(history_lines.get(i));
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
