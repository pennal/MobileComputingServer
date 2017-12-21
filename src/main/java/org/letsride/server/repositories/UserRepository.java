package org.letsride.server.repositories;

import org.letsride.server.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmailAddress(String emailAddress);

    User findByAccount_Username(String username);
    User findByAccount_UsernameOrEmailAddress(String username, String emailAddress);
}
