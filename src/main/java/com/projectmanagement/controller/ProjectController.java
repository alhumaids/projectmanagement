package com.projectmanagement.controller;

import jakarta.validation.Valid;
import com.projectmanagement.model.Project;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.projectmanagement.service.ProjectService;
import com.projectmanagement.service.UserService;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("activeMenu", "projects");
        model.addAttribute("pageTitle", "المشاريع");
        return "projects/list";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("activeMenu", "projects");
        model.addAttribute("pageTitle", "مشروع جديد");
        return "projects/form";
    }

    @GetMapping("/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("المشروع غير موجود"));
        model.addAttribute("project", project);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("activeMenu", "projects");
        model.addAttribute("pageTitle", "تعديل المشروع");
        return "projects/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Project project,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "projects/form";
        }

        projectService.save(project);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ المشروع بنجاح");
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "تم حذف المشروع بنجاح");
        return "redirect:/projects";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("المشروع غير موجود"));
        model.addAttribute("project", project);
        model.addAttribute("activeMenu", "projects");
        model.addAttribute("pageTitle", "تفاصيل المشروع");
        return "projects/view";
    }
}
