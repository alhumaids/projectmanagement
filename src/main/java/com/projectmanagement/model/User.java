package com.projectmanagement.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "اسم المستخدم مطلوب")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "كلمة المرور مطلوبة")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "الاسم الكامل مطلوب")
    @Column(nullable = false)
    private String fullName;

    @Email(message = "البريد الإلكتروني غير صحيح")
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.DEVELOPER;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "assignedTo")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany(mappedBy = "teamMembers")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Project> projects = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum Role {
        ADMIN("مدير النظام"),
        MANAGER("مدير مشروع"),
        DEVELOPER("مطور");

        private final String arabicName;

        Role(String arabicName) {
            this.arabicName = arabicName;
        }

        public String getArabicName() {
            return arabicName;
        }
    }
}