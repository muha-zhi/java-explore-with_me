package ru.practicum.mainservice.dao.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.category.Category;

@Repository
public interface AdminCategoryRepository extends JpaRepository<Category, Long> {
}
