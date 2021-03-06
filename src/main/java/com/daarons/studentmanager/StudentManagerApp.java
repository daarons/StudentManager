package com.daarons.studentmanager;

import com.daarons.config.SpringConfig;
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
import org.apache.logging.log4j.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class StudentManagerApp extends Application {

    private static final Logger log = LogManager.getLogger(StudentManagerApp.class);
    private AbstractApplicationContext applicationContext;
    private final String WINDOW_ICON_URL1 = "/image/16x16-icon.png";
    private final String WINDOW_ICON_URL2 = "/image/20x20-icon.png";
    private final String WINDOW_ICON_URL3 = "/image/24x24-icon.png";
    private final String WINDOW_ICON_URL4 = "/image/32x32-icon.png";  
    private Pane splashLayout;
    private Stage splashStage, mainStage;

    @Override
    public void init() {
        //stops unnecessary logging for hibernate/c3p0
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
    }

    @Override
    public void start(Stage stage) throws Exception {

        final Task startSpring = new Task() {
            @Override
            protected Object call() throws InterruptedException {
                //starts Spring context
                applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
                //saves context for access later
                SpringConfig.setApplicationContext(applicationContext);
                //starts entity manager factory
                applicationContext.getBean(LocalContainerEntityManagerFactoryBean.class);
                return null;
            }
        };
        startSpring.setOnSucceeded((Event event) -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1.2), splashLayout);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(actionEvent -> {
                splashStage.close();
                createMainStage();
                mainStage.show();
                SpringConfig.setStage(mainStage);
            });
            fade.play();
        });

        createSplash();
        splashStage.show();

        new Thread(startSpring).start();
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
        //closes Spring
        applicationContext.close();
    }

    private void createMainStage() {       
        FXMLLoader fxmlLoader = null;
        Parent root = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            root = fxmlLoader.load();
        } catch (Exception ex) {
            log.error("Couldn't load home.fxml", ex);
        }
        Scene scene = new Scene(root);

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

        mainStage = new Stage();
        mainStage.getIcons().addAll(new Image(WINDOW_ICON_URL1),
                new Image(WINDOW_ICON_URL2), new Image(WINDOW_ICON_URL3),
                new Image(WINDOW_ICON_URL4));
        mainStage.setTitle("Student Manager");
        mainStage.setScene(scene);
        mainStage.setHeight(screenSize.getHeight());
        mainStage.setWidth(473);
        mainStage.setX(screenSize.getMinX());
        mainStage.setY(screenSize.getMinY());
    }

    private void createSplash() {
        splashStage = new Stage();
        splashStage.getIcons().addAll(new Image(WINDOW_ICON_URL1),
                new Image(WINDOW_ICON_URL2), new Image(WINDOW_ICON_URL3),
                new Image(WINDOW_ICON_URL4));

        Label title = new Label("Student Manager");
        title.setFont(Font.font(STYLESHEET_MODENA, 35));
        StackPane.setAlignment(title, Pos.BOTTOM_CENTER);
        title.setStyle("-fx-padding: 0 0 20px 0; -fx-font-weight: bold;");

        ProgressBar progress = new ProgressBar();
        progress.setMaxWidth(300);
        StackPane.setAlignment(progress, Pos.BOTTOM_LEFT);

        splashLayout = new StackPane();
        splashLayout.setMinSize(300, 300);
        splashLayout.setMaxSize(300, 300);
        splashLayout.setStyle("-fx-background-image: url('/image/splash.jpeg');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-background-position: center;"
                + "-fx-background-size: cover");
        splashLayout.getChildren().addAll(title, progress);

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);

        splashStage.setScene(splashScene);
        splashStage.initStyle(StageStyle.TRANSPARENT);
    }
}
