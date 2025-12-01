package app;

import javax.swing.filechooser.FileNameExtensionFilter;

public class GlobalConstants {
    // I do NOT know where to put any of these values other than here

    public static final String APP_NAME = "Jason's Extravagant Timetable Builder";

    public static final String WORKBOOK_FILE_EXTENSION = "jasonworkbook";
    public static final FileNameExtensionFilter WORKBOOK_FILE_EXTENSION_FILTER = new FileNameExtensionFilter(
            "Jason's Extravagant Timetable Builder Workbooks (." + WORKBOOK_FILE_EXTENSION + ")",
            WORKBOOK_FILE_EXTENSION
    );
}
