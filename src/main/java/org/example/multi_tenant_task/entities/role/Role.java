package org.example.multi_tenant_task.entities.role;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
//import org.example.multi_tenant_task.util.Auditable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private RoleEnum role;

}
