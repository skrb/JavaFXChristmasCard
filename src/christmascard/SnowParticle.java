package christmascard;


import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;

public class SnowParticle {
    private static final double MAX_RADIUS = 5.0;
    private static final double MAX_BLUR = 3.0;
    private static final double MAX_STAGGER = 2.0;

    private double x;
    private double y;
    private double radius;
    private Color color;
    private BoxBlur blur;
    
    private Random random = new Random();
    
    public SnowParticle(double initX) {
        x = initX;
        y = 0.0;
        radius = random.nextDouble() * MAX_RADIUS;
        color = Color.rgb(255, 255, 255, random.nextDouble());
        double blurSize = random.nextDouble() * MAX_BLUR + MAX_BLUR;  
        blur = new BoxBlur(blurSize, blurSize, 2);
    }
    
    public double update() {
        x += random.nextDouble() * MAX_STAGGER - MAX_STAGGER / 2.0;
        y += random.nextDouble() * MAX_STAGGER;
        
        return y;
    }
    
    public void draw(GraphicsContext context) {
        context.setFill(color);
        context.setEffect(blur);
        
        context.fillOval(x, y, radius, radius);
    }
}
