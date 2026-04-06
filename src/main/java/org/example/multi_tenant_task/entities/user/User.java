package org.example.multi_tenant_task.entities.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.multi_tenant_task.entities.organization.Organization;
import org.example.multi_tenant_task.entities.project.Project;
import org.example.multi_tenant_task.entities.role.Role;
import org.example.multi_tenant_task.entities.task.Task;
//import org.example.multi_tenant_task.util.Auditable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean isVerified;

    @ManyToMany
    //use fetch on field or EntityGraph on repo methods but use EntityGraph instead because it's good practice compared to fetch which loads even when not needed.
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Organization organization;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;


}
