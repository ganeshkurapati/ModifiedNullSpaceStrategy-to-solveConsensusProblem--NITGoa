import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
    {
    
    public double i_dummy=0;
    public int i_imp=0;
    public int i_obst=0;
    public int numRobots = 10;
    double X_int[][]= new double [2][numRobots];
    double X_old[][] = new double[2][numRobots];
    double  X_new[][] = new double[2][numRobots];
    
    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    

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
        obstaclesEnvironment = new Continuous2D(30, (XMAX-XMIN), (YMAX-YMIN) );
        yard.clear();
        
        for(int i = 0; i < numRobots; i++)
            {
            Student student = new Student();
   //         double x_int=yard.getWidth() * 0.5 + random.nextDouble()*25.0;
   //         double y_int=yard.getHeight() * 0.5 + random.nextDouble()*25.0;
                double x_int=0; double y_int=0;
              if(i==0){ yard.setObjectLocation(student,  new Double2D(10,65));x_int=10;y_int=65;}
              if(i==1){yard.setObjectLocation(student,  new Double2D(42,80));x_int=42;y_int=80;}
              if(i==2){yard.setObjectLocation(student,  new Double2D(65,80));x_int=65;y_int=80;}
              if(i==3){yard.setObjectLocation(student,  new Double2D(95,50));x_int=95;y_int=50;}
              if(i==4){ yard.setObjectLocation(student,  new Double2D(10,68));x_int=10;y_int=68;}
              if(i==5){ yard.setObjectLocation(student,  new Double2D(40,80));x_int=40;y_int=80;}
              if(i==6){ yard.setObjectLocation(student,  new Double2D(42,80));x_int=42;y_int=80;}
              if(i==7){ yard.setObjectLocation(student,  new Double2D(40,82));x_int=40;y_int=82;}
              if(i==8){ yard.setObjectLocation(student,  new Double2D(65,82));x_int=65;y_int=82;}
              if(i==9){ yard.setObjectLocation(student,  new Double2D(92,50));x_int=92;y_int=50;}
//              x_int=i+10;y_int=80;
//              yard.setObjectLocation(student,  new Double2D(x_int,y_int));
              
               X_int[0][i]=x_int;X_int[1][i]=y_int;
  //          yard.setObjectLocation(student, new Double2D(x_int,y_int));
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
