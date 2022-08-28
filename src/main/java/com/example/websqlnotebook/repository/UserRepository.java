package com.example.websqlnotebook.repository;

import com.example.websqlnotebook.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
