package org.example.multi_tenant_task.organization;

import jakarta.persistence.*;
import lombok.*;
import org.example.multi_tenant_task.project.Project;
import org.example.multi_tenant_task.user.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<User> user;

    @OneToMany(mappedBy = "organization")
    private List<Project> projects;
}
