package tic.tac.toe.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);
        MainFrame mainFrame = (MainFrame) context.getBean("mainFrame");
        mainFrame.setArgs(args);
        mainFrame.init();
    }

}
