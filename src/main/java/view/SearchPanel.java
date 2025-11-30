package view;

import interface_adapter.search_courses.SearchCoursesViewModel;
import interface_adapter.search_courses.SearchCoursesState;
import interface_adapter.search_courses.SearchCoursesController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

public class SearchPanel extends JPanel implements PropertyChangeListener {
    private JButton filterButton; //not going to be used
    private JButton searchButton;
    private JTextField searchField;
    private JPanel SearchPanel;
    private JScrollPane resultsScrollPane;
    private JList<String> resultsList;
    private DefaultListModel<String> resultsListModel;

    private SearchCoursesController searchCoursesController;
    private SearchCoursesViewModel searchCoursesViewModel;

    /**
     * Creates a new SearchPanel.
     */
    public SearchPanel() {
        $$$setupUI$$$();
        setupResultsList();
        setupListeners();
    }

    private void setupResultsList() {
        resultsListModel = new DefaultListModel<>();
        resultsList = new JList<>(resultsListModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer with a button for dropdown
        resultsList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel(value.toString());
            JButton button = new JButton("▼"); // dropdown button
            button.setMargin(new Insets(0, 0, 0, 0));
            button.addActionListener(e -> showCoursePopup(value.toString(), button));
            panel.add(label, BorderLayout.CENTER);
            panel.add(button, BorderLayout.EAST);

            if (isSelected) panel.setBackground(list.getSelectionBackground());
            else panel.setBackground(list.getBackground());

            return panel;
        });

        // Attach to scroll pane
        for (Component comp : SearchPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                resultsScrollPane = (JScrollPane) comp;
                resultsScrollPane.setViewportView(resultsList);
                break;
            }
        }
    }

    private void setupListeners() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSearch();
            }
        });

        // Also allow pressing Enter in the search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSearch();
            }
        });
    }

    private void executeSearch() {
        if (searchCoursesController != null) {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                searchCoursesController.execute(query);
            }
        }
    }

    public void setSearchCoursesController(SearchCoursesController controller) {
        this.searchCoursesController = controller;
    }

    /**
     * Set the search courses view model and subscribe to its changes
     *
     * @param viewModel the view model to observe
     */
    public void setSearchCoursesViewModel(SearchCoursesViewModel viewModel) {
        this.searchCoursesViewModel = viewModel;
        // Subscribe to property changes
        this.searchCoursesViewModel.addPropertyChangeListener(
                SearchCoursesViewModel.SEARCH_RESULTS_UPDATED, this);
    }

    /**
     * Handle property change events from the ViewModel
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (SearchCoursesViewModel.SEARCH_RESULTS_UPDATED.equals(evt.getPropertyName())) {
            updateSearchResults();
        }
    }

    private void updateSearchResults() {
        if (searchCoursesViewModel == null) return;

        SearchCoursesState state = searchCoursesViewModel.getState();
        if (state == null) return;

        resultsListModel.clear();

        if (state.isError()) {
            resultsListModel.addElement("Error: " + state.getErrorMessage());
        } else {
            Set<String> courses = state.getMatchedCourses();
            if (courses.isEmpty()) {
                resultsListModel.addElement("No courses found.");
            } else {
                for (String course : courses) {
                    resultsListModel.addElement(course);
                }
            }
        }
    }

    // Show a popup with course details when dropdown button is clicked
    private void showCoursePopup(String course, Component invoker) {
        JPopupMenu popup = new JPopupMenu();
        // Example items – later replace with real CourseOffering sections
        popup.add(new JMenuItem(course + " - Section 001 - Prof. Smith"));
        popup.add(new JMenuItem(course + " - Section 002 - Prof. Lee"));
        popup.show(invoker, 0, invoker.getHeight());
    }

    /**
     * Returns the root Swing panel for this view.
     * This method is typically used to embed the UI designed in this class into another container.
     *
     * @return the root SearchPanel containing all UI components
     */
    public JPanel getRootPanel() {
        return SearchPanel;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        SearchPanel = new JPanel();
        SearchPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
        SearchPanel.setFocusCycleRoot(true);
        SearchPanel.setMaximumSize(new Dimension(357, 63));
        SearchPanel.setPreferredSize(new Dimension(12, 25));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setMaximumSize(new Dimension(-1, -1));
        scrollPane1.setMinimumSize(new Dimension(-1, -1));
        scrollPane1.setOpaque(true);
        scrollPane1.setPreferredSize(new Dimension(-1, -1));
        SearchPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        filterButton = new JButton();
        filterButton.setMaximumSize(new Dimension(65, 34));
        filterButton.setMinimumSize(new Dimension(50, 34));
        filterButton.setPreferredSize(new Dimension(50, 34));
        filterButton.setText("Filter");
        SearchPanel.add(filterButton, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 34), new Dimension(80, 34), 0, false));
        searchButton = new JButton();
        searchButton.setMaximumSize(new Dimension(65, 34));
        searchButton.setMinimumSize(new Dimension(50, 34));
        searchButton.setPreferredSize(new Dimension(50, 34));
        searchButton.setText("Search");
        SearchPanel.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 34), new Dimension(80, 34), 0, false));
        searchField = new JTextField();
        searchField.setMargin(new Insets(2, 6, 2, 6));
        searchField.setMaximumSize(new Dimension(100, 34));
        searchField.setMinimumSize(new Dimension(80, 34));
        searchField.setPreferredSize(new Dimension(80, 34));
        SearchPanel.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(177, 34), new Dimension(185, 34), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText(" ");
        SearchPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("");
        SearchPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("");
        SearchPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return SearchPanel;
    }

}