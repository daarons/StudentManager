package com.daarons.studentmanager;

import com.daarons.DAO.EMSingleton;
import java.io.*;
import java.util.logging.LogManager;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.Duration;

public class StudentManagerApp extends Application {
    private final String sep = File.separator;
    private final String SPLASH_IMAGE_URL = "src" + sep + "main" + sep
            + "resources" + sep + "image" + sep + "splash.jpeg";
    private Pane splashLayout;
    private Stage splashStage, mainStage;

    @Override
    public void start(Stage stage) throws Exception {       

        final Task connectToDB = new Task() {
            @Override
            protected Object call() throws InterruptedException {
                //turns off logging b/c derby logs a lot on startup
                LogManager.getLogManager().reset(); 
                //connects to db
                EMSingleton.getEntityManager(); 
                return null;
            }
        };        
        connectToDB.setOnSucceeded((Event event) -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1.2), splashLayout);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(actionEvent -> {
                splashStage.close();
                showMainStage();
            });
            fade.play();
        });
        
        showSplash();
        
        new Thread(connectToDB).start();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        EMSingleton.getEntityManager().close();
        EMSingleton.getEntityManagerFactory().close();
    }

    private void showMainStage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
        } catch (IOException ex) {
            System.out.println(ex);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        mainStage = new Stage();
        mainStage.setTitle("Student Manager");
        mainStage.setScene(scene);
        mainStage.setHeight(screenSize.getHeight());
        mainStage.setWidth(473);
        mainStage.setX(screenSize.getMinX());
        mainStage.setY(screenSize.getMinY());
        mainStage.show();
    }

    private void showSplash() {
        splashStage = new Stage();
        
        File imageFile = new File(SPLASH_IMAGE_URL);
        ImageView splashImage = new ImageView(new Image(imageFile.toURI().toString()));
        splashImage.setFitHeight(300);
        splashImage.setFitWidth(300);
        splashImage.setPreserveRatio(true);
        
        Label title = new Label("Student Manager");
        title.setFont(Font.font(STYLESHEET_MODENA, 35));
        StackPane.setAlignment(title, Pos.BOTTOM_CENTER);
        title.setStyle("-fx-padding: 0 0 20px 0; -fx-font-weight: bold;");
        
        ProgressBar progress = new ProgressBar();
        progress.setMaxWidth(300);
        StackPane.setAlignment(progress, Pos.BOTTOM_LEFT);
        
        splashLayout = new StackPane();
        splashLayout.setMaxSize(300, 300);
        splashLayout.getChildren().addAll(splashImage, title, progress);
        
        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        
        splashStage.setScene(splashScene);
        splashStage.initStyle(StageStyle.TRANSPARENT);
        splashStage.show();
    }
}
