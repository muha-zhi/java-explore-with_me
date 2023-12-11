package ru.practicum.mainservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.enums.PatchRequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequestDto {
    private List<Long> requestIds;
    private PatchRequestStatus status;
}
