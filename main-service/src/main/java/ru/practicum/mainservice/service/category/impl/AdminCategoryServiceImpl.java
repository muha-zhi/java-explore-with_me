package ru.practicum.mainservice.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dao.category.AdminCategoryRepository;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.mapper.category.CategoryMapper;
import ru.practicum.mainservice.models.category.Category;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final AdminCategoryRepository adminCategoryRepository;


    @Override
    public CategoryDto saveNewCategory(NewCategoryDto categoryDto) {
        log.info("save category {}", categoryDto);
        Category savedCategory = adminCategoryRepository.save(CategoryMapper.newCategoryToCategory(categoryDto));
        return CategoryMapper.categoryToCategoryDto(savedCategory);
    }

    @Override
    public void deleteCategoryById(Long catId) {
        log.info("delete category {}", catId);
        adminCategoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(Long catId, NewCategoryDto categoryDto) {
        Optional<Category> patchedCategory = adminCategoryRepository.findById(catId);
        if (patchedCategory.isPresent()) {
            Category category = patchedCategory.get();
            category.setName(categoryDto.getName());
            return CategoryMapper.categoryToCategoryDto(adminCategoryRepository.save(category));
        }
        return null;
    }
}
