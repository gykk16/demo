package dev.glory.demo.docs;

import static dev.glory.demo.docs.DocumentFormatGenerator.getDateTimeFormat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.operation.preprocess.UriModifyingOperationPreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * RestDocs 지원 클래스
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected static final String BEARER_TOKEN = "Bearer token-here-------";

    protected static ObjectMapper                   objectMapper = new ObjectMapper();
    protected        RestDocumentationResultHandler restDocs     = write();
    protected        MockMvc                        mockMvc;

    protected abstract Object initController();

    /**
     * paging 응답 기본 spec
     */
    protected List<FieldDescriptor> pageCommonFormat() {
        return List.of(
                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호 (0부터 시작)"),
                fieldWithPath("pageElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 수"),
                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지 여부"),
                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("데이터 존재 여부")
        );
    }

    /**
     * paging 요청 기본 spec
     */
    protected List<ParameterDescriptor> pageRequestFormat() {
        return List.of(
                parameterWithName("page").optional().description("요청 페이지 (시작번호: `0`, 기본: `0`)"),
                parameterWithName("size").optional().description("페이지의 데이터 수")
        );
    }

    private RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(initController())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .addFilters(new CharacterEncodingFilter(UTF_8.name(), true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider)
                        .uris().withScheme("http").withHost("localhost").withPort(8080))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    /**
     * api 응답 기본 spec
     */
    protected static List<FieldDescriptor> responseCommonFormat() {
        return List.of(fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                fieldWithPath("responseDt").type(JsonFieldType.STRING)
                        .attributes(getDateTimeFormat())
                        .description("응답 일시"));
    }

    private static UriModifyingOperationPreprocessor getHost() {
        return Preprocessors.modifyUris()
                .scheme("https")
                .host("");
    }

}
