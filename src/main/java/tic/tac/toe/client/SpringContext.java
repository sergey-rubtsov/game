package tic.tac.toe.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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
import tic.tac.toe.client.repository.UserRepository;
import tic.tac.toe.client.repository.UserRepositoryImpl;
import tic.tac.toe.client.service.GameStompSessionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Configuration
@PropertySource("classpath:application.properties")
//@ComponentScan(basePackages = "tic.tac.toe.client.*")
//@EnableAutoConfiguration
public class SpringContext {

    @Value("${title}")
    private String title;

    @Value("${playfield.size}")
    private Integer playfield;

    @Value("${server.endpoint}")
    private String url;

    @Bean
    public static PropertySourcesPlaceholderConfigurer setUp() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "userRepository")
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }

    @Bean(name = "sessionHandler")
    @DependsOn("userRepository")
    public StompSessionHandler sessionHandler() {
        return new GameStompSessionHandler();
    }

    @Bean(name = "session")
    @DependsOn("sessionHandler")
    public StompSession session(@Autowired StompSessionHandler sessionHandler)
            throws ExecutionException, InterruptedException {
        return stompClient().connect(url, sessionHandler).get();
    }

    private WebSocketStompClient stompClient() {
        WebSocketClient simpleWebSocketClient =
                new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient =
                new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    @Bean(name = "mainFrame")
    public MainFrame mainFrame() {
        return new MainFrame(title);
    }

    @Bean(name = "gameTable")
    @DependsOn("session")
    public GameTable gameTable() {
        return new GameTable(playfield);
    }

    @Bean(name = "infoPanel")
    @DependsOn("session")
    public InfoPanel infoPanel() {
        return new InfoPanel();
    }

}
