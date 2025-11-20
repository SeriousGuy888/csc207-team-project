package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // fall/winter toggle buttons
        fallButton.setEnabled(true);
        winterButton.setEnabled(false);
        fallButton.addActionListener(new ActionListener() {
            /**
             * @param e click event
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                fallButton.setEnabled(false);
                winterButton.setEnabled(true);
            }
        });
        winterButton.addActionListener(new ActionListener() {
            /**
             * @param e click event
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                fallButton.setEnabled(true);
                winterButton.setEnabled(false);
            }
        });
    }
}
