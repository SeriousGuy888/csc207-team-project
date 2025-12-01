package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import interface_adapter.GlobalViewController;
import interface_adapter.GlobalViewModel;
import interface_adapter.GlobalViewState;
import interface_adapter.TimetableState;
import interface_adapter.autogen.AutogenController;
import interface_adapter.locksections.LockSectionController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

    private AutogenController autogenController;
    private LockSectionController lockSectionController;

    /**
     * Flag to prevent infinite loops when the View updates itself.
     */
    private boolean isRebuilding;

    /**
     * Creates a new MainPanel. Then create tabs and listeners after UI builder initializes components.
     *
     * @param globalViewModel      the GlobalViewModel to observe
     * @param globalViewController the GlobalViewController to call when user interacts with the UI
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
     */
    private void setupListeners() {
        tabbedPane.addChangeListener(e -> {
            if (isRebuilding) {
                return;
            }

            final int selectedIndex = tabbedPane.getSelectedIndex();
            final int maxIndex = tabbedPane.getTabCount() - 1;

            // Case 1: User clicked the "+" tab
            if (selectedIndex == maxIndex) {
                // Reset selection to previous temporarily to avoid visual glitch
                // while waiting for backend response
                if (maxIndex > 0) {
                    tabbedPane.setSelectedIndex(selectedIndex - 1);
                }

                // CALL CONTROLLER
                globalViewController.addTab();
            }

            // Case 2: User switched to a normal tab
            else if (selectedIndex >= 0) {
                // Check against state to prevent infinite loops (View updates State -> State updates View -> View fires listener)
                if (globalViewModel.getState().getSelectedTabIndex() != selectedIndex) {
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
        final GlobalViewState state = (GlobalViewState) evt.getNewValue();
        final ArrayList<TimetableState> stateList = (ArrayList<TimetableState>) state.getTimetableStateList();

        final int currentContentTabs = tabbedPane.getTabCount() - 1;

        if (stateList.size() != currentContentTabs) {
            rebuildTabs(stateList);
        } else {
            refreshTabContent(stateList);
        }

        // Now sync selection
        final int desiredIndex = state.getSelectedTabIndex();
        if (desiredIndex < tabbedPane.getTabCount() - 1
                && tabbedPane.getSelectedIndex() != desiredIndex) {

            // Block listener so we don't trigger "switchTab" controller
            isRebuilding = true;
            tabbedPane.setSelectedIndex(desiredIndex);
            isRebuilding = false;
        }
    }

    /**
     * Creates a fake "+" tab that handles adding tabs.
     */
    private void addPlusTab() {
        final JPanel dummy = new JPanel();
        tabbedPane.addTab("+", dummy);
    }

    /**
     * Heavy Refresh: Used when a tab is Added or Deleted.
     * Clears tabs and recreates them from the state list.
     *
     * @param stateList The list of TimetableStates to rebuild from.
     */
    private void rebuildTabs(ArrayList<TimetableState> stateList) {
        isRebuilding = true;
        // Remove all tabs EXCEPT the last one (the "+")
        while (tabbedPane.getTabCount() > 1) {
            tabbedPane.remove(0);
        }

        // Re-add tabs from State
        for (int i = 0; i < stateList.size(); i++) {
            final TimetableState ts = stateList.get(i);
            final String title = ts.getTimetableName();

            final TimetablePanel panel = new TimetablePanel();
            panel.updateView(ts);

            if (autogenController != null) {
                panel.setAutogenController(autogenController);
            }

            // Note: We assume TimetablePanel extends JPanel and adds its content to itself.
            // If TimetablePanel relies on .getRootPanel(), use: panel.getRootPanel()
            // However, we need to cast it back to TimetablePanel later, so extending JPanel is best.
            tabbedPane.insertTab(title, null, panel.getRootPanel(), null, i);
            tabbedPane.setTabComponentAt(i, createTabHeader(title, i));
        }

        isRebuilding = false;
    }

    /**
     * Light Refresh: Used when a course is added, or a tab is renamed.
     * Updates content without destroying the tab components.
     *
     * @param stateList The list of TimetableStates to refresh from.
     */
    private void refreshTabContent(ArrayList<TimetableState> stateList) {
        for (int i = 0; i < stateList.size(); i++) {
            final TimetableState state = stateList.get(i);
            final String newName = state.getTimetableName();

            // 1. Update Grid Content
            final Component comp = tabbedPane.getComponentAt(i);
            if (comp instanceof TimetablePanel) {
                ((TimetablePanel) comp).updateView(state);
            }

            // 2. Update Title (if changed via Rename)
            // We check the custom tab component's label
            updateTabHeaderTitle(i, newName);
        }
    }

    private void updateTabHeaderTitle(int index, String newName) {
        final Component tabComp = tabbedPane.getTabComponentAt(index);
        if (tabComp instanceof JPanel) {
            final JPanel header = (JPanel) tabComp;
            for (Component c : header.getComponents()) {
                if (c instanceof JLabel) {
                    final JLabel titleLabel = (JLabel) c;
                    // Only update if text is actually different
                    if (!titleLabel.getText().trim().equals(newName)) {
                        titleLabel.setText(newName);
                        tabbedPane.setTitleAt(index, newName);
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

        final JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tabPanel.setOpaque(false);

        final JLabel titleLabel = new JLabel(title + " ");
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f));

        final JButton closeButton = new JButton("×");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(16, 16));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusable(false);

        // close button logic
        closeButton.addActionListener(e -> {
            final int response = JOptionPane.showConfirmDialog(
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
                    final int index = tabbedPane.indexOfTabComponent(tabHeader);
                    if (index == -1) {
                        return;
                    }

                    // don't rename "+"
                    if (index == tabbedPane.getTabCount() - 1) {
                        return;
                    }

                    final String current = titleLabel.getText().trim();
                    final String newTitle = JOptionPane.showInputDialog(
                            tabbedPane, "Enter new timetable name:", current
                    );
                    if (newTitle != null && !newTitle.trim().isEmpty()) {
                        globalViewController.renameTab(index, newTitle.trim());
                    }
                } else if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    final int index = tabbedPane.indexOfTabComponent(tabHeader);
                    globalViewController.switchTab(index);
                }
            }
        });
    }

    public void setAutogenController(AutogenController autogenController) {
        this.autogenController = autogenController;

        for (int i = 0; i < tabbedPane.getTabCount() - 1; i++) { // skip the "+" tab
            Component comp = tabbedPane.getComponentAt(i);
            if (comp instanceof TimetablePanel) {
                ((TimetablePanel) comp).setAutogenController(autogenController);
            }
        }
    }

    public void setLockSectionController(LockSectionController controller) {
        this.lockSectionController = controller;
    }



    public SearchPanel getSearchPanel() {
        return searchPanel;
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
        leftPanel.add(searchPanel.$$$getRootComponent$$$(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

}
