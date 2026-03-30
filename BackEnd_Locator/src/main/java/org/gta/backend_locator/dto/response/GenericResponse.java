package org.gta.backend_locator.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private List<T> dataList;

}
