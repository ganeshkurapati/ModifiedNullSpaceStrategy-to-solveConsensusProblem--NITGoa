import java.awt.*;
import sim.portrayal.simple.*;

public class Obstacle extends OvalPortrayal2D
    {
    

    public final static Paint obstacleColor = new Color(192,255,192);
  
    public Obstacle(double diam)
        {
        super(obstacleColor,diam);
        }
    }
