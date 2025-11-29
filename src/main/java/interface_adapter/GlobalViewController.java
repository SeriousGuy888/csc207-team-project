package interface_adapter;

import use_case.tab_actions.add_tab.AddTabInputBoundary;
import use_case.tab_actions.delete_tab.DeleteTabInputBoundary;
import use_case.tab_actions.rename_tab.RenameTabInteractor;
import use_case.tab_actions.switch_tab.SwitchTabInputBoundary;

public class GlobalViewController {
    private final AddTabInputBoundary addTabInteractor;
    private final DeleteTabInputBoundary deleteTabInteractor;
    private final SwitchTabInputBoundary switchTabInteractor;
    private final RenameTabInteractor renameTabInteractor;

    public GlobalViewController(AddTabInputBoundary addTab,
                         DeleteTabInputBoundary removeTab,
                         SwitchTabInputBoundary switchTab,
                         RenameTabInteractor renameTab) {
        this.addTabInteractor = addTab;
        this.deleteTabInteractor = removeTab;
        this.switchTabInteractor = switchTab;
        this.renameTabInteractor = renameTab;
    }

    /**
     * Adds a blank tab to the UI.
     */
    public void addTab() {
        // No input data needed for adding a blank tab
        addTabInteractor.execute();
    }

    /**
     * Deletes a tab from the UI.
     * @param tabIndex the index of the tab to delete
     */
    public void deleteTab(int tabIndex) {
        deleteTabInteractor.execute(tabIndex);
    }

    /**
     * Switches to a different tab in the UI.
     * @param newIndex the index of the tab to switch to
     */
    public void switchTab(int newIndex) {
        switchTabInteractor.execute(newIndex);
    }

    /**
     * Rename a tab in the UI.
     * @param tabIndex the index of the tab to rename
     * @param newName the new name of the tab
     */
    public void renameTab(int tabIndex, String newName) {
        renameTabInteractor.execute(tabIndex, newName);
    }
}
