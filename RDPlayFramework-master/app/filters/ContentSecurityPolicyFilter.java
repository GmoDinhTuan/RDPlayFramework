package filters;

import controllers.routes;
import dto.ChatRoomDto;
import play.core.Execution;
import play.mvc.EssentialAction;
import play.mvc.EssentialFilter;
import play.mvc.Http;
import play.mvc.Result;

public class ContentSecurityPolicyFilter extends EssentialFilter {

    @Override
    public EssentialAction apply(EssentialAction next) {
        return EssentialAction.of((Http.RequestHeader requestHeader) -> {
            if(ChatRoomDto.roomName == null || ChatRoomDto.roomName.equals("")) {
                String webSocketUrl = routes.Application.chatRoom("").webSocketURL(requestHeader.asScala());
                return next.apply(requestHeader).map((Result result) ->
                        result.withHeader("Content-Security-Policy", "connect-src 'self' " + webSocketUrl), Execution.trampoline());
            } else {
            String webSocketUrl = routes.Application.chatRoom(ChatRoomDto.roomName).webSocketURL(requestHeader.asScala());
            return next.apply(requestHeader).map((Result result) ->
                    result.withHeader("Content-Security-Policy", "connect-src 'self' " + webSocketUrl), Execution.trampoline());
            }
        });
    }
}
