package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class ModernTable extends JTable {
    private TableRowSorter<DefaultTableModel> rowSorter;

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
        setGridColor(UITheme.BORDER);
        setSelectionBackground(UITheme.PRIMARY_LIGHT);
        setSelectionForeground(UITheme.PRIMARY_DARK);
        setIntercellSpacing(new Dimension(0, 1));
        setFillsViewportHeight(true);

        // Enable Row Sorter for instant searching & column sorting
        DefaultTableModel model = (DefaultTableModel) getModel();
        rowSorter = new TableRowSorter<>(model);
        setRowSorter(rowSorter);

        // Header Styling
        JTableHeader header = getTableHeader();
        header.setFont(UITheme.FONT_BODY_BOLD);
        header.setBackground(UITheme.SIDEBAR_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 44));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(UITheme.SIDEBAR_BG);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(UITheme.FONT_BODY_BOLD);
                lbl.setBorder(new EmptyBorder(8, 14, 8, 14));
                return lbl;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        // Custom Cell Renderer with Status Pills and Alternating Colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBorder(new EmptyBorder(6, 14, 6, 14));

                String strVal = value != null ? value.toString() : "";

                if (isSelected) {
                    lbl.setBackground(UITheme.PRIMARY_LIGHT);
                    lbl.setForeground(UITheme.PRIMARY_DARK);
                    lbl.setFont(UITheme.FONT_BODY_BOLD);
                } else {
                    lbl.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    lbl.setFont(UITheme.FONT_BODY);

                    // Stylize status keywords
                    if (strVal.contains("Active") || strVal.contains("Passed") || strVal.contains("Graded") || strVal.contains("Resolved")) {
                        lbl.setForeground(UITheme.SUCCESS_DARK);
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else if (strVal.contains("Inactive") || strVal.contains("Failed") || strVal.contains("Delete")) {
                        lbl.setForeground(UITheme.DANGER_DARK);
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else if (strVal.contains("Submitted") || strVal.contains("Pending") || strVal.contains("Awaiting")) {
                        lbl.setForeground(new Color(217, 119, 6)); // Amber 600
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else if (strVal.contains("Pinned")) {
                        lbl.setForeground(UITheme.PRIMARY);
                        lbl.setFont(UITheme.FONT_BODY_BOLD);
                    } else {
                        lbl.setForeground(UITheme.TEXT_BODY);
                    }
                }
                return lbl;
            }
        });
    }

    public void filter(String query) {
        if (rowSorter == null) return;
        if (query == null || query.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query.trim()));
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public static JScrollPane wrapInScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
}
