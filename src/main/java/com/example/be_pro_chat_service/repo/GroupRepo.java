package com.example.be_pro_chat_service.repo;

import com.example.be_pro_chat_service.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group, Long> {
    List<Group> findByUsersContaining(String username);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.users WHERE g.id = :id")
    Optional<Group> findByIdWithUsers(@Param("id") Long id);

}
