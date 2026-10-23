package com.lms.gui;

import com.lms.gui.db.DataStore;
import com.lms.gui.views.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class LmsGuiApplication {
    public static void main(String[] args) {
        System.out.println("Starting LMS Desktop GUI Application...");

        // Configure Swing rendering quality
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                    try {
                        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch (Exception ignored2) {}
                }

                // Initialize persistent data store
                DataStore.getInstance();

                // Launch desktop GUI login window
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                loginFrame.toFront();
                loginFrame.requestFocus();
                System.out.println("Login window launched successfully (visible=" + loginFrame.isVisible() + ")!");
            } catch (Throwable t) {
                System.err.println("CRITICAL ERROR launching GUI: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
