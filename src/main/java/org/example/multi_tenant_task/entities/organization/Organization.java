package org.example.multi_tenant_task.entities.organization;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.multi_tenant_task.entities.project.Project;
import org.example.multi_tenant_task.entities.user.User;
import org.springframework.data.annotation.CreatedDate;
//import org.example.multi_tenant_task.util.Auditable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Organization  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "organization")
    private List<User> user;

    @OneToMany(mappedBy = "organization")
    private List<Project> projects;
}
