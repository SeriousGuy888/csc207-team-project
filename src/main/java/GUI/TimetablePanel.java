package GUI;

import javax.swing.*;
import java.awt.*;

public class TimetablePanel extends JPanel {
    private JPanel TimetablePanel;
    private JTabbedPane tabbedPane1;
    private JTable table1;
    private JButton fallButton;
    private JButton winterButton;
    private JButton clearAllButton;
    private JButton exportButton;
    private JButton autogenerateButton;

    public TimetablePanel() {
        setSize(1200, 800);
        setVisible(true);
    }
}
