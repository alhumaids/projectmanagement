package com.projectmanagement.controller;

import jakarta.validation.Valid;
import com.projectmanagement.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.projectmanagement.service.ProjectService;
import com.projectmanagement.service.TaskService;
import com.projectmanagement.service.UserService;


@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("activeMenu", "tasks");
        model.addAttribute("pageTitle", "المهام");
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("activeMenu", "tasks");
        model.addAttribute("pageTitle", "مهمة جديدة");
        return "tasks/form";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("المهمة غير موجودة"));
        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("activeMenu", "tasks");
        model.addAttribute("pageTitle", "تعديل المهمة");
        return "tasks/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Task task,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("users", userService.findAll());
            return "tasks/form";
        }

        taskService.save(task);
        redirectAttributes.addFlashAttribute("successMessage", "تم حفظ المهمة بنجاح");
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "تم حذف المهمة بنجاح");
        return "redirect:/tasks";
    }
}
