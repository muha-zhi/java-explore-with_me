package ru.practicum.mainservice.controllers.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.service.category.PublicCategoryService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryController {

    private final PublicCategoryService publicCategoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                              @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("GET from: {}, size: {}", from, size);
        return publicCategoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryInfo(@PathVariable Long catId) {
        log.info("GET catId: {}", catId);
        return publicCategoryService.getCategoryById(catId);
    }


}
