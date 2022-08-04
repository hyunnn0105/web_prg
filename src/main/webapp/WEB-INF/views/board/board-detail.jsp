<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <%@ include file="../include/static-head.jsp" %>

    <style>
        .content-container {
            width: 60%;
            margin: 150px auto;
            position: relative;
        }

        .content-container .main-title {
            font-size: 24px;
            font-weight: 700;
            text-align: center;
            border-bottom: 2px solid rgb(75, 73, 73);
            padding: 0 20px 15px;
            width: fit-content;
            margin: 20px auto 30px;
        }

        .content-container .main-content {
            border: 2px solid #ccc;
            border-radius: 20px;
            padding: 10px 25px;
            font-size: 1.1em;
            text-align: justify;
            min-height: 400px;
        }

        .content-container .custom-btn-group {
            /* position: absolute; */
            /* bottom: -10%; */
            left: 50%;
            transform: translateX(-50%);
        }

        /* 페이지 액티브 기능 */
        .pagination .page-item.p-active a{
            background: #333 !important;
            color: #fff !important;
            /* 엑티브 된 페이지는 클릭 불가 */
            cursor: default;
            pointer-events: none;
        }

        .pagination .page-item:hover a {
            background: #888 !important;
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

    <div class="wrap">
        <%@ include file="../include/header.jsp" %>

        <div class="content-container">

            <h1 class="main-title">${b.boardNo}번 게시물</h1>
            <h2>${b.account}</h2>

            <div class="mb-3">
                <label for="exampleFormControlInput1" class="form-label">작성자</label>
                <input type="text" class="form-control" id="exampleFormControlInput1" placeholder="이름" name="writer"
                    value="${b.writer}" disabled>
            </div>
            <div class="mb-3">
                <label for="exampleFormControlInput2" class="form-label">글제목</label>
                <input type="text" class="form-control" id="exampleFormControlInput2" placeholder="제목" name="title"
                    value="${b.title}" disabled>
            </div>
            <div class="mb-3">
                <label for="exampleFormControlTextarea1" class="form-label">내용</label>

                <p class="main-content">
                    ${b.content}
                </p>

            </div>

            <!-- 파일 첨부 영역 -->
            <div class="form-group">
                <ul class="uploaded-list"></ul>
            </div>

            
                <div class="btn-group btn-group-lg custom-btn-group" role="group" >
                    <c:if test="${loginUser.account == b.account || loginUser.auth == 'ADMIN'}">
                        <button id="mod-btn" type="button" class="btn btn-warning">수정</button>
                        <button id="del-btn" type="button" class="btn btn-danger">삭제</button>

                    </c:if>
                        <button id="list-btn" type="button" class="btn btn-dark" onclick="location.href = '/board/modify?boardNo=${b.boardNo}'">목록</button>
                </div>  

            

            <!-- 댓글 영역 -->

            <!-- 댓글 영역 -->

            <div id="replies" class="row">
                <div class="offset-md-1 col-md-10">
                    <!-- 댓글 쓰기 영역 -->
                    <div class="card">
                        <div class="card-body">
                            <c:if test="${loginUser==null}">
                                <a href="/member/sign-in">댓글은 로그인 후 작성 가능합니다.</a>
                            </c:if>
                            <c:if test="${loginUser!=null}">
                                <div class="row">
                                    <div class="col-md-9">
                                        <div class="form-group">
                                            <label for="newReplyText" hidden>댓글 내용</label>
                                            <textarea rows="3" id="newReplyText" name="replyText" class="form-control"
                                                placeholder="댓글을 입력해주세요."></textarea>
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-group">
                                            <label for="newReplyWriter" hidden>댓글 작성자</label>
                                            <input id="newReplyWriter" name="replyWriter" type="text" class="form-control"
                                                value="${loginUser.name}" style="margin-bottom: 6px;" readonly>
                                            <button id="replyAddBtn" type="button"
                                                class="btn btn-dark form-control">등록</button>
                                        </div>
                                    </div>



                                </div>
                            </c:if>
                        </div>
                    </div> <!-- end reply write -->

                    <!--댓글 내용 영역-->
                    <div class="card">
                        <!-- 댓글 내용 헤더 -->
                        <div class="card-header text-white m-0" style="background: #343A40;">
                            <div class="float-left">댓글 (<span id="replyCnt">0</span>)</div>
                        </div>

                        <!-- 댓글 내용 바디 -->
                        <div id="replyCollapse" class="card">
                            <!-- 댓글 데이터 여기서 가져옴!!! -->
                            <div id="replyData">
                                <!-- 
                        < JS로 댓글 정보 DIV삽입 > 
                     -->
                            </div>

                            <!-- 댓글 페이징 영역 -->
                            <ul class="pagination justify-content-center">
                                <!-- 
                        < JS로 댓글 페이징 DIV삽입 > 
                     -->
                            </ul>
                        </div>
                    </div> <!-- end reply content -->
                </div>
            </div> <!-- end replies row -->

            <!-- [22/08/01]댓글 수정 모달 -->
            <div class="modal fade bd-example-modal-lg" id="replyModifyModal">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">

                        <!-- Modal Header -->
                        <div class="modal-header" style="background: #343A40; color: white;">
                            <h4 class="modal-title">댓글 수정하기</h4>
                            <button type="button" class="close text-white" data-bs-dismiss="modal">X</button>
                        </div>

                        <!-- Modal body -->
                        <div class="modal-body">
                            <div class="form-group">
                                <input id="modReplyId" type="hidden">
                                <label for="modReplyText" hidden>댓글내용</label>
                                <textarea id="modReplyText" class="form-control" placeholder="수정할 댓글 내용을 입력하세요."
                                    rows="3"></textarea>
                            </div>
                        </div>

                        <!-- Modal footer -->
                        <div class="modal-footer">
                            <!-- [수정] 작성자를 어케 잡아오지? -->
                            <c:if test="${loginUser.account == b.account || loginUser.auth == 'ADMIN'}">
                                <button id="replyModBtn" type="button" class="btn btn-dark">수정</button>
                                <button id="modal-close" type="button" class="btn btn-danger"
                                    data-bs-dismiss="modal">닫기</button>
                            </c:if>
                            
                        </div>
                    </div>
                </div>

            </div>

            <!-- end replyModifyModal -->

        </div>

        <%@ include file="../include/footer.jsp" %>


    </div>


    <!-- 게시글 관련 js -->
    
    <script>
    //    const [$modBtn, $delBtn, $listBtn] 
    //     = [...document.querySelector('div[role=group]').children];
        const $modBtn = document.getElementById('mod-btn');
        const $delBtn = document.getElementById('del-btn');
        const $listBtn = document.getElementById('list-btn');

        // const $modBtn = document.getElementById('mod-btn');
        //수정버튼
        $modBtn.onclick = e => {
            // console.log('수정버튼');
            location.href = '/board/modify?boardNo=${b.boardNo}';
        };

        //삭제버튼
        $delBtn.onclick = e => {
            if(!confirm('정말 삭제하시겠습니까?')) {
                return;
            }
            location.href = '/board/delete?boardNo=${b.boardNo}';
        };
        //목록버튼
        $listBtn.onclick = e => {
            // console.log('삭제버튼');
            location.href = '/board/list?pageNum=${p.pageNum}&amount=${p.amount}';
        };
    </script>

    <!-- 파일 불러오기 -->

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
                    // life GET요청 -> 순수한 데이터 넣어주기?
                    $img.setAttribute('src', '/loadFile?fileName=' + fileName);
                    $img.setAttribute('alt', originFileName);
                    $('.uploaded-list').append($img);

                } else {
                    // 이미지가 아니라면 다운로드 링크를 생성
                    const $a = document.createElement('a');
                    // 바이트 배열 받아서
                    $a.setAttribute('href', '/loadFile?fileName=' + fileName);

                    const $img = document.createElement('img');
                    $img.classList.add('img-sizing');
                    // life GET요청 -> 순수한 데이터 넣어주기?
                    $img.setAttribute('src', '/img/file_icon.jpg');
                    $img.setAttribute('alt', originFileName);

                    $a.append($img)
                    $a.innerHTML += '<span>' + originFileName + '</span>';

                    $('.uploaded-list').append($a);
                }



            }


            // 드롭한 파일을 화면에 보여주는 함수 (파일에 따라 다르게 보여줘야함)
            // 파일 경로 들에서 파일 경로를 하나씩 받음
            function ShowFileData(fileNames) {
                // 이미지 인지? 이미지가 아닌지에 따라 구분하여 처리
                // 이미지이면 썸네일을 렌더링하고 아니면 다운로드 링크를 렌더링한다
                for(let fileName of fileNames){
                    checkExtType(fileName);
                }
            }

            // 파일 목록 불러오기
            function showFileList() {
                fetch('/board/file/' + bno)
                    .then(res => res.json())
                    .then(fileNames => {
                        ShowFileData(fileNames);
                    });
            }
                showFileList();






        });
                    // jquery 구문 끝
    </script>

    <!-- 댓글관련 js -->

    <script>

    // 로그인 한회원 계정명
        const currentAccount = '${$loginUser.account}';
        const auth = '${loginUser.auth}';
        const $newReplyWriter = document.getElementById('newReplyWriter');
        console.log($newReplyWriter.value);

        // 원본 글 번호
        let bno = '${b.boardNo}';
        // console.log(bno);

        // 댓글요청 url
        const URL = '/api/v1/replies';
        // const URL = '/api/v1/replies/3/11';
        // 3번글의 11페이지 /글번호/페이지번호 **

        //날짜 포맷 변환 함수
        function formatDate(datetime) {
                //문자열 날짜 데이터를 js날짜객체로 변환
                const dateObj = new Date(datetime);
                // console.log(dateObj);

                // getter를 통해 분리해주기
                //날짜객체를 통해 각 날짜 정보 얻기
                let year = dateObj.getFullYear();
                //1월이 0으로 설정되어있음.
                let month = dateObj.getMonth() + 1;
                let day = dateObj.getDate();
                let hour = dateObj.getHours();
                let minute = dateObj.getMinutes();
                //오전, 오후 시간체크
                let ampm = '';
                if (hour < 12 && hour >= 6) {
                    ampm = '오전';
                } else if (hour >= 12 && hour < 21) {
                    ampm = '오후';
                    if (hour !== 12) {
                        hour -= 12;
                    }
                } else if (hour >= 21 && hour <= 24) {
                    ampm = '밤';
                    hour -= 12;
                } else {
                    ampm = '새벽';
                }
                //숫자가 1자리일 경우 2자리로 변환
                (month < 10) ? month = '0' + month: month;
                (day < 10) ? day = '0' + day: day;
                (hour < 10) ? hour = '0' + hour: hour;
                (minute < 10) ? minute = '0' + minute: minute;
                return year + "-" + month + "-" + day + " " + ampm + " " + hour + ":" + minute;
        }


        // 댓글 페이지 태그 생성 렌더링 함수 -> 여기 수정하기
        // 객체는 쪼개서 넣어줄 수 있음 {beginPage, endPage}
        function makePageDOM(pageInfo) {
            let tag = "";
            const begin = pageInfo.beginPage;
            const end = pageInfo.endPage;
            //이전 버튼 만들기 - ture면 생성
            // href를 비동기로 처리?(버튼X)
            if (pageInfo.prev) {
                tag += "<li class='page-item'><a class='page-link page-active' href='" + (begin - 1) +
                    "'>이전</a></li>";
            }
            //페이지 번호 리스트 만들기
            for (let i = begin; i <= end; i++) {

                let active = '';
                if (pageInfo.page.pageNum === i) {
                    active = 'p-active';
                }

                tag += "<li class='page-item " + active + "'><a class='page-link page-custom' href='" + i +
                                "'>" + i + "</a></li>";

                // const active = (pageInfo.page.pageNum === i) ? 'p-active' : '';
                // tag += "<li class='page-item " + active + "'><a class='page-link page-custom' href='" + i +
                //     "'>" +
                //    i + "</a></li>";
            }

            //다음 버튼 만들기
            if (pageInfo.next) {
                tag += "<li class='page-item'><a class='page-link page-active' href='" + (end + 1) +
                    "'>다음</a></li>";
            }
            
            // 페이지 태그 랜더링
            const $pageUl = document.querySelector('.pagination');
            $pageUl.innerHTML = tag;
            $pageUl.dataset.fp = pageInfo.finalPage; // 수정????
            // document.querySelector('.pagination').innerHTML = tag;
            
        }

        // 댓글목록 돔을 생성하는 함수
        // 여기에서도 디스트럭쳐링 가능! -> count만 추가해주기
        function makeReplyDOM({replyList, count, maker}) {

            let tag = '';


            if (replyList === null || replyList.length === 0) {
                // 아직 댓글이 없습니다 이런 메세지 만ㄴ들기
                // li 만들어서 안에 텍스트 콘텐츠 추가하기?
                //const make = document.createElement('li');
                tag += "<div id='replyContent' class='card-body'>댓글이 아직 없습니다!</div>";

            } else {
                    // 각 댓글 하나의 태그
                for (let rep of replyList) {
                    tag += "<div id='replyContent' class='card-body' data-replyId='" + rep.replyNo + "'>" +
                            "    <div class='row user-block'>" +
                            "       <span class='col-md-3'>" +
                            "         <b>" + rep.replyWriter + "</b>" +
                            "       </span>" +
                            "       <span class='offset-md-6 col-md-3 text-right'><b>" + formatDate(rep.replyDate) +
                            "</b></span>" +
                            "    </div><br>" +
                            "    <div class='row'>" +
                            "       <div class='col-md-6'>" + rep.replyText + "</div>" +
                            "       <div class='offset-md-2 col-md-4 text-right'>";
                            if(currentAccount == '${rep.getAccount}' || auth == 'ADMIN') {
                                tag += "         <a id='replyModBtn' class='btn btn-sm btn-outline-dark' data-bs-toggle='modal' data-bs-target='#replyModifyModal'>수정</a>&nbsp;" +
                                                            "         <a id='replyDelBtn' class='btn btn-sm btn-outline-dark' href='#'>삭제</a>";
                            }

                            tag += "       </div>" +
                            "    </div>" +
                            " </div>";
                
                        }

            }
            

            // 댓글 목록에 생성된 DOM 추가
            document.getElementById('replyData').innerHTML = tag;

            // 댓글 수 배치
            document.getElementById('replyCnt').textContent = count;


            // 페이지 렌더링
            makePageDOM(maker);
        }

        // 댓글 목록을 서버로부터 비동기요청으로 불러오는 함수
        function showReplies(pageNum=1) {

            // fetch(URL + '?boardNo=' + bno + '&pageNum=' + pageNum)
            //     .then(res => res.json())
            //     .then(replyMap => {
            //         makeReplyDOM(replyMap);
            //     });

            fetch(URL + '?boardNo=' + bno + '&pageNum=' + pageNum)
                .then(res => res.json())
                .then(replyMap => {
                    makeReplyDOM(replyMap);
                });
        }


        // 페이지 버튼 클릭이벤트 등록 함수 220802 추가
        function makePageButtonClickEvent() {
            // 페이지 버튼 클릭이벤트 처리
            const $pageUl = document.querySelector('.pagination');
            $pageUl.onclick = e => {
                if (!e.target.matches('.page-item a')) return;
                e.preventDefault();
                // 누른 페이지 번호 가져오기
                const pageNum = e.target.getAttribute('href');
                // console.log(pageNum);
                // 페이지 번호에 맞는 목록 비동기 요청
                showReplies(pageNum);
            };
        }


        // 댓글 등록 이벤트 처리 핸들러 등록 함수
        function makeReplyRegisterClickEvent(e) {
            
            document.getElementById('replyAddBtn').onclick 
            = makeReplyRegisterClickHandler();
        }

        // 댓글 수정화면 열기 상세처리 
        function processModifyShow(e, rno) {
            // 클릭한 버튼 근처에 있는 댓글 내용 텍스트를 얻어온다
            const replyText = e.target.parentElement.parentElement.firstElementChild.textContent;
                console.log('댓글 내용 : ' , replyText);

                // 모달에 해당 댓글 내용을 배치한다
                document.getElementById('modReplyText').value = replyText;

                // 모달을 띄울 때 다음작업(수정완료 처리) 을 위해 댓글번호 모달에 달아주기

                const $modal = document.querySelector('.modal');
                $modal.dataset.rno = rno;
        }


        // 댓글 삭제화면 열기 상세처리 
        function processRemove(rno) {
            if (!confirm('댓글을 삭제하시겠습니까?')) return;

            fetch(URL + '/' + rno, {
                    method: 'DELETE'
                })
                .then(res => res.text())
                .then(msg => {
                    if (msg === 'del-success') {
                        alert('삭제 성공!!');
                        showReplies(); // 댓글 새로불러오기
                    } else {
                        alert('삭제 실패!!');
                    }
                });

        }

        // 댓글 등록 이벤트 처리 핸들러 등록 함수
        function makeReplyRegisterClickEvent() {
            document.getElementById('replyAddBtn').onclick = makeReplyRegisterClickHandler;
        }

        // 댓글 등록 이벤트 처리 핸들러 함수
        function makeReplyRegisterClickHandler(e) {

            const $writerInput = document.getElementById('newReplyWriter');
            const $contentInput = document.getElementById('newReplyText');

            // 서버로 전송할 데이터들
            const replyData = {
                replyWriter: $writerInput.value,
                replyText: $contentInput.value,
                boardNo: bno
            };
            
            // POST 요청을 위한 요청정보 객체
            const reqInfo = {
                method: 'POST'
                , headers: {
                    'content-type' : 'application/json'
                }
                , body: JSON.stringify(replyData)
            };

            fetch(URL, reqInfo)
            .then(res => res.text())
            .then(msg => {
                    console.log(msg);
                    if (msg === 'insert-success') {
                        alert('저장성공');
                        // 댓글 리셋
                        // $writerInput.value = ''; -- 이제는 account 정보 넘겨줌! [220804]
                        $contentInput.value = '';
                        // 댓글목록재요청
                        showReplies(document.querySelector('.pagination').dataset.fp);
                        // focus 함수로 등록된 ul 자리로 이동하기
                    } else {
                        alert('실패');
                    }
                });
        }

        // 댓글 수정, 삭제처리 핸들러 정의
        function makeReplyModAndDelHandlerEvent(e) {
            
            const rno = e.target.parentElement.parentElement.parentElement.dataset.replyid;
            
            console.log(rno);
            console.log(e.target);
            
            e.preventDefault();
            if(e.target.matches('#replyModBtn')){

                processModifyShow(e, rno);
                console.log('modishow');

            } else if(e.target.matches('#replyDelBtn')) {
                processRemove(rno);
            }

        }

        // 댓글 수정화면 열기 이벤트 처리
        function openModifyModalAndRemoveEvent() {

            const $replyData = document.getElementById('replyData');
            
            $replyData.onclick = makeReplyModAndDelHandlerEvent;
                
                // console.log('수정버튼 클릭창1');

                
                // e.target.parentElement.parentElement.parentElement.dataset.replyId;
                // console.log($modal.dataset.rno);
        }
        

        // 댓글 수정 비동기 처리 이벤트
        function replyModifyEvent() {

            const $modal = $('#replyModifyModal');
        
            document.getElementById('replyModBtn').onclick = e => {
                
                console.log('수정완료');
                // 서버에 수정 비동기 요청 보내기
                // 수정할 댓글 번호 잡아오가

                const rno = e.target.closest('.modal').dataset.rno;
                console.log(rno);
                const reqInfo = {
                    method : 'PUT',
                    headers: {
                        'content-type' : 'application/json'
                    } , 
                    body : JSON.stringify(
                        {
                            // jquery $('#modReplyText').val();
                            // value에 test값 있다....
                            replyText: document.getElementById('modReplyText').value,
                            replyNo: rno
                        }
                    )
                };

                fetch(URL + '/' + rno, reqInfo)
                    .then(res => res.text())
                    .then(msg => {
                        if(msg === 'modi-success'){
                            alert('수정성공');
                            $modal.modal('hide'); // 모달창 닫기
                            showReplies(); // 댓글 새로불러오기

                        } else {
                            alert('수정삭제');
                        }
                    });

            }
            

        }

        // 댓글 삭제버튼 이벤트 처리
        // 온클릭 사용 X





        // main 실행부 **즉시실행함수 (function(){})() 이거임
        (function () {
            showReplies();

            //페이지버튼처리 랜더링
            // 여기서 최초의 한번만!! 실행시켜주기
            const $pageUl = document.querySelector('.pagination');
            $pageUl.addEventListener('click', e => {
                if(e.target.matches('.page-item a')) return;

                e.preventDefault();

                // 누른 페이지 번호 가져오기
                const pageNum = e.target.getAttribute('href');
                console.log(pageNum);

                showReplies(pageNum);
            })
            // 댓글등록 이벤트 처리 함수

            makeReplyRegisterClickEvent();

            // 댓글 수정 모달 오픈,삭제 이벤트 처리
            openModifyModalAndRemoveEvent();

            // 댓글 수정 비동기 처리 이벤트
            replyModifyEvent();

            // 댓글 수정, 삭제처리 핸들러 정의
            makeReplyModAndDelHandlerEvent();


        })();
        



        // 댓글 저장

        // fetch(url + '?boardNo=' + bno )
        //     .then(res => req.json())
        //     .then(replyMap=> {
        //         makeReplyDOM(replyMap.replyList);
        //     })

        /*

        document.getElementById('replyAddBtn').onclick = e => {
            e.preventDefault();

            const reqObj = {
                method : 'POST',
                headers : {
                    'content-type' : 'aplication/json'
                },
                body : JSON.stringify({
                    "replyText": document.querySelector('[name=replyText]').value,
                    "replyWriter": document.querySelector('[name=replyWriter]').value,
                    boardNo : bno
                })
            };

            // fetch
            fetch(url, reqObj)
                .then(res => res.text())
                .then(resultmsg => {
                    console.log(resultmsg);
                    if (resultmsg === 'insert-success') {
                        alert('저장성공');
                    } else {
                        alert('실패');
                    }
                });


        }
        */

    </script>
</body>

</html>