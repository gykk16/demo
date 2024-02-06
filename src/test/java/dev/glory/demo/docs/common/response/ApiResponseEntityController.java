package dev.glory.demo.docs.common.response;

import static dev.glory.demo.common.code.SuccessCode.SUCCESS;
import static dev.glory.demo.common.constant.WebAppConst.REQUEST_ID_HEADER;

import jakarta.servlet.http.HttpServletResponse;

import dev.glory.demo.common.response.ApiResponseEntity;
import dev.glory.demo.common.response.PageResObj;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiResponseEntityController {

    @GetMapping("/docs/response")
    public ResponseEntity<ApiResponseEntity<Object>> response(HttpServletResponse response) {
        response.setHeader(REQUEST_ID_HEADER, "1173504442884562944");
        return ApiResponseEntity.ofSuccess();
    }

    @GetMapping("/docs/response/page")
    public ResponseEntity<ApiResponseEntity<PageResObj<Object>>> responsePage() {

        var pageResObj = new PageResObj<>(Page.empty());
        return ApiResponseEntity.of(SUCCESS, pageResObj);
    }

}
