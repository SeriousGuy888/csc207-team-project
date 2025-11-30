package use_case.tab_actions.switch_tab;

public interface SwitchTabOutputBoundary {
    /**
     * Prepares the view for the success of the Switch Tab Use Case.
     * @param newIndex the index of the tab that was switched to.
     */
    void prepareSuccessView(int newIndex);
}
