package org.example.command.ConcreteCommand.ShowCommand;

import org.example.command.Command;

public class HelpCommand implements Command {
    @Override
    public boolean execute(){
        System.out.println("|-----------HELP-----------|");
        System.out.println("|  load  ⽂件路径            |");
        System.out.println("|  save                    |");
        System.out.println("|  insert [⾏号] 标题/⽂本    |");
        System.out.println("|  append-head 标题/⽂本     |");
        System.out.println("|  append-tail 标题/⽂本     |");
        System.out.println("|  delete 标题/⽂本          |");
        System.out.println("|  delete ⾏号              |");
        System.out.println("|  undo                    |");
        System.out.println("|  redo                    |");
        System.out.println("|  list                    |");
        System.out.println("|  list-tree               |");
        System.out.println("|  dir-tree [⽬录]          |");
        System.out.println("|  history [数量]           |");
        System.out.println("|  stats [all | current]   |");
        System.out.println("|-----------END------------|");
        return true;
    }
}
