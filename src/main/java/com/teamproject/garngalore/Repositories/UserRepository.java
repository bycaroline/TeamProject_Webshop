package com.teamproject.garngalore.Repositories;

import com.teamproject.garngalore.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    User findByEmail(String email);
}
