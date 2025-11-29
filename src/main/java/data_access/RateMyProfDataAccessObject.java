package data_access;

import entity.Professor;
import use_case.ratemyprof.RateMyProfDataAccessInterface;

import java.util.*;

/**
 * This class caches RateMyProf calls to improve performance and lessen the load on underlying data source.
 */
public class RateMyProfDataAccessObject implements RateMyProfDataAccessInterface {
    private final RateMyProfAPI innerFetcher;
    private final HashMap<String, Professor> cache;
    private int callsMade = 0;

    public RateMyProfDataAccessObject(RateMyProfAPI fetcher) {
        this.innerFetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public Professor getProfessorInfo(String profFirstName, String profLastName) throws RuntimeException {
        if (!cache.containsKey(profFirstName + ' ' + profLastName)) {
            callsMade++;
            try {
                cache.put(profFirstName + ' ' + profLastName, innerFetcher.getProfessorInfo(profFirstName, profLastName));
            } catch (Exception e) {
                throw e;
            }
        }
        // else cache already contains this professor
        return cache.get(profFirstName + ' ' + profLastName);
    }

    public int getCallsMade() {
        return callsMade;
    }
}
