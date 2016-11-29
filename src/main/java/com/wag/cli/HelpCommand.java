package com.wag.cli;

/**
 * Created by paopao on 16/11/29.
 */
public class HelpCommand {

    @Command(name = "list", abbrev = "ls", description = "list some thing")
    public String list() {
        return "hello";
    }
}
