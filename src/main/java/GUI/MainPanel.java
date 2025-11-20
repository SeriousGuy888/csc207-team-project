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
    private boolean isAddingTab = false;    //variable to prevent stack overflow

    private static final int MAX_TABS = 10;
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
            if (isAddingTab) return;

            int last = tabbedPane.getTabCount() - 1;
            int selected = tabbedPane.getSelectedIndex();

            // "+" tab selected
            if (selected == last) {

                if (last >= MAX_TABS) {
                    JOptionPane.showMessageDialog(tabbedPane,
                            "Maximum number of tabs reached.");
                    tabbedPane.setSelectedIndex(last - 1);
                    return;
                }

                isAddingTab = true;
                addNewTab();
                tabbedPane.setSelectedIndex(last); // new tab is inserted at last index
                isAddingTab = false;
            }
        });
    }

    private void addNewTab() {
        JPanel panel = new TimetablePanel();

        int insertIndex = tabbedPane.getTabCount() - 1; // before "+"
        String title = "Timetable " + (tabCounter);
        tabCounter++;
        System.out.println(tabCounter);

        tabbedPane.insertTab(title, null, panel, null, insertIndex);
        tabbedPane.setTabComponentAt(insertIndex, createTabHeader(title));
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

            // prevent closing "+"
            if (i == tabbedPane.getTabCount() - 1) return;

            // Don't allow closing the last real tab
            if (tabbedPane.getTabCount()  <= 2) {
                return;
            }

            tabbedPane.remove(i);
        });

        // rename on double click
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
                            tabbedPane, "Enter new tab name:", current
                    );
                    if (newTitle != null && !newTitle.trim().isEmpty()) {
                        titleLabel.setText(newTitle.trim() + " ");
                        tabbedPane.setTitleAt(index, newTitle.trim());
                    }
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