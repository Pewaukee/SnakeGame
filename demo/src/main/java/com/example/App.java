package com.example;

// for writing to files
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException; // required for application
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList; // a list of pivots
import java.util.Random; // to make random position of the apple circle

import javafx.animation.KeyFrame; // for task running
import javafx.animation.Timeline; // to run a timed task every set amount of time
import javafx.application.Application; // a GUI app needs to extend Application
import javafx.collections.FXCollections;
import javafx.collections.ObservableList; // to find which children of group to delete
import javafx.event.ActionEvent; // for buttons, timer
import javafx.event.EventHandler; // for buttons, timer
import javafx.fxml.FXMLLoader; // to switch between screens
import javafx.scene.Group; // to make a rectangle outlined
import javafx.scene.Node; // for the ObservableLists
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent; // for sensing input (arrow keys)
import javafx.scene.layout.Pane; // the scene is put into a pane
import javafx.scene.paint.Color; // to change colors for fill rect and circle
import javafx.scene.shape.Circle; // to make the apple
import javafx.scene.shape.Rectangle; // to make the grid and snake
import javafx.scene.text.Font; // for font settings
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration; // for running time task using animation.Timeline


/**
 * JavaFX App
 */
public class App extends Application {

    // when using static, the variable can be accessed 
    // from other methods of the class without needing
    // to be passed as an argument I believe

    /*
     * What does static mean? When you declare a variable
     * or a method as static, it belongs to the class, 
     * rather than a specific instance. This means that 
     * only one instance of a static member exists, even 
     * if you create multiple objects of the class, or if 
     * you don't create any.
     */

    private static Scene scene;
    private static Pane pane;
    private static Button startButton;
    private static Button playAgain;
    private static Label startLabel;
    private static Label scoreLabel;
    private static Label score;
    private static Label highScore;
    private static Label gameOver;
    private static Group group;
    private static Group snakeGroup;
    private static Timeline timeline;
    private static Snake snake;
    private static int[] appleCoords;
    private static ArrayList<Location> pivots;
    private static String pathname = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\SnakeGame\\demo\\src\\main\\java\\com\\highScores.txt";
    private static File file = new File(pathname);
    private static int highestScore = 0;
    private static int timeInteval = 300;

    @Override
    public void start(Stage stage) throws IOException {
        // starting text label
        startLabel = new Label("Welcome to Snake Game, \npress the Play! button \nto start");
        startLabel.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 30));
        startLabel.setTextFill(Color.WHITE);
        // use label.setTranslateX and label.setTranslateY
        // top left corner of startLabel at (x=50, y=50)
        startLabel.setTranslateX(50.0); 
        startLabel.setTranslateY(50.0);
        
        // score label
        scoreLabel = new Label("Score: ");
        scoreLabel.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 15));
        scoreLabel.setTextFill(Color.RED);
        scoreLabel.setTranslateX(215.0);
        scoreLabel.setTranslateY(450.0);

        // score (to be edited)
        score = new Label("0");
        score.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 15));
        score.setTextFill(Color.RED);
        score.setTranslateX(265.0);
        score.setTranslateY(450.0);

        // gameOver label
        gameOver = new Label("Game over! \nWant to play again?");
        gameOver.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 30));
        gameOver.setTextFill(Color.WHITE);
        gameOver.setVisible(false); // default is to not be seen
        gameOver.setTranslateX(100.0);
        gameOver.setTranslateY(100.0);

        // high score label, but set the text only when starting game
        highScore = new Label();
        highScore.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
        highScore.setTranslateX(25.0);
        highScore.setTranslateY(450.0);
        highScore.setTextFill(Color.BLUE);

        // starting play button
        startButton = new Button("Play!");
        // top left corner of startButton at (x=225, y=225)
        startButton.setTranslateX(225.0);
        startButton.setTranslateY(225.0);

        // play again button
        playAgain = new Button("Play Again!");
        playAgain.setTranslateX(350.0);
        playAgain.setTranslateY(450.0);
        playAgain.setDisable(true);
        playAgain.setVisible(false);
        
        // create a group object to add shapes, labels, and buttons.
        // the pane object is needed with group arg
        group = new Group();
        snakeGroup = new Group();
        pane = new Pane(group, snakeGroup); // a different snakeGroup makes easy for snake deletion

        // this double for loop creates a grid of black squares
        // to be used as the playing space, grid is 20x20
        
        for (int i = 0; i < 20; i++) { 
            for (int j = 0; j < 20; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(25+21*i);
                rectangle.setY(25+21*j);
                rectangle.setWidth(20);
                rectangle.setHeight(20);
                // every rectangle is added to the group with predefined characteristics
                group.getChildren().add(rectangle); 
            }
        }

        group.getChildren().add(startLabel);
        group.getChildren().add(startButton);
        group.getChildren().add(scoreLabel);
        group.getChildren().add(score);
        group.getChildren().add(gameOver);
        group.getChildren().add(playAgain);
        group.getChildren().add(highScore);

        startButton.setOnAction(new EventHandler<ActionEvent>() { // defining a function for startButton press
            @Override
            public void handle(ActionEvent event) {
                startGame();      
            }
        });

        playAgain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame();
            }
        });

        /* creating scene
         * create the scene with all added group elements,
         * the length and width of window being = 475,
         * and the background color white
         * must use pane to construct properly and add/ remove snake parts
         */
        
        scene = new Scene(pane, 475, 500, Color.WHITE);
        
        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        
        
    }

    private void startGame() {
        /* purpose
         * this function starts or restarts the game
         * its a function that can be called forever, no matter
         * how many times the playagain button is pressed
         */
        startLabel.setVisible(false);
        gameOver.setVisible(false);

        startButton.setVisible(false);
        startButton.setDisable(true);
        playAgain.setVisible(false);
        playAgain.setDisable(true);

        score.setText("0"); // put score back to 0        

        try {
            fileWriterAndReader();
            highScore.setText("High score: " + highestScore);
        } catch (IOException e) {System.out.println("error occured"); e.printStackTrace();}

        snake = new Snake();
        snakeMake();
        appleCoords = makeNewApple();
        pivots = new ArrayList<Location>();

                
        // taken from https://stackoverflow.com/questions/30589470/is-there-a-more-elegant-way-of-executing-timertasks-on-javafx-application-thread
        timeline = new Timeline(
            new KeyFrame(Duration.millis(timeInteval), // millis = milliseconds
            new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) { // this func runs every 500 ms
                snakeDelete();
                if (checkCollision() || outOfBounds()) {
                    // call endgame function to display score and such
                    // endGame();
                    //System.exit(100);
                    try {endGame();}
                    catch (IOException e) {e.printStackTrace();}
                    
                    timeline.stop();
                    return;
                }
                    updateCoordinatesAndSize();
                    snakeMake();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /* purpose
             * using javafx, we can incorporate event handlers
             * in almost the same way as an ActionListener. Also, 
             * even if the snake is not defined yet, the NullPointerException
             * is likely thrown from this function and a simple print statement 
             * doesn't work, which would be my guess anyways
             * also, default: break; gets rid of the warning
             * idea was found here: https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
             * 
             * also, don't want an up and down, or left and right next to each other 
             * in the pivot ArrayList, so need some checking statements for that
             * 
             * also in if statements the pivots == 0 condition should be checked first 
             * to avoid IndexOufOfBoundsException
             */
            @Override
            public void handle(KeyEvent event) {
                Location head = snake.getBodyParts().get(0); // head of snake switches direction
                int headX = snake.getHeadCoordinates()[0];
                int headY = snake.getHeadCoordinates()[1];
                try {
                    // only need to check head direction and if pivot already exists in certain square
                    switch (event.getCode()) {
                        case UP: 
                            if (!head.getDirection().equals("down") && !pivotExists(headX, headY)) {
                                head.setDirection("up");
                                pivots.add(new Location(headX, headY, "up")); 
                            }
                            break;
                        case DOWN: 
                            if (!head.getDirection().equals("up") && !pivotExists(headX, headY)) {
                                head.setDirection("down"); 
                                pivots.add(new Location(headX, headY, "down"));
                            }
                            break;
                        case RIGHT: 
                            if (!head.getDirection().equals("left") && !pivotExists(headX, headY)) {
                                head.setDirection("right");
                                pivots.add(new Location(headX, headY, "right"));
                            }
                            break;
                        case LEFT: 
                            if (!head.getDirection().equals("right") && !pivotExists(headX, headY)) {
                                head.setDirection("left"); 
                                pivots.add(new Location(headX, headY, "left"));
                            }
                            break;
                        default: break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("indexOoB excep from handle()");
                }
            }
        });
    }

    private boolean pivotExists(int x, int y) {
        /* purpose
         * checks to see if there is an available pivot
         * to use, this should be done so that one grid point
         * does not have more than one pivot because the snake can't
         * change direction twice at one point
         */
        for (Location pivot: pivots) {
            if (x == pivot.getX() && y == pivot.getY()) return true;
        }
        return false;
    }

    private void snakeMake() {
        /* purpose
         * make the snake by adding to snakeGroup.getChildren()
         * using a green color and the same width and 
         * height as the grid pieces
         */
        for (int i = 0; i < snake.getBodyParts().size(); i++) {
            Location bodyPart = snake.getBodyParts().get(i);
            Rectangle r = new Rectangle();
            r.setX(bodyPart.getX());
            r.setY(bodyPart.getY());
            r.setWidth(20);
            r.setHeight(20);
            if (i == 0) r.setFill(Color.CYAN); // head color
            else r.setFill(Color.GREEN); // body color
            snakeGroup.getChildren().add(r);
        }    
    }

    private void snakeDelete() {
        /* purpose
         * delete all the snake parts, which is very easy
         * when I'm able to add multiple groups to a pane
         * so the snake group just has to be cleared before being 
         * remade
         */
        snakeGroup.getChildren().clear(); 
    }

    private boolean checkCollision() {
        /* purpose
         * see that the head coordinates are not the same
         * as the coordinates of any other part of the snake
         * can do this by checking getBodyParts()
         * if headX and headY match any of the other
         * body parts' coordinates, then return true, else false
         */
        int headX = snake.getHeadCoordinates()[0];
        int headY = snake.getHeadCoordinates()[1];
        for (int i = 1; i < snake.getBodyParts().size(); i++) { // skip the head element
            Location bodyPart = snake.getBodyParts().get(i);
            if ((headX == bodyPart.getX()) && (headY == bodyPart.getY())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean outOfBounds() {
        /* purpose
         * using the headCoordinates, check to make
         * sure whether or not the snake's head has left the
         * grid positioning, which can be checked due to the fact 
         * that the grid is 20x20
         * using < and > also allow the snake to travel out of bounds
         */
        int headX = snake.getHeadCoordinates()[0];
        int headY = snake.getHeadCoordinates()[1];
        if (headX <= 25 || headX >= 25+21*20) return true;
        if (headY <= 25 || headY >= 25+21*20) return true;
        return false;
    }

    private void updateCoordinatesAndSize() {
        /* purpose
         * update the snake's coordinates based on direction
         * attribute, then move it the corresponding amount of squares
         * afterwards, see if the snake has collected the apple circle
         * if the head coordinates are the same as where the apple is
         * randomly positioned
         */
        int headX = snake.getHeadCoordinates()[0];
        int headY = snake.getHeadCoordinates()[1];
        for (int i = 0; i < snake.getBodyParts().size(); i++) {
            int changeX = 0;
            int changeY = 0;
            Location bodyPart = snake.getBodyParts().get(i);
            

            // determine if a change in direction needs to be made
            /* exception
             * trying to remove in the loop causes 
             * java.util.ConcurrentModificationException,
             * remove it later
             */
            
            for (Location pivot: pivots) {
                if (bodyPart.getX() == pivot.getX() && bodyPart.getY() == pivot.getY()) {
                    bodyPart.setDirection(pivot.getDirection());
                }
            }       
            switch (bodyPart.getDirection()) {
                case "up":
                    changeY = -21;
                    break;
                case "down":
                    changeY = 21;
                    break;
                case "right":
                    changeX = 21;
                    break;
                case "left":
                    changeX = -21;
                    break;
                default: // hope this doesn't ever happen
                    System.out.println("couldn't find a direction");
                    System.exit(42);
                    break;
            }
            if (i == 0) { // when the head of snake is changed, want to update head coordinates
                snake.setHeadCoordinates(snake.getHeadCoordinates()[0] + 
                    changeX, snake.getHeadCoordinates()[1] + changeY);
            }
            if (i == snake.getBodyParts().size()-1) { // when tail is changed, update tail coords
                snake.setTailCoordinates(snake.getTailCoordinates()[0] + 
                    changeX, snake.getTailCoordinates()[1] + changeY);
            }
            bodyPart.setX(bodyPart.getX() + changeX);
            bodyPart.setY(bodyPart.getY() + changeY);
            snake.changeBodyPart(i, bodyPart);
        }
        if (headX == appleCoords[0] && headY == appleCoords[1]) {
            insertNewBodyPart();
            snake.setLength(snake.getLength() + 1);
            appleCoords = makeNewApple();
            updateScore();
        }
        /* for deletion of a pivot
         * when the tail coordinates match the first pivot
         * inserted, this means that then there is no
         * more snake bodyparts to pivot on, therefore
         * the pivot should be deleted
         * 
         * this should be outside the loop, because then all
         * the bodyparts will have new directions, and
         * then it can be erased if needed
         * 
         * need a try catch because when the program starts
         * there are no pivots
         * 
         * also need to set the direction of the tail element
         * before deleting the pivot so that it can continue 
         * in the correct direction; it would initially travel in
         * the same direction without
         * snake.getBodyParts().get(snake.getLength()-1).setDirection() = pivots.get(0).getDirection(); 
         */
        try {
            if (snake.getTailCoordinates()[0] == pivots.get(0).getX()
                && snake.getTailCoordinates()[1] == pivots.get(0).getY()) {
                snake.getBodyParts().get(snake.getLength()-1).setDirection(pivots.get(0).getDirection());
                pivots.remove(0);
            }
        } catch (IndexOutOfBoundsException e) {}
    }

    private int[] makeNewApple() {
        /* purpose
         * this function uses the group object
         * to add in an apple for the snake to grab
         * we want the apple to not be in conflict with
         * any of the snake body parts, so we are going to 
         * make a while loop and generate random numbers and test it
         * also must delete the old apple so need to find it in the list
         * of styleProperties(), the fill value if "0xff0000ff"
         */
        ObservableList<Node> toDelete = FXCollections.observableArrayList();
        for (int z = 0; z < group.getChildren().size(); z++) {
            String property = group.getChildren().get(z).styleProperty().toString();
            try {
                int index1 = property.indexOf("fill=");
                int index2 = property.indexOf("]");
                // get substring of fill element
                String colorSubString = property.substring(index1+5, index2); 
                if (colorSubString.equals("0xff0000ff")) {
                    toDelete.add(group.getChildren().get(z));
                }
            } catch (StringIndexOutOfBoundsException e) { 
                /* exception examples
                 * this exception will be thrown on 
                 * button and label objects added to the 
                 * program for easy use, so we don't want to 
                 * scan these for color, and the fill attr 
                 * doesn't show up anyways for these objects
                 */
                continue;
            }
        }
        for (Node node: toDelete) { // delete each apple from the screen (there should only be 0 or 1)
            group.getChildren().remove(node);
        }
        //create new apple
        Random random = new Random();
        boolean collision = false;
        int xCoord, yCoord;
        do {
            xCoord = 25 + 21 * random.nextInt(20);
            yCoord = 25 + 21 * random.nextInt(20);
            for (Location bodyPart: snake.getBodyParts()) {
                if (bodyPart.getX() == xCoord && bodyPart.getY() == yCoord) {
                    collision = true;
                    break;
                }
            }
        } while (collision);
        Circle apple = new Circle();
        /* apple making
         * since the apple's center is at the top left 
         * edge of the grid, the center has to be adjusted by
         * half the width and height to get it in the middle of 
         * one of the grid things
         */
        apple.setCenterX(xCoord + 10);
        apple.setCenterY(yCoord + 10);
        apple.setRadius(10);
        apple.setFill(Color.RED);
        group.getChildren().add(apple);
        
        int[] res = new int[2];
        res[0] = xCoord;
        res[1] = yCoord;

        return res;
        
    }

    private void insertNewBodyPart() {
        /* purpose
         * find the place to insert a new snake part to the snake
         * do this by calling Snake.addBodyPart(Location location)
         * get the correct location by checking the second last element
         * and putting the tail whenever it is opposite
         * 
         * for example, if the snake is like so
         *    o1
         *    o2
         *    o3
         *    o4  o5
         * then insert to the right of o5
         * or can do it based on direction 
         */
        int tailX = snake.getTailCoordinates()[0];
        int tailY = snake.getTailCoordinates()[1];
        String direction = snake.getBodyParts().get(snake.getBodyParts().size()-1).getDirection();
        Location newLocation = null;
        switch (direction) { // the placements have to be in the opposite direction
            case "up":
                newLocation = new Location(tailX, tailY+21, direction); break;
            case "down":
                newLocation = new Location(tailX, tailY-21, direction); break;
            case "right":
                newLocation = new Location(tailX-21, tailY, direction); break;
            case "left":
                newLocation = new Location(tailX+21, tailY, direction); break;
            default:
                System.out.println("Couldn't add bodypart, couldn't find direction");
                System.exit(5);
                break;
        }
        snake.addBodyPart(newLocation);
        snake.setTailCoordinates(newLocation.getX(), newLocation.getY());
        
    }

    private void updateScore() {
        String text = score.getText();
        int number = Integer.valueOf(text) + 1;
        score.setText(String.valueOf(number));
        if (number % 5 == 0) { // every 5 points, speed increases by 25%
            timeInteval = (int)(timeInteval * 0.75);
        }
    }

    private void fileWriterAndReader() throws IOException {
        /* purpose
         * read the highest score from the file to be used 
         * for the highScoreLabel, then find the curscore and
         * update if necessary
         * also if curScore <= highScore, the highScore
         * must be rewritten to the file
         */
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        while ((str = br.readLine()) != null) {
            highestScore = Integer.valueOf(str);
        }
        br.close();

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(pathname), StandardCharsets.UTF_8));
            if (Integer.valueOf(score.getText()) > highestScore) {
                writer.write(score.getText());
            }
            else {
                writer.write(String.valueOf(highestScore)); // high schore must be rewritten to the file
            }
            writer.close();
        } 
        catch (IOException e) {System.out.println("error"); e.printStackTrace();}  
        
    }

    private void endGame() throws IOException {
        /* purpose
         * set the required text fields and buttons to
         * be either visible or not, then proceed to write
         * the score to the file for displaying highScores
         */
        gameOver.setVisible(true);
        playAgain.setDisable(false);
        playAgain.setVisible(true);
        fileWriterAndReader();
        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}

/*
 * additional notes
 * 
 * attempting to add objects like labels and buttons using gridpane
 * this does work except when trying to add rectangles
 * gridPane = new GridPane();
 * gridPane.add(startLabel, 1, 0); // add start label to location (0, 1) (x, y)
 * gridPane.add(startButton, 1, 2); // add start button to location (1, 1) (x, y)
 * gridPane.setPadding(new Insets(20, 20, 20, 20)); // padding around grid
 * gridPane.setVgap(100);
 * gridPane.setHgap(10);
 * 
 * pane.getChildren().add(canvas); currently no Canvas object when using Group()
 * 
 * FXML is an XML-based user interface markup language 
 * created by Oracle Corporation for defining the user 
 * interface of a JavaFX application
 * scene = new Scene(loadFXML("primary"), 640, 480); 
 * 
 * other code:
 *  making the grid at which the snake will travel
        pane = new Pane();
        scene = new Scene(pane);
        canvas = new Canvas(500, 500);

        Get the canvas' graphics context to draw
        graphicsContext = canvas.getGraphicsContext2D();

         set the color 
        Color boardColor = Color.BLACK;
        graphicsContext.setFill(boardColor);
        creating a 20x20 grid of black squares
         
        for (int i = 0; i < 20; i++) { 
            for (int j = 0; j < 20; j++) {
                graphicsContext.fillRect(25+21*i, 25+21*j, 20, 20); // (x, y, width, height)
            }
        }
 *
 * 
 * group.getChildren().remove(startButton); // remove the button entirely
 * this may be effective, however concern arises when wanting to play the game again
 * for example, don't want to have to re-add the button but i might have to make an
 * infinite loop through my code anyways or like a while true and if a certain button is
 * pressed then the eventHandler closes the program
 * 
 * remove object from scene in javafx with Group
 * Group.getChildren().remove(Object);
 * 
 * /*general code for a timer and to scheudle a timertask in the 
    * same file, timertask runs in a seperate thread, so other
    * commands and code will run
    * I believe a cancel() method on the timertask is not needed
    * because java garbage collector will do, but can use for cleanliness
    * isDaemon = true terminates this process once quit button is pressed
    *
    * also need a runnable object, this is because otherwise i get error
    * java.lang.IllegalStateException: Not on FX application thread
    * this is because the timer thread is not on the same thread as fx
    * therefore, the actual run() function of runnable needs to be 
    * executed after the timer function kind of closes
    * can maybe also use java's animation class Timeline
    * view https://stackoverflow.com/questions/30589470/is-there-a-more-elegant-way-of-executing-timertasks-on-javafx-application-thread
                
 * new java.util.Timer(true).schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(
                                new Runnable() {
                                    public void run() {
                                        snakeMove(group, snake); // make a seperate snake group?                                      
                                        
                                    }   
                                }
                            );
                        }
                    }, 500); // add back the period arg
 *
 * moving code for the snake
 * the only problem with platform.runLater is that 
 * on internet it is usually advised to complete only quick 
 * tasks with this type of format, so although my code will likely
 * run quite fast and I could use this, I decided to go with the animation
 * class' Timeline to perform my tasks, because there isn't a javafx application
 * thread conflict as well
 * 
 * link to help understand why the scene isn't updating
 * https://edencoding.com/force-refresh-scene/#:~:text=The%20JavaFX%20scene%20can%20be,requirements%20for%20the%20scene%20graph.
 * the main problem however is because no pane object set
 * 
 * attempted code to try and delete snake using the fact that it was inserted last
 * for (int i = 0; i < snake.getLength(); i++) {
                            int length = group.getChildren().size();
                            System.out.println(group.getChildren().get(length-1));
                            group.getChildren().remove(length-1);
                        }
 * 
 * in order to work with java.awt.event.KeyEvent
 * had to include 
 * requires java.desktop; in module-info.java file
 * idk why, but it works\
 * https://www.codegrepper.com/code-examples/java/The+package+java.awt.event+is+not+accessible
 * actually don't need this now since using javafx.event.ActionEvent and javafx.event.EventHandler
 * 
 * for the makeNewApple() method, I had used the appleCoords argument which only worked
 * for one apple, this is because I would call the function updateCoordinates() function
 * with the previous defined appleCoords, so the appleCoords didn't update properly 
 * and only used the first version
 * 
 * old snake delete code
 *
 * 
 *  /* purpose
         * get the style property for each getChildren() object,
         * then see which of the objects have a green attribute
         * so that they can be deleted
         * green color is a fill = "0x008000ff"
         * so we search for that, add it to an ObservableList<Node> 
         * to match the type of group.getChildren() which is also ObservableList<Node>
         * and then iterate through all the green objects and delete
         */
        /*ObservableList<Node> toDelete = FXCollections.observableArrayList();
        for (int z = 0; z < group.getChildren().size(); z++) {
            String property = group.getChildren().get(z).styleProperty().toString();
            System.out.println(property);
            try {
                int index1 = property.indexOf("fill=");
                int index2 = property.indexOf("]");
                // get substring of fill element
                String colorSubString = property.substring(index1+5, index2); 
                if (colorSubString.equals("0x008000ff") ||
                    colorSubString.equals("0x0000ffff")) { // blue or green
                    toDelete.add(group.getChildren().get(z));
                }
            } catch (StringIndexOutOfBoundsException e) { 
                /* exception examples
                 * this exception will be thrown on 
                 * button and label objects added to the 
                 * program for easy use, so we don't want to 
                 * scan these for color, and the fill attr 
                 * doesn't show up anyways for these objects
                 *
                continue;
            }
        }
        for (Node node: toDelete) { // delete each snake part from the screen
            group.getChildren().remove(node);
        } 
 *
 * Location class attributes used to be protected, but decided to make them 
 * private and use getter and setter methods, perhaps a style choice but idk
 * 
 * trying to use java.awt action event as such
 * 
 * /*public void keyPressed (KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println("workd");
        try {
            switch (keyCode) {
                case KeyEvent.VK_UP: // if up arrow is pressed
                    snake.setDirection("up");
                    break;
                case KeyEvent.VK_DOWN: // if down arrow is pressed
                    snake.setDirection("down");
                    break;
                case KeyEvent.VK_RIGHT: // if right arrow is pressed
                    snake.setDirection("right");
                    break;
                case KeyEvent.VK_LEFT: // if left arrow is pressed
                    snake.setDirection("left");
                    break;
                default:
                    break;
            } 
        } catch (NullPointerException z) { // can't use 'e' again
            return;
        }
    }

    /*
     * these two methods, keyTyped() and keyReleased()
     * are necessary to override in order to implement
     * java.awt.event.KeyListener to the class App
     */
    
    /*@Override
    public void keyTyped(KeyEvent e) {
        System.out.println("key was typed");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("key was released");
    }
 * 
 * 
 */