package org.rozenberg.craftshop.controller.command;

import org.rozenberg.craftshop.controller.command.impl.common.ChangeLanguageCommand;

import java.util.Optional;

public enum CommandProvider {
    CHANGE_LANGUAGE(new ChangeLanguageCommand());

    private final Command command;

    CommandProvider(Command command){
        this.command = command;
    }

    public Command getCommandName(){
        return command;
    }

    public static Optional<Command> getCommand(String command){
        if(command == null || command.isEmpty()){
            return Optional.empty();
        }
        Optional<Command> result;
        try{
            CommandProvider realCommand = CommandProvider.valueOf(command.toUpperCase());
            result = Optional.of(realCommand.getCommandName());
        }
        catch (IllegalArgumentException e){
            result = Optional.empty();
        }
        return result;
    }
}
