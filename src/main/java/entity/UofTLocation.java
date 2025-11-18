package entity;

/**
 * Represents a specific location at UofT, defined by a building and room.
 */
public class UofTLocation {
    private final String buildingCode;
    private final String roomNumber;

    public UofTLocation(String buildingCode, String roomNumber) {
        this.buildingCode = buildingCode;
        this.roomNumber = roomNumber;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return buildingCode + " " + roomNumber;
    }
}