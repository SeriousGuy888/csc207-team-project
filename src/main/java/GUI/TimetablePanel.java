package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimetablePanel extends JPanel {
    private JPanel TimetablePanel;
    private JTable table1;
    private JButton fallButton;
    private JButton winterButton;
    private JButton clearAllButton;
    private JButton exportButton;
    private JButton autogenerateButton;
    private JPanel buttonPanel;

    public TimetablePanel() {
        setVisible(true);
        this.TimetablePanel.setVisible(true);

        // fall/winter toggle buttons
        fallButton.setEnabled(false);
        winterButton.setEnabled(true);
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

    public JPanel getRootPanel() {
        return TimetablePanel;
    }
}
