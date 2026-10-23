package com.lms.gui;

import com.lms.gui.db.DataStore;
import com.lms.gui.theme.BrandLogo;
import com.lms.gui.views.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Main application launcher for SmartLearn India LMS Desktop Platform.
 * Tagline: Learn Smarter. Grow Further.
 */
public class LmsGuiApplication {
    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("       SmartLearn India — Smart Learning Management System        ");
        System.out.println("                 Learn Smarter. Grow Further.                     ");
        System.out.println("==================================================================");

        // High-DPI and Anti-Aliasing JVM Flags
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");
        System.setProperty("sun.java2d.dpiaware", "true");

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

                // Global font refinement for clean typography
                Font defaultFont = new Font("Segoe UI", Font.PLAIN, 13);
                UIManager.put("Button.font", defaultFont);
                UIManager.put("Label.font", defaultFont);
                UIManager.put("TextField.font", defaultFont);
                UIManager.put("Table.font", defaultFont);
                UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

                // Initialize persistent data store
                DataStore.getInstance();

                // Launch desktop GUI login window
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setIconImage(BrandLogo.createLogoImage(64));
                loginFrame.setVisible(true);
                loginFrame.toFront();
                loginFrame.requestFocus();
                System.out.println("SmartLearn India portal launched successfully!");
            } catch (Throwable t) {
                System.err.println("CRITICAL ERROR launching SmartLearn India GUI: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
