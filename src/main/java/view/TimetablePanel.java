package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import interface_adapter.TimetableState;
import interface_adapter.TimetableState.MeetingBlock;
import interface_adapter.autogen.AutogenController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimetablePanel extends JPanel {
    public static final int NUM_ROWS = 24;
    public static final int NUM_COLS = 5;
    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};
    private static final String[] TIMES = {
            "9:00", "9:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30",
            "6:00", "6:30", "7:00", "7:30", "8:00", "8:30"
    };

    private JPanel TimetablePanel;
    private JButton fallButton;
    private JButton winterButton;
    private JButton clearAllButton;
    private JButton exportButton;
    private JButton autogenerateButton;
    private JPanel buttonPanel;
    private JButton saveButton;
    private JButton loadButton;

    private JScrollPane scrollPane;
    private JPanel firstSemesterGridContainer;
    private JPanel secondSemesterGridContainer;
    private JPanel[][] firstSemesterPanel = new JPanel[NUM_ROWS][NUM_COLS];
    private JPanel[][] secondSemesterPanel = new JPanel[NUM_ROWS][NUM_COLS];
    private JPanel rightSidePanel;
    private JTable lockedSectionsTable;

    private AutogenController autogenController;

    /**
     * Creates a new TimetablePanel and initializes the term-selection controls.
     */
    public TimetablePanel() {
        $$$setupUI$$$();

        initializeGrid();

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
                scrollPane.setViewportView(firstSemesterGridContainer);
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
                scrollPane.setViewportView(secondSemesterGridContainer);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveDialog.getSingletonInstance().display(TimetablePanel);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadDialog.getSingletonInstance().display(TimetablePanel);
            }
        });
        setupRightSidePanel();


        this.setLayout(new BorderLayout());
        this.add(TimetablePanel, BorderLayout.CENTER);
    }

    public void setAutogenController(AutogenController controller) {
        this.autogenController = controller;
        autogenerateButton.addActionListener(e -> {
            if (autogenController != null) {
                autogenController.autogenerate();
            }
        });
    }
    private void initializeGrid() {
        // 1. Initialize the Container Panels with GridLayout
        // 24 rows, 5 columns
        firstSemesterGridContainer = new JPanel(new GridLayout(NUM_ROWS, NUM_COLS));
        secondSemesterGridContainer = new JPanel(new GridLayout(NUM_ROWS, NUM_COLS));

        // 2. Populate the Grids
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                // --- FIRST SEMESTER SLOT ---
                final JPanel slot1 = new JPanel(new BorderLayout());
                slot1.setPreferredSize(new Dimension(80, 50));
                slot1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                slot1.setBackground(Color.WHITE);

                // Add to array (for logic access) AND container (for display)
                firstSemesterPanel[row][col] = slot1;
                firstSemesterGridContainer.add(slot1);

                // --- SECOND SEMESTER SLOT ---
                // We MUST create a new object. Cannot reuse slot1.
                final JPanel slot2 = new JPanel(new BorderLayout());
                slot2.setPreferredSize(new Dimension(80, 50));
                slot2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                slot2.setBackground(Color.WHITE);

                secondSemesterPanel[row][col] = slot2;
                secondSemesterGridContainer.add(slot2);
            }
        }

        // 3. Set Default View to First Semester
        scrollPane.setViewportView(firstSemesterGridContainer);

        // 4. Set Scroll Speed (Optional, makes scrolling smoother)
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * Paints the timetable UI based on the current state.
     *
     * @param state The current TimetableState to render.
     */
    public void updateView(TimetableState state) {
        if (state == null) {
            return;
        }

        final MeetingBlock[][][] firstSemesterGridData = state.getFirstSemesterGrid();
        final MeetingBlock[][][] secondSemesterGridData = state.getSecondSemesterGrid();

        // Iterate over every cell in the UI Grid
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                updateSlot(firstSemesterPanel[row][col], firstSemesterGridData[row][col], row);
                updateSlot(secondSemesterPanel[row][col], secondSemesterGridData[row][col], row);
            }
        }
    }

    /**
     * Updates a single UI slot based on the pre-aligned data.
     *
     * @param slotPanel  The JPanel to update
     * @param blocks     The MeetingBlocks to render in this slot
     * @param currentRow The current row index (0-23) used to check text visibility.
     */
    private void updateSlot(JPanel slotPanel, MeetingBlock[] blocks, int currentRow) {
        slotPanel.removeAll();

        // CASE 1: Completely Empty
        if (blocks[0] == null && blocks[1] == null) {
            slotPanel.setLayout(new BorderLayout());
            slotPanel.setBackground(Color.WHITE);
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            slotPanel.revalidate();
            slotPanel.repaint();
            return;
        }

        // Check for Conflict Flag (Logical or Physical)
        boolean isConflict = (blocks[0] != null && blocks[0].isConflict()) ||
                (blocks[1] != null && blocks[1].isConflict());

        // CASE 2: No Conflict -> Full Width
        // Only if we have exactly 1 block at index 0 and it's not flagged as conflict
        if (!isConflict && blocks[0] != null && blocks[1] == null) {
            slotPanel.setLayout(new GridLayout(1, 1));
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 0));
            slotPanel.add(createBlockPanel(blocks[0], currentRow));
        }

        // CASE 3: Conflict / Forced Alignment -> Split View (1x2)
        else {
            slotPanel.setLayout(new GridLayout(1, 2));
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 0));

            // Left Half (Index 0)
            if (blocks[0] != null) {
                slotPanel.add(createBlockPanel(blocks[0], currentRow));
            } else {
                slotPanel.add(createEmptyHalf());
            }

            // Right Half (Index 1)
            if (blocks[1] != null) {
                slotPanel.add(createBlockPanel(blocks[1], currentRow));
            } else {
                slotPanel.add(createEmptyHalf());
            }
        }

        slotPanel.revalidate();
        slotPanel.repaint();
    }

    /**
     * Creates the visual panel for a single meeting block.
     *
     * @param block      The MeetingBlock to render
     * @param currentRow The current row index (0-23) used to check text visibility.
     */
    private JPanel createBlockPanel(MeetingBlock block, int currentRow) {
        final JPanel panel = new JPanel(new BorderLayout());

        // Color Logic
        if (block.isConflict()) {
            panel.setBackground(new Color(255, 102, 102));
        } else {
            panel.setBackground(new Color(173, 216, 230));
        }

        // Border to distinguish blocks
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Text Logic: Only show text if this is the Start Row
        if (currentRow == block.getStartRow()) {
            // Use HTML to allow multi-line text
            final JLabel label = new JLabel("<html>" + block.getDisplayText() + "</html>");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.TOP);
            label.setFont(new Font("SansSerif", Font.PLAIN, 10));
            panel.add(label, BorderLayout.CENTER);
        }

        return panel;
    }
    private void setupRightSidePanel() {
        // Panel that will live to the RIGHT of the timetable grid
        rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BorderLayout());
        rightSidePanel.setBorder(BorderFactory.createTitledBorder("Locked Sections"));

        // For now: simple 3-column table: [Course, Section, Locked?]
        String[] columnNames = {"Course", "Section", "Locked"};
        Object[][] data = {};  // initially empty, we'll fill later

        lockedSectionsTable = new JTable(
                new javax.swing.table.DefaultTableModel(data, columnNames) {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        if (columnIndex == 2) {
                            return Boolean.class; // checkbox column
                        }
                        return String.class;
                    }

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        // only the "Locked" checkbox is editable
                        return column == 2;
                    }
                }
        );

        JScrollPane tableScroll = new JScrollPane(lockedSectionsTable);
        rightSidePanel.add(tableScroll, BorderLayout.CENTER);

        // 🔹 Add this panel into the IntelliJ-designed grid on the right side of the timetable
        // TimetablePanel uses GridLayoutManager(3, 10). Your scrollPane is at (2,1) span (1,6).
        // We'll put this at row=2, col=7 spanning 1 row x 3 columns.
        TimetablePanel.add(
                rightSidePanel,
                new com.intellij.uiDesigner.core.GridConstraints(
                        2, 7,       // row, col
                        1, 3,       // rowSpan, colSpan
                        com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                        com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
                        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                        com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
                                | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null, 0, false
                )
        );
    }


    /**
     * Creates a white/transparent spacer for when a slot is split but only one side has a course.
     */
    private JPanel createEmptyHalf() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        return panel;
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        TimetablePanel = new JPanel();
        TimetablePanel.setLayout(new GridLayoutManager(3, 10, new Insets(0, 0, 0, 0), -1, -1));
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
        TimetablePanel.add(label3, new GridConstraints(1, 9, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        TimetablePanel.add(saveButton, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 34), null, 0, false));
        loadButton = new JButton();
        loadButton.setText("Load");
        TimetablePanel.add(loadButton, new GridConstraints(1, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, 34), null, 0, false));
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
