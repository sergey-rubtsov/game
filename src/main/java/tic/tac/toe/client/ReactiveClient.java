package tic.tac.toe.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ReactiveClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveClient.class);

    public static final int CELL_SIZE = 50;

    public static final String HOST = "localhost";

    public static final int PORT = 8080;

    private ExchangeFunction exchange = ExchangeFunctions.create(new ReactorClientHttpConnector());

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


        String url = getProperties().getProperty("server.endpoint");
        //ReactiveClient client = new ReactiveClient();
        //client.createPerson();
        //client.printAllPeople();



        WebClient webClient = WebClient.create("http://localhost:8080/person");


        for (int i = 0; i < 60; i++) {
            webClient
                    .get()
                    //.uri("")
                    .retrieve()
                    .bodyToFlux(Person.class)

                    //.flatMap(response -> System.out.println(response))
                    .subscribe(System.out::println);

            Thread.sleep(1000);
        }


/*        Flux<Person> flux = webClient.get().uri("/power").accept(MediaType.APPLICATION_STREAM_JSON).exchange()
                .flatMap(response -> response.bodyToFlux(Person.class));*/
/*
        List<Person> powerList = flux.collectList().block();*/
        //flux.doOnNext(power -> System.out.println(power.toString())).blockLast();

    }

    public void createPerson() {
        URI uri = URI.create(String.format("http://%s:%d/person", HOST, PORT));
        Person jack = new Person("Jack Doe", 16);

        ClientRequest request = ClientRequest.method(HttpMethod.POST, uri)
                .body(BodyInserters.fromObject(jack)).build();

        Mono<ClientResponse> response = exchange.exchange(request);

        System.out.println(response.block().statusCode());
    }

    public void printAllPeople() {
        URI uri = URI.create(String.format("http://%s:%d/person", HOST, PORT));
        ClientRequest request = ClientRequest.method(HttpMethod.GET, uri).build();

        Flux<Person> people = exchange.exchange(request)
                .flatMapMany(response -> response.bodyToFlux(Person.class));

        //Mono<List<Person>> peopleList = people.collectList();

        //System.out.println(peopleList.block());
        people.subscribe(System.out::println);
        //people.doOnNext(power -> System.out.println(power.toString())).blockLast();
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
