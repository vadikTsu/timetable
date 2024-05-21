package com.vadik.timetable.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASKS")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESC")
    private String description;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name="TASK_START", nullable = false)
    private LocalDateTime taskStart;

    @Column(name="TASK_END",nullable = false)
    private LocalDateTime taskEnd;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "TASK_ID", nullable = false)
//    private User user;

    public Task() {
    }

    public Task(String title, String description, EStatus status, LocalDateTime taskStart, LocalDateTime taskEnd) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
    }

    public Task(Long id, String title, String description, EStatus status, LocalDateTime taskStart, LocalDateTime taskEnd) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public LocalDateTime getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(LocalDateTime taskStart) {
        this.taskStart = taskStart;
    }

    public LocalDateTime getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(LocalDateTime taskEnd) {
        this.taskEnd = taskEnd;
    }
}
