package com.example.poetools;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HelloController {
    @FXML
    private VBox pane;
    @FXML
    private Label counter;

    @FXML
    private Button launch5WayButton;

    @FXML
    protected void begin5Way()
    {
        launch5WayButton.setVisible(false);
        counter.setVisible(true);
        counter.setFont(new Font("Arial", 24));

        try {

            WatchService service = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("C:\\Program Files (x86)\\Grinding Gear Games\\Path of Exile\\logs");

            // get to last line
            reader = new BufferedReader(new FileReader("C:\\Program Files (x86)\\Grinding Gear Games\\Path of Exile\\logs\\client.txt"));
            while ((reader.readLine()) != null) {}

            WatchKey watchKey = path.register(service, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread thread = new Thread(() -> {
                pollClient(watchKey);
            });
            thread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    BufferedReader reader;
    int aukunaCounter = 0;
    boolean counterRunning = false;

    private void parseLine(String str)
    {
        if (str.contains("Aukuna"))
        {
            if (counterRunning) return;

            aukunaCounter++;
            if (aukunaCounter == 3)
            {
                counterRunning = true;
                aukunaCounter = 0;
                pane.setStyle("-fx-background-color: white;");
                Thread thread = new Thread(() -> {
                    startCounter();
                });
                thread.start();
            }
        }

        else if (str.contains("Primeval Hideout") || str.contains("Domain of Timeless"))
        {
            aukunaCounter = 0;
            counterRunning = false;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    counter.setText("Ready");
                    pane.setStyle("-fx-background-color: white;");
                }
            });
        }
    }

    int count;
    private void startCounter()
    {
        count = 25;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                counter.setText(String.valueOf(count));
            }
        });

        while (count >= 1)
        {
            try {
                Thread.sleep(1000);
                if (!counterRunning)
                {
                    return;
                }
                count--;

                if (count <= 2)
                {
                    pane.setStyle("-fx-background-color: lightgreen;");
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        counter.setText(String.valueOf(count));
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                counter.setText("");
            }
        });

        counterRunning = false;
    }

    private void parse()
    {
        try {
            String s;
            while ((s = reader.readLine()) != null) {
                parseLine(s.substring(s.indexOf(']') + 1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void pollClient(WatchKey key)
    {
        while(true)
        {
            try {
                Thread.sleep(100);
//                if (counterRunning) continue;
                List<WatchEvent<?>> s = key.pollEvents();
                if (s.size() >= 1)
                {
                    parse();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}