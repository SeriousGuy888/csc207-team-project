package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JFrame {
    private JPanel rootPanel;
    private JPanel searchPanel1;
    private JSplitPane SplitPane;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JTabbedPane tabbedPane;
    private boolean isModifyingTab = false;    //variable to prevent stack overflow

    private static final int MAX_TABS = 8;
    private int tabCounter = 1;      // for naming new tabs

    public MainPanel() {
        setTitle("Timetable Builder");
        setContentPane(rootPanel);

        // Create tabs after UI builder initializes components
        SwingUtilities.invokeLater(() -> {
            setupInitialTabs();
            setupListeners();
            tabbedPane.setSelectedIndex(0);   // focus first tab
        });
    }

    private void setupInitialTabs() {
        addPlusTab();   // always last
        addNewTab();    // create first real tab
    }

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

    private void addNewTab() {
        TimetablePanel panel = new TimetablePanel();

        int insertIndex = tabbedPane.getTabCount() - 1; // before "+"
        String title = "Timetable " + (tabCounter++);

        tabbedPane.insertTab(title, null, panel.getRootPanel(), null, insertIndex);
        tabbedPane.setTabComponentAt(insertIndex, createTabHeader(title));
        System.out.println("Added a new tab  ");
    }

    private void addPlusTab() {
        JPanel dummy = new JPanel();
        tabbedPane.addTab("+", dummy);
    }

    private Component createTabHeader(String title) {
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tabPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title + " ");
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f));

        JButton closeButton = new JButton("×");
        closeButton.setMargin(new Insets(0,0,0,0));
        closeButton.setPreferredSize(new Dimension(16,16));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusable(false);

        // close button logic
        closeButton.addActionListener(e -> {
            int i = tabbedPane.indexOfTabComponent(tabPanel);

            // Don't allow closing the last real tab
            if (tabbedPane.getTabCount()  <= 2) {
                JOptionPane.showMessageDialog(tabbedPane,
                        "You have to have at least 1 timetable.");
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
                }
                else if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    tabbedPane.setSelectedIndex(tabbedPane.indexOfTabComponent(tabHeader));
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new MainPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,1000);
        frame.pack();
        frame.setVisible(true);
    }
}