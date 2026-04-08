package org.example.multi_tenant_task.entities.project;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.multi_tenant_task.entities.organization.Organization;
import org.example.multi_tenant_task.entities.task.Task;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.util.Auditable;
import org.hibernate.annotations.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SoftDelete(columnName = "deleted")
@Entity
public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User user;

    @OneToMany(mappedBy = "project")
    private List<Task> task;

}
