package com.project.web_prg.board.common.controller;

import com.project.web_prg.util.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
public class UploadController {
    private static final String UPLOADPATH = "C:\\sl_test\\upload";
    // 업로드 파일 저장 경로

    //
    @GetMapping("/upload-form")
    public String uploadForm(){
        return "upload/upload-form";
    }

    // 파일 업로드 처리를 위한 요청
    // MultipartFile: 클라이언트가 전송한 파일 정보들을 담은 객체
    // ex) 원본 파일명, 파일 용량, 파일 컨텐츠타입...
    @PostMapping("/upload")
    public String upload(@RequestParam("file") List<MultipartFile> fileList) {
        log.info("/upload POST! - {}", fileList);

        for (MultipartFile file: fileList) {
            log.info("file-name: {}", file.getName());
            log.info("file-origin-name: {}", file.getOriginalFilename());
            log.info("file-size: {}KB", (double) file.getSize() / 1024);
            log.info("file-type: {}", file.getContentType());
            System.out.println("==================================================================");


            // 서버에 업로드파일 저장



            // 1. 세이브파일 객체 생성
            //  - 첫번째 파라미터는 파일 저장경로 지정, 두번째 파일명지정
            /*
            File f = new File(uploadPath, file.getOriginalFilename());

            try {
                file.transferTo(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            FileUtils.uploadFile(file, UPLOADPATH);
        }

        return "redirect:/upload-form";
    }

    // 비동기요청 파일 업로드처리
    @PostMapping("/ajax-upload")
    @ResponseBody
    public List<String> ajaxUpload(List<MultipartFile> files){
        log.info("/ajax-upload POST - {}", files.get(0).getOriginalFilename());
        //form data는 파라미터 받는것처럼 받으면 된다
        // Q > origin file 이름 받아서 자르는 이유??

        // 클라이언트에게 전송할 파일 경로 리스트
        List<String> fileNames = new ArrayList<>();

        // 클라이언트가 전송한 파일 업로드하기
        for (MultipartFile file : files){
            String fullPath = FileUtils.uploadFile(file, UPLOADPATH);
            fileNames.add(fullPath);
        }

        return fileNames;
    }

    //filename = /2022/08/01/변환된 파일명
     /*
        비동기 통신 응답시 ResponseEntity를 쓰는 이유는
        이 객체는 응답 body정보 이외에도 header정보를 포함할 수 있고
        추가로 응답 상태코드도 제어할 수 있다.
     */

    // @GetMapping ?
    // 이미지를 화면에 불러와서 보여줘서 Getmapping
    @GetMapping("/loadFile")
    @ResponseBody
    public ResponseEntity<byte[]> loadFile(String fileName){
        
        log.info("upload Controller loadFile GET - {}", fileName);
        
        // 클라이언트가 요청하는 파일의 진짜 바이트 데이터를 가져다줘야함
        
        // 1. 요청 파일 찾아서 file 객체로 포장
        File f = new File(UPLOADPATH+fileName);
        // 1-2. 못찾았다
        if (!f.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 2. 해당 파일을 inputStream을 통해 불러온다
        try(FileInputStream fis = new FileInputStream(f)) {

            // 3. 클라이언트에게 순수 이미지를 응답해야 하므로 MINE(미디어) TYPE을 응답헤더에 설정
            // -> 확장자를 추출해야 함
            // image/jpeg, image/png
            String ext = FileUtils.getFileExtension(fileName);
            MediaType mediaType = FileUtils.getMediaType(ext);
//            ("image/"+ ext )
            HttpHeaders headers = new HttpHeaders();

            if (mediaType != null){
                headers.setContentType(mediaType);
            }
            
            // 4. 파일 순수데이터 바이트 배열에 저장 (변환)
            byte[] rawData = IOUtils.toByteArray(fis);

            // (중요) 5. 비동기 통신에서 데이터 응답할 떄 ResponseEntity 객체를 사용
            return new ResponseEntity<>(rawData, headers, HttpStatus.OK);
            // 클라이언트에 파일 데이터 응답

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}
