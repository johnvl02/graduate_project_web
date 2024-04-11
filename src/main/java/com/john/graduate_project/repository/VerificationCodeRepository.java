package com.john.graduate_project.repository;

import com.john.graduate_project.model.VerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {
}
