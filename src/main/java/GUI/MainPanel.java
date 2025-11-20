package GUI;

import javax.swing.*;

public class MainPanel extends JFrame{
    private JPanel rootPanel;
    private SearchPanel searchPanel1;
    private TimetablePanel timetablePanel1;
    private JSplitPane SplitPane;
    private JPanel rightPanel;
    private JPanel leftPanel;

    public MainPanel() {
        setTitle("Timetable Builder");
        setContentPane(rootPanel);
    }

    public static void main(String[] args) {
        JFrame frame = new MainPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,1000);
        frame.pack();
        frame.setVisible(true);
    }
}
