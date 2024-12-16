package com.example.stopper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    @FXML
    private Button otherButton;
    @FXML
    private ListView<String> timeListView;
    @FXML
    private Button startStopButton;
    @FXML
    private Label timerLabel;

    private LocalDateTime start;
    private Duration elapsedTime = Duration.ZERO;
    private Timer timer;

    private static final DateTimeFormatter TIMER_FORMATTER = DateTimeFormatter.ofPattern("mm:ss:SSS");

    @FXML
    public void onStartStopButtonClick(ActionEvent actionEvent) {
        if (startStopButton.getText().equals("Start")) {
            if (elapsedTime.isZero()) {
                start();
            } else {
                resume();
            }
        } else {
            stop();
        }
    }

    @FXML
    public void onOtherButtonClick(ActionEvent actionEvent) {
        if (otherButton.getText().equals("Reset")) {
            reset();
        } else if (otherButton.getText().equals("Részidő")) {
            recordLapTime();
        }
    }

    private void start() {
        startStopButton.setText("Stop");
        otherButton.setText("Részidő");
        start = LocalDateTime.now();
        timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Duration currentDuration = Duration.between(start, LocalDateTime.now()).plus(elapsedTime);
                Platform.runLater(() -> timerLabel.setText(formatDuration(currentDuration)));
            }
        }, 0, 10);
    }

    private void stop() {
        startStopButton.setText("Start");
        otherButton.setText("Reset");
        timer.cancel();
        elapsedTime = elapsedTime.plus(Duration.between(start, LocalDateTime.now()));
    }

    private void resume() {
        start = LocalDateTime.now();
        start();
    }

    private void reset() {
        startStopButton.setText("Start");
        otherButton.setText("Reset");
        elapsedTime = Duration.ZERO;
        timerLabel.setText(formatDuration(elapsedTime));
        timeListView.getItems().clear();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void recordLapTime() {
        String lapTime = timerLabel.getText();
        timeListView.getItems().add("Lap: " + lapTime);
    }

    private String formatDuration(Duration duration) {
        long millis = duration.toMillis();
        long minutes = millis / 60000;
        long seconds = (millis % 60000) / 1000;
        long milliseconds = millis % 1000;

        return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
    }
}
