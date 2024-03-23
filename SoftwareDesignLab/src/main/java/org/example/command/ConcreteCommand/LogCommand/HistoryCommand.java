package org.example.command.ConcreteCommand.LogCommand;

import org.example.command.Command;
import org.example.observer.ConcreteObserver.HistoryLog;

import java.util.List;

public class HistoryCommand implements Command {
    HistoryLog historyLog;
    boolean use_all;

    int num;

    public HistoryCommand(List<String> cmdArgs, HistoryLog historyLog) {
        this.historyLog = historyLog;
        if(cmdArgs.size()==0){
            use_all=true;
        }
        else {
            try {
                String str_num = cmdArgs.get(0);
                num = Integer.parseInt(str_num);
                use_all=false;
            }catch(NumberFormatException e)
            {
                num=-1;
                use_all=false;
            }
        }

    }

    @Override
    public boolean execute() {
        if(use_all){
            return historyLog.history_all();
        }
        else{
            return historyLog.history_by_num(num);
        }
    }
}
