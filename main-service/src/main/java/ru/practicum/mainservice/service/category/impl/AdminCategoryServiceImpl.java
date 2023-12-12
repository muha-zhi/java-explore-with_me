package ru.practicum.mainservice.service.category.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.category.CategoryRepository;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.mapper.category.CategoryMapper;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.service.category.AdminCategoryService;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional
    @Override
    public CategoryDto saveNewCategory(NewCategoryDto categoryDto) {
        log.info("save category {}", categoryDto);
        Category savedCategory = categoryRepository.save(CategoryMapper.newCategoryToCategory(categoryDto));
        return CategoryMapper.categoryToCategoryDto(savedCategory);
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long catId) {
        log.info("delete category {}", catId);
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto patchCategory(Long catId, NewCategoryDto categoryDto) {
        Optional<Category> patchedCategory = categoryRepository.findById(catId);
        if (patchedCategory.isPresent()) {
            Category category = patchedCategory.get();
            category.setName(categoryDto.getName());
            return CategoryMapper.categoryToCategoryDto(categoryRepository.save(category));
        }
        return null;
    }
}
