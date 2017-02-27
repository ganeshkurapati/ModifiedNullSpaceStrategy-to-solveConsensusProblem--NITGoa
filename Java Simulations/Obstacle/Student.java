
import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;

public class Student implements Steppable
    {
    private static final long serialVersionUID = 1;

    public void step(SimState state)
        {
        Students students = (Students) state;
        Continuous2D yard = students.yard;

        Double2D me = students.yard.getObjectLocation(this);

        MutableDouble2D sumForces = new MutableDouble2D();

       
        sumForces.addIn(new Double2D((10.0 - me.x) * students.GoToGoalMultiplier, 
                (10.0 - me.y) * students.GoToGoalMultiplier));
        
        
       sumForces.addIn(me);

        students.yard.setObjectLocation(this, new Double2D(sumForces));
        }
    } 
    
