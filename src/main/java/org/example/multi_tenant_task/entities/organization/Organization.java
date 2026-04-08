package org.example.multi_tenant_task.entities.organization;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.multi_tenant_task.entities.project.Project;
import org.example.multi_tenant_task.entities.user.User;
import org.example.multi_tenant_task.util.Auditable;
import org.springframework.data.annotation.CreatedDate;
//import org.example.multi_tenant_task.util.Auditable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Organization extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

//    @CreatedDate
//    @Column(updatable = false, nullable = false)
//    private LocalDateTime createdAt;

//    @JsonManagedReference
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private List<User> user;

    @OneToMany(mappedBy = "organization")
    private List<Project> projects;
}
