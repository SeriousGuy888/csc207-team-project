package use_case.tab_actions.delete_tab;

public interface DeleteTabInputBoundary {
    /**
     * Executes the Delete Tab Use Case.
     * @param tabIndex the index of the tab to delete.
     */
    void execute(int tabIndex);
}
