import java.awt.*;
import sim.portrayal.simple.*;

public class Obstacle extends OvalPortrayal2D
    {
    private static final long serialVersionUID = 1;

    public final static Paint obstacleColor = new Color(92,55,192);
  
    public Obstacle(double diam)
        {
        super(obstacleColor,diam);
        }
    }
