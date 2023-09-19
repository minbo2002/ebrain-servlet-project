package example.domain.board.service;

import example.domain.board.dto.BoardPage;

public interface BoardService {

    BoardPage getBoardPage(int pageNo, int rowSize) throws Exception;
}
