package org.example.observer.ConcreteObserver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.invoker.Invoker;
import org.example.observer.Observer;
import org.example.receiver.WorkSpaceManager;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.FileUtils.check_and_create_file_if_not_exist;

@NoArgsConstructor
@AllArgsConstructor
public class StatsLog extends Observer {
    public LocalDateTime ses_start;
    public LocalDateTime ses_end;
    public String current_file=null;
    public LocalDateTime current_file_start;
    public LocalDateTime current_file_end;
    public String current_stats;
    public static final String stats_file_path = "run_time_files/stats.log";

    public StatsLog(Invoker invoker){
        this.invoker = invoker;
        invoker.attach(this);
        ses_start_log();
    }

    @Override
    public void update() {
        if (this.invoker.getCmd_type().equals("exit")) {
            if (current_file != null) {
                file_end_log();
            }
            ses_end_log();
            System.out.println("Bye~");
            System.exit(0);
        }
        if (this.invoker.getCmd_type().equals("load") || this.invoker.getCmd_type().equals("open")) {
            if (current_file != null) {
                file_end_log();
            }
            file_start_log();
        }
    }

    public void ses_start_log() {
        ses_start = LocalDateTime.now();
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        stringBuilder.append("session start at ").append(fmt.format(ses_start)).append('\n');
        current_stats=stringBuilder.toString();
        write_stats();
    }
    public void ses_end_log(){
        ses_end = LocalDateTime.now();
        file_end_log();
    }

    private void write_stats() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(current_stats);
        check_and_create_file_if_not_exist(stats_file_path);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(stats_file_path,true));
            bw.write(stringBuilder.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void file_start_log() {
        current_file_start=LocalDateTime.now();
        current_file= WorkSpaceManager.getInstance().workSpaceList.get(WorkSpaceManager.getInstance().currentWorkspace).document.file_path;
    }
    public void file_end_log() {
        current_file_end=LocalDateTime.now();
        File getFileName;
        if(current_file!=null){
            getFileName = new File(current_file);
            current_stats="./" + getFileName.getName() + " " + get_time_string(Duration.between(current_file_start, current_file_end).getSeconds())+ "\n";
            current_file=null;
            write_stats();
        }
    }

    private String get_time_string(long second){
        StringBuilder stringBuilder = new StringBuilder();
        long time=second;
        int minute = 60;
        int hour = 60*60;
        int day = 60*60*24;
        if(time/day > 0){
            stringBuilder.append(time/day).append(" 天 ");
            time %=day;
        }
        if(time/hour > 0){
            stringBuilder.append(time/hour).append(" 小时 ");
            time %=hour;
        }
        if(time/minute > 0){
            stringBuilder.append(time/minute).append(" 分钟 ");
            time %=minute;
        }
        stringBuilder.append(time).append(" 秒 ");
        return stringBuilder.toString();
    }

    public boolean stats_current() {
        File getFileName = new File(current_file);
        System.out.println("./" + getFileName.getName() + " " + get_time_string(Duration.between(current_file_start, LocalDateTime.now()).getSeconds()));
        return true;
    }

    public boolean stats_all() {
        try{
            BufferedReader br = new BufferedReader(new FileReader(stats_file_path));
            String s;
            // 使用readLine方法，一次读一行
            while((s = br.readLine())!=null){
                System.out.println(s);
            }
            br.close();
            stats_current();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
