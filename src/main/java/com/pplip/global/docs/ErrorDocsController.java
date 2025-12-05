package com.pplip.global.docs;

import com.pplip.global.api.code.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
@Tag(name = "Error Code 조회", description = "오류코드 조회")

public class ErrorDocsController {

    @GetMapping("/error")
    public List<ErrorResponse> getErrorList(){
        return Arrays.stream(ErrorCode.values()).map(errorCode ->
                new ErrorResponse(errorCode.status(), errorCode.name(), errorCode.getDefaultMessage())
                ).toList();
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse{
        private int code;
        private String name;
        private String desc;
    }
}
