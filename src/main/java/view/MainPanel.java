package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel {
    private JPanel MainPanel;
    private SearchPanel searchPanel;
    private JSplitPane SplitPane;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JTabbedPane tabbedPane;

    /**
     * The maximum amount of tabs tabbedPane can have.
     */
    private static final int MAX_TABS = 8;

    /**
     * Records whether the program is adding/deleting a tab.
     * This helper variable prevents unwanted behaviors when adding/deleting tabs.
     */
    private boolean isModifyingTab = false;

    /**
     * Counter for the default naming of added tabs.
     */
    private int tabCounter = 1;      // for naming new tabs

    /**
     * Creates a new MainPanel. Then create tabs and listeners after UI builder initializes components.
     */
    public MainPanel() {
        SwingUtilities.invokeLater(() -> {
            setupInitialTabs();
            setupListeners();
            tabbedPane.setSelectedIndex(0);   // focus the first tab
        });
    }

    /**
     * Returns the root Swing panel for this view.
     * This method is typically used to embed the UI designed in this class into another container.
     *
     * @return the root MainPanel containing all UI components
     */
    public JPanel getRootPanel() {
        return MainPanel;
    }

    /**
     * Creates a "+" tab and the first default tab.
     */
    private void setupInitialTabs() {
        addPlusTab();
        addNewTab();
    }

    /**
     * Configures the tabbed pane's change listener to support dynamic tab creation
     * through the final "+" tab. When the user selects the "+" tab, a new timetable
     * tab is automatically inserted immediately before it.
     * The method also enforces a maximum {@link #MAX_TABS} number of tabs.
     */
    private void setupListeners() {
        tabbedPane.addChangeListener(e -> {
            if (isModifyingTab) return;

            int last = tabbedPane.getTabCount() - 1;
            int selected = tabbedPane.getSelectedIndex();

            // "+" tab selected
            if (selected == last) {

                if (last >= MAX_TABS) {
                    JOptionPane.showMessageDialog(tabbedPane,
                            "You can only have at most " + MAX_TABS + " timetables.");
                    tabbedPane.setSelectedIndex(last - 1);
                    return;
                }

                isModifyingTab = true;
                addNewTab();
                tabbedPane.setSelectedIndex(last); // new tab is inserted at last index
                isModifyingTab = false;
            }
        });
    }

    /**
     * Creates and inserts a new timetable tab into the tabbed pane.
     * A new {@link TimetablePanel} instance is created for each tab. The tab is inserted
     * immediately before the final "+" tab, which is reserved for creating additional tabs.
     */
    private void addNewTab() {
        TimetablePanel panel = new TimetablePanel();

        int insertIndex = tabbedPane.getTabCount() - 1;
        String title = "Timetable " + (tabCounter++);

        tabbedPane.insertTab(title, null, panel.getRootPanel(), null, insertIndex);
        tabbedPane.setTabComponentAt(insertIndex, createTabHeader(title));
    }

    /**
     * Creates a dummy "+" tab that handles adding tabs.
     */
    private void addPlusTab() {
        JPanel dummy = new JPanel();
        tabbedPane.addTab("+", dummy);
    }

    /**
     * Creates a custom tab header component containing a title label and a close button.
     * The tab header is displayed in the tabbed pane for each timetable tab.
     * It shows the tab’s title and provides a close button for removing the tab.
     * The close button prevents removal of the last remaining timetable and
     * updates the selected tab appropriately after a close action.
     *
     * @param title the text to display in the tab header
     * @return a Swing component representing the tab header
     */
    private Component createTabHeader(String title) {
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tabPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title + " ");
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f));

        JButton closeButton = new JButton("×");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(16, 16));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusable(false);

        // close button logic
        closeButton.addActionListener(e -> {
            int i = tabbedPane.indexOfTabComponent(tabPanel);

            // Don't allow closing the last real tab
            if (tabbedPane.getTabCount() <= 2) {
                JOptionPane.showMessageDialog(tabbedPane,
                        "You must have at least 1 timetable.");
                return;
            }

            isModifyingTab = true;
            tabbedPane.remove(i);
            isModifyingTab = false;
            tabbedPane.setSelectedIndex(i == tabbedPane.getTabCount() - 1 ? i - 1 : i);
        });

        // rename tab on double click and select tab on single click
        addRenameOnDoubleClick(titleLabel, tabPanel);

        tabPanel.add(titleLabel);
        tabPanel.add(closeButton);

        return tabPanel;
    }

    /**
     * Adds mouse interaction behavior to a tab header, enabling tab renaming
     * on double-click and tab selection on single-click.
     * When the user double-clicks the title label, an input dialog appears
     * allowing the tab to be renamed.
     * A single left-click selects the corresponding tab in the tabbed pane.
     *
     * @param titleLabel the label component that displays the tab title
     * @param tabHeader  the tab header component used to identify the tab
     */
    private void addRenameOnDoubleClick(JLabel titleLabel, JPanel tabHeader) {
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = tabbedPane.indexOfTabComponent(tabHeader);
                    if (index == -1) return;

                    // don't rename "+"
                    if (index == tabbedPane.getTabCount() - 1) return;

                    String current = titleLabel.getText().trim();
                    String newTitle = JOptionPane.showInputDialog(
                            tabbedPane, "Enter new timetable name:", current
                    );
                    if (newTitle != null && !newTitle.trim().isEmpty()) {
                        titleLabel.setText(newTitle.trim() + " ");
                        tabbedPane.setTitleAt(index, newTitle.trim());
                    }
                } else if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    tabbedPane.setSelectedIndex(tabbedPane.indexOfTabComponent(tabHeader));
                }
            }
        });
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
        MainPanel = new JPanel();
        MainPanel.setLayout(new GridBagLayout());
        SplitPane = new JSplitPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        MainPanel.add(SplitPane, gbc);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rightPanel.setMinimumSize(new Dimension(900, 1000));
        SplitPane.setRightComponent(rightPanel);
        tabbedPane = new JTabbedPane();
        rightPanel.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.setMinimumSize(new Dimension(400, 1000));
        SplitPane.setLeftComponent(leftPanel);
        searchPanel = new SearchPanel();
        leftPanel.add(searchPanel.$$$getRootComponent$$$(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

}