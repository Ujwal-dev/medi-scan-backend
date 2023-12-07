package com.mediscan.backend.Repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mediscan.backend.Components.User;

@Repository
public interface UserRepo extends MongoRepository<User , ObjectId>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
