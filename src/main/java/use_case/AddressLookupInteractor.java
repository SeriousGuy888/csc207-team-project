package use_case;

import entity.UofTLocation;
import java.util.Optional;

/**
 * The interactor for looking up the street addresses of UofTLocations.
 */
public class AddressLookupInteractor {

    // Depend on the abstract interface, not a concrete DAO
    private final AddressDataAccessInterface addressDAO;

    /**
     * Constructs the use case, injecting the data access dependency.
     */
    public AddressLookupInteractor(AddressDataAccessInterface addressDAO) {
        this.addressDAO = addressDAO;
    }

    public Optional<String> getStreetAddress(UofTLocation location) {
        String code = location.getBuildingCode();

        // addressDAO.getAddressByCode(code) returns Optional<String>
        return addressDAO.getAddressByCode(code)
                .map(baseAddress -> {
                    String roomNumber = location.getRoomNumber();

                    // Business rule: Append room number if provided
                    if (roomNumber != null) {
                        return baseAddress;
                    }

                    // Otherwise just return the base address
                    return null;
                });
    }
}