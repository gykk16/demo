package dev.glory.demo.docs.api.notice;

import static dev.glory.demo.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import dev.glory.demo.api.notice.NoticeController;
import dev.glory.demo.api.notice.dto.NoticeUpdateRequest;
import dev.glory.demo.docs.RestDocsSupport;
import dev.glory.demo.domain.file.FileService;
import dev.glory.demo.domain.notice.query.NoticeTitleDto;
import dev.glory.demo.domain.notice.service.NoticeService;
import dev.glory.demo.domain.notice.service.dto.NoticeDto;
import dev.glory.demo.domain.notice.service.dto.NoticeServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class NoticeControllerDocsTest extends RestDocsSupport {

    private final NoticeService noticeService = mock(NoticeService.class);
    private final FileService   fileService   = mock(FileService.class);

    @Override
    protected Object initController() {
        return new NoticeController(noticeService, fileService);
    }

    @DisplayName("공지 등록")
    @Test
    void save() throws Exception {
        // given
        final long SAVED_ID = 101L;
        when(noticeService.save(any(NoticeServiceRequest.class))).thenReturn(SAVED_ID);
        when(fileService.isValidExtension(any())).thenReturn(true);

        String csvContent = """
                1,q,w,e,r,t,y,2024
                2,a,s,d,f,g,h,2024
                """;
        MockMultipartFile file = new MockMultipartFile("file", "csv-test.csv", "text/csv", csvContent.getBytes());

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("title", "공지사항 등록 방법");
        paramMap.add("content", "공지사항 등록 방법에 대한 안내입니다.");
        paramMap.add("beginDt", null);
        paramMap.add("endDt", null);

        // when
        mockMvc.perform(multipart("/api/v1/notice").file(file)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .params(paramMap))
                // then
                .andExpect(status().isCreated())
                .andDo(restDocs.document(requestParts(partWithName("file").description("파일")),
                        formParameters(requestSaveParams()), responseFields(responseCommonFormat()).and(
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("공지 id"))));
    }

    @DisplayName("공지 조회")
    @Test
    void findById() throws Exception {
        // given
        final long SAVED_ID = 101L;
        NoticeDto noticeDto = new NoticeDto(SAVED_ID, "공지사항 등록 방법", "공지사항 등록 방법에 대한 안내입니다.", 20, "kildong",
                LocalDateTime.now().minusHours(2));

        when(noticeService.findContent(anyLong())).thenReturn(noticeDto);

        // when
        mockMvc.perform(get("/api/v1/notice/{noticeId}", SAVED_ID).header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andDo(restDocs.document(pathParameters(parameterWithName("noticeId").description("공지 id")),
                        responseFields(responseCommonFormat()).andWithPrefix("data.", responseFindByIdParams()),
                        //
                        responseFields(beneathPath("data").withSubsectionId("data"), responseFindByIdParams())));
    }

    @DisplayName("공지 목록 조회")
    @Test
    void findList() throws Exception {
        // given
        NoticeTitleDto noticeTitleDto = new NoticeTitleDto(101L, "공지사항 등록 방법", 20, "kildong",
                LocalDateTime.now().minusHours(2));
        PageImpl<NoticeTitleDto> dtoPage = new PageImpl<>(List.of(noticeTitleDto), Pageable.ofSize(10), 1);

        when(noticeService.findNoticeList(any(Pageable.class))).thenReturn(dtoPage);

        // when
        mockMvc.perform(get("/api/v1/notice").header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                // then
                .andExpect(status().isOk())
                .andDo(restDocs.document(queryParameters(pageRequestFormat()),
                        responseFields(responseCommonFormat()).andWithPrefix("data.content[].",
                                responseFindListParams()).andWithPrefix("data.pageInfo.", pageCommonFormat()),
                        //
                        responseFields(beneathPath("data.content[]").withSubsectionId("data.content"),
                                responseFindListParams()),
                        //
                        responseFields(beneathPath("data.pageInfo").withSubsectionId("data.page"),
                                pageCommonFormat())));
    }

    @WithMockUser(username = "test")
    @DisplayName("공지 목록 수정")
    @Test
    void patch() throws Exception {
        // given
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("kildong",
                "");

        final long SAVED_ID = 101L;
        String titleN = "공지사항 수정 방법";
        String contentN = "공지사항 수정 방법에 대한 안내입니다.";
        NoticeDto noticeDto = new NoticeDto(SAVED_ID, titleN, contentN, 20, "kildong",
                LocalDateTime.now().minusHours(2));

        when(noticeService.patch(anyLong(), any(NoticeServiceRequest.class), anyString())).thenReturn(noticeDto);

        NoticeUpdateRequest request = new NoticeUpdateRequest(titleN, contentN, null, null);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/notice/{noticeId}", SAVED_ID)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(authenticationToken))
                // then
                .andExpect(status().isOk())
                .andDo(restDocs.document(pathParameters(parameterWithName("noticeId").description("공지 id")),
                        requestFields(requestPatchParams()),
                        responseFields(responseCommonFormat()).andWithPrefix("data.", responseFindByIdParams()),
                        //
                        responseFields(beneathPath("data").withSubsectionId("data"), responseFindByIdParams())));
    }

    @DisplayName("delete")
    @Test
    void delete() throws Exception {
        // given
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("kildong",
                "");

        final long SAVED_ID = 101L;

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/notice/{noticeId}/delete", SAVED_ID)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(authenticationToken))
                // then
                .andExpect(status().isOk())
                .andDo(restDocs.document(pathParameters(parameterWithName("noticeId").description("공지 id")),
                        responseFields(responseCommonFormat()).and(
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("삭제 된 공지 id"))));
    }

    private static List<ParameterDescriptor> requestSaveParams() {
        return List.of(parameterWithName("title").description("제목").optional(),
                parameterWithName("content").description("내용").optional(),
                parameterWithName("beginDt").description("노출 시작일시").attributes(getDateTimeFormat()).optional(),
                parameterWithName("endDt").description("노출 종료일시").attributes(getDateTimeFormat()).optional());
    }

    private static List<FieldDescriptor> requestPatchParams() {
        return List.of(fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional(),
                fieldWithPath("content").type(JsonFieldType.STRING).description("내용").optional(),
                fieldWithPath("beginDt").type(JsonFieldType.STRING)
                        .attributes(getDateTimeFormat())
                        .description("등록일지")
                        .optional(), fieldWithPath("endDt").type(JsonFieldType.STRING)
                        .attributes(getDateTimeFormat())
                        .description("등록일지")
                        .optional());
    }

    private static List<FieldDescriptor> responseFindByIdParams() {
        return List.of(fieldWithPath("noticeId").type(JsonFieldType.NUMBER).description("공지 id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                fieldWithPath("hit").type(JsonFieldType.NUMBER).description("조회 수"),
                fieldWithPath("regId").type(JsonFieldType.STRING).description("등록자"),
                fieldWithPath("regDt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("등록일지"));
    }

    private static List<FieldDescriptor> responseFindListParams() {
        return List.of(fieldWithPath("noticeId").type(JsonFieldType.NUMBER).description("공지 id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                fieldWithPath("hit").type(JsonFieldType.NUMBER).description("조회 수"),
                fieldWithPath("regId").type(JsonFieldType.STRING).description("등록자"),
                fieldWithPath("regDt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("등록일지"));
    }

}