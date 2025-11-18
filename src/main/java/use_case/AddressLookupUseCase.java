package use_case;

import entity.UofTLocation;
import java.util.Optional;

/**
 * The interactor for looking up the street addresses of UofTLocations.
 */
public class AddressLookupUseCase {

    // Depend on the abstract interface, not a concrete DAO
    private final AddressDataAccessInterface addressDAO;

    /**
     * Constructs the use case, injecting the data access dependency.
     */
    public AddressLookupUseCase(AddressDataAccessInterface addressDAO) {
        this.addressDAO = addressDAO;
    }

    public String getStreetAddress(UofTLocation location) {
        String code = location.getBuildingCode();
        Optional<String> addressOptional = addressDAO.getAddressByCode(code);

        if (addressOptional.isEmpty()) {
            // Handle the "Address not found" case
            return "Address not found for building code: " + code;
        } else {
            // Success: Format the address
            String baseAddress = addressOptional.get();
            String roomNumber = location.getRoomNumber();

            // Simple business rule: only add room if it's provided
            if (roomNumber != null && !roomNumber.trim().isEmpty()) {
                return baseAddress + ", Room " + roomNumber;
            } else {
                return baseAddress;
            }
        }
    }
}