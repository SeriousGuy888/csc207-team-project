package GUI;

import javax.swing.*;

public class MainPanel extends JFrame{
    private JPanel rootPanel;
    private SearchPanel searchPanel1;
    private JSplitPane SplitPane;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel addPanel;
    private JTabbedPane tabbedPane;
    private int tabCount = 1;
    private boolean isAddingTab = false;    //variable to prevent stack overflow

    public MainPanel() {
        setTitle("Timetable Builder");
        setContentPane(rootPanel);
        addNewTab();    // add a default tab
        tabbedPane.setSelectedIndex(0);    //focus on the default tab

        tabbedPane.addChangeListener(e -> {
            if (isAddingTab) return;

            int lastIndex = tabbedPane.getTabCount() - 1;
            if (tabbedPane.getSelectedIndex() == lastIndex) {    // If the "+" tab is selected
                isAddingTab = true;
                addNewTab();
                tabbedPane.setSelectedIndex(lastIndex);
                isAddingTab = false;
            }
        });
    }

    private void addNewTab() {
        JPanel timetablePanel = new TimetablePanel();
        tabbedPane.insertTab("Timetable " + tabCount, null, timetablePanel, null, tabbedPane.getTabCount() - 1);
        tabCount++;
    }

    public static void main(String[] args) {
        JFrame frame = new MainPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,1000);
        frame.pack();
        frame.setVisible(true);
    }
}
