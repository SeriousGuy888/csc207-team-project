package view;

import app.GlobalConstants;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import interface_adapter.save_workbook.SaveWorkbookController;
import interface_adapter.save_workbook.SaveWorkbookState;
import interface_adapter.save_workbook.SaveWorkbookViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveDialog extends JDialog implements PropertyChangeListener {
    private static SaveDialog singletonInstance;

    private static final String DIALOG_TITLE = "Save...";
    private static final String FILE_CHOOSER_TITLE = "Choose save location...";
    private static final String REQUIRED_PATH_ENDING = "." + GlobalConstants.WORKBOOK_FILE_EXTENSION;

    private JPanel contentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel buttonRow;
    private JTextField pathField;
    private JButton browseButton;
    private JTextPane statusTextPane;

    private final JFileChooser fileChooser;
    private final SaveWorkbookViewModel saveWorkbookViewModel;
    private final SaveWorkbookController saveWorkbookController;


    private SaveDialog(SaveWorkbookViewModel saveWorkbookViewModel,
                       SaveWorkbookController saveWorkbookController) {
        this.saveWorkbookViewModel = saveWorkbookViewModel;
        this.saveWorkbookController = saveWorkbookController;
        saveWorkbookViewModel.addPropertyChangeListener(SaveWorkbookViewModel.STATE_PROPERTY_NAME, this);

        setTitle(DIALOG_TITLE);

        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle(FILE_CHOOSER_TITLE);
        fileChooser.setFileFilter(GlobalConstants.WORKBOOK_FILE_EXTENSION_FILTER);
        setPath("");

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowse();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pathField.addComponentListener(new ComponentAdapter() {
        });

        pathField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> onPathUpdate());
            }
        });
    }

    public static void createSingletonInstance(SaveWorkbookViewModel saveWorkbookViewModel,
                                               SaveWorkbookController saveWorkbookController) {
        if (singletonInstance != null) {
            throw new IllegalStateException("An instance of SaveDialog already exists.");
        }

        singletonInstance = new SaveDialog(saveWorkbookViewModel, saveWorkbookController);
    }

    public static SaveDialog getSingletonInstance() {
        if (singletonInstance == null) {
            throw new IllegalStateException("SaveDialog has not been instantiated yet.");
        }
        return singletonInstance;
    }

    /**
     * Show the dialog to the user.
     *
     * @param relativeComponent the component that the popup window should be centered relative to. can be null.
     */
    public void display(Component relativeComponent) {
        setGreyedOut(false);

        pack();
        setLocationRelativeTo(relativeComponent);
        setVisible(true);
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    private void setGreyedOut(boolean flag) {
        if (flag) {
            buttonRow.setVisible(false);
            browseButton.setEnabled(false);
            pathField.setEnabled(false);
        } else {
            buttonRow.setVisible(true);
            browseButton.setEnabled(true);
            pathField.setEnabled(true);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SaveWorkbookState state = saveWorkbookViewModel.getState();
        setStatusText(state.getMessage(), state.isSuccess());
        if (state.isSuccess()) {
            setGreyedOut(true);
            closeAfterDelay();
        }
    }

    private void closeAfterDelay() {
        final Timer timer = new Timer(1500, e -> close());
        timer.setRepeats(false);
        timer.start();
    }

    private void setStatusText(String text, boolean success) {
        final Color textColour = success ? Color.BLACK : Color.RED;
        statusTextPane.setForeground(textColour);
        statusTextPane.setText(text);
    }

    private void setPath(String path) {
        final String processedPath;
        if (!path.isBlank() && !path.endsWith(REQUIRED_PATH_ENDING)) {
            // require that the user saves the file with the correct file extension
            // (otherwise they'll have trouble finding it again later, since the file browser won't display it)
            processedPath = path + REQUIRED_PATH_ENDING;
        } else {
            processedPath = path;
        }

        pathField.setText(processedPath);
        onPathUpdate();
    }


    private void onSave() {
        final Path path = Paths.get(pathField.getText());
        saveWorkbookController.execute(path);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onBrowse() {
        final int responseFromChooser = fileChooser.showSaveDialog(this);

        // only do something if the user actually presses save
        // otherwise do nothing
        if (responseFromChooser == JFileChooser.APPROVE_OPTION) {
            setPath(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void onPathUpdate() {
        final String pathText = pathField.getText();
        saveButton.setEnabled(!pathText.isBlank());

        final File file = new File(pathText);

        if (!pathText.isBlank()) {
            final StringBuilder newStatusText = new StringBuilder();
            if (file.exists()) {
                if (file.isFile()) {
                    newStatusText.append("Warning: A file already exists at this path.");
                } else if (file.isDirectory()) {
                    newStatusText.append("Warning: A folder already exists at this path.");
                }
            }
            if (!pathText.endsWith(REQUIRED_PATH_ENDING)) {
                newStatusText.append(
                        "\nWarning: File should ideally be named using file extension " + REQUIRED_PATH_ENDING);
            }

            setStatusText(newStatusText.toString(), true);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        pathField = new JTextField();
        pathField.setText("");
        panel2.add(pathField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, -1), new Dimension(350, -1), null, 0, false));
        browseButton = new JButton();
        browseButton.setText("Browse");
        panel2.add(browseButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Choose save location...");
        panel2.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusTextPane = new JTextPane();
        statusTextPane.setEditable(false);
        statusTextPane.setEnabled(true);
        statusTextPane.setText("");
        panel1.add(statusTextPane, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        buttonRow = new JPanel();
        buttonRow.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(buttonRow, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        buttonRow.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setEnabled(true);
        saveButton.setText("Save");
        panel3.add(saveButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        panel3.add(cancelButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
