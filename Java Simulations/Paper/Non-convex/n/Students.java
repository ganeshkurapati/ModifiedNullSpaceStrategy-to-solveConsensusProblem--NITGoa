import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
    {
    
    public double i_dummy=0;
    public int i_imp=0;
    public int i_obst=0;
    public int numRobots = 15;
    double X_int[][]= new double [2][numRobots];
    double X_old[][] = new double[2][numRobots];
    double  X_new[][] = new double[2][numRobots];
    
    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    

    double GoToGoalMultiplier = 0.01;
    double avoidobstaclemultipiler=0.01;
    
    int wallf[][]= new int [1][numRobots];
    int wallf1[][] = new int[1][numRobots];
    double  dist_w[][] = new double[1][numRobots];
    
    public static final double[][] obstInfo = { {20, 40, 40},{20,80,40},{20,60,70},{20,20,70},{2,40,90},{20,11,30},{20,11,20} };
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
            double x_int=0;double y_int=0;
            if(i==0){x_int=90;y_int=15;}
            if(i==1){x_int=90;y_int=17;}
            if(i==2){x_int=92;y_int=15;}
            if(i==3){x_int=92;y_int=17;}
            if(i==4){x_int=35;y_int=15;}
            if(i==5){x_int=37;y_int=15;}
            if(i==6){x_int=35;y_int=17;}
            if(i==7){x_int=37;y_int=17;}
            if(i==8){x_int=10;y_int=10;}
            if(i==9){x_int=10;y_int=12;}
            if(i==10){x_int=12;y_int=12;}
            if(i==11){x_int=2;y_int=79;}
            if(i==12){x_int=4;y_int=75;}
            if(i==13){x_int=2;y_int=77;}
            if(i==14){x_int=4;y_int=77;}
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
           if(i==0){ Obstacle1 obst = new Obstacle1( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
           if(i==1){ Obstacle1 obst = new Obstacle1( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
           if(i==2){ Obstacle obst = new Obstacle( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
           if(i==3){ Obstacle obst = new Obstacle( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
           if(i==4){ Obstacle3 obst = new Obstacle3( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
           if(i==5){ Obstacle1 obst = new Obstacle1( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
           if(i==6){ Obstacle4 obst = new Obstacle4( obstInfo[i][0] );
            obstaclesEnvironment.setObjectLocation( obst, new Double2D( obstInfo[i][1], obstInfo[i][2] ) );}
            }
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }
