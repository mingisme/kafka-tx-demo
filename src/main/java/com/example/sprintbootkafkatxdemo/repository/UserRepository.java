package com.example.sprintbootkafkatxdemo.repository;

import com.example.sprintbootkafkatxdemo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
