package use_case.tab_actions.rename_tab;

public interface RenameTabInputBoundary {
    /**
     * Executes the Rename Tab Use Case.
     * @param tabIndex the index of the tab to rename.
     * @param newName the new name of the tab.
     */
    void execute(int tabIndex, String newName);
}
