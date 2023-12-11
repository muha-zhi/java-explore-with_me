package ru.practicum.mainservice.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be blank")
    @Size(min = 3, max = 50, message = "name must be between 3 and 10 characters")
    private String name;
}
