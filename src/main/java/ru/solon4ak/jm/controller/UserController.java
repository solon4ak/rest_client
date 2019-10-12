package ru.solon4ak.jm.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.solon4ak.jm.model.User;
import ru.solon4ak.jm.service.RoleService;
import ru.solon4ak.jm.service.UserService;

import java.util.Optional;

@Controller
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String viewAll(Model model) {
        User[] users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "view-all";
    }

    @GetMapping("view/{id}")
    public String viewUser(Model model, @PathVariable Long id) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "view";
    }

    @GetMapping({"edit", "edit/{id}"})
    public String editUser(Model model, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            User user = userService.getUser(id.get());
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        model.addAttribute("user_roles", roleService.getAllUserRoles());
        return "add-edit-user";
    }

    @PostMapping("add")
    public String createOrUpdateUser(@RequestParam Long id, User user) {

        if (id == null) {
            userService.createUser(user);
        } else {
            User aUser = userService.getUser(id);
            BeanUtils.copyProperties(user, aUser, "id");
            userService.updateUser(user);
        }
        return "redirect:/";
    }
}
