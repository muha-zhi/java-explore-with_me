package ru.practicum.mainservice.dao.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.users.User;

import java.util.List;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u " +
            " FROM User u " +
            " WHERE (:ids IS NULL OR u.id IN :ids) " +
            " ORDER BY u.id")
    List<User> getAllUsersBIds(List<Long> ids, PageRequest pageRequest);
}
