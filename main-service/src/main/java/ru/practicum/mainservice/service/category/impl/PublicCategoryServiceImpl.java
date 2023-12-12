package ru.practicum.mainservice.service.category.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.category.CategoryRepository;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.category.CategoryMapper;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.service.category.PublicCategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {

        List<Category> forRet = categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();

        log.info("get all categories");

        return forRet.stream()
                .map(CategoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {

        log.info("get category by id {}", catId);

        return CategoryMapper.categoryToCategoryDto(getCategoryIfExist(catId));
    }


    @Override
    public Category getCategoryIfExist(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new DataNotFoundException("Category not found" + catId));
    }
}
