package GUI;

import javax.swing.*;

public class MainPanel extends JFrame {
    private SearchPanel searchPanel1;
    private TimetablePanel timetablePanel1;

    public static void main(String[] args) {
        JFrame frame = new MainPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
