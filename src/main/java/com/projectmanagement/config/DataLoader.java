package com.projectmanagement.config;

import com.projectmanagement.model.Project;
import com.projectmanagement.model.Task;
import com.projectmanagement.model.User;
import com.projectmanagement.repository.ProjectRepository;
import com.projectmanagement.repository.TaskRepository;
import com.projectmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      ProjectRepository projectRepository,
                      TaskRepository taskRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // تحقق من عدم وجود بيانات سابقة
        if (userRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        System.out.println("🔄 جاري تحميل البيانات التجريبية...");

        // Create Users
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .fullName("راشد حمد راشد الحميدي")
                .email("admin@intima.com")
                .role(User.Role.ADMIN)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        User manager = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("manager123"))
                .fullName("أحمد محمد العلي")
                .email("manager@intima.com")
                .role(User.Role.MANAGER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        User developer1 = User.builder()
                .username("developer")
                .password(passwordEncoder.encode("dev123"))
                .fullName("خالد عبدالله السالم")
                .email("developer@intima.com")
                .role(User.Role.DEVELOPER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        User developer2 = User.builder()
                .username("sara")
                .password(passwordEncoder.encode("sara123"))
                .fullName("سارة عبدالعزيز")
                .email("sara@intima.com")
                .role(User.Role.DEVELOPER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.saveAll(Arrays.asList(admin, manager, developer1, developer2));

        // Create Projects
        Project project1 = Project.builder()
                .name("نظام إدارة الموظفين")
                .description("تطوير نظام متكامل لإدارة شؤون الموظفين والحضور والانصراف")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 6, 30))
                .status(Project.Status.IN_PROGRESS)
                .priority(Project.Priority.HIGH)
                .createdAt(LocalDateTime.now().minusDays(30))
                .teamMembers(new HashSet<>(Arrays.asList(manager, developer1, developer2)))
                .build();

        Project project2 = Project.builder()
                .name("تطبيق الخدمات الإلكترونية")
                .description("تطوير تطبيق موبايل للخدمات الإلكترونية الحكومية")
                .startDate(LocalDate.of(2024, 2, 1))
                .endDate(LocalDate.of(2024, 8, 31))
                .status(Project.Status.IN_PROGRESS)
                .priority(Project.Priority.URGENT)
                .createdAt(LocalDateTime.now().minusDays(25))
                .teamMembers(new HashSet<>(Arrays.asList(manager, developer1)))
                .build();

        Project project3 = Project.builder()
                .name("منصة التجارة الإلكترونية")
                .description("بناء منصة متكاملة للتجارة الإلكترونية")
                .startDate(LocalDate.of(2024, 3, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Project.Status.PLANNING)
                .priority(Project.Priority.MEDIUM)
                .createdAt(LocalDateTime.now().minusDays(15))
                .teamMembers(new HashSet<>(Arrays.asList(developer2)))
                .build();

        Project project4 = Project.builder()
                .name("نظام إدارة المخزون")
                .description("نظام ذكي لإدارة المخزون والمشتريات")
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2024, 1, 31))
                .status(Project.Status.COMPLETED)
                .priority(Project.Priority.MEDIUM)
                .createdAt(LocalDateTime.now().minusDays(60))
                .teamMembers(new HashSet<>(Arrays.asList(developer1)))
                .build();

        projectRepository.saveAll(Arrays.asList(project1, project2, project3, project4));

        // Create Tasks for Project 1
        Task task1 = Task.builder()
                .title("تصميم قاعدة البيانات")
                .description("تصميم جداول قاعدة البيانات للموظفين والأقسام")
                .project(project1)
                .status(Task.Status.COMPLETED)
                .priority(Task.Priority.HIGH)
                .assignedTo(developer1)
                .dueDate(LocalDate.of(2024, 1, 15))
                .createdAt(LocalDateTime.now().minusDays(28))
                .build();

        Task task2 = Task.builder()
                .title("تطوير واجهة تسجيل الدخول")
                .description("تطوير صفحة تسجيل الدخول والمصادقة")
                .project(project1)
                .status(Task.Status.COMPLETED)
                .priority(Task.Priority.HIGH)
                .assignedTo(developer2)
                .dueDate(LocalDate.of(2024, 1, 20))
                .createdAt(LocalDateTime.now().minusDays(26))
                .build();

        Task task3 = Task.builder()
                .title("تطوير لوحة التحكم")
                .description("إنشاء لوحة تحكم رئيسية مع الإحصائيات")
                .project(project1)
                .status(Task.Status.IN_PROGRESS)
                .priority(Task.Priority.MEDIUM)
                .assignedTo(developer1)
                .dueDate(LocalDate.of(2024, 2, 10))
                .createdAt(LocalDateTime.now().minusDays(20))
                .build();

        Task task4 = Task.builder()
                .title("إدارة الموظفين")
                .description("تطوير صفحات إضافة وتعديل وحذف الموظفين")
                .project(project1)
                .status(Task.Status.IN_PROGRESS)
                .priority(Task.Priority.HIGH)
                .assignedTo(developer2)
                .dueDate(LocalDate.of(2024, 2, 15))
                .createdAt(LocalDateTime.now().minusDays(18))
                .build();

        Task task5 = Task.builder()
                .title("نظام الحضور والانصراف")
                .description("تطوير نظام تسجيل الحضور والانصراف")
                .project(project1)
                .status(Task.Status.TODO)
                .priority(Task.Priority.MEDIUM)
                .assignedTo(developer1)
                .dueDate(LocalDate.of(2024, 3, 1))
                .createdAt(LocalDateTime.now().minusDays(15))
                .build();

        // Create Tasks for Project 2
        Task task6 = Task.builder()
                .title("تصميم واجهة المستخدم")
                .description("تصميم واجهات التطبيق بالكامل")
                .project(project2)
                .status(Task.Status.COMPLETED)
                .priority(Task.Priority.URGENT)
                .assignedTo(developer1)
                .dueDate(LocalDate.of(2024, 2, 15))
                .createdAt(LocalDateTime.now().minusDays(24))
                .build();

        Task task7 = Task.builder()
                .title("تطوير API الخدمات")
                .description("بناء واجهات برمجية للخدمات الإلكترونية")
                .project(project2)
                .status(Task.Status.IN_PROGRESS)
                .priority(Task.Priority.URGENT)
                .assignedTo(developer1)
                .dueDate(LocalDate.of(2024, 3, 20))
                .createdAt(LocalDateTime.now().minusDays(20))
                .build();

        Task task8 = Task.builder()
                .title("ربط بوابة الدفع")
                .description("دمج بوابة الدفع الإلكتروني")
                .project(project2)
                .status(Task.Status.TODO)
                .priority(Task.Priority.HIGH)
                .dueDate(LocalDate.of(2024, 4, 1))
                .createdAt(LocalDateTime.now().minusDays(18))
                .build();

        // Create Tasks for Project 3
        Task task9 = Task.builder()
                .title("دراسة المتطلبات")
                .description("جمع وتحليل متطلبات المنصة")
                .project(project3)
                .status(Task.Status.IN_REVIEW)
                .priority(Task.Priority.HIGH)
                .assignedTo(developer2)
                .dueDate(LocalDate.of(2024, 3, 15))
                .createdAt(LocalDateTime.now().minusDays(14))
                .build();

        Task task10 = Task.builder()
                .title("اختيار التقنيات")
                .description("اختيار الأطر والتقنيات المناسبة")
                .project(project3)
                .status(Task.Status.TODO)
                .priority(Task.Priority.MEDIUM)
                .assignedTo(developer2)
                .dueDate(LocalDate.of(2024, 4, 1))
                .createdAt(LocalDateTime.now().minusDays(12))
                .build();

        // Overdue task
        Task task11 = Task.builder()
                .title("مراجعة الأمان")
                .description("فحص الثغرات الأمنية والتأكد من حماية البيانات")
                .project(project2)
                .status(Task.Status.TODO)
                .priority(Task.Priority.URGENT)
                .assignedTo(developer1)
                .dueDate(LocalDate.of(2024, 1, 5))
                .createdAt(LocalDateTime.now().minusDays(30))
                .build();

        taskRepository.saveAll(Arrays.asList(
                task1, task2, task3, task4, task5, task6,
                task7, task8, task9, task10, task11
        ));

        System.out.println("✅ تم تحميل البيانات التجريبية بنجاح!");
        System.out.println("👤 المستخدمون:");
        System.out.println("   - Admin: admin / admin123");
        System.out.println("   - Manager: manager / manager123");
        System.out.println("   - Developer: developer / dev123");
        System.out.println("   - Sara: sara / sara123");
    }
}
