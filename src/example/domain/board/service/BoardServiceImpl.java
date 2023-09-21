package example.domain.board.service;

import example.domain.board.dao.BoardDao;
import example.domain.board.dto.BoardPage;
import example.domain.board.entity.Board;
import example.jdbc.conn.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao = new BoardDao();

    @Override
    public BoardPage getBoardPage(int pageNo, int rowSize, String searchOption, String word) throws Exception {

        try {
            Connection conn = ConnectionProvider.getConnection();

            int afterTotal = boardDao.countBoard(conn, searchOption, word);
            System.out.println("afterTotal="+afterTotal);

            List<Board> boardList = boardDao.getBoard(conn, (pageNo-1)*rowSize, rowSize, searchOption, word);

            return new BoardPage(afterTotal, pageNo, rowSize, boardList);

//            int total = boardDao.selectCount(conn);
//            List<Board> boardList = boardDao.selectAll(conn, (pageNo-1)*rowSize, rowSize);
//
//            return new BoardPage(total, pageNo, rowSize, boardList);

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
