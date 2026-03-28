package org.example.multi_tenant_task.task;

import jakarta.persistence.*;
import lombok.*;
import org.example.multi_tenant_task.project.Project;
import org.example.multi_tenant_task.user.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User user;


    @CreatedDate
    private LocalDateTime dueDate;
}
