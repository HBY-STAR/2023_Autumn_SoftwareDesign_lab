package org.example.command.ConcreteCommand.EditCommand;

import org.example.command.Command;

public class NothingCommand implements Command {
    @Override
    public boolean execute(){
        //Nothing
        return false;
    }
}
