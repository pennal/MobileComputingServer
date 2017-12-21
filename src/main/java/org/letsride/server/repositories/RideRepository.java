package org.letsride.server.repositories;

import org.letsride.server.models.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RideRepository extends MongoRepository<Ride, String> {
    Ride findById(String id);
}
