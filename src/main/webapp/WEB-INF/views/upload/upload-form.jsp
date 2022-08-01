<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>
    <!-- jquery -->
    <script src="/js/jquery-3.3.1.min.js"></script>

    <style>

        .fileDrop {
            width: 800px;
            height: 400px;
            border: 1px dashed gray;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 1.5em;
        }
        .uploaded-list {
            display: flex;
        }
        .img-sizing {
            display: block;
            width: 100px;
            height: 100px;
        }
    </style>
</head>
<body>

    <!-- 파일 업로드를 위한 form -->
    <!-- multipart/form-data : 내가 보내는건 파일형식의 데이터라고 알려줌 -->
    <form action="/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" multiple>
        <button type="submit">업로드</button>

    </form>
    <div class="fileDrop">
        <span>DROP HERE!!</span>
    </div>
    <!-- 
        - 파일 정보를 서버로 보내기 위해서는 <input type="file"> 이 필요
        - 해당 input태그는 사용자에게 보여주어 파일을 직접 선택하게 할 것이냐
          혹은 드래그앤 드롭으로만 처리를 할 것이냐에 따라 display를 상태를 결정
     -->

    <div class="uploadDiv">
        <input type="file" name="files" id="ajax-file" style="display:none;">
    </div>

    <!-- 업로드 된 이미지의 썸네일을 보여주는 영역 -->

    <div class="uploaded-list">

    </div>



    <script>

        // jquery 구문 시작
        $(document).ready(function () {

            function isImageFile(originFileName) {
                
                //정규표현식
                const pattern = /jpg$|gif$|png$/i;
                return originFileName.match(pattern);
                
                // 내가 쓴 코드 -> if/swich
                // let endpage = originFileName.substring(originFileName.indexof(".")+1)
            }

            function checkExtType(fileName) {
                // 원본 파일 명 추출
                let originFileName = fileName.substring(fileName.indexOf("_") + 1);

                //확장자 추출후 이미지인지까지 확인
                if (isImageFile(originFileName)) { // 파일이 이미지라면

                    const $img = document.createElement('img');
                    $img.classList.add('img-sizing');
                    // life GET요청
                    $img.setAttribute('src', '/loadFile?fileName=' + fileName);
                    $img.setAttribute('alt', originFileName);
                    $('.uploaded-list').append($img);
                }

                                    
                
            }


            // 드롭한 파일을 화면에 보여주는 함수 (파일에 따라 다르게 보여줘야함)
            function ShowFileData(fileNames) {
                // 이미지 인지? 이미지가 아닌지에 따라 구분하여 처리
                // 이미지이면 썸네일을 렌더링하고 아니면 다운로드 링크를 렌더링한다
                for(let fileName of fileNames){
                    checkExtType(fileName);
                }
            }



            // drag and drop event
            const $dropBox = $('.fileDrop');

            // drag 진입 이벤트
            $dropBox.on('dragover dragenter', e => {
                e.preventDefault();
                $dropBox.css('border-color', 'red')
                    .css('background', 'lightgray');
            });
            // drag 탈출 이벤트
            $dropBox.on('dragleave', e => {
                e.preventDefault(); 
                $dropBox
                    .css('border-color', 'gray')
                    .css('background', 'transparent');
            });
            // drop event
            $dropBox.on('drop', e => {
                e.preventDefault();
                
                // 1. drop된 파일 데이터 읽기
                const files = e.originalEvent.dataTransfer.files;
                // console.log( 'dpd',files);
                const $fileInput = $('#ajax-file');
                // 실제 파일 정보 넣어줌
                $fileInput.prop('files', files);

                // console.log($fileInput);

                // 3. 파일 데이터를 비동기 전송하기 위해서는 formData 객체가 필요하다
                const formData = new FormData();

                // 4. 전송할 파일들을 전부 formatData 안에 포장
                for (let file of $fileInput[0].files){
                    formData.append('files', file);
                }

                // 5. 비동기 요청 전송
                // form은 header 필요 없음
                const reqInfo = {
                    method : 'POST',
                    body: formData
                };
                fetch('/ajax-upload', reqInfo)
                    .then(res => {
                        // console.log(res.status);
                        return res.json();
                    })
                    .then(fileNames => {
                        console.log(fileNames);
                        
                        ShowFileData(fileNames);
                    });


            })



            
        })
        // jquery 구문 끝
    </script>

</body>
</html>