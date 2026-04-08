package org.example.multi_tenant_task.entities.task;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.multi_tenant_task.entities.project.Project;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.util.Auditable;
//import org.example.multi_tenant_task.util.Auditable;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Task extends Auditable {

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

    private LocalDateTime dueDate;
}
