package ru.practicum.mainservice.service.category;

import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto saveNewCategory(NewCategoryDto categoryDto);

    void deleteCategoryById(Long catId);

    CategoryDto patchCategory(Long catId, NewCategoryDto categoryDto);
}
