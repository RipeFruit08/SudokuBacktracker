

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class test extends Application{

    @Override
    public void start ( Stage primaryStage ){
        
        /*
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction( new EventHandler<ActionEvent>() {

            @Override
            public void handle ( ActionEvent event ) {
                System.out.println ( "Hello World!" );
            }
        });
        */

        //StackPane root = new StackPane();
        //root.getChildren().add(btn);

        GridPane grid = new GridPane();
        grid.add(new Button(), 0, 0);
        grid.add(new Button(), 0, 1);
        grid.add(new Button(), 0, 2);
        grid.add(new Button(), 1, 0);
        grid.add(new Button(), 1, 1);
        grid.add(new Button(), 1, 2);
        grid.add(new Button(), 2, 0);
        grid.add(new Button(), 2, 1);
        grid.add(new Button(), 2, 2);

        Scene scene = new Scene(grid, 600, 600);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
