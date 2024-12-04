package com.nitor.todoh2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "todouser")
    @NotBlank(message = "User cannot be null or empty!")
    private String user;
    @Size(min = 10, max = 200, message = "Task should be b/w 10-200 characters!")
    private String task;

    private boolean done;

    @FutureOrPresent(message = "Target date must be present or future date!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

}
