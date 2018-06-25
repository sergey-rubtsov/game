package tic.tac.toe.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tic.tac.toe.client.ui.MainFrame;

public class ClientApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainFrame.class);
        MainFrame mainFrame = (MainFrame) context.getBean("mainFrame");
        mainFrame.init();
    }

}
