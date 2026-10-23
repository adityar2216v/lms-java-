package com.lms.gui.views;

import com.lms.gui.db.DataStore;
import com.lms.gui.model.Exam;
import com.lms.gui.model.ExamResult;
import com.lms.gui.model.Question;
import com.lms.gui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamTakingDialog extends JDialog {
    private final Exam exam;
    private final int studentId;
    private final String studentName;
    private final List<Question> questions;
    private int currentIdx = 0;
    private final Map<Integer, Integer> answers = new HashMap<>(); // question index -> selected option index

    private int secondsRemaining;
    private Timer countdownTimer;

    private JLabel timerLabel;
    private JLabel progressLabel;
    private JLabel questionTextLabel;
    private JRadioButton[] optionRadios;
    private ButtonGroup optionGroup;
    private JPanel palettePanel;
    private ModernButton prevBtn;
    private ModernButton nextBtn;

    public ExamTakingDialog(Frame parent, Exam exam, int studentId, String studentName) {
        super(parent, "Exam Session: " + exam.getTitle(), true);
        this.exam = exam;
        this.studentId = studentId;
        this.studentName = studentName;
        this.questions = exam.getQuestions();
        this.secondsRemaining = Math.max(1, exam.getDurationMinutes()) * 60;

        setSize(850, 620);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(UITheme.APP_BG);
        setLayout(new BorderLayout());

        // Header Panel with Title and Timer
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(UITheme.SIDEBAR_BG);
        headerPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel titleGrp = new JPanel(new GridLayout(2, 1, 2, 2));
        titleGrp.setOpaque(false);
        JLabel titleLbl = new JLabel("📝 " + exam.getTitle());
        titleLbl.setFont(UITheme.FONT_TITLE);
        titleLbl.setForeground(Color.WHITE);

        JLabel courseLbl = new JLabel(exam.getCourseTitle() + " | Total Questions: " + questions.size() + " | Max Marks: " + exam.getTotalMarks());
        courseLbl.setFont(UITheme.FONT_BODY);
        courseLbl.setForeground(UITheme.SIDEBAR_TEXT);
        titleGrp.add(titleLbl);
        titleGrp.add(courseLbl);

        timerLabel = new JLabel("⏱ 00:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        timerLabel.setForeground(UITheme.WARNING);

        headerPanel.add(titleGrp, BorderLayout.CENTER);
        headerPanel.add(timerLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center: Question Content
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 24, 15, 24));

        RoundedPanel questionCard = new RoundedPanel(14, Color.WHITE);
        questionCard.setLayout(new BorderLayout(12, 12));
        questionCard.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Question Top Bar
        JPanel qTopBar = new JPanel(new BorderLayout());
        qTopBar.setOpaque(false);
        progressLabel = new JLabel("Question 1 of " + questions.size());
        progressLabel.setFont(UITheme.FONT_SUBTITLE);
        progressLabel.setForeground(UITheme.PRIMARY);
        qTopBar.add(progressLabel, BorderLayout.WEST);

        questionCard.add(qTopBar, BorderLayout.NORTH);

        // Question Text and Options
        JPanel qBody = new JPanel();
        qBody.setLayout(new BoxLayout(qBody, BoxLayout.Y_AXIS));
        qBody.setOpaque(false);

        questionTextLabel = new JLabel("<html><b>Loading question...</b></html>");
        questionTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        questionTextLabel.setForeground(UITheme.TEXT_MAIN);

        qBody.add(questionTextLabel);
        qBody.add(Box.createVerticalStrut(20));

        optionRadios = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            final int optIdx = i;
            optionRadios[i] = new JRadioButton("Option " + (i + 1));
            optionRadios[i].setFont(UITheme.FONT_BODY);
            optionRadios[i].setForeground(UITheme.TEXT_MAIN);
            optionRadios[i].setOpaque(false);
            optionRadios[i].setFocusPainted(false);
            optionRadios[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            optionRadios[i].addActionListener(e -> {
                answers.put(currentIdx, optIdx);
                updatePalette();
            });
            optionGroup.add(optionRadios[i]);
            qBody.add(optionRadios[i]);
            qBody.add(Box.createVerticalStrut(10));
        }

        questionCard.add(qBody, BorderLayout.CENTER);
        centerPanel.add(questionCard, BorderLayout.CENTER);

        // Right: Question Palette
        JPanel rightPaletteWrapper = new JPanel(new BorderLayout(5, 5));
        rightPaletteWrapper.setOpaque(false);
        rightPaletteWrapper.setPreferredSize(new Dimension(180, 0));

        RoundedPanel paletteCard = new RoundedPanel(14, Color.WHITE);
        paletteCard.setLayout(new BorderLayout(8, 8));
        paletteCard.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel palTitle = new JLabel("Question Palette");
        palTitle.setFont(UITheme.FONT_BODY_BOLD);
        palTitle.setForeground(UITheme.TEXT_MAIN);
        paletteCard.add(palTitle, BorderLayout.NORTH);

        palettePanel = new JPanel(new GridLayout(0, 4, 6, 6));
        palettePanel.setOpaque(false);
        initPalette();
        paletteCard.add(palettePanel, BorderLayout.CENTER);

        rightPaletteWrapper.add(paletteCard, BorderLayout.CENTER);
        centerPanel.add(rightPaletteWrapper, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Action Buttons
        JPanel bottomBar = new JPanel(new BorderLayout(15, 10));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setBorder(new EmptyBorder(14, 24, 14, 24));

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navPanel.setOpaque(false);

        prevBtn = new ModernButton("◀ Previous", ModernButton.Style.SECONDARY);
        prevBtn.addActionListener(e -> {
            if (currentIdx > 0) {
                currentIdx--;
                loadQuestion(currentIdx);
            }
        });

        nextBtn = new ModernButton("Next ▶", ModernButton.Style.SECONDARY);
        nextBtn.addActionListener(e -> {
            if (currentIdx < questions.size() - 1) {
                currentIdx++;
                loadQuestion(currentIdx);
            }
        });

        navPanel.add(prevBtn);
        navPanel.add(nextBtn);

        ModernButton submitBtn = new ModernButton("🏁 Finish & Submit Exam", ModernButton.Style.SUCCESS);
        submitBtn.addActionListener(e -> confirmAndSubmit());

        bottomBar.add(navPanel, BorderLayout.WEST);
        bottomBar.add(submitBtn, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);

        // Load first question & start timer
        if (!questions.isEmpty()) {
            loadQuestion(0);
        } else {
            questionTextLabel.setText("No questions found in this exam.");
        }

        startTimer();
    }

    private void initPalette() {
        palettePanel.removeAll();
        for (int i = 0; i < questions.size(); i++) {
            final int idx = i;
            JButton btn = new JButton(String.valueOf(i + 1));
            btn.setFont(UITheme.FONT_SMALL_BOLD);
            btn.setFocusPainted(false);
            btn.setMargin(new Insets(4, 4, 4, 4));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                currentIdx = idx;
                loadQuestion(currentIdx);
            });
            palettePanel.add(btn);
        }
        palettePanel.revalidate();
        palettePanel.repaint();
    }

    private void updatePalette() {
        for (int i = 0; i < palettePanel.getComponentCount(); i++) {
            Component c = palettePanel.getComponent(i);
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                if (answers.containsKey(i)) {
                    b.setBackground(UITheme.SUCCESS_LIGHT);
                    b.setForeground(UITheme.SUCCESS_DARK);
                } else {
                    b.setBackground(new Color(241, 245, 249));
                    b.setForeground(UITheme.TEXT_MAIN);
                }
                if (i == currentIdx) {
                    b.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY, 2));
                } else {
                    b.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
                }
            }
        }
    }

    private void loadQuestion(int idx) {
        if (idx < 0 || idx >= questions.size()) return;
        Question q = questions.get(idx);
        progressLabel.setText("Question " + (idx + 1) + " of " + questions.size() + " (" + q.getMarks() + " Marks)");
        questionTextLabel.setText("<html><body style='width: 440px;'>" + q.getQuestionText() + "</body></html>");

        optionGroup.clearSelection();
        List<String> opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            if (i < opts.size()) {
                optionRadios[i].setText("  " + (char)('A' + i) + ") " + opts.get(i));
                optionRadios[i].setVisible(true);
            } else {
                optionRadios[i].setVisible(false);
            }
        }

        if (answers.containsKey(idx)) {
            int sel = answers.get(idx);
            if (sel >= 0 && sel < 4) {
                optionRadios[sel].setSelected(true);
            }
        }

        prevBtn.setEnabled(idx > 0);
        nextBtn.setEnabled(idx < questions.size() - 1);
        updatePalette();
    }

    private void startTimer() {
        updateTimerLabel();
        countdownTimer = new Timer(1000, e -> {
            secondsRemaining--;
            updateTimerLabel();
            if (secondsRemaining <= 0) {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(this, "⏰ Time is up! Submitting your answers now.", "Time Limit Reached", JOptionPane.WARNING_MESSAGE);
                calculateAndFinish();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerLabel() {
        int mins = secondsRemaining / 60;
        int secs = secondsRemaining % 60;
        timerLabel.setText(String.format("⏱ %02d:%02d", mins, secs));
        if (secondsRemaining < 300) { // < 5 mins
            timerLabel.setForeground(UITheme.DANGER);
        }
    }

    private void confirmAndSubmit() {
        int unanswered = questions.size() - answers.size();
        String msg = "Are you sure you want to finish and submit?";
        if (unanswered > 0) {
            msg += "\n⚠️ You have " + unanswered + " unanswered question(s).";
        }
        int confirm = JOptionPane.showConfirmDialog(this, msg, "Submit Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (countdownTimer != null) countdownTimer.stop();
            calculateAndFinish();
        }
    }

    private void calculateAndFinish() {
        int score = 0;
        int totalMarks = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            totalMarks += q.getMarks();
            Integer selectedOpt = answers.get(i);
            if (selectedOpt != null && selectedOpt == q.getCorrectOptionIndex()) {
                score += q.getMarks();
            }
        }

        if (totalMarks == 0) totalMarks = exam.getTotalMarks();

        ExamResult result = DataStore.getInstance().saveExamResult(
                exam.getId(), exam.getTitle(), exam.getCourseId(), exam.getCourseTitle(),
                studentId, studentName, score, totalMarks
        );

        String statusMsg = result.getStatus().equals("PASSED") ? "🎉 CONGRATULATIONS! YOU PASSED!" : "⚠️ KEEP STUDYING! YOU FAILED.";
        String breakdown = String.format(
                "<html><div style='text-align: center; font-family: Segoe UI;'>"
                        + "<h2>%s</h2>"
                        + "<p style='font-size: 14px;'>Exam: <b>%s</b></p>"
                        + "<p style='font-size: 18px;'>Your Score: <b>%d / %d</b> (%.1f%%)</p>"
                        + "<p style='font-size: 13px; color: #64748b;'>Result recorded in academic transcript.</p>"
                        + "</div></html>",
                statusMsg, exam.getTitle(), score, totalMarks, result.getPercentage()
        );

        JOptionPane.showMessageDialog(this, breakdown, "Exam Result Summary", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
