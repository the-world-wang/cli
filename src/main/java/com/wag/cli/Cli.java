package com.wag.cli;

/**
 * Created by paopao on 16/11/28.
 */
public class Cli {

    public static void main(String[] args) {
        Shell shell = new Shell.ShellBuilder().prompt("hello").appName("hello").setHandler(new HelpCommand()).build();
        shell.loopListen();
    }
}
