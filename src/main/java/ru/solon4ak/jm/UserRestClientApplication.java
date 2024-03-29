package ru.solon4ak.jm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ru.solon4ak.jm.model.Role;
import ru.solon4ak.jm.model.User;
import ru.solon4ak.jm.service.RoleService;
import ru.solon4ak.jm.service.UserService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SpringBootApplication
public class UserRestClientApplication {

    private static final String URL_USERS = "http://localhost:8080/rest";

    private static final Logger log = LoggerFactory.getLogger(UserRestClientApplication.class);

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRestClientApplication(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserRestClientApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            /*  List all users  */
            User[] users = userService.getAllUsers(URL_USERS, restTemplate);
            log.info("1. List all users");
            log.info("Users: " + users.length);
            log.info("All users: " + printUsers(users));

            /*  Find one  */
            log.info("2. Get user");
            User user = userService.getUser(URL_USERS, restTemplate, 2L);
            log.info("User with id = 2: " + user.toString());

            /*  Create user  */
            log.info("3. Create user");
            User userToCreate = new User();
            userToCreate.setFirstName("Erich");
            userToCreate.setLastName("Hearst");
            userToCreate.setAddress("Spain, Madrid");
            userToCreate
                    .setBirthDate(
                            new Date(
                                    new GregorianCalendar(
                                            1988, Calendar.MAY, 12
                                    ).getTime().getTime()));
            userToCreate.setEmail("erich@");
            userToCreate.setPassword("erich");
            userToCreate.setPhoneNumber("2254-23-658");

            userToCreate.setUsername("erich");
            Role[] roles = roleService.getAllUserRoles(URL_USERS, restTemplate);
            log.info("Roles: " + Arrays.toString(roles));
            userToCreate.addRole(roles[1]);

            userService.createUser(URL_USERS, restTemplate, userToCreate);
            users = userService.getAllUsers(URL_USERS, restTemplate);
            log.info("Users: " + users.length);
            log.info("All users after adding new user: " + printUsers(users));

            /*  Update user  */
            log.info("4. Update user");
            User editedUser = userService.getUser(URL_USERS, restTemplate, 4L);
            editedUser.setFirstName("Edvard");
            editedUser.setLastName("Brooks");
            editedUser.addRole(roles[0]);
            userService.updateUser(URL_USERS, restTemplate, editedUser);
            log.info("Edited user (id 4): " + userService.getUser(URL_USERS, restTemplate, 4L).toString());

            /*  Delete user  */
            log.info("5. Delete user");
            users = userService.getAllUsers(URL_USERS, restTemplate);
            log.info("Users: " + users.length);
            log.info("All users before deleting user: " + printUsers(users));
            User userToDelete = userService.getUser(URL_USERS, restTemplate, 3L);
            userService.deleteUser(URL_USERS, restTemplate, userToDelete.getId());
            users = userService.getAllUsers(URL_USERS, restTemplate);
            log.info("Users: " + users.length);
            log.info("All users after deleting user: " + printUsers(users));
        };
    }

    private String printUsers(User[] users) {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(System.lineSeparator());
            sb.append(user.toString());
        }
        return sb.toString();
    }

}
