package tic.tac.toe.client.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import tic.tac.toe.client.service.ClientService;
import tic.tac.toe.client.service.ClientServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class MainFrame extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

    public static final int WIDTH = 600;

    public static final int HEIGHT = 680;

    @Value("${title}")
    private String gameTitle;

    @Value("${playfield.size}")
    private Integer size;

    @Value("${server.endpoint}")
    private String url;

    @Bean
    public static PropertySourcesPlaceholderConfigurer setUp() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public MainFrame() {
        super();
        setTitle(gameTitle);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    public void init() {
        UUID uuid = UUID.randomUUID();
        ClientService service = new ClientServiceImpl(url, uuid.toString());
        GameTable gameTable = new GameTable(size, service);
        InfoPanel infoPanel = new InfoPanel();
        try {
            service.initGameSession(gameTable, infoPanel);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("unable connect to server", e);
        }
        getContentPane().add(gameTable, BorderLayout.NORTH);
        getContentPane().add(infoPanel, BorderLayout.SOUTH);

        Font font = infoPanel.getFont();
        infoPanel.setFont((new Font(font.getName(), Font.PLAIN, 30)));
        infoPanel.setHorizontalAlignment(JLabel.CENTER);
    }
}
