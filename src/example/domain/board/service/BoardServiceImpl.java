package example.domain.board.service;

import example.domain.board.dao.BoardDao;
import example.domain.board.dto.BoardPage;
import example.domain.board.entity.Board;
import example.jdbc.conn.ConnectionProvider;

import java.sql.Connection;
import java.util.List;

public class BoardServiceImpl implements BoardService {

    private final BoardDao boardDao = new BoardDao();

    @Override
    public BoardPage getBoardPage(int pageNo, int rowSize) throws Exception {

            Connection conn = ConnectionProvider.getConnection();

            int total = boardDao.selectCount(conn);
            List<Board> boardList = boardDao.selectAll(conn, (pageNo-1)*rowSize, rowSize);

            return new BoardPage(total, pageNo, rowSize, boardList);
    }
}
