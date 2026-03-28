package org.example.multi_tenant_task.project;

import jakarta.persistence.*;
import lombok.*;
import org.example.multi_tenant_task.organization.Organization;
import org.example.multi_tenant_task.task.Task;
import org.example.multi_tenant_task.user.User;
import org.springframework.data.annotation.CreatedBy;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User user;

    @OneToMany(mappedBy = "project")
    private List<Task> task;
}
