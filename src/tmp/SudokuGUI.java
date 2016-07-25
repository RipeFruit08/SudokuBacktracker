
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.List;

public class SudokuGUI extends Application implements Observer{

    private Button[][] cells;
    private SudokuModel model;
    
    @Override
    public void init(){
        System.out.println("init was called");
        Parameters p = getParameters();
        List<String> argv = p.getRaw();
        String fn = argv.get(0);
        this.model = new SudokuModel(fn);
        System.out.printf("config file: %s\n", fn); 
    }

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

        System.out.println("start was called"); 

        int dim = this.model.get_board_dim();
        this.cells = new Button[dim][dim];
        int[][] tmpboard = model.get_board();

        GridPane grid = new GridPane();
        for ( int r = 0; r < dim; r++ ){
            for ( int c = 0; c < dim; c++ ){
                //String text = "(" + r + ", " + c + ")";
                int val = tmpboard[r][c];
                Button b = new Button(String.valueOf(val));
                final int row = r;
                final int col = c;
                b.setOnAction( (event) -> {
                    System.out.println(row +", " + col); 
                    changeButton(row, col); 
                });
                cells[r][c] = b;
                grid.add(b, c, r);
            }
        }

        /*
        grid.add(new Button(), 0, 0);
        grid.add(new Button(), 0, 1);
        grid.add(new Button(), 0, 2);
        grid.add(new Button(), 1, 0);
        grid.add(new Button(), 1, 1);
        grid.add(new Button(), 1, 2);
        grid.add(new Button(), 2, 0);
        grid.add(new Button(), 2, 1);
        grid.add(new Button(), 2, 2);
        */

        Scene scene = new Scene(grid, 600, 600);
        
        // getting parameters passed in from invoking program
        //primaryStage.setTitle(argv.get(0));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void printHello(){
        System.out.println("hello"); 
    }

    private void changeButton(int r, int c){
        this.cells[r][c].setText("Hi");
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("update was called"); 
    }

    public static void main(String[] args) {
        launch(args);
    }

}
