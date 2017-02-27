import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
    {
    private static final long serialVersionUID = 1;

    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    public int numRobots = 1;

    double GoToGoalMultiplier = 0.01;
    double avoidobstaclemultipiler=0.01;
    int wallf=0;
    int wallf1=0;
    double dist_w=0;
    int i_obst=0;
    // where the obstacles are located (diameter, xpos, ypos)
    public static final double[][] obstInfo = { {20, 40, 40},{20,30,40},{2,70,45} };
    public Continuous2D obstaclesEnvironment = null;
    public static final double XMIN = 0;
    public static final double XMAX = 100;
    public static final double YMIN = 0;
    public static final double YMAX = 100;
    public Students(long seed)
        {
        super(seed);
        }

    public void start()
        {
        super.start();
        obstaclesEnvironment = new Continuous2D(30, (XMAX-XMIN), (YMAX-YMIN) );
        // clear the yard
        yard.clear();
        
        // add some students to the yard
        for(int i = 0; i < numRobots; i++)
            {
            Student student = new Student();
            if(i==0){yard.setObjectLocation(student, new Double2D(10,40));}

            schedule.scheduleRepeating(student);
            }
        for( int i = 0 ; i < obstInfo.length ; i++ )
            {
         if(i==0){   Obstacle1 obst = new Obstacle1( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
          if(i==1){   Obstacle obst = new Obstacle( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
          if(i==2){   Obstacle3 obst = new Obstacle3( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
            }
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }
