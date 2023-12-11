package ru.practicum.mainservice.controllers.category;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.category.NewCategoryDto;
import ru.practicum.mainservice.service.category.AdminCategoryService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;


    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@RequestBody @Validated NewCategoryDto categoryDto) {
        log.info("POST CATEGORY: {}", categoryDto);
        return adminCategoryService.saveNewCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        adminCategoryService.deleteCategoryById(catId);
        log.info("DELETE CATEGORY: {}", catId);

    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId, @RequestBody @Validated NewCategoryDto categoryDto) {
        log.info("PATCH CATEGORY: {}", categoryDto);
        return adminCategoryService.patchCategory(catId, categoryDto);
    }


}
