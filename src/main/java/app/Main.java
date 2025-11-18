package app;

import javax.swing.*;
import data_access.RateMyProfAPI;
import entity.Professor;

public class Main {
    public static void main(String[] args) {
//        RateMyProfAPI testing
        RateMyProfAPI api = new RateMyProfAPI();
        try {
            Professor prof = api.getProfessorInfo("Paul", "Gries");
            System.out.println("API Response:");
            System.out.println(prof.stringProf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}