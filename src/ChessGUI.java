import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;



public class ChessGUI extends Application {

    VBox root = new VBox();
    GridPane board = new GridPane();
    HBox controls = new HBox();






    public void start(Stage stage) throws Exception {


        // Create 64 rectangles and add to pane
        int count = 0;
        double s = 100; // side of rectangle
        for (int i = 0; i < 8; i++) {
            count++;
            for (int j = 0; j < 8; j++) {
                StackPane square = new StackPane();
                Rectangle r = new Rectangle(s, s, s, s);
                if (count % 2 == 0)
                    r.setFill(Color.WHITE);
                square.getChildren().add(r);
                board.add(square, j, i);
                count++;
            }
        }





        Button restart = new Button("Restart");
        controls.getChildren().add(restart);
        Button flipBoard = new Button("Flip Board");
        controls.getChildren().add(flipBoard);
        controls.setAlignment(Pos.CENTER);


        // Create a scene and place it in the stage
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene); // Place in scene in the stage
        root.getChildren().add(board);
        root.getChildren().add(controls);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
