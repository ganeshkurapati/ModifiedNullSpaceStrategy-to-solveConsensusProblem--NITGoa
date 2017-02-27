import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
    {
    

    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    public int numRobots = 4;

    double GoToGoalMultiplier = 0.01;
    double avoidobstaclemultipiler=0.01;
    
    public static final double[][] obstInfo = { {20, 40, 40},{15, 20, 60},{20, 60, 60},{15, 80, 40}};
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
        obstaclesEnvironment = new Continuous2D( 30, (XMAX-XMIN), (YMAX-YMIN) );
        
        yard.clear();
        
        
        for(int i = 0; i < numRobots; i++)
            {
            Student student = new Student();
//            yard.setObjectLocation(student,  new Double2D(yard.getWidth() * 0.5 + random.nextDouble()*25.0,yard.getHeight() * 0.5 + random.nextDouble()*25.0));
              if(i==0) yard.setObjectLocation(student,  new Double2D(10,65));
              if(i==1) yard.setObjectLocation(student,  new Double2D(42,80));
              if(i==2) yard.setObjectLocation(student,  new Double2D(65,80));
              if(i==3) yard.setObjectLocation(student,  new Double2D(95,50));
            schedule.scheduleRepeating(student);
            }
        for( int i = 0 ; i < obstInfo.length ; i++ )
            {
            Obstacle obst = new Obstacle( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );
            }
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }
