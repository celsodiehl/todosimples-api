package com.celsodiehl.todosimples.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.celsodiehl.todosimples.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    //List<Task> findByUser_Id(Long id);
    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    List<Task> findByUser_Id(@Param ("id") Long id);
}
