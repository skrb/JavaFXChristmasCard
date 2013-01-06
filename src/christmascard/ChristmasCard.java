package christmascard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.javainthebox.caraibe.svg.SVGContent;
import net.javainthebox.caraibe.svg.SVGLoader;

public class ChristmasCard extends Application {
    private static final Color TRANSPARENT = Color.rgb(255, 255, 255, 0.0);
    
    private Canvas snowCanvas;
    private Group illuminationPane;
    private List<SnowParticle> particles;
    
    private Random random = new Random();
    
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        
        // load background image
        ImageView background = new ImageView(new Image(getClass().getResource("scene.jpg").toString()));
        illuminationPane = new Group();
        initIllumination();
        
        snowCanvas = new Canvas(902, 600);
        snowCanvas.setBlendMode(BlendMode.ADD);
        
        root.getChildren().addAll(background, illuminationPane, snowCanvas);
        
        Scene scene = new Scene(root, 902, 600);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("JavaFX Christmas Card");
        stage.show();
        
        initSnowParticle();
        
        // Animation for snow flying
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }
    
    private void initIllumination() {
        // Character glowing animation
        Timeline timeline = new Timeline();
        
        // Loading "Merry Christmas" character from SVG file
        SVGContent svgContent = SVGLoader.load(getClass().getResource("illumination.svg").toString());
        
        for (int i = 1; i < 15; i++ ) {
            Shape ch = (Shape)svgContent.getNode(String.format("merry%02d", i));
            ch.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.YELLOW, 20.0, 0.4, 0.0, 0.0));
            ch.setStroke(TRANSPARENT);
            ch.setFill(TRANSPARENT);
            ch.setTranslateX(50);
            ch.setTranslateY(40);

            illuminationPane.getChildren().add(ch);

            KeyFrame frame0 = new KeyFrame(Duration.ZERO,
                                           new KeyValue(ch.strokeProperty(), TRANSPARENT),
                                           new KeyValue(ch.fillProperty(), TRANSPARENT));
            KeyFrame frame1 = new KeyFrame(Duration.seconds(2),
                                           new KeyValue(ch.strokeProperty(), TRANSPARENT),
                                           new KeyValue(ch.fillProperty(), TRANSPARENT));
            KeyFrame frame2 = new KeyFrame(Duration.seconds(5),
                                           new KeyValue(ch.strokeProperty(), Color.YELLOW),
                                           new KeyValue(ch.fillProperty(), TRANSPARENT));
            KeyFrame frame3 = new KeyFrame(Duration.seconds(6),
                                           new KeyValue(ch.strokeProperty(), Color.YELLOW),
                                           new KeyValue(ch.fillProperty(), Color.WHITE));
            KeyFrame frame4 = new KeyFrame(Duration.seconds(10),
                                           new KeyValue(ch.strokeProperty(), Color.YELLOW),
                                           new KeyValue(ch.fillProperty(), Color.WHITE));
            KeyFrame frame5 = new KeyFrame(Duration.seconds(12),
                                           new KeyValue(ch.strokeProperty(), Color.YELLOW),
                                           new KeyValue(ch.fillProperty(), TRANSPARENT));
            KeyFrame frame6 = new KeyFrame(Duration.seconds(15),
                                           new KeyValue(ch.strokeProperty(), TRANSPARENT),
                                           new KeyValue(ch.fillProperty(), TRANSPARENT));
            timeline.getKeyFrames().addAll(frame0, frame1, frame2, frame3, frame4, frame5, frame6);
        }
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void initSnowParticle() {
        particles = new ArrayList<>();
        
        // Initially, there are 5 snow particles
        int size = random.nextInt(5);
        for (int i = 0; i < size; i++) {
            particles.add(new SnowParticle(random.nextDouble() * snowCanvas.getWidth()));
        }
    }

    private void update() {
        GraphicsContext context = snowCanvas.getGraphicsContext2D();
        
        context.setFill(Color.rgb(0, 0, 0, 0.0));
        context.fillRect(0, 0, snowCanvas.getWidth(), snowCanvas.getHeight());

        Iterator<SnowParticle> it = particles.iterator();
        while (it.hasNext()) {
            SnowParticle particle = it.next();
            
            double y = particle.update();
            if (y >= snowCanvas.getHeight()) {
                it.remove();
            } else {       
                particle.draw(context);
            }
        }
        
        if (random.nextInt(3) == 0) {
            particles.add(new SnowParticle(random.nextDouble() * snowCanvas.getWidth()));
        }
    }
    
    public static void main(String... args) {
        launch(args);
    }
}
