package app;

import data_access.BuildingAddressDAO;
import entity.UofTLocation;
import use_case.AddressDataAccessInterface;
import use_case.AddressLookupUseCase;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}