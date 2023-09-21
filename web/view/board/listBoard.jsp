<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>

    <style>
    #searchDiv, #tableDiv, #homeDiv {
        text-align: center;
    }
    table {
        border-collapse: separate;
        border-spacing: 0;
        width: 100%;
        margin: auto;
    }
    </style>

    <script>
        $(function() {
            $("#btnMain").click(function() {
                location.href="<%=request.getContextPath()%>/index.jsp";
            });
            $("#btnWrite").click(function() {
                location.href="<%=request.getContextPath()%>/createBoard.do?rowSize=${rowSize}";
            });
        });
    </script>
</head>
<body>
    <%-- ListArticleHandler 컨트롤러에 의해 아래와 같이 Model 받는다
		request.setAttribute("boardPage", boardPage);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("rowSize", rowSize);
		request.setAttribute("searchOption", searchOption);
		request.setAttribute("word", word);

		*세션 : ${authUser} <br/>
		*pageNo(보고싶은페이지) : ${pageNo} <br/>
		*rowSize(한페이지당 글 개수) : ${rowSize}  <br/>
		*col(조회조건) : ${searchOption}  <br/>
		*word(조회한 단어) : ${word}  <br/>
		*starPage : ${boardPage.startPage} <br/>
		*endPage :  ${boardPage.endPage} <br/>
		*totalPages : ${boardPage.totalPages} <br/><br/>
	--%>

    <h2 style="text-align: center">추천게시판</h2>

    <br/>
    <div id="searchDiv">
        <form name="frm" method="get" action="<%=request.getContextPath()%>/boardList.do">
            조회조건 :
            <select name="searchOption">
                <option value="">선택안함</option>
                <option value="category">카테고리</option>
                <option value="title">제목</option>
                <option value="content">내용</option>
            </select> <br/>

            페이지당 게시물 개수 :
            <select name="rowSize" id="rowSize">
                <option value="10">선택</option>
                <option value="3">3</option>
                <option value="5">5</option>
                <option value="10">10</option>
            </select> <br/>

            <input type="text" name="word" value="" placeholder="특수문자는 사용 불가능">
            <button type="submit">검색</button>
        </form>
    </div>

    <br/><br/>
    <div id="homeDiv">
        <input type="button" value="글쓰기" id="btnWrite">
    </div>


    <div id="tableDiv">
        <table border="1" style="width: 1200px;">
            <thead>
            <tr>
                <th>글 번호</th>
                <th>카테고리</th>
                <th>작성자</th>
                <th>제목</th>
                <th>내용</th>
                <th>조회수</th>
                <th>작성일</th>
                <th>수정일</th>
            </tr>
            </thead>
            <tbody>
            <%-- 게시글이 없는 경우 --%>
            <c:if test="${boardPage.hasNoBoards()}">
                <tr>
                    <td colspan="6" style="text-align: center;">게시글이 없습니다</td>
                </tr>
            </c:if>

            <%-- 게시글이 있는 경우 --%>
            <c:if test="${boardPage.hasBoards()}">
                <c:forEach var="item" items="${boardPage.list}">
                    <tr>
                        <td>${item.boardId}</td>
                        <td>${item.categoryName}</td>
                        <td>${item.writer}</td>
                        <td><a href="">${item.title}</a></td>
                        <td>${item.content}</td>
                        <td>${item.count}</td>
                        <td>
                            <fmt:formatDate pattern="yyyy.MM.dd. HH:mm:ss" value="${item.createdAt}" />
                        </td>
                        <td>
                            <fmt:formatDate pattern="yyyy.MM.dd. HH:mm:ss" value="${item.modifiedAt}" />
                        </td>
                    </tr>
                </c:forEach>
            </c:if>

            <%-- paging 처리 --%>
            <tr>
                <td colspan="8" style="text-align: center;">
                    <%-- JSTL if조건문 : 이전출력 --%>
                    <c:if test="${boardPage.startPage > 5}">
                        <a href="/boardList.do?pageNo=${boardPage.startPage-5}&rowSize=${rowSize}">prev</a>
                    </c:if>

                    <%-- JSTL forEach조건문 : 페이지번호출력 --%>
                    <c:forEach var="pageNo" begin="${boardPage.startPage}" end="${boardPage.endPage}">
                        <a href="/boardList.do?pageNo=${pageNo}&rowSize=${rowSize}">${pageNo}</a>
                    </c:forEach>

                    <%-- JSTL if조건문 : 다음출력 --%>
                    <c:if test="${boardPage.endPage < boardPage.totalPages}">
                        <a href="/boardList.do?pageNo=${boardPage.startPage+5}&rowSize=${rowSize}">next</a>
                    </c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

</body>
</html>
