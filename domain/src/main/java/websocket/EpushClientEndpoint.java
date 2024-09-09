package websocket;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

/**
 * EpushClientEndpoint
 * 功能描述：EpushClientEndpoint
 *
 * @author zhanghaojie
 * @date 2023/6/5 16:13
 */
@ClientEndpoint
public class EpushClientEndpoint {

    private Session session;


    public EpushClientEndpoint() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI("wss://epush.ctrip.com/epush/?app=EB_WEB&EIO=3&transport=websocket&sid=8bcdd3ed-561b-4868-8c17-04e8b1e4bb14"));
            System.out.println("进行了初始化操作" + session.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("接收到消息:" + message);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        System.out.println("连接关闭, code: " + reason.getCloseCode() + ", reason: " + reason.getReasonPhrase());
    }


}
