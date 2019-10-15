package ru.solon4ak.jm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.solon4ak.jm.model.Role;
import ru.solon4ak.jm.model.User;
import ru.solon4ak.jm.service.RoleService;
import ru.solon4ak.jm.service.UserService;

import java.util.*;

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

    @GetMapping({"edit", "edit/{id}"})
    public String editUserById(Model model, @PathVariable("id") Optional<Long> id) {
        Form form = new Form();
        if (id.isPresent()) {
            User user = userService.getUser(id.get());

            form.setId(user.getId());
            form.setUserName(user.getUsername());
            form.setUserPassword(user.getPassword());
            form.setFirstName(user.getFirstName());
            form.setLastName(user.getLastName());
            form.setAddress(user.getAddress());
            form.setPhoneNumber(user.getPhoneNumber());
            form.setEmail(user.getEmail());
            form.setBirthday(user.getBirthDate());

            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(role.getName());
            }

            form.setRoles(roles);
        }

        model.addAttribute("form", form);

//        List<Role> all_roles = ;
        List<String> all_roles = new ArrayList<>();
        for (Role role : roleService.getAllUserRoles()) {
            all_roles.add(role.getName());
        }
        model.addAttribute("user_roles", all_roles);
        return "add-edit-user";
    }

    @PostMapping("add")
    public String createOrUpdateUser(@RequestParam Long id, Form form) {
        User user = new User();

        user.setUsername(form.getUserName());
        user.setPassword(form.getUserPassword());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setAddress(form.getAddress());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setBirthDate(form.getBirthday());

        Set<Role> userRoles = new HashSet<>();
        for (String s : form.getRoles()) {
            for(Role role : roleService.getAllUserRoles()) {
                if (s.equals(role.getName())) {
                    userRoles.add(role);
                }
            }
        }
        user.setRoles(userRoles);

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

    public class Form {
        private Long id;
        private String userName;
        private String userPassword;
        private String firstName;
        private String lastName;
        private String email;
        private String address;
        private String phoneNumber;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date birthday;
        private List<String> roles;

        public Form() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

}
