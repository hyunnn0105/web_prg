<!-- 조각 파일 -->

<!-- jsp 설정 -->
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<header>
    <div class="inner-header">
        <h1 class="logo">
            <a href="#">
                <img src="/img/logo.png" alt="로고이미지">
            </a>
        </h1>
        <h2 class="intro-text">Welcome</h2>
        <a href="#" class="menu-open">
            <span class="menu-txt">MENU</span>
            <span class="lnr lnr-menu"></span>
        </a>
    </div>

    <nav class="gnb">
        <a href="#" class="close">
            <span class="lnr lnr-cross"></span>
        </a>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="#">About</a></li>
            <li><a href="/board/list">Board</a></li>
            <li><a href="#">Contact</a></li>
            <li><a href="#">News</a></li>
            <li><a href="#">Favorite</a></li>
            <!-- sessionScope : 세션에서 (로그인유저를) 가져와라 -->
            <c:if test="${sessionScope.loginUser == null}">
                <li><a href="/member/sign-up">Sign Up</a></li>
                <li><a href="/member/sign-in">Sign In</a></li>
            </c:if>

            <c:if test="${sessionScope.loginUser != null}">
                <li><a href="#">My Page</a></li>
                <li><a href="/member/sign-out">Sign Out</a></li>
            </c:if>

        </ul>
    </nav>

</header>
<!-- //header -->