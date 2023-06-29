package lk.ijse.chat_app.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Clock {
    public static void setClock(Label clock){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event -> clock.setText(
                                new SimpleDateFormat("hh:mm:ss a").format(Calendar.getInstance().getTime()))),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}