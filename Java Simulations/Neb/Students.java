import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;
import sim.field.network.*;
public class Students extends SimState
    {
    
    public double i_dummy=0;
    public int i_imp=0;
    public int numRobots = 5;
    double X_int[][]= new double [2][numRobots];
    double X_old[][] = new double[2][numRobots];
    double  X_new[][] = new double[2][numRobots];
    
    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    

    double GoToGoalMultiplier = 0.01;
   public Network buddies = new Network(false);
    public Students(long seed)
        {
        super(seed);
        }

    public void start()
        {
        super.start();
        
        yard.clear();
        
        for(int i = 0; i < numRobots; i++)
            {
            Student student = new Student();
            double x_int=yard.getWidth() * 0.5 + random.nextDouble()*25.0;
            double y_int=yard.getHeight() * 0.5 + random.nextDouble()*25.0;
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
        
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }
