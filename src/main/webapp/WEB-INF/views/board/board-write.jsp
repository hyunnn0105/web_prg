<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="../include/static-head.jsp" %>

    <style>
        .write-container {
            width: 50%;
            margin: 200px auto 150px;
            font-size: 1.2em;
        }
    </style>
</head>

<body>
    <div class="wrap">
        <%@ include file="../include/header.jsp" %>

        <div class="write-container">

            <form id="write-form" action="/board/write" method="post" autocomplete="off">

                <!-- input name  = commnd객체 필드명 -->
                <div class="mb-3">
                    <label for="writer-input" class="form-label">작성자</label>
                    <input type="text" class="form-control" id="writer-input" placeholder="이름"
                        name="writer">
                </div>
                <div class="mb-3">
                    <label for="title-input" class="form-label">글제목</label>
                    <input type="text" class="form-control" id="title-input" placeholder="제목" name="title" >
                </div>
                <div class="mb-3">
                    <label for="exampleFormControlTextarea1" class="form-label">내용</label>
                    <textarea name="content" class="form-control" id="exampleFormControlTextarea1" rows="10"></textarea>
                </div>

                <div class="d-grid gap-2">
                    <button id="reg-btn" class="btn btn-dark" type="button">글 작성하기</button>
                    <button id="to-list" class="btn btn-warning" type="button">목록으로</button>
                </div>

            </form>

        </div>

        <%@ include file="../include/footer.jsp" %>


        
    </div>


    <script>
        // 최대길이 검증
        function vaildateFormValue(){
            // 이름 입력 태그, 제목 입력 태그 받아오기
            const $writerInput = document.getElementById('writer-input');
            const $titleInput = document.getElementById('title-input');
            // html 6강 label!!
            
            let flag = false;

            console.log($writerInput.value);
            console.log($writerInput.value);

            if($writerInput.value.trim() === ''){
                alert('작성자는 필수값입니다.');
            } else if($titleInput.value.trim() === ''){
                alert('제목은 필수값입니다.')
            } else{
                flag = true;
            }
            console.log('flag : ', flag);
            return flag;
        }

        // 게시글 입력값 검증
        const $regBtn = document.getElementById('reg-btn');

        $regBtn.onclick = e => {
            // 입력값을 재대로 채우지 않았는지 확인
            if(!vaildateFormValue()){
                return;
            }


            // 필수입력값 있음 -> form을 submit
            // 검증을 위해 여기로 빼줌
            const $form = document.getElementById('write-form');
            $form.submit();
        }

        //목록버튼 이벤트
        const $toList = document.getElementById('to-list');
        $toList.onclick = e => {
            location.href = '/board/list?pageNum=${p.pageNum}&amount=${p.amount}';
        };
    </script>

</body>

</html>