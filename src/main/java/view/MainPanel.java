package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import interface_adapter.GlobalViewController;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewState;
import interface_adapter.TimetableState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Time;
import java.util.ArrayList;

public class MainPanel extends JPanel implements PropertyChangeListener {
    private JPanel MainPanel;
    private SearchPanel searchPanel;
    private JSplitPane SplitPane;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JTabbedPane tabbedPane;

    private final GlobalViewModel globalViewModel;
    private final GlobalViewController globalViewController;

    /**
     * The maximum amount of tabs tabbedPane can have.
     */
    private static final int MAX_TABS = 8;

    /**
     * Counter for the default naming of added tabs.
     */
    private int tabCounter = 1;      // for naming new tabs

    /**
     * Creates a new MainPanel. Then create tabs and listeners after UI builder initializes components.
     */
    public MainPanel(GlobalViewModel globalViewModel, GlobalViewController globalViewController) {
        this.globalViewModel = globalViewModel;
        this.globalViewModel.addPropertyChangeListener(GlobalViewModel.TIMETABLE_CHANGED, this);
        this.globalViewController = globalViewController;

        $$$setupUI$$$();
        SwingUtilities.invokeLater(() -> {
            setupInitialUI();
            setupListeners();
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
    private void setupInitialUI() {
        addPlusTab();
    }

    /**
     * Configures the tabbed pane's change listener to support dynamic tab creation
     * through the final "+" tab. When the user selects the "+" tab, a new timetable
     * tab is automatically inserted immediately before it.
     * The method also enforces a maximum {@link #MAX_TABS} number of tabs.
     */
    private void setupListeners() {
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            int maxIndex = tabbedPane.getTabCount() - 1; // The last index is the "+" button

            // Case 1: User clicked the "+" tab
            if (selectedIndex == maxIndex) {
                // Reset selection to previous temporarily to avoid visual glitch
                // while waiting for backend response
                if (maxIndex > 0) tabbedPane.setSelectedIndex(selectedIndex - 1);

                // CALL CONTROLLER
                System.out.println("add tab called controller");
                globalViewController.addTab();
            }

            // Case 2: User switched to a normal tab
            else if (selectedIndex >= 0) {
                // Check against state to prevent infinite loops (View updates State -> State updates View -> View fires listener)
                if (globalViewModel.getState().getSelectedTabIndex() != selectedIndex) {
                    System.out.println("switched to tab " + selectedIndex);
                    globalViewController.switchTab(selectedIndex);
                }
            }
        });
    }

    /**
     * The View rebuilds or updates itself based solely on the State.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GlobalViewState state = (GlobalViewState) evt.getNewValue();
        ArrayList<TimetableState> stateList = (ArrayList<TimetableState>) state.getTimetableStateList();

        // 1. SYNC TABS (Structure)
        // We compare the number of content tabs (total tabs - 1 for the plus tab)
        int currentContentTabs = tabbedPane.getTabCount() - 1;

        // If the counts don't match, the structure changed (Add/Delete occurred)
        if (stateList.size() != currentContentTabs) {
            rebuildTabs(stateList); // Heavy refresh
        } else {
            refreshTabContent(stateList); // Light refresh (just updates grids)
        }

        // 2. SYNC SELECTION
        // Update selection only if strictly necessary
        int desiredIndex = state.getSelectedTabIndex();
        if (desiredIndex < tabbedPane.getTabCount() - 1
                && tabbedPane.getSelectedIndex() != desiredIndex) {
            tabbedPane.setSelectedIndex(desiredIndex);
        }
    }

    /**
     * Creates a dummy "+" tab that handles adding tabs.
     */
    private void addPlusTab() {
        JPanel dummy = new JPanel();
        tabbedPane.addTab("+", dummy);
    }

    /**
     * Heavy Refresh: Used when a tab is Added or Deleted.
     * Clears tabs and recreates them from the state list.
     */
    private void rebuildTabs(ArrayList<TimetableState> stateList) {
        // Remove all tabs EXCEPT the last one (the "+")
        while (tabbedPane.getTabCount() > 1) {
            tabbedPane.remove(0);
        }

        // Re-add tabs from State
        for (int i = 0; i < stateList.size(); i++) {
            TimetableState ts = stateList.get(i);
            String title = ts.getTimetableName();

            TimetablePanel panel = new TimetablePanel();
            panel.updateView(ts);

            // Note: We assume TimetablePanel extends JPanel and adds its content to itself.
            // If TimetablePanel relies on .getRootPanel(), use: panel.getRootPanel()
            // However, we need to cast it back to TimetablePanel later, so extending JPanel is best.
            tabbedPane.insertTab(title, null, panel.getRootPanel(), null, i);
            tabbedPane.setTabComponentAt(i, createTabHeader(title, i));
        }
    }

    /**
     * Light Refresh: Used when a course is added, or a tab is renamed.
     * Updates content without destroying the tab components.
     */
    private void refreshTabContent(ArrayList<TimetableState> stateList) {
        for (int i = 0; i < stateList.size(); i++) {
            TimetableState state = stateList.get(i);
            String newName = state.getTimetableName();

            // 1. Update Grid Content
            Component comp = tabbedPane.getComponentAt(i);
            if (comp instanceof TimetablePanel) {
                ((TimetablePanel) comp).updateView(state);
            }

            // 2. Update Title (if changed via Rename)
            // We check the custom tab component's label
            updateTabHeaderTitle(i, newName);
        }
    }

    private void updateTabHeaderTitle(int index, String newName) {
        Component tabComp = tabbedPane.getTabComponentAt(index);
        if (tabComp instanceof JPanel) {
            JPanel header = (JPanel) tabComp;
            for (Component c : header.getComponents()) {
                if (c instanceof JLabel) {
                    JLabel titleLabel = (JLabel) c;
                    // Only update if text is actually different
                    if (!titleLabel.getText().trim().equals(newName)) {
                        titleLabel.setText(newName);
                        tabbedPane.setTitleAt(index, newName); // Update internal title too
                    }
                    break;
                }
            }
        }
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
    private Component createTabHeader(String title, int index) {
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
            int response = JOptionPane.showConfirmDialog(
                    tabbedPane,
                    "Are you sure you want to delete this timetable?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                globalViewController.deleteTab(index);
            }
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
                        globalViewController.renameTab(index, newTitle.trim());
                    }
                } else if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = tabbedPane.indexOfTabComponent(tabHeader);
                    globalViewController.switchTab(index);
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