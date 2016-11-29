package com.wang.cli;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by paopao on 16/11/28.
 */
public class Shell {

    private static final String NOT_FOUND = "NOT FOUND";
    private String prompt;
    private String appName;
    private Input input;
    private Output output;
    private List<Object> handlers;
    private List<ShellCommand> commands = new ArrayList<ShellCommand>();

    private Shell() {

    }

    private Shell(ShellBuilder sb) {
        this.prompt = sb.prompt + "> ";
        this.appName = sb.appName;
        this.input = sb.input;
        this.output = sb.output;
        this.handlers = sb.handlers;

        for (Object handler : sb.handlers) {
            Method[] methods = handler.getClass().getMethods();
            for (Method method : methods) {
                Command command = method.getAnnotation(Command.class);
                if (command == null) {
                    return;
                }
                ShellCommand shellCommand = new ShellCommand();
                shellCommand.setName(command.name());
                shellCommand.setAbbrev(command.abbrev());
                shellCommand.setDescription(command.description());
                shellCommand.setMethod(method);
                shellCommand.setHandler(handler);
                this.commands.add(shellCommand);
            }
        }
    }

    public void loopListen() {
        if (input == null) {
            input = new Input() {
                Scanner scanner = new Scanner(System.in);

                public String readCommand() {
                    return scanner.nextLine();
                }
            };
        }
        while (true) {
            System.out.print(prompt);
            String command = input.readCommand();
            processCommand(command);
        }
    }

    public void processCommand(String command) {
        ShellCommand target = null;
        String[] splitCommand = command.split("\\s+");
        if (splitCommand.length == 0) {
            print(NOT_FOUND);
            return;
        }
        command = splitCommand[0];
        String[] args = new String[splitCommand.length - 1];
        System.arraycopy(splitCommand, 1, args, 0, splitCommand.length - 1);
        for (ShellCommand sc : commands) {
            if (command.equals(sc.getName()) || command.equals(sc.getAbbrev())) {
                target = sc;
            }
        }
        if (target != null) {
            print(target.invoke(args));
        } else {
            print(NOT_FOUND);
        }
    }

    private void print(Object... objects) {
        for (Object obj : objects) {
            System.out.print(prompt);
            System.out.println(obj);
        }
    }

    public static class ShellBuilder {
        private String prompt;
        private String appName;
        private Input input;
        private Output output;
        private List<Object> handlers = new ArrayList<Object>();

        public ShellBuilder() {

        }

        public ShellBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public ShellBuilder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public ShellBuilder input(Input input) {
            this.input = input;
            return this;
        }

        public ShellBuilder output(Output output) {
            this.output = output;
            return this;
        }

        public ShellBuilder setHandler(Object... handlers) {
            this.handlers.addAll(Arrays.asList(handlers));
            return this;
        }

        public Shell build() {
            return new Shell(this);
        }
    }
}
