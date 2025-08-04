package com.cooper_filme.auth_service.util;

import com.cooper_filme.shared_model.model.entity.Role;
import com.cooper_filme.shared_model.model.entity.User;
import com.cooper_filme.shared_model.repository.RoleRepository;
import com.cooper_filme.shared_model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            List<Role> roles = getInitialRoles();
            roleRepository.saveAll(roles);
        }

        if (userRepository.count() == 0) {
            List<User> users = getInitialUsers();
            userRepository.saveAll(users);
        }
    }

    private List<Role> getInitialRoles() {
        Role analystRole = new Role();
        analystRole.setName("ANALYST");

        Role proofReaderRole = new Role();
        proofReaderRole.setName("PROOFREADER");

        Role approverRole = new Role();
        approverRole.setName("APPROVER");

        List<Role> roles = new LinkedList<>();
        roles.add(analystRole);
        roles.add(proofReaderRole);
        roles.add(approverRole);

        return roles;
    }

    private List<User> getInitialUsers() {
        List<User> users = new LinkedList<>();

        Role analystRole = roleRepository.findByName("ANALYST").orElse(null);
        Role proofReaderRole = roleRepository.findByName("PROOFREADER").orElse(null);
        Role approverRole = roleRepository.findByName("APPROVER").orElse(null);

        if (analystRole != null) {
            User analyst = new User();
            analyst.setName("jorge");
            analyst.setEmail("jorge@fakemail.com");
            analyst.setPassword(passwordEncoder.encode("jorge123"));

            Set<Role> analystRoles = new HashSet<>();
            analystRoles.add(analystRole);
            analyst.setRoles(analystRoles);

            users.add(analyst);
        }

        if (proofReaderRole != null) {
            User proofReader = new User();
            proofReader.setName("amanda");
            proofReader.setEmail("amanda@fakemail.com");
            proofReader.setPassword(passwordEncoder.encode("amanda123"));

            Set<Role> proofReaderRoles = new HashSet<>();
            proofReaderRoles.add(proofReaderRole);
            proofReader.setRoles(proofReaderRoles);

            users.add(proofReader);
        }

        if (approverRole != null) {
            User approver1 = new User();
            approver1.setName("sabrina");
            approver1.setEmail("sabrina@fakemail.com");
            approver1.setPassword(passwordEncoder.encode("sabrina123"));

            Set<Role> approver1Roles = new HashSet<>();
            approver1Roles.add(approverRole);
            approver1.setRoles(approver1Roles);

            User approver2 = new User();
            approver2.setName("joao");
            approver2.setEmail("joao@fakemail.com");
            approver2.setPassword(passwordEncoder.encode("joao123"));

            Set<Role> approver2Roles = new HashSet<>();
            approver2Roles.add(approverRole);
            approver2.setRoles(approver2Roles);

            User approver3 = new User();
            approver3.setName("mirian");
            approver3.setEmail("mirian@fakemail.com");
            approver3.setPassword(passwordEncoder.encode("mirian123"));

            Set<Role> approver3Roles = new HashSet<>();
            approver3Roles.add(approverRole);
            approver3.setRoles(approver3Roles);

            users.add(approver1);
            users.add(approver2);
            users.add(approver3);
        }

        return users;

    }
}
