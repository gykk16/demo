package dev.glory.demo.docs.common.codes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.glory.demo.common.code.ErrorCode;
import dev.glory.demo.common.code.ResponseCode;
import dev.glory.demo.common.code.SuccessCode;
import dev.glory.demo.common.response.ApiResponseEntity;
import dev.glory.demo.domain.base.BaseCode;
import dev.glory.demo.domain.user.role.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumCodeController {

    @GetMapping("/docs/enums-response-code")
    public ResponseEntity<ApiResponseEntity<ResponseEnumDocs>> responseCodes() {

        Map<String, List<String>> successCode = getResponseEnumDocs(SuccessCode.values());
        Map<String, List<String>> errorCode = getResponseEnumDocs(ErrorCode.values());

        return ApiResponseEntity.of(SuccessCode.SUCCESS,
                ResponseEnumDocs.builder()
                        .successCode(successCode)
                        .errorCode(errorCode)
                        .build()
        );
    }

    @GetMapping("/docs/enums-codes")
    public ResponseEntity<ApiResponseEntity<EnumDocs>> statusCodes() {

        Map<String, List<String>> roles = getStatusEnumDocs(Role.values());

        return ApiResponseEntity.of(SuccessCode.SUCCESS,
                EnumDocs.builder()
                        .roles(roles)
                        .build()
        );
    }

    private Map<String, List<String>> getResponseEnumDocs(ResponseCode[] enumCode) {
        return Arrays.stream(enumCode)
                .collect(Collectors.toMap(ResponseCode::getCode,
                        code -> List.of(String.valueOf(code.getStatusCode()), code.getMessage()),
                        (a, b) -> b, LinkedHashMap::new)
                );
    }

    private Map<String, List<String>> getStatusEnumDocs(BaseCode[] enumCode) {
        return Arrays.stream(enumCode)
                .collect(Collectors.toMap(BaseCode::toString,
                        code -> List.of(code.getDescription()),
                        (a, b) -> b, LinkedHashMap::new)
                );
    }

}
