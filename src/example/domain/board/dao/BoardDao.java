package example.domain.board.dao;

import example.domain.board.entity.Board;
import example.jdbc.conn.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {

    public int selectCount(Connection conn) throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select count(no) " +
                     "from t_board";

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getInt(1);  // count란 컬럼이 없기때문에 조회해서 index 값을 준다.
            }
            return 0;

        }finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(conn);
        }
    }

    public List<Board> selectAll(Connection conn, int startRow, int rowSize) throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Board> boardList = new ArrayList<Board>();

        String sql = "SELECT a.board_id, b.category_id, a.writer, a.title, a.content, a.count, a.board_pw, a.board_repw, a.created_at, a.modified_at "+
                     "FROM board a JOIN category b ON a.category_id=b.category_id "+
                     "order BY a.board_id desc LIMIT ?,?";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, startRow);  // int startRow : 시작행 index번호를 의미. 가장 첫번째행은 0부터 시작.
            pstmt.setInt(2, rowSize);	// int rowSize : 1페이지에 보여줄 글 개수

            rs = pstmt.executeQuery();
            System.out.println("rs = " + rs);

            while(rs.next()) {
                Board board = new Board(rs.getLong("BOARD_ID"),
                                        rs.getLong("CATEGORY_ID"),
                                        rs.getString("WRITER"),
                                        rs.getString("TITLE"),
                                        rs.getString("CONTENT"),
                                        rs.getInt("COUNT"),
                                        rs.getString("BOARD_PW"),
                                        rs.getString("BOARD_REPW"),
                                        rs.getTimestamp("CREATED_AT"),
                                        rs.getTimestamp("UPDATED_AT"));

                boardList.add(board);
            }
            return boardList;

        }finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(conn);
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
