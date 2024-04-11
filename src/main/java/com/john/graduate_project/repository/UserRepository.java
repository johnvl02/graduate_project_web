package com.john.graduate_project.repository;

import com.john.graduate_project.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}