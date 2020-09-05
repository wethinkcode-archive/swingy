package com.swingy;

import com.swingy.utils.database.DatabaseWrapper;
import com.swingy.view.console.ConsoleView;
import com.swingy.view.gui.GUIView;

import javax.swing.*;

import static com.swingy.utils.Colors.*;
import static com.swingy.utils.Log.log;

public class Swingy {
    public static void main(String[] args) {
        try {
            String view = args[0];
            DatabaseWrapper.getInstance().setupDatabase();
            if (view.toLowerCase().equals("console")) {
                ConsoleView.run();
            } else if (view.toLowerCase().equals("gui")) {
//                GameWindow.run();
                SwingUtilities.invokeLater(GUIView::new);
//                new Test();
            } else {
                log(ANSI_RED + ":::ERROR::: Invalid argument" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log(ANSI_RED + ":::ERROR::: Invalid argument" + ANSI_RESET);
        }
    }
}
