package entity;
import data_access.BuildingAddressDAO;

public class UofTLocation {
    private final String buildingCode;
    private final String roomNumber;

    // Initializes the DAO once. This instance handles reading the JSON file.
    private static final BuildingAddressDAO ADDRESS_DAO = new BuildingAddressDAO();

    public UofTLocation(String buildingCode, String roomNumber) {
        this.buildingCode = buildingCode;
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return buildingCode + " " + roomNumber;
    }

    public String toStreetAddress() {
        String address = ADDRESS_DAO.getAddressByCode(this.buildingCode);

        if (address != null) {
            // Success: Return the address with the specific room number
            return address;
        } else {
            // Failure: Address not found
            return "Address not found for building code: " + this.buildingCode;
        }
    }
}
