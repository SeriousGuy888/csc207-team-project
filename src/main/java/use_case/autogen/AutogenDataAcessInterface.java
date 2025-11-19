package use_case.autogen;
import entity.CourseOffering;
import  java.util.List;
import entity.CourseCode;
import java.util.Set;

public interface AutogenDataAcessInterface {
    List<CourseOffering> getOfferingsFor(Set<CourseCode> courseCodes);
}
