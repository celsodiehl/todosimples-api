package com.celsodiehl.todosimples.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.celsodiehl.todosimples.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    
}
