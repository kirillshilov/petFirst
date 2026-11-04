package org.example.petFirst.repository;

import org.example.petFirst.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    User findUserByLogin(String login);
}
