package org.example.invoker;

import lombok.Data;
import org.example.command.Command;
import org.example.observer.Observer;

import java.util.ArrayList;
import java.util.List;
@Data
public class Invoker {
    private Command cmd;
    private List<String> cmd_args = new ArrayList<>();
    private String cmd_type;
    private List<Observer> observers = new ArrayList<>();


    public void setCommand(Command cmd,String cmd_type,List<String> cmd_args){
        this.cmd=cmd;
        this.cmd_type=cmd_type;
        this.cmd_args=cmd_args;
    }
    public void execCommand() {
        boolean exec_success = cmd.execute();
        if(exec_success){
            for (Observer observer: observers){
                observer.update();
            }
        }
    }

    public void attach(Observer observer){
        observers.add(observer);
    }
}
