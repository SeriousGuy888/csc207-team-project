package interface_adapter.autogen;
import entity.Timetable;
import entity.Section;
import java.util.Set;

public class AutogenViewModel {
    private Timetable generatedTimetable;
    private String message;
    private String error;

    /** Getters and Setters for variables **/
    public Timetable getTimetable(){return generatedTimetable;}
    public String getMessage(){return message;}
    public String getError(){return error;}
    public void setTimetable(Timetable generatedTimetable){this.generatedTimetable = generatedTimetable;}
    public void setMessage(String message){this.message = message;}
    public void setError(String error){this.error = error;}
}
