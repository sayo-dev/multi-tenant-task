package org.example.multi_tenant_task.entities.project;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.multi_tenant_task.entities.organization.Organization;
import org.example.multi_tenant_task.entities.task.Task;
import org.example.multi_tenant_task.entities.user.User;
//import org.example.multi_tenant_task.util.Auditable;

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
