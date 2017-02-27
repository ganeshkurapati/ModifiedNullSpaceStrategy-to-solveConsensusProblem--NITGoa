
import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;

public class Students extends SimState
    {
    

    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    public int numRobots = 100;

    double GoToGoalMultiplier = 0.01;

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
            yard.setObjectLocation(student, 
                new Double2D(yard.getWidth() * 0.5 + random.nextDouble()*25.0,
                    yard.getHeight() * 0.5 + random.nextDouble()*25.0));

            schedule.scheduleRepeating(student);
            }
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }
