package com.social.app.repositories;

import com.social.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.groups g " +
            "WHERE u.id = :userId AND g.id = :groupId")
    boolean existsUserInGroup(@Param("groupId") Long groupId, @Param("userId") Long userId);

}
