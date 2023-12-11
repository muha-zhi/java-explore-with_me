package ru.practicum.mainservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedRejectedRequests {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
