package com.project.web_prg.board.common.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Log4j2
public class UploadController {
    //
    @GetMapping("/upload-form")
    public String uploadForm(){
        return "upload/upload-form";
    }

    // 파일 업로드 처리를 위한 요청
    // MultipartFile : 클라이언트가 전송한 파일 정보들을 담은 객체
    // ex) 원본 파일명,파일용량, 파일컨텐츠타입
    @PostMapping("/upload")
    public String upload(MultipartFile file){
        log.info("/upload POST - {}", file);
        log.info("file-name : {}", file.getName());
        log.info("file-ori-name : {}", file.getOriginalFilename());
        log.info("file-size : {}", (double) file.getSize() / 1024);
        log.info("file-type : {}", file.getContentType());
        
        // 서버에 업로드 파일 저장
        
        // 업로드 파일 저장경로 -> window는 파일 경로 \ 역 슬래시 리눅스는 / 이거
        String uploadPath = "C:\\sl_test\\upload";
        
        // 1. 세이브파일 객체 생성
        // 첫번째 파라미터는 파일저장 경로 지정, 두번째는 파일 명 지정
        File f = new File(uploadPath, file.getOriginalFilename());
        // 파일명이 동일한건 덮어씌워짐 -> 올릴때 서버에서 파일명을 바꿔줌
        // 파일 형식의 제한?
        // 디비 처리 등등 단순 업로드 이후에도 할거 많음

        try {
            file.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/upload-form";
    }
}
