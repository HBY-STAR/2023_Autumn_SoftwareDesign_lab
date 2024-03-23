package org.example.command.ConcreteCommand.LogCommand;

import org.example.command.Command;
import org.example.observer.ConcreteObserver.StatsLog;

import java.util.List;

public class StatsCommand implements Command {
    StatsLog statsLog;
    boolean use_current;

    boolean error=false;
    public StatsCommand(List<String> cmdArgs, StatsLog statsLog) {
        this.statsLog=statsLog;
        if(cmdArgs.size()==0){
            use_current=true;
        }else{
            String arg = cmdArgs.get(0);
            if(arg.equals("current")){
                use_current=true;
            }else if(arg.equals("all")){
                use_current=false;
            }else{
                error=true;
            }
        }
    }
    @Override
    public boolean execute(){
        if(error){
            System.out.println("stats参数错误");
            return false;
        }else {
            if(use_current){
                return statsLog.stats_current();
            }else {
                return statsLog.stats_all();
            }
        }
    }
}
