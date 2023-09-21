package example.domain.board.dao;

import example.domain.board.entity.Board;
import example.jdbc.conn.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {

    // 게시판 조회
    public List<Board> getBoard(Connection conn, int startRow, int rowSize, String searchOption, String word) throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        System.out.println("startRow="+startRow+", rowSize="+rowSize+", searchOption="+searchOption+", word="+word);

        List<Board> boardList = new ArrayList<Board>();
        String sql = "";

        try {
            if(searchOption.equals("category")) {  // 카테고리

                System.out.println("검색옵션이 카테고리");

                sql = "SELECT a.board_id, b.category_name, a.writer, a.title, a.content, " +
                             "a.count, a.board_pw, a.board_repw, a.created_at, a.modified_at " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE b.category_name LIKE CONCAT('%',CONCAT(?,'%')) " +
                      "ORDER BY a.board_id DESC LIMIT ?,?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                pstmt.setInt(2, startRow);
                pstmt.setInt(3, rowSize);

            }else if(searchOption.equals("title")) {  // 제목

                System.out.println("검색옵션이 제목");

                sql = "SELECT a.board_id, b.category_name, a.writer, a.title, a.content, " +
                             "a.count, a.board_pw, a.board_repw, a.created_at, a.modified_at " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE a.title LIKE CONCAT('%',CONCAT(?,'%')) " +
                      "ORDER BY a.board_id DESC LIMIT ?,?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                pstmt.setInt(2, startRow);
                pstmt.setInt(3, rowSize);

            }else if(searchOption.equals("content")) {  // 내용

                System.out.println("검색옵션이 내용");

                sql = "SELECT a.board_id, b.category_name, a.writer, a.title, a.content, " +
                             "a.count, a.board_pw, a.board_repw, a.created_at, a.modified_at " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE a.content LIKE CONCAT('%',CONCAT(?,'%')) " +
                      "ORDER BY a.board_id DESC LIMIT ?,?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                pstmt.setInt(2, startRow);
                pstmt.setInt(3, rowSize);

                rs = pstmt.executeQuery();

            }else {  // 조회조건 없음

                System.out.println("조회조건 없음");

                sql = "SELECT a.board_id, b.category_name, a.writer, a.title, a.content, " +
                             "a.count, a.board_pw, a.board_repw, a.created_at, a.modified_at " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE b.category_name LIKE CONCAT('%',CONCAT(?,'%')) OR a.writer LIKE CONCAT('%',CONCAT(?,'%')) " +
                      "OR a.title LIKE CONCAT('%',CONCAT(?,'%')) OR a.content LIKE CONCAT('%',CONCAT(?,'%')) " +
                      "ORDER BY a.board_id DESC LIMIT ?,?";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, word);
                pstmt.setString(2, word);
                pstmt.setString(3, word);
                pstmt.setString(4, word);
                pstmt.setInt(5, startRow);  // int startRow : 시작행 index번호를 의미. 가장 첫번째행은 0부터 시작.
                pstmt.setInt(6, rowSize);	// int rowSize : 1페이지에 보여줄 글 개수

            }

            rs = pstmt.executeQuery();
            System.out.println("rs = " + rs);

            while(rs.next()) {
                Board board = new Board(rs.getLong("board_id"),
                                        rs.getString("CATEGORY_NAME"),
                                        rs.getString("WRITER"),
                                        rs.getString("TITLE"),
                                        rs.getString("CONTENT"),
                                        rs.getInt("COUNT"),
                                        rs.getString("BOARD_PW"),
                                        rs.getString("BOARD_REPW"),
                                        rs.getTimestamp("CREATED_AT"),
                                        rs.getTimestamp("MODIFIED_AT"));

                boardList.add(board);
            }
            return boardList;

        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            /* JdbcUtil.close(conn);

               DB Connection Pool을 통해서 Connection을 관리하므로 getConnection() 메서드를 통해 Connection을 얻은 다음에
               이후에는 직접 닫지말고 반환해서 재사용해야 한다.
            */
        }
    }

    // 게시판 개수
    public int countBoard(Connection conn, String searchOption, String word) throws SQLException  {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        System.out.println("searchCount() method run...");
        String sql = "";

        try {
                if(searchOption.equals("category")) {  // 카테고리

                System.out.println("검색옵션이 카테고리");

                sql = "SELECT COUNT(*) cnt " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE b.category_name LIKE CONCAT('%',CONCAT(?,'%'))";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;

            }else if(searchOption.equals("title")) {  // 제목

                    System.out.println("검색옵션이 제목");

                sql = "SELECT COUNT(*) cnt " +
                      "FROM board a " +
                      "WHERE a.title LIKE CONCAT('%',CONCAT(?,'%'))";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;

            }else if(searchOption.equals("content")) {   // 내용

                    System.out.println("검색옵션이 내용");

                sql = "SELECT COUNT(*) cnt " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE a.content LIKE CONCAT('%',CONCAT(?,'%'))";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;

            }else {  // 조회조건 없음

                System.out.println("조회조건 없음");

                sql = "SELECT COUNT(*) cnt " +
                      "FROM board a left join category b " +
                      "on a.category_id=b.category_id " +
                      "WHERE b.category_name LIKE CONCAT('%',CONCAT(?,'%')) OR a.writer LIKE CONCAT('%',CONCAT(?,'%')) " +
                      "OR a.title LIKE CONCAT('%',CONCAT(?,'%')) OR a.content LIKE CONCAT('%',CONCAT(?,'%'))";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, word);
                pstmt.setString(2, word);
                pstmt.setString(3, word);
                pstmt.setString(4, word);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }

        }finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            /* JdbcUtil.close(conn);

               DB Connection Pool을 통해서 Connection을 관리하므로 getConnection() 메서드를 통해 Connection을 얻은 다음에
               이후에는 직접 닫지말고 반환해서 재사용해야 한다.
            */
        }
    }

    // 자바 필드의 Date타입을 DB의 Timestamp 타입으로 변환하기
    private Timestamp toTimestamp(Date date) {

        return new Timestamp(date.getTime());
    }

    // Timestamp -> Date 타입으로 변환하기
    private Date toDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
}
