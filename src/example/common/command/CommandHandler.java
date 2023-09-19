package example.common.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandHandler {

    // String 타입의 view 리턴하는 메서드
    public abstract String process(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
