package com.projectmanagement.controller;

import com.projectmanagement.model.Project;
import com.projectmanagement.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.projectmanagement.service.ProjectService;
import com.projectmanagement.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public DashboardController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping
    public String dashboard(Model model) {
        // Statistics
        model.addAttribute("totalProjects", projectService.findAll().size());
        model.addAttribute("activeProjects", projectService.countActive());
        model.addAttribute("totalTasks", taskService.findAll().size());
        model.addAttribute("completedTasks", taskService.countByStatus(Task.Status.COMPLETED));

        // Recent Projects (last 5) - مع التحقق من null
        List<Project> recentProjects = projectService.findAll().stream()
                .filter(p -> p.getCreatedAt() != null)
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentProjects", recentProjects);

        // Recent Tasks (last 10) - مع التحقق من null
        List<Task> recentTasks = taskService.findAll().stream()
                .filter(t -> t.getCreatedAt() != null)
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .limit(10)
                .collect(Collectors.toList());
        model.addAttribute("recentTasks", recentTasks);

        // current authenticated username (fallback for templates)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      //  String currentUser = (auth != null && auth.getName() != null) ? auth.getName() : "المستخدم";
        String usernameFallback = (auth != null && auth.getName() != null) ? auth.getName() : "المستخدم";
        String displayName = extractDisplayName(auth, usernameFallback);
        model.addAttribute("currentUser", displayName);

        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("pageTitle", "لوحة التحكم");

        return "dashboard/index";
    }


    private String extractDisplayName(Authentication auth, String fallback) {
        if (auth == null) return fallback;
        Object principal = auth.getPrincipal();
        if (principal == null) return fallback;

        // try common getter names on custom principal/user objects
        String[] getters = {"getFullName", "getFullname", "getDisplayName", "getName", "getUsername", "getGivenName"};
        for (String g : getters) {
            try {
                Method m = principal.getClass().getMethod(g);
                Object res = m.invoke(principal);
                if (res != null) return res.toString();
            } catch (Exception ignored) { }
        }

        // try OIDC / OAuth2 user attributes (reflectively to avoid compile-time dependency)
        try {
            Class<?> oidcClass = Class.forName("org.springframework.security.oauth2.core.oidc.user.OidcUser");
            if (oidcClass.isInstance(principal)) {
                Method getAttr = oidcClass.getMethod("getAttribute", String.class);
                Object name = getAttr.invoke(principal, "name");
                if (name != null) return name.toString();
                Object full = getAttr.invoke(principal, "full_name");
                if (full != null) return full.toString();
            }
        } catch (ClassNotFoundException ignored) { } catch (Exception ignored) { }

        // try Jwt claims reflectively
        try {
            Class<?> jwtClass = Class.forName("org.springframework.security.oauth2.jwt.Jwt");
            if (jwtClass.isInstance(principal)) {
                Method getClaim = jwtClass.getMethod("getClaim", String.class);
                Object name = getClaim.invoke(principal, "name");
                if (name != null) return name.toString();
                Object preferred = getClaim.invoke(principal, "preferred_username");
                if (preferred != null) return preferred.toString();
            }
        } catch (ClassNotFoundException ignored) { } catch (Exception ignored) { }

        return fallback;
    }
}
