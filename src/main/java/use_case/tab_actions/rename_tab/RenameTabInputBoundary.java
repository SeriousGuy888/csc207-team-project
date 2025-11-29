package use_case.tab_actions.rename_tab;

public interface RenameTabInputBoundary {
    void execute(int tabIndex, String newName);
}
