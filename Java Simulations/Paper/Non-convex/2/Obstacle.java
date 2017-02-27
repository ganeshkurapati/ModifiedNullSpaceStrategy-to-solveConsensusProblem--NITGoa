import java.awt.*;
import sim.portrayal.simple.*;

public class Obstacle extends OvalPortrayal2D
    {
    private static final long serialVersionUID = 1;

    public final static Paint obstacleColor = new Color(255,255,255);
  
    public Obstacle(double diam)
        {
        super(obstacleColor,diam);
        }
    }