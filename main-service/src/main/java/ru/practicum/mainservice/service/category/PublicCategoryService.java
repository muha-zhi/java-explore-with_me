package ru.practicum.mainservice.service.category;

import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.models.category.Category;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);

    Category getCategoryIfExist(Long catId);
}
