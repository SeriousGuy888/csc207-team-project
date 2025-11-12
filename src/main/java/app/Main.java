package app;

import javax.swing.*;
import data_access.RateMyProfAPI;

public class Main {
    public static void main(String[] args) {
//        RateMyProfAPI testing
//        RateMyProfAPI api = new RateMyProfAPI();
//        try {
//            String result = api.getProfessorInfo("Boris", "Khesin");
//            System.out.println("API Response:");
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}