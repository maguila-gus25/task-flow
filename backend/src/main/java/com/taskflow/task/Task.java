package com.taskflow.task;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

/**
 * Entidade que representa uma tarefa do TaskFlow.
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean done = false;

    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecurrenceFrequency recurrenceFrequency = RecurrenceFrequency.NONE;

    @Column(nullable = false)
    private int recurrenceInterval = 1;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_recurrence_days", joinColumns = @JoinColumn(name = "task_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private Set<DayOfWeek> recurrenceDaysOfWeek = EnumSet.noneOf(DayOfWeek.class);

    private Instant recurrenceEndDate;

    /** Construtor exigido pelo JPA. */
    protected Task() {
    }

    public Task(String title, String description, boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public RecurrenceFrequency getRecurrenceFrequency() {
        return recurrenceFrequency;
    }

    public void setRecurrenceFrequency(RecurrenceFrequency recurrenceFrequency) {
        this.recurrenceFrequency = recurrenceFrequency == null ? RecurrenceFrequency.NONE : recurrenceFrequency;
    }

    public int getRecurrenceInterval() {
        return recurrenceInterval;
    }

    public void setRecurrenceInterval(int recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }

    public Set<DayOfWeek> getRecurrenceDaysOfWeek() {
        return recurrenceDaysOfWeek;
    }

    public void setRecurrenceDaysOfWeek(Set<DayOfWeek> recurrenceDaysOfWeek) {
        this.recurrenceDaysOfWeek = (recurrenceDaysOfWeek == null || recurrenceDaysOfWeek.isEmpty())
                ? EnumSet.noneOf(DayOfWeek.class)
                : EnumSet.copyOf(recurrenceDaysOfWeek);
    }

    public Instant getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(Instant recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public boolean isRecurring() {
        return recurrenceFrequency != RecurrenceFrequency.NONE;
    }
}
