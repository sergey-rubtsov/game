package tic.tac.toe.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import tic.tac.toe.client.board.GameTable;
import tic.tac.toe.client.service.GameStompSessionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan
public class SpringContext {

    @Value("${title}")
    private String title;

    @Value("${playfield.size}")
    private Integer playfield;

    @Value("${playfield.size}")
    private String url;

    @Bean(name = "mainFrame")
    public MainFrame mainFrame() {
        return new MainFrame(title, gameTable(), infoPanel());
    }

    @Bean(name = "gameTable")
    public GameTable gameTable() {
        return new GameTable(playfield);
    }

    @Bean(name = "infoPanel")
    public InfoPanel infoPanel() {
        return new InfoPanel();
    }

    @Bean(name = "sessionHandler")
    public StompSessionHandler sessionHandler() {
        UUID uuid = UUID.randomUUID();
        return new GameStompSessionHandler(uuid.toString());
    }

    @Bean
    public WebSocketStompClient stompClient() {
        WebSocketClient simpleWebSocketClient =
                new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient =
                new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        //stompClient.set
        //return stompClient.connect(url, sessionHandler()).get();
        return stompClient;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer setUp() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
