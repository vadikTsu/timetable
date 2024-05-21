package com.vadik.timetable.web;

import com.vadik.timetable.domain.EStatus;
import com.vadik.timetable.domain.Task;
import com.vadik.timetable.repo.TaskRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getTasksReturnListOfTasks_whenRequestSucceed() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(List.of(new Task(1l, "String title", "String description", EStatus.SCHEDULED, LocalDateTime.now(), LocalDateTime.now().plusHours(1))));
        Mockito.when(taskRepo.findAll()).thenReturn(tasks);

        mvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("String title"))
                .andExpect(jsonPath("$[0].description").value("String description"))
                .andExpect(jsonPath("$[0].status").value(EStatus.SCHEDULED.name()))
                .andExpect(jsonPath("$[0].taskStart").isString())
                .andExpect(jsonPath("$[0].taskEnd").isString());
    }

    @Test
    void deleteById_whenExceptionThrown() throws Exception {
        Long taskId = 1L;
        Mockito.doThrow(new RuntimeException()).when(taskRepo).deleteById(taskId);

        mvc.perform(delete("/delete/{id}", taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("Failed to delete the Task"))
                .andExpect(status().isNotModified());
        verify(taskRepo).deleteById(taskId);
    }

    @Test
    void deleteById_whenRequestSucceed() throws Exception {
        Long taskId = 1L;
        Mockito.doNothing().when(taskRepo).deleteById(taskId);

        mvc.perform(delete("/delete/{id}", taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("The Task delete successfully"))
                .andExpect(status().isOk());
        verify(taskRepo).deleteById(taskId);

    }

    @Test
    void placeTask_whenRequestSucceed() throws Exception {
        Task task = new Task(1L, "String title", "String description", EStatus.SCHEDULED, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Mockito.when(taskRepo.save(Mockito.any(Task.class))).thenReturn(task);

        String taskJson = "{\"title\": \"String title\", \"description\": \"String description\", \"taskStart\": \"" + LocalDateTime.now().plusHours(1).toString() + "\", \"taskEnd\": \"" + LocalDateTime.now().plusHours(2).toString() + "\"}";

        mvc.perform(MockMvcRequestBuilders.post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("New Task placed"));
        verify(taskRepo).save(Mockito.any(Task.class));
    }

    @Test
    void placeTask_whenValidationFails_ThrowsException() throws Exception {
        Task task = new Task(1L, "String title", "String description", EStatus.SCHEDULED, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(2));

        String taskJson = "{\"title\": \"String title\", \"description\": \"String description\", \"taskStart\": \"" + LocalDateTime.now().minusHours(1).toString() + "\", \"taskEnd\": \"" + LocalDateTime.now().plusHours(2).toString() + "\"}";

        mvc.perform(MockMvcRequestBuilders.post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNotModified())
                .andExpect(jsonPath("$").value("Failed to place a task, invalid time"));
    }
    @Test
    void updateTask_whenRequestSucceed_returnsOkStatus() throws Exception {
        Task task = new Task(1L, "Updated title", "Updated description", EStatus.SCHEDULED, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Mockito.when(taskRepo.existsById(task.getId())).thenReturn(true);
        Mockito.when(taskRepo.save(Mockito.any(Task.class))).thenReturn(task);

        String taskJson = "{\"id\": 1, \"title\": \"Updated title\", \"description\": \"Updated description\", \"taskStart\": \"" + LocalDateTime.now().plusHours(1).toString() + "\", \"taskEnd\": \"" + LocalDateTime.now().plusHours(2).toString() + "\"}";

        mvc.perform(MockMvcRequestBuilders.put("/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Task Was Updated"));
        verify(taskRepo).existsById(task.getId());
        verify(taskRepo).save(Mockito.any(Task.class));
    }

    @Test
    void updateTask_whenTaskDoesNotExist_ThrowsException() throws Exception {
        Task task = new Task(1L, "Updated title", "Updated description", EStatus.SCHEDULED, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Mockito.when(taskRepo.existsById(task.getId())).thenReturn(false);

        String taskJson = "{\"id\": 1, \"title\": \"Updated title\", \"description\": \"Updated description\", \"taskStart\": \"" + LocalDateTime.now().plusHours(1).toString() + "\", \"taskEnd\": \"" + LocalDateTime.now().plusHours(2).toString() + "\"}";

        mvc.perform(MockMvcRequestBuilders.put("/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNotModified())
                .andExpect(jsonPath("$").value("Failed To update a Task"));
    }
}