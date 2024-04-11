package com.john.graduate_project.repository;

import com.john.graduate_project.model.ReviewForUser;
import org.springframework.data.repository.CrudRepository;

public interface ReviewUserRepository extends CrudRepository<ReviewForUser, String> {
}
