package view;

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

    /**
     * Creates a new TimetablePanel and initializes the term-selection controls.
     */
    public TimetablePanel() {

        // fall/winter toggle buttons
        fallButton.setEnabled(false);
        winterButton.setEnabled(true);

        fallButton.addActionListener(new ActionListener() {
            /**
             * Handles clicks on the Fall button.
             * @param e the click event that triggered the action
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                fallButton.setEnabled(false);
                winterButton.setEnabled(true);
            }
        });

        winterButton.addActionListener(new ActionListener() {
            /**
             * Handles clicks on the Winter button.
             * @param e the click event that triggered the action
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                fallButton.setEnabled(true);
                winterButton.setEnabled(false);
            }
        });
    }

    /**
     * Returns the root Swing panel for this view.
     * This method is typically used to embed the UI designed in this class into another container.
     * @return the root TimetablePanel containing all UI components
     */
    public JPanel getRootPanel() {
        return TimetablePanel;
    }
}
