package use_case;

import java.util.Optional;

/**
 * A Data Access Interface (Port/Gateway) for fetching building addresses.
 */

public interface AddressDataAccessInterface {

    Optional<String> getAddressByCode(String code);
}