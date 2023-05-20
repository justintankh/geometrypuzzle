package com.geometrypuzzle.backend.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@Slf4j
@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public boolean hasSession(String uuid) {
        return storeRepository.findById(uuid).isPresent();
    }

    public Store retrieveStore(String uuid) {
        Store store = storeRepository.findById(uuid).orElseThrow(StoreDoesNotExistException(uuid));
        return store;
    }

    public Store safeRetrieveStore(String uuid) {
        return storeRepository.findById(uuid).orElseGet(() -> createStore(uuid));
    }

    public Store createStore(String uuid) {
        Store newStore = new Store();
        newStore.setProcessKey(uuid);
        return newStore;
    }

    public void updateStore(Store store){
        storeRepository.save(store);
    }

    private static Supplier<NoSuchElementException> StoreDoesNotExistException(String uuid) {
        return () -> new NoSuchElementException("Error fetching store for ID %s".formatted(uuid));
    }

    /* for debugging, to be removed */
    public List<Store> retrieveAllStore() {
        return storeRepository.findAll();
    }
}
