package use_case.tab_actions.switch_tab;

public interface SwitchTabInputBoundary {
    /**
     * Executes the Switch Tab Use Case.
     * @param newIndex the index of the tab to switch to.
     */
    void execute(int newIndex);
}
