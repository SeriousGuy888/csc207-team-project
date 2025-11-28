package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import interface_adapter.TimetableState;
import interface_adapter.TimetableState.MeetingBlock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TimetablePanel extends JPanel {
    private JPanel TimetablePanel;
    private JButton fallButton;
    private JButton winterButton;
    private JButton clearAllButton;
    private JButton exportButton;
    private JButton autogenerateButton;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private JPanel[][] firstSemesterPanel = new JPanel[24][5];
    private JPanel[][] secondSemesterPanel = new JPanel[24][5];

    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};
    private static final String[] TIMES = {
            "9:00", "9:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30",
            "6:00", "6:30", "7:00", "7:30", "8:00", "8:30"};

    private boolean isFirst = true;

    /**
     * Creates a new TimetablePanel and initializes the term-selection controls.
     */
    public TimetablePanel() {

        // fall/winter toggle buttons
        fallButton.setEnabled(false);
        winterButton.setEnabled(true);

        fallButton.addActionListener(new ActionListener() {
            /**
             * Handles clicks on the Fall button.
             * @param e the click event that triggered the action
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                fallButton.setEnabled(false);
                winterButton.setEnabled(true);
            }
        });

        winterButton.addActionListener(new ActionListener() {
            /**
             * Handles clicks on the Winter button.
             * @param e the click event that triggered the action
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                fallButton.setEnabled(true);
                winterButton.setEnabled(false);
            }
        });
    }

    // This is the ONLY entry point for data. Adheres to Clean Architecture.
    public void updateView(TimetableState state) {
        if (state == null) return;

        ArrayList<MeetingBlock>[][] firstSemesterGridData = (ArrayList<MeetingBlock>[][]) state.getFirstSemesterGrid();
        ArrayList<MeetingBlock>[][] secondSemesterGridData = (ArrayList<MeetingBlock>[][]) state.getSecondSemesterGrid();

        // Iterate over every cell in the UI Grid
        for (int row = 0; row < 24; row++) {
            for (int col = 0; col < 5; col++) {
                updateSlot(firstSemesterPanel[row][col], firstSemesterGridData[row][col]);
                updateSlot(secondSemesterPanel[row][col], secondSemesterGridData[row][col]);
            }
        }
    }

    private void updateSlot(JPanel slotPanel, ArrayList<MeetingBlock> blocks) {
        slotPanel.removeAll(); // Clear old drawings

        // CASE 1: Empty Slot
        if (blocks.isEmpty()) {
            slotPanel.setBackground(Color.WHITE);
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            slotPanel.setLayout(new BorderLayout());
        }
        // CASE 2: Single or Multiple Courses (Standard or Conflict)
        else {
            // LOGIC: If size > 1, use GridLayout to split. If size == 1, use BorderLayout.
            // Actually, GridLayout(1, size) handles size=1 perfectly too!
            slotPanel.setLayout(new GridLayout(1, blocks.size()));

            // If ANY block is a conflict, outline the whole cell in Red
            boolean hasConflict = blocks.stream().anyMatch(MeetingBlock::isConflict);
            Color borderColor = hasConflict ? Color.RED : Color.LIGHT_GRAY;
            int borderWidth = hasConflict ? 2 : 1;
            slotPanel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));

            for (MeetingBlock block : blocks) {
                JPanel blockPanel = new JPanel(new BorderLayout());

                // Color logic
                if (block.isConflict()) {
                    blockPanel.setBackground(new Color(255, 200, 200)); // Light Red
                } else {
                    blockPanel.setBackground(new Color(173, 216, 230)); // Light Blue
                }

                // Add border to separate split blocks
                blockPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

                JLabel label = new JLabel(block.getDisplayText(), SwingConstants.CENTER);
                label.setFont(new Font("SansSerif", Font.PLAIN, 10));
                blockPanel.add(label, BorderLayout.CENTER);

                slotPanel.add(blockPanel);
            }
        }

        slotPanel.revalidate();
        slotPanel.repaint();
    }

    private void initializeGrid() {
        // (Keep your existing initialization logic, just refined for array access)
        JPanel gridPanel = new JPanel(new GridLayout(24, 5));

        // ... (Add your Headers here like in your snippet) ...

        for (int row = 0; row < 24; row++) {
            for (int col = 0; col < 5; col++) {
                JPanel slot = new JPanel();
                slot.setPreferredSize(new Dimension(100, 25)); // Set default size
                firstSemesterPanel[row][col] = slot;
                secondSemesterPanel[row][col] = slot;
                gridPanel.add(slot);
            }
        }
    }

    /**
     * Returns the root Swing panel for this view.
     * This method is typically used to embed the UI designed in this class into another container.
     *
     * @return the root TimetablePanel containing all UI components
     */
    public JPanel getRootPanel() {
        return TimetablePanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        TimetablePanel = new JPanel();
        TimetablePanel.setLayout(new GridLayoutManager(3, 8, new Insets(0, 0, 0, 0), -1, -1));
        TimetablePanel.setMinimumSize(new Dimension(36, 25));
        TimetablePanel.setPreferredSize(new Dimension(36, 25));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        TimetablePanel.add(buttonPanel, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fallButton = new JButton();
        fallButton.setText("Fall");
        buttonPanel.add(fallButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 34), null, 0, false));
        winterButton = new JButton();
        winterButton.setText("Winter");
        buttonPanel.add(winterButton, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 34), null, 0, false));
        clearAllButton = new JButton();
        clearAllButton.setText("Clear All");
        TimetablePanel.add(clearAllButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 34), null, 0, false));
        final Spacer spacer1 = new Spacer();
        TimetablePanel.add(spacer1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        exportButton = new JButton();
        exportButton.setText("Export");
        TimetablePanel.add(exportButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 34), null, 0, false));
        autogenerateButton = new JButton();
        autogenerateButton.setText("Autogenerate");
        TimetablePanel.add(autogenerateButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(110, 34), null, 0, false));
        final Spacer spacer2 = new Spacer();
        TimetablePanel.add(spacer2, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("");
        TimetablePanel.add(label1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("");
        TimetablePanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("");
        TimetablePanel.add(label3, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        TimetablePanel.add(scrollPane, new GridConstraints(2, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return TimetablePanel;
    }

}
