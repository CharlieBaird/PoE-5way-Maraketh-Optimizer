package com.example.poetools;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static com.example.poetools.Main.stageSingleton;

public class Controller {
    @FXML
    private VBox pane;
    @FXML
    private Label counter;
    @FXML
    private HBox hBox;
    private int needed = 1;
    private String name = "";
    double xOffset;
    double yOffset;
    BufferedReader reader;
    int bossCounter = 0;
    boolean counterRunning = false;
    int count;

    @FXML
    protected void beginE() {
        name = "Lioneye";
        begin5Way();
    }
    @FXML
    protected void beginK() {
        name = "Hyrri";
        begin5Way();
    }
    @FXML
    protected void beginV() {
        name = "Viper Napu";
        begin5Way();
    }
    @FXML
    protected void beginT() {
        name = "Cardinal Sanctus";
        begin5Way();
    }
    @FXML
    protected void beginM() {
        name = "Aukuna";
        needed = 3;
        begin5Way();
    }

    public void reset()
    {
        hBox.setVisible(true);
        counter.setVisible(false);
        needed = 1;
        name = "";
        counterRunning = false;
        bossCounter = 0;

        pane.setStyle("-fx-background-color: white;");
        counter.setText("Ready");
        count = 25;
    }

    @FXML
    protected void initialize()
    {
        pane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY)
            {
                reset();
            }
        });

        pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    xOffset = stageSingleton.getX() - event.getScreenX();
                    yOffset = stageSingleton.getY() - event.getScreenY();
                }
            }
        });

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    stageSingleton.setX(event.getScreenX() + xOffset);
                    stageSingleton.setY(event.getScreenY() + yOffset);
                }
            }
        });
    }

    @FXML
    protected void begin5Way()
    {
        hBox.setVisible(false);
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

    private void parseLine(String str)
    {
        if (str.contains(name))
        {
            if (counterRunning) return;

            bossCounter++;
            if (bossCounter == needed)
            {
                counterRunning = true;
                bossCounter = 0;
                pane.setStyle("-fx-background-color: white;");
                Thread thread = new Thread(() -> {
                    startCounter();
                });
                thread.start();
            }
        }

        else if (str.contains("Primeval Hideout") || str.contains("Domain of Timeless"))
        {
            bossCounter = 0;
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
                Thread.sleep(250);
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