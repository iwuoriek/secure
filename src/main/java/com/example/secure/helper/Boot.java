package com.example.secure.helper;

import com.example.secure.auth.Role;
import com.example.secure.dto.UserDto;
import com.example.secure.repository.RoleRepository;
import com.example.secure.service.AppService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Boot {
    @Bean
    public CommandLineRunner lineRunner(AppService service, RoleRepository repository){
        return args -> {
            Role role = new Role();
            role.setId(1);
            role.setRoleName(RoleType.ADMIN.name());
            role.setDescription("Has access to only admin stuff");
            repository.save(role);
            Role role1 = new Role();
            role1.setId(2);
            role1.setRoleName(RoleType.CUSTOMER.name());
            role1.setDescription("Has access to mostly customer stuff");
            repository.save(role1);

            UserDto dto1 = new UserDto();
            dto1.setFirstName("Mike");
            dto1.setLastName("Shawn");
            dto1.setEmail("mikeshaw@mail.com");
            dto1.setAddress("71 University Drive");
            dto1.setUsername("mikeshaw");
            dto1.setPassword("password1234");
            dto1.setRoles("customer");

            UserDto dto2 = new UserDto();
            dto2.setFirstName("Tony");
            dto2.setLastName("Hawk");
            dto2.setEmail("tonyhawk@mail.com");
            dto2.setAddress("57 University Drive");
            dto2.setUsername("tonyhawk");
            dto2.setPassword("password1234");
            dto2.setRoles("customer");

            UserDto dto3 = new UserDto();
            dto3.setFirstName("Mel");
            dto3.setLastName("Carter");
            dto3.setEmail("melcarter@mail.com");
            dto3.setAddress("28 University Drive");
            dto3.setUsername("melcarter");
            dto3.setPassword("password1234");
            dto3.setRoles("admin");

            service.registerUser(dto1);
            service.registerUser(dto2);
            service.registerUser(dto3);
        };
    }
}
