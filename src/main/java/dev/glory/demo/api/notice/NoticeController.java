package dev.glory.demo.api.notice;

import static dev.glory.demo.common.code.SuccessCode.CREATED;
import static dev.glory.demo.common.code.SuccessCode.SUCCESS;

import java.io.File;
import java.security.Principal;

import lombok.RequiredArgsConstructor;

import dev.glory.demo.api.notice.dto.NoticeSaveRequest;
import dev.glory.demo.api.notice.dto.NoticeUpdateRequest;
import dev.glory.demo.common.response.ApiResponseEntity;
import dev.glory.demo.common.response.PageResObj;
import dev.glory.demo.domain.file.FileService;
import dev.glory.demo.domain.notice.query.NoticeTitleDto;
import dev.glory.demo.domain.notice.service.NoticeService;
import dev.glory.demo.domain.notice.service.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 공지 api
 */
@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final FileService   fileService;

    /**
     * 공지 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponseEntity<PageResObj<NoticeTitleDto>>> findList(@PageableDefault Pageable pageable) {

        Page<NoticeTitleDto> noticeList = noticeService.findNoticeList(pageable);
        var pageResObj = new PageResObj<>(noticeList);
        return ApiResponseEntity.of(SUCCESS, pageResObj);
    }

    /**
     * 공지 조회
     *
     * @param id 공지 noticeId
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseEntity<NoticeDto>> findById(@PathVariable("id") Long id) {

        NoticeDto noticeDto = noticeService.findContent(id);
        return ApiResponseEntity.of(SUCCESS, noticeDto);
    }

    /**
     * 공지 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponseEntity<Long>> save(@Validated @ModelAttribute NoticeSaveRequest requestDTO) {

        MultipartFile[] files = requestDTO.file();
        fileService.isValidExtension(files);

        Long noticeId = noticeService.save(requestDTO.toServiceRequest());

        /* todo: 임의 파일 저장 로직, 파일 저장명과 저장경로는 properties 통해 동적으로 전달 */
        String filename = "notice-file-" + noticeId;
        String savePath = System.getProperty("user.home") + File.separator + "Desktop";
        String[] savedFiles = fileService.saveFile(filename, savePath, files);

        return ApiResponseEntity.of(CREATED, noticeId);
    }

    /**
     * 공지 수정
     */
    @PatchMapping("{id}")
    public ResponseEntity<ApiResponseEntity<NoticeDto>> patch(@PathVariable("id") Long id,
            @Validated @RequestBody NoticeUpdateRequest requestDTO,
            Principal principal) {

        NoticeDto noticeDto = noticeService.patch(id, requestDTO.toServiceRequest(), principal.getName());
        return ApiResponseEntity.of(SUCCESS, noticeDto);
    }

    /**
     * 공지 삭제
     */
    @PostMapping("/{id}/delete")
    public ResponseEntity<ApiResponseEntity<Long>> deletePost(@PathVariable("id") Long id, Principal principal) {

        noticeService.delete(id, principal.getName());
        return ApiResponseEntity.of(SUCCESS, id);
    }

    /**
     * 공지 삭제
     */
    // @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseEntity<Long>> delete(@PathVariable("id") Long id, Principal principal) {

        noticeService.delete(id, principal.getName());
        return ApiResponseEntity.of(SUCCESS, id);
    }

}
