import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
    {
    private static final long serialVersionUID = 1;
    public double i_dummy=0;
    public int i_imp=0;
    public int i_obst=0;
    public int numRobots = 4;
    double X_int[][]= new double [2][numRobots];
    double X_old[][] = new double[2][numRobots];
    double  X_new[][] = new double[2][numRobots];
    
    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    

    double GoToGoalMultiplier = 0.01;
    double avoidobstaclemultipiler=0.01;

    public static final double[][] obstInfo = { {20, 40, 40},{2,40,10} };
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
        yard.clear();
        
        for(int i = 0; i < numRobots; i++)
            {
            Student student = new Student();
            double x_int=0;
            double y_int=0;
            if(i==0){x_int=45;y_int=90;}
            if(i==1){x_int=48;y_int=90;}
            if(i==2){x_int=50;y_int=90;}
            if(i==3){x_int=53;y_int=90;}
            if(i==4){x_int=55;y_int=90;}
            X_int[0][i]=x_int;X_int[1][i]=y_int;
            yard.setObjectLocation(student, new Double2D(x_int,y_int));
//            for(int k=0; k<2; k++) 
//               {
//                for(int j=0; j<numRobots; j++)
//                 {
//                  System.out.println(X_int[k][j]);
//                    }
//                   }
            
            schedule.scheduleRepeating(student);
            }
        for( int i = 0 ; i < obstInfo.length ; i++ )
            {
          if(i==0) { Obstacle1 obst = new Obstacle1( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
          if(i==1) { Obstacle3 obst = new Obstacle3( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
            }
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }