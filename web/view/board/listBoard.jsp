<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<head>
    <title>게시판 목록</title>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>

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
		request.setAttribute("col", col);
		request.setAttribute("word", word);

		*세션 : ${authUser} <br/>
		*pageNo(보고싶은페이지) : ${pageNo} <br/>
		*rowSize(한페이지당 글 개수) : ${rowSize}  <br/>
		*col(조회조건) : ${col}  <br/>
		*word(조회한 단어) : ${word}  <br/>
		*starPage : ${boardPage.startPage} <br/>
		*endPage :  ${boardPage.endPage} <br/>
		*totalPages : ${boardPage.totalPages} <br/><br/>
	--%>

    <h2 align="center">추천게시판</h2>

    <br/>

    <div id="formDiv">
        <form name="frm" method="get" action="<%=request.getContextPath()%>/recomboardSearch.do">
            조회조건 :
            <select name="col">
                <option value="">선택안함(전체조회)</option>
                <option value="searchId">아이디</option>
                <option value="searchTitle">제목</option>
                <option value="searchContent">내용</option>
                <!-- <option value="searchTitleContent">제목+내용</option> -->
                <!-- <option value="all">아이디+제목+내용</option> -->
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

        <c:if test="${not empty authUser}">
            <input type="button" value="글쓰기" id="btnWrite">
        </c:if>
    </div>


    <div id="tableDiv">
        <table border="1" style="width: 1200px;">
            <thead>
            <tr>
                <th>게시판번호</th>
                <th>카테고리 ID</th>
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
                        <td>${item.categoryId}</td>
                        <td>${item.writer}</td>
                        <td><a href="/recomboardRead.do?no=${item.rNo}&pageNo=${boardPage.currentPage}&rowSize=${rowSize}">${item.title}</a></td>
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
                <td colspan="6" style="text-align: center;">
                    <%-- JSTL if조건문 : 이전출력 --%>
                    <c:if test="${boardPage.startPage > 5}">
                        <a href="/boardList.do?pageNo=${boardPage.startPage-5}&rowSize=${rowSize}">prev</a>
                    </c:if>

                    <%-- JSTL forEach조건문 : 페이지번호출력 --%>
                    <c:forEach var="pNo" begin="${boardPage.startPage}" end="${boardPage.endPage}">
                        <a href="/boardList.do?pageNo=${pNo}&rowSize=${rowSize}">${pNo}</a>
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
