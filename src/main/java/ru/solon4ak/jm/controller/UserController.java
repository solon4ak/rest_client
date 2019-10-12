package ru.solon4ak.jm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.solon4ak.jm.model.User;
import ru.solon4ak.jm.service.RoleService;
import ru.solon4ak.jm.service.UserService;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

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
        model.addAttribute("user_list", users);
        return "view-all";
    }

    @GetMapping("view/{id}")
    public String viewUser(Model model, @PathVariable Long id) {
        User user = userService.getUser(id);
        model.addAttribute("usr", user);
        return "view";
    }

    @GetMapping({"edit/{id}"})
    public String editUser(Model model, @PathVariable Long id) {
        User user = userService.getUser(id);
        model.addAttribute("usr", user);
        model.addAttribute("user_roles", roleService.getAllUserRoles());
        return "add-edit-user";
    }

    @GetMapping("edit")
    public String addUser(Model model) {
        model.addAttribute("usr", new User());
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
            userService.updateUser(aUser);
        }
        return "redirect:/";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";
    }
}
