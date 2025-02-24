package com.engagewmep.backend.repository;

import com.engagewmep.backend.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByStudentId_Found() {
        // Arrange: Create and persist a Student entity with a specific studentId
        Student student = new Student();
        student.setStudentId("S001");
        // Persist the student entity into the in-memory database
        student = entityManager.persistAndFlush(student);

        // Act: Retrieve the student by its studentId
        Optional<Student> foundStudent = studentRepository.findByStudentId("S001");

        // Assert: Verify that the student is found and the studentId matches
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getStudentId()).isEqualTo("S001");
    }

    @Test
    public void testFindByStudentId_NotFound() {
        // Act: Attempt to find a student with a studentId that does not exist
        Optional<Student> foundStudent = studentRepository.findByStudentId("NON_EXISTENT");

        // Assert: Verify that no student is found
        assertThat(foundStudent).isNotPresent();
    }
}
