package com.vadik.timetable.repo;

import com.vadik.timetable.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {


    @Query(value = "SELECT * FROM TASKS WHERE TIMESTAMPDIFF(MINUTE, TASK_START, NOW()) <= 5", nativeQuery = true)
    List<Task> fetchForStateChange();
}
