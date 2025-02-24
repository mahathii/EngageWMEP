package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByName_Found() {
        // Arrange: create a Role entity and persist it
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        role = entityManager.persistAndFlush(role);

        // Act: search for the role by name
        Optional<Role> foundRole = roleRepository.findByName("ROLE_ADMIN");

        // Assert: the role should be found with the correct name
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void testFindByName_NotFound() {
        // Act: attempt to find a role that doesn't exist
        Optional<Role> foundRole = roleRepository.findByName("ROLE_NON_EXISTENT");

        // Assert: the result should be empty
        assertThat(foundRole).isNotPresent();
    }
}
