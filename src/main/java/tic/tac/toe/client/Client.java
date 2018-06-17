package tic.tac.toe.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static final int CELL_SIZE = 50;

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                    new File(ClassLoader.getSystemClassLoader().getResource("WalkwayBold.ttf").getFile())));
        } catch (IOException | FontFormatException e) {
            LOGGER.error("unable to load custom font", e);
        }
        JFrame frame = new JFrame();
        frame.setSize(505, 580);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        GameTable table = new GameTable();
        frame.getContentPane().add(table, BorderLayout.NORTH);
        //frame.getContentPane().add(jb2, BorderLayout.SOUTH);
        frame.setVisible(true);
        WebSocketClient simpleWebSocketClient =
                new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient =
                new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = getProperties().getProperty("server.endpoint");
        String userId = "client-" +
                ThreadLocalRandom.current().nextInt(1, 99);
        StompSessionHandler sessionHandler = new GameStompSessionHandler(userId, table);
        StompSession session = stompClient.connect(url, sessionHandler)
                .get();
        BufferedReader in =
                new BufferedReader(new InputStreamReader(System.in));
        for (; ; ) {
            System.out.print(userId + " >> ");
            System.out.flush();
            String line = in.readLine();
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            ClientMessage msg = new ClientMessage(userId, line);
            session.send("/app/chat/java", msg);
        }
    }

    public static Properties getProperties() {
        //getProperties().getProperty("server.endpoint")
        Properties prop = new Properties();
        InputStream in = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("application.properties");
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            LOGGER.error("unable to load property file", e);
        }
        return prop;
    }

}
