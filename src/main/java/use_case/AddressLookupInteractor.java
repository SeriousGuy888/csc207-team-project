package use_case;

import entity.UofTLocation;
import java.util.Optional;

public class AddressLookupInteractor {

    private final AddressDataAccessInterface addressDAO;

    public AddressLookupInteractor(AddressDataAccessInterface addressDAO) {
        this.addressDAO = addressDAO;
    }

    public Optional<String> getStreetAddress(UofTLocation location) {
        String code = location.getBuildingCode();
        return addressDAO.getAddressByCode(code);
    }
}