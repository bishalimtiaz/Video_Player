package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    MediaPlayer player;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button play_btn;

    @FXML
    private Button prev_btn;

    @FXML
    private Button forward_btn;

    @FXML
    private Slider timeSlider;


    @FXML
    void openSongMenu(ActionEvent event) {




        try {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);



            if (player != null){
                player.dispose();
            }
            setVideoNplay(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void playVideo(ActionEvent event) {

        //player.play();



        try {
            MediaPlayer.Status status = player.getStatus();

            if(status == MediaPlayer.Status.PLAYING){
                //Video is Playing and we need to pause it
                player.pause();
                play_btn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));

            }

            else{
                player.play();
                play_btn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.png"))));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            play_btn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
            prev_btn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/rewind.png"))));
            forward_btn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/fast_forward.png"))));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //For Rewinding the Video
    // this function rewind the video 10 second

    @FXML
    void rewindVideo(ActionEvent event) {

        Double d = player.getCurrentTime().toSeconds();
        d = d-10;
        player.seek(new Duration(d*1000));

    }


    // For Forwarding te video
    // this function forward the video 10 second

    @FXML
    void forwardVideo(ActionEvent event) {
        Double d = player.getCurrentTime().toSeconds();
        d = d+10;
        player.seek(new Duration(d*1000));

    }

    private void setVideoNplay(String path)
    {

        System.out.println(path);
        Media media =new Media(path);
        player = new MediaPlayer(media);
        mediaView.setMediaPlayer(player);
        player.play();
        try {
            play_btn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //when player is set the slider
        player.setOnReady(()->{

            timeSlider.setMin(0);
            timeSlider.setMax(player.getMedia().getDuration().toMinutes());
            timeSlider.setValue(0);

        });


        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                Duration d = player.getCurrentTime();
                timeSlider.setValue(d.toMinutes());
            }
        });


        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (timeSlider.isPressed()){
                    double val = timeSlider.getValue();
                    player.seek(new Duration(val*60*1000));
                }
            }
        });
    }
}
