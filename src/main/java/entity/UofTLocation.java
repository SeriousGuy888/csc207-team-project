package entity;

public class UofTLocation {
    private final String buildingCode;
    private final String roomNumber;

    public UofTLocation(String buildingCode, String roomNumber) {
        this.buildingCode = buildingCode;
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return buildingCode + " " + roomNumber;
    }

    public String toStreetAddress() {
        // todo: return an actually correct street addresses once the conversion table exists
        return "100 St George Street";
    }
}
