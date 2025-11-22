package view;

import javax.swing.*;

public class SearchPanel extends JPanel{

    private JButton filterButton;
    private JButton searchButton;
    private JTextField searchField;
    private JPanel SearchPanel;

    /**
     * Creates a new SearchPanel.
     */
    public SearchPanel() {
        // not implemented yet
    }

    /**
     * Returns the root Swing panel for this view.
     * This method is typically used to embed the UI designed in this class into another container.
     * @return the root SearchPanel containing all UI components
     */
    public JPanel getRootPanel() {
        return SearchPanel;
    }
}
