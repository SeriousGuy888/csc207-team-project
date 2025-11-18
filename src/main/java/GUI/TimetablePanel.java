package GUI;

import javax.swing.*;

public class TimetablePanel extends JPanel {
    private JPanel TimetablePanel;
    private JTabbedPane tabbedPane1;
    private JTable table1;
    private JButton fallButton;
    private JButton winterButton;
    private JButton clearAllButton;
    private JButton exportButton;
    private JButton autogenerateButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Timetable Panel");
        TimetablePanel ttpanel = new TimetablePanel();
        frame.setContentPane(ttpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
