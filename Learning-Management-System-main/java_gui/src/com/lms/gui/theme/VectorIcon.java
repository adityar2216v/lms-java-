package com.lms.gui.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * High-performance vector icon renderer for Swing.
 * Renders anti-aliased vector glyphs without external image assets or font dependencies.
 */
public class VectorIcon implements Icon {
    public enum Type {
        DASHBOARD,
        COURSES,
        LECTURES,
        ASSIGNMENTS,
        EXAMS,
        DOUBTS,
        NOTICES,
        ATTENDANCE,
        CERTIFICATE,
        USERS,
        BELL,
        SEARCH,
        LOGOUT,
        EYE,
        EYE_OFF,
        CHECK,
        PLUS,
        TRASH,
        CHART,
        REFRESH,
        SHIELD,
        BOOKMARK
    }

    private final Type type;
    private final int size;
    private final Color color;

    public VectorIcon(Type type) {
        this(type, 18, UITheme.TEXT_MUTED);
    }

    public VectorIcon(Type type, int size) {
        this(type, size, UITheme.TEXT_MUTED);
    }

    public VectorIcon(Type type, int size, Color color) {
        this.type = type;
        this.size = size;
        this.color = color;
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Color drawColor = color != null ? color : (c != null ? c.getForeground() : UITheme.TEXT_MAIN);
        g2.setColor(drawColor);

        // Normalize coordinates to 0..size
        g2.translate(x, y);
        float s = size / 24.0f; // based on 24x24 canvas
        g2.scale(s, s);

        float strokeWidth = 2.0f;
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (type) {
            case DASHBOARD:
                // 4 modern tiles
                g2.fill(new RoundRectangle2D.Float(3, 3, 8, 8, 2.5f, 2.5f));
                g2.fill(new RoundRectangle2D.Float(13, 3, 8, 8, 2.5f, 2.5f));
                g2.fill(new RoundRectangle2D.Float(3, 13, 8, 8, 2.5f, 2.5f));
                g2.fill(new RoundRectangle2D.Float(13, 13, 8, 8, 2.5f, 2.5f));
                break;

            case COURSES:
                // Open book
                Path2D bookLeft = new Path2D.Float();
                bookLeft.moveTo(12, 19);
                bookLeft.curveTo(8, 17, 4, 18, 3, 18);
                bookLeft.lineTo(3, 6);
                bookLeft.curveTo(4, 6, 8, 5, 12, 7);
                bookLeft.closePath();
                g2.draw(bookLeft);

                Path2D bookRight = new Path2D.Float();
                bookRight.moveTo(12, 19);
                bookRight.curveTo(16, 17, 20, 18, 21, 18);
                bookRight.lineTo(21, 6);
                bookRight.curveTo(20, 6, 16, 5, 12, 7);
                bookRight.closePath();
                g2.draw(bookRight);
                g2.draw(new Line2D.Float(12, 7, 12, 19));
                break;

            case LECTURES:
                // Video monitor with play arrow
                g2.draw(new RoundRectangle2D.Float(2, 4, 20, 14, 3, 3));
                Path2D play = new Path2D.Float();
                play.moveTo(10, 8);
                play.lineTo(16, 11);
                play.lineTo(10, 14);
                play.closePath();
                g2.fill(play);
                g2.draw(new Line2D.Float(8, 21, 16, 21));
                g2.draw(new Line2D.Float(12, 18, 12, 21));
                break;

            case ASSIGNMENTS:
                // Clipboard with lines
                g2.draw(new RoundRectangle2D.Float(4, 5, 16, 16, 3, 3));
                g2.draw(new RoundRectangle2D.Float(8, 2, 8, 4, 2, 2));
                g2.draw(new Line2D.Float(8, 10, 16, 10));
                g2.draw(new Line2D.Float(8, 14, 16, 14));
                g2.draw(new Line2D.Float(8, 18, 13, 18));
                break;

            case EXAMS:
                // Trophy
                Path2D cup = new Path2D.Float();
                cup.moveTo(6, 4);
                cup.lineTo(18, 4);
                cup.curveTo(18, 12, 15, 14, 12, 14);
                cup.curveTo(9, 14, 6, 12, 6, 4);
                g2.draw(cup);
                // Handles
                g2.draw(new Arc2D.Float(3, 5, 5, 6, 90, 180, Arc2D.OPEN));
                g2.draw(new Arc2D.Float(16, 5, 5, 6, 270, 180, Arc2D.OPEN));
                // Base
                g2.draw(new Line2D.Float(12, 14, 12, 18));
                g2.draw(new Line2D.Float(8, 20, 16, 20));
                g2.draw(new Line2D.Float(9, 18, 15, 18));
                break;

            case DOUBTS:
                // Chat bubbles
                g2.draw(new RoundRectangle2D.Float(3, 4, 13, 10, 4, 4));
                Path2D bubbleTail = new Path2D.Float();
                bubbleTail.moveTo(6, 14);
                bubbleTail.lineTo(4, 17);
                bubbleTail.lineTo(9, 14);
                g2.draw(bubbleTail);

                g2.draw(new RoundRectangle2D.Float(10, 9, 11, 8, 3, 3));
                break;

            case NOTICES:
                // Megaphone
                Path2D mega = new Path2D.Float();
                mega.moveTo(4, 10);
                mega.lineTo(12, 6);
                mega.lineTo(12, 17);
                mega.lineTo(4, 13);
                mega.closePath();
                g2.draw(mega);
                g2.draw(new Arc2D.Float(12, 7, 6, 9, -60, 120, Arc2D.OPEN));
                g2.draw(new Line2D.Float(6, 13, 6, 19));
                g2.draw(new Line2D.Float(6, 19, 8, 19));
                break;

            case ATTENDANCE:
                // Calendar with checkmark
                g2.draw(new RoundRectangle2D.Float(3, 5, 18, 16, 3, 3));
                g2.draw(new Line2D.Float(3, 9, 21, 9));
                g2.draw(new Line2D.Float(7, 2, 7, 5));
                g2.draw(new Line2D.Float(17, 2, 17, 5));
                // Checkmark inside calendar
                Path2D chk = new Path2D.Float();
                chk.moveTo(8, 15);
                chk.lineTo(11, 18);
                chk.lineTo(16, 12);
                g2.draw(chk);
                break;

            case CERTIFICATE:
                // Diploma scroll / award ribbon
                g2.draw(new RoundRectangle2D.Float(3, 3, 18, 14, 2, 2));
                g2.draw(new Line2D.Float(7, 7, 17, 7));
                g2.draw(new Line2D.Float(7, 10, 14, 10));
                // Star or medal at bottom
                g2.fill(new Ellipse2D.Float(14, 13, 6, 6));
                Path2D rib = new Path2D.Float();
                rib.moveTo(15, 18);
                rib.lineTo(14, 22);
                rib.lineTo(17, 20);
                rib.lineTo(20, 22);
                rib.lineTo(19, 18);
                g2.fill(rib);
                break;

            case USERS:
                // User silhouette
                g2.draw(new Ellipse2D.Float(8, 4, 8, 8));
                Path2D body = new Path2D.Float();
                body.moveTo(4, 20);
                body.curveTo(4, 15, 8, 14, 12, 14);
                body.curveTo(16, 14, 20, 15, 20, 20);
                g2.draw(body);
                break;

            case BELL:
                // Bell
                Path2D bell = new Path2D.Float();
                bell.moveTo(12, 3);
                bell.curveTo(8, 3, 6, 7, 6, 12);
                bell.lineTo(4, 16);
                bell.lineTo(20, 16);
                bell.lineTo(18, 12);
                bell.curveTo(18, 7, 16, 3, 12, 3);
                g2.draw(bell);
                g2.draw(new Arc2D.Float(10, 16, 4, 4, 180, 180, Arc2D.OPEN));
                break;

            case SEARCH:
                // Magnifying glass
                g2.draw(new Ellipse2D.Float(4, 4, 11, 11));
                g2.draw(new Line2D.Float(13, 13, 20, 20));
                break;

            case LOGOUT:
                // Door + arrow
                g2.draw(new Line2D.Float(10, 4, 4, 4));
                g2.draw(new Line2D.Float(4, 4, 4, 20));
                g2.draw(new Line2D.Float(4, 20, 10, 20));
                // Arrow pointing right
                g2.draw(new Line2D.Float(9, 12, 20, 12));
                Path2D arr = new Path2D.Float();
                arr.moveTo(16, 8);
                arr.lineTo(20, 12);
                arr.lineTo(16, 16);
                g2.draw(arr);
                break;

            case EYE:
                // Open eye
                Path2D eye = new Path2D.Float();
                eye.moveTo(2, 12);
                eye.curveTo(6, 6, 18, 6, 22, 12);
                eye.curveTo(18, 18, 6, 18, 2, 12);
                g2.draw(eye);
                g2.fill(new Ellipse2D.Float(10, 10, 4, 4));
                break;

            case EYE_OFF:
                // Eye with slash
                Path2D eye2 = new Path2D.Float();
                eye2.moveTo(2, 12);
                eye2.curveTo(6, 6, 18, 6, 22, 12);
                eye2.curveTo(18, 18, 6, 18, 2, 12);
                g2.draw(eye2);
                g2.draw(new Line2D.Float(4, 4, 20, 20));
                break;

            case CHECK:
                Path2D chk2 = new Path2D.Float();
                chk2.moveTo(4, 12);
                chk2.lineTo(9, 17);
                chk2.lineTo(20, 6);
                g2.draw(chk2);
                break;

            case PLUS:
                g2.draw(new Line2D.Float(12, 5, 12, 19));
                g2.draw(new Line2D.Float(5, 12, 19, 12));
                break;

            case TRASH:
                g2.draw(new Line2D.Float(4, 6, 20, 6));
                g2.draw(new RoundRectangle2D.Float(6, 6, 12, 14, 2, 2));
                g2.draw(new Line2D.Float(10, 9, 10, 16));
                g2.draw(new Line2D.Float(14, 9, 14, 16));
                g2.draw(new Line2D.Float(9, 4, 15, 4));
                break;

            case CHART:
                g2.draw(new Line2D.Float(4, 20, 20, 20));
                g2.draw(new Line2D.Float(4, 4, 4, 20));
                g2.fill(new Rectangle2D.Float(7, 12, 3, 7));
                g2.fill(new Rectangle2D.Float(12, 8, 3, 11));
                g2.fill(new Rectangle2D.Float(17, 5, 3, 14));
                break;

            case REFRESH:
                g2.draw(new Arc2D.Float(4, 4, 16, 16, 45, 270, Arc2D.OPEN));
                Path2D arrow = new Path2D.Float();
                arrow.moveTo(14, 2);
                arrow.lineTo(19, 5);
                arrow.lineTo(15, 8);
                g2.fill(arrow);
                break;

            case SHIELD:
                Path2D shield = new Path2D.Float();
                shield.moveTo(12, 3);
                shield.lineTo(20, 6);
                shield.lineTo(20, 12);
                shield.curveTo(20, 17, 16, 20, 12, 22);
                shield.curveTo(8, 20, 4, 17, 4, 12);
                shield.lineTo(4, 6);
                shield.closePath();
                g2.draw(shield);
                break;

            case BOOKMARK:
                Path2D bkmk = new Path2D.Float();
                bkmk.moveTo(6, 3);
                bkmk.lineTo(18, 3);
                bkmk.lineTo(18, 21);
                bkmk.lineTo(12, 17);
                bkmk.lineTo(6, 21);
                bkmk.closePath();
                g2.draw(bkmk);
                break;
        }

        g2.dispose();
    }
}
