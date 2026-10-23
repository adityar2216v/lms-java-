package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);
        initStyle();
    }

    private void initStyle() {
        setFont(UITheme.FONT_BODY);
        setForeground(UITheme.TEXT_MAIN);
        setRowHeight(42);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(243, 244, 246)); // Very light grid
        setSelectionBackground(new Color(224, 231, 255)); // Indigo 100
        setSelectionForeground(UITheme.PRIMARY_DARK);
        setIntercellSpacing(new Dimension(0, 1));
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header Styling - sleek dark header with proper spacing
        JTableHeader header = getTableHeader();
        header.setFont(UITheme.FONT_SMALL_BOLD);
        header.setBackground(new Color(31, 41, 55)); // Gray 800
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 44));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(new Color(31, 41, 55));
                lbl.setForeground(new Color(209, 213, 219)); // Gray 300
                lbl.setFont(UITheme.FONT_SMALL_BOLD);
                lbl.setBorder(new EmptyBorder(8, 14, 8, 14));
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                return lbl;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        // Cell Renderer with alternating rows + status-aware coloring
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBorder(new EmptyBorder(8, 14, 8, 14));

                String text = value != null ? value.toString() : "";

                if (isSelected) {
                    lbl.setBackground(new Color(224, 231, 255));
                    lbl.setForeground(UITheme.PRIMARY_DARK);
                    lbl.setFont(UITheme.FONT_BODY_BOLD);
                } else {
                    // Alternating row colors
                    if (row % 2 == 0) {
                        lbl.setBackground(Color.WHITE);
                    } else {
                        lbl.setBackground(new Color(249, 250, 251)); // Gray 50
                    }

                    // Status-aware text colors
                    if (text.contains("Active") || text.contains("Enrolled") || text.contains("Resolved") || text.contains("Graded") || text.contains("Passed")) {
                        lbl.setForeground(UITheme.SUCCESS_DARK);
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else if (text.contains("Inactive") || text.contains("Failed")) {
                        lbl.setForeground(UITheme.DANGER_DARK);
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else if (text.contains("Pending") || text.contains("Submitted")) {
                        lbl.setForeground(new Color(180, 130, 0));
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else {
                        lbl.setForeground(UITheme.TEXT_MAIN);
                        lbl.setFont(UITheme.FONT_BODY);
                    }
                }
                return lbl;
            }
        });
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public static JScrollPane wrapInScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
}
