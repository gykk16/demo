package dev.glory.demo.docs.api.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import dev.glory.demo.api.auth.AuthController;
import dev.glory.demo.api.auth.dto.AuthRequest;
import dev.glory.demo.api.auth.dto.RefreshTokenRequest;
import dev.glory.demo.api.auth.dto.RegisterRequest;
import dev.glory.demo.docs.RestDocsSupport;
import dev.glory.demo.domain.auth.service.AuthService;
import dev.glory.demo.domain.auth.service.dto.AuthServiceRequest;
import dev.glory.demo.domain.auth.service.dto.AuthServiceResponse;
import dev.glory.demo.domain.auth.service.dto.RegisterServiceRequest;
import dev.glory.demo.domain.user.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

class AuthControllerDocsTest extends RestDocsSupport {

    private final AuthService authService = mock(AuthService.class);

    @Override
    protected Object initController() {
        return new AuthController(authService);
    }

    @DisplayName("사용자 등록")
    @Test
    void register() throws Exception {
        // given
        when(authService.register(any(RegisterServiceRequest.class)))
                .thenReturn(new AuthServiceResponse("access-token", "refresh-token"));

        RegisterRequest requestBody = new RegisterRequest(
                "kildong", "pass135!", "김길동", "kildong@test.com", Role.MANAGER);

        // when
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                )
                // then
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                                requestFields(requestRegisterParams()),
                                responseFields(responseCommonFormat())
                                        .andWithPrefix("data.", responseRegisterParams()),
                                //
                                responseFields(beneathPath("data")
                                        .withSubsectionId("data"), responseRegisterParams())
                        )
                );
    }

    @DisplayName("사용자 인증")
    @Test
    void authenticate() throws Exception {
        // given
        when(authService.authenticate(any(AuthServiceRequest.class)))
                .thenReturn(new AuthServiceResponse("access-token", "refresh-token"));

        AuthRequest requestBody = new AuthRequest("kildong", "pass135!");

        // when
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                )
                // then
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                                requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 id"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(responseCommonFormat())
                                        .andWithPrefix("data.", responseRegisterParams()),
                                //
                                responseFields(beneathPath("data")
                                        .withSubsectionId("data"), responseRegisterParams())
                        )
                );
    }

    @DisplayName("토큰 재발급")
    @Test
    void refreshToken() throws Exception {
        // given
        when(authService.validateRefreshToken(anyString()))
                .thenReturn(new AuthServiceResponse("access-token", "refresh-token"));

        RefreshTokenRequest requestBody = new RefreshTokenRequest("refresh-token");

        // when
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                )
                // then
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                                requestFields(
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh 토큰")
                                ),
                                responseFields(responseCommonFormat())
                                        .andWithPrefix("data.", responseRegisterParams()),
                                //
                                responseFields(beneathPath("data")
                                        .withSubsectionId("data"), responseRegisterParams())
                        )
                );
    }

    private static List<FieldDescriptor> requestRegisterParams() {
        return List.of(
                fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 id"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("email").type(JsonFieldType.STRING).description(""),
                fieldWithPath("role").type(JsonFieldType.STRING).description("권한 Enum")
        );
    }

    private static List<FieldDescriptor> responseRegisterParams() {
        return List.of(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh 토큰")
        );
    }

}