package com.networkmanagement.networkhealthmonitor.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.networkmanagement.networkhealthmonitor.model.Domain;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends ArangoRepository<Domain, String> {

    Optional<Domain> findByName(String name);

    boolean existsByName(String name);

}
