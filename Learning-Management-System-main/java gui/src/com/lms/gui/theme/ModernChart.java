package com.lms.gui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;

/**
 * Pure Java 2D vector chart components for SmartLearn India dashboards.
 * Provides interactive weekly learning activity bar charts and circular completion donut charts.
 */
public class ModernChart {

    /**
     * Interactive 7-Day Study Activity Bar Chart.
     */
    public static class WeeklyActivityChart extends JPanel {
        private String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        private double[] hours = {4.5, 5.0, 6.2, 3.8, 5.5, 7.0, 4.2};
        private double maxHours = 8.0;

        public WeeklyActivityChart() {
            setOpaque(false);
            setPreferredSize(new Dimension(360, 180));
            setBorder(new EmptyBorder(10, 10, 10, 10));
        }

        public void setData(String[] days, double[] hours) {
            this.days = days;
            this.hours = hours;
            double max = 4.0;
            for (double h : hours) {
                if (h > max) max = h;
            }
            this.maxHours = Math.ceil(max);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            UITheme.setupAntiAliasing(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();

            int leftPadding = 30;
            int rightPadding = 16;
            int bottomPadding = 26;
            int topPadding = 24;

            int chartWidth = w - leftPadding - rightPadding;
            int chartHeight = h - topPadding - bottomPadding;

            // Draw subtle horizontal gridlines
            g2.setColor(new Color(226, 232, 240, 150));
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0));

            for (int i = 0; i <= 4; i++) {
                int y = topPadding + (int) (chartHeight * (1.0 - (i / 4.0)));
                g2.drawLine(leftPadding, y, w - rightPadding, y);

                // Y-axis label
                g2.setFont(UITheme.FONT_SMALL);
                g2.setColor(UITheme.TEXT_LIGHT);
                String valStr = String.format("%.0fh", (maxHours * i / 4.0));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(valStr, leftPadding - fm.stringWidth(valStr) - 6, y + 4);
            }

            // Draw Bars
            int numBars = Math.min(days.length, hours.length);
            if (numBars == 0) return;

            float slotWidth = (float) chartWidth / numBars;
            float barWidth = Math.max(14, slotWidth * 0.45f);

            for (int i = 0; i < numBars; i++) {
                float cx = leftPadding + (i + 0.5f) * slotWidth;
                float bx = cx - (barWidth / 2.0f);
                float val = (float) hours[i];
                float barHeight = (float) ((val / maxHours) * chartHeight);
                float by = topPadding + (chartHeight - barHeight);

                // Bar Gradient
                GradientPaint gp = new GradientPaint(
                        bx, by, UITheme.PRIMARY,
                        bx, topPadding + chartHeight, UITheme.PRIMARY_DARK
                );
                // Highlight Saturday/Sunday with saffron accent
                if (i >= 5) {
                    gp = new GradientPaint(
                            bx, by, UITheme.SAFFRON,
                            bx, topPadding + chartHeight, UITheme.SAFFRON_DARK
                    );
                }

                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(bx, by, barWidth, barHeight, 8, 8));

                // Value above bar
                g2.setFont(UITheme.FONT_SMALL_BOLD);
                g2.setColor(UITheme.TEXT_MAIN);
                String hrsStr = String.format("%.1fh", val);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(hrsStr, cx - (fm.stringWidth(hrsStr) / 2.0f), by - 5);

                // Day below bar
                g2.setFont(UITheme.FONT_SMALL);
                g2.setColor(UITheme.TEXT_MUTED);
                String dayStr = days[i];
                g2.drawString(dayStr, cx - (fm.stringWidth(dayStr) / 2.0f), h - 8);
            }
        }
    }

    /**
     * Circular Donut Progress Chart with Percentage.
     */
    public static class ProgressDonutChart extends JPanel {
        private int progressPercent = 75;
        private String label = "Curriculum Completion";

        public ProgressDonutChart(int percent, String label) {
            this.progressPercent = Math.max(0, Math.min(100, percent));
            this.label = label;
            setOpaque(false);
            setPreferredSize(new Dimension(170, 170));
        }

        public void setProgress(int percent) {
            this.progressPercent = Math.max(0, Math.min(100, percent));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            UITheme.setupAntiAliasing(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h) - 20;
            int x = (w - size) / 2;
            int y = (h - size) / 2;
            int strokeWidth = 14;

            // Background circle track
            g2.setColor(new Color(241, 245, 249)); // Slate 100
            g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Arc2D.Float(x + strokeWidth / 2.0f, y + strokeWidth / 2.0f, size - strokeWidth, size - strokeWidth, 0, 360, Arc2D.OPEN));

            // Foreground progress arc with gradient
            float angle = -(progressPercent / 100.0f) * 360.0f;
            g2.setColor(UITheme.PRIMARY);
            g2.draw(new Arc2D.Float(x + strokeWidth / 2.0f, y + strokeWidth / 2.0f, size - strokeWidth, size - strokeWidth, 90, angle, Arc2D.OPEN));

            // Saffron end indicator dot
            if (progressPercent > 0 && progressPercent < 100) {
                double rad = Math.toRadians(90 + angle);
                float radius = (size - strokeWidth) / 2.0f;
                float dotX = (float) (x + size / 2.0 + radius * Math.cos(rad));
                float dotY = (float) (y + size / 2.0 - radius * Math.sin(rad));
                g2.setColor(UITheme.SAFFRON);
                g2.fill(new Ellipse2D.Float(dotX - 5, dotY - 5, 10, 10));
            }

            // Center Text
            String pctText = progressPercent + "%";
            g2.setColor(UITheme.TEXT_MAIN);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(pctText)) / 2;
            int ty = (h / 2) + (fm.getAscent() / 3);
            g2.drawString(pctText, tx, ty);

            // Subtitle
            g2.setColor(UITheme.TEXT_MUTED);
            g2.setFont(UITheme.FONT_SMALL_BOLD);
            FontMetrics fmSub = g2.getFontMetrics();
            int subX = (w - fmSub.stringWidth(label)) / 2;
            g2.drawString(label, subX, ty + 16);
        }
    }
}
