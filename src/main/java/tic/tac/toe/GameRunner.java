package tic.tac.toe;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameRunner implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GameRunner.class);
        //disabled banner, don't want to see the spring logo
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    public void run(String... args) {

    }

}
