package dev.glory.demo.docs.common.codes;

import static dev.glory.demo.docs.CustomResponseFieldsSnippet.customResponseFields;
import static dev.glory.demo.docs.CustomResponseFieldsSnippet.responseEnumConvertFieldDescriptor;
import static dev.glory.demo.docs.CustomResponseFieldsSnippet.statusEnumConvertFieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.glory.demo.common.response.ApiResponseEntity;
import dev.glory.demo.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class EnumCodeDocsTest extends RestDocsSupport {

    @Override
    protected Object initController() {
        return new EnumCodeController();
    }

    private EnumDocs getEnumsData(MvcResult result) throws IOException {
        ApiResponseEntity<EnumDocs> apiResponseEntity = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                });

        return apiResponseEntity.getData();
    }

    private ResponseEnumDocs getResponseEnumsData(MvcResult result) throws IOException {
        ApiResponseEntity<ResponseEnumDocs> apiResponseEntity = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                });

        return apiResponseEntity.getData();
    }

    @DisplayName("공통 응답 코드")
    @Test
    void responseCode() throws Exception {
        // given
        ResultActions result = mockMvc.perform(get("/docs/enums-response-code")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        ResponseEnumDocs responseEnums = getResponseEnumsData(mvcResult);

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        customResponseFields("common-response-code",
                                beneathPath("data.successCode").withSubsectionId("success"),
                                attributes(key("code").value("description")),
                                responseEnumConvertFieldDescriptor(responseEnums.getSuccessCode())
                        ),
                        customResponseFields("common-response-code",
                                beneathPath("data.errorCode").withSubsectionId("error"),
                                attributes(key("code").value("description")),
                                responseEnumConvertFieldDescriptor(responseEnums.getErrorCode())
                        )
                ));
    }

    @DisplayName("Enum 정의 코드")
    @Test
    void enumCodes() throws Exception {
        // given
        ResultActions result = mockMvc.perform(get("/docs/enums-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        EnumDocs enumDocs = getEnumsData(mvcResult);

        final String SNIPPET_NAME = "common-enum-code";
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        customResponseFields(SNIPPET_NAME,
                                beneathPath("data.roles").withSubsectionId("roles"),
                                attributes(key("code").value("description")),
                                statusEnumConvertFieldDescriptor(enumDocs.getRoles())
                        )
                ));
    }

}
