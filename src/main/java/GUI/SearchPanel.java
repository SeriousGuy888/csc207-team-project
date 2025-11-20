package GUI;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel{

    private JButton filterButton;
    private JButton searchButton;
    private JTextField searchField;
    private JPanel SearchPanel;

    public SearchPanel() {
        setPreferredSize(new Dimension(400, 800));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Search Panel");
        SearchPanel spanel = new SearchPanel();
        frame.setContentPane(spanel.SearchPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400, 800);
        frame.setVisible(true);
    }
}
