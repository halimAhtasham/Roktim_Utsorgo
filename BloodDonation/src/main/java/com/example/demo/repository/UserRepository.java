package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    List<User> findByBloodGroupContainingIgnoreCase(String bloodGroup);
    List<User> findByCityContainingIgnoreCase(String city);
    List<User> findByBloodGroupContainingIgnoreCaseAndCityContainingIgnoreCase(String bloodGroup, String city);

}