/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.*;
import javax.swing.*;

public class ThemeManager {
    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static Color getBackgroundColor() {
        return currentTheme == Theme.DARK ? new Color(45, 45, 45) : Color.WHITE;
    }

    public static Color getForegroundColor() {
        return currentTheme == Theme.DARK ? Color.WHITE : Color.BLACK;
    }

    public static void applyTheme(Component component) {
        Color bg = getBackgroundColor();
        Color fg = getForegroundColor();
        applyComponentTheme(component, bg, fg);
    }

    private static void applyComponentTheme(Component comp, Color bg, Color fg) {
        if (comp instanceof JLabel || comp instanceof JButton ||
            comp instanceof JCheckBox || comp instanceof JComboBox ||
            comp instanceof JTextField) {
            comp.setForeground(fg);
            comp.setBackground(bg);
        }

        if (comp instanceof JPanel || comp instanceof JFrame || comp instanceof JComponent) {
            comp.setBackground(bg);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyComponentTheme(child, bg, fg);
            }
        }
    }
}

