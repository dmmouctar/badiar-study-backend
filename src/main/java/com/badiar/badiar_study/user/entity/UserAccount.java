package com.badiar.badiar_study.user.entity;

import com.badiar.badiar_study.common.constant.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;



@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_email", columnNames = "email")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(
            min = ValidationConstants.NAME_MIN_LENGTH,
            max = ValidationConstants.NAME_MAX_LENGTH,
            message = "Le prénom doit contenir entre "
                    + ValidationConstants.NAME_MIN_LENGTH + " et "
                    + ValidationConstants.NAME_MAX_LENGTH + " caractères"
    )
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(
            min = ValidationConstants.NAME_MIN_LENGTH,
            max = ValidationConstants.NAME_MAX_LENGTH,
            message = "Le nom doit contenir entre "
                    + ValidationConstants.NAME_MIN_LENGTH + " et "
                    + ValidationConstants.NAME_MAX_LENGTH + " caractères"
    )
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = ValidationConstants.EMAIL_INVALID_MESSAGE)
    @Size(max = ValidationConstants.EMAIL_MAX_LENGTH)
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "Le rôle est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Retourne le nom complet : "Prénom Nom"
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    // Vérifie si l'utilisateur est un admin (SUPER_ADMIN ou ADMIN)
    public boolean isAdministrator() {
        return this.role == UserRole.SUPER_ADMIN || this.role == UserRole.ADMIN;
    }

    // Enumération des rôles disponibles
    public enum UserRole {
        SUPER_ADMIN,
        ADMIN,
        ETUDIANT
    }
}
