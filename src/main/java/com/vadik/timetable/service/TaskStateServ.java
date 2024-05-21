package com.vadik.timetable.service;

import com.vadik.timetable.repo.TaskRepo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class TaskStateServ {
    private final TaskRepo taskRepo;

    public TaskStateServ(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Scheduled(fixedRate = 5000l)
    public void updateTasks(){
        System.out.println("TASK UPDATE");
    }

}
