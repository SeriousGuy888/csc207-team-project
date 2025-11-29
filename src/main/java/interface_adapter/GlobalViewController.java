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

    public void addTab() {
        // No input data needed for adding a blank tab
        addTabInteractor.execute();
    }

    public void deleteTab(int tabIndex) {
        deleteTabInteractor.execute(tabIndex);
    }

    public void switchTab(int newIndex) {
        switchTabInteractor.execute(newIndex);
    }

    public void renameTab(int tabIndex, String newName) {
        renameTabInteractor.execute(tabIndex, newName);
    }
}
