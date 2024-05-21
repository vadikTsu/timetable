package com.vadik.timetable.web;

import com.vadik.timetable.domain.EStatus;
import com.vadik.timetable.domain.Task;
import com.vadik.timetable.repo.TaskRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
public class TaskController {

    private final TaskRepo taskRepo;

    public TaskController(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }


    @GetMapping("/tasks")
    public List<Task> getTasks(){
        return taskRepo.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long taskId){
        try {
            taskRepo.deleteById(taskId);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed to delete the Task");
        }
        return ResponseEntity.ok("The Task delete successfully");
    }

    @PostMapping("/new")
    public ResponseEntity<?> placeTask(@RequestBody Task task){
        try{
            Task toPersistTask = validate(task);
            toPersistTask.setStatus(EStatus.SCHEDULED);
            taskRepo.save(toPersistTask);
            return ResponseEntity.status(HttpStatus.OK).body("New Task placed");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(e.getMessage());
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody Task task){
        try{
            if(taskRepo.existsById(task.getId())){

                taskRepo.save(validate(task));
            } else {
                throw new RuntimeException();
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed To update a Task");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Task Was Updated");
    }

    private Task validate(Task task){
        if(task.getTaskStart().isAfter(LocalDateTime.now()) && task.getTaskStart().isBefore(task.getTaskEnd())){
            return task;
        } else {
            throw new RuntimeException("Failed to place a task, invalid time");
        }
    }
}
