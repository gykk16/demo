package dev.glory.demo.docs.common.response;

import static dev.glory.demo.common.constant.WebAppConst.REQUEST_ID_HEADER;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.glory.demo.docs.RestDocsSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class ApiResponseEntityDocsTest extends RestDocsSupport {

    @Override
    protected Object initController() {
        return new ApiResponseEntityController();
    }

    @Test
    void commonResponseFormat() throws Exception {

        mockMvc.perform(get("/docs/response")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseHeaders(headerWithName(REQUEST_ID_HEADER).description("요청 추적 id")),
                        responseFields(responseCommonFormat())
                                .and(fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("데이터")))
                );
    }

    @Test
    void commonPageFormat() throws Exception {

        mockMvc.perform(get("/docs/response/page")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                                responseFields(responseCommonFormat())
                                        .and(fieldWithPath("data.content[]")
                                                .type(JsonFieldType.ARRAY).optional().description("데이터"))
                                        .andWithPrefix("data.pageInfo.", pageCommonFormat()),
                                //
                                responseFields(beneathPath("data.pageInfo")
                                        .withSubsectionId("data.page"), pageCommonFormat()
                                )
                        )
                );
    }

}
