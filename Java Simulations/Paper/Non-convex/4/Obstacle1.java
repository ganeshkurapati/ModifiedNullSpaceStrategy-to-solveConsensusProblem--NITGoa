import java.awt.*;
import sim.portrayal.simple.*;

public class Obstacle1 extends RectanglePortrayal2D
    {
    private static final long serialVersionUID = 1;

    public final static Paint obstacleColor = new Color(192,255,192);
  
    public Obstacle1(double diam)
        {
        super(obstacleColor,diam);
        }
    }
