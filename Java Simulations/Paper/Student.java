
import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import java.awt.geom.*;

public class Student implements Steppable {

    private static final long serialVersionUID = 1;

    public final double length(double x, double y) 
    {
        return Math.sqrt(x * x + y * y);
    }

    public void step(SimState state)
    {
        Students students = (Students) state;
        Continuous2D yard = students.yard;

        Double2D me = students.yard.getObjectLocation(this);
        double x_g = 40.0;
        double y_g = 20.0;

        MutableDouble2D sumForces = new MutableDouble2D();
        double x_temp1 =me.x+((x_g - me.x) * students.GoToGoalMultiplier);
        double y_temp1 =me.y+((y_g - me.y) * students.GoToGoalMultiplier);
        if ((28 <= x_temp1) && (x_temp1 <= 52) && (28 <= y_temp1) && (y_temp1 <= 52)) {
            double x_o = (52 * (x_temp1 - me.x) - (x_temp1 - me.x) * me.y + (y_temp1 - me.x) * me.x) / (y_temp1 - me.y);
            double y_o = 50;
            double x_me = x_o;
            double y_me = 52;
        //    students.yard.setObjectLocation(this, new Double2D(x_temp1,50));
            double dist = length(x_o - x_me, y_o - y_me);

            double x_temp= ((x_me-x_o)/dist)+(((1-(x_me-x_o)*(x_me-x_o))*(x_g-x_me)-(x_me-x_o)*(y_me-y_o)*(y_g-y_me))/(dist*dist))*0.001;
            double y_temp= ((y_me-y_o)/dist)+(((1-(y_me-y_o)*(y_me-y_o))*(y_g-y_me)-(x_me-x_o)*(y_me-y_o)*(x_g-x_me))/(dist*dist))* 0.001;
            sumForces.addIn(new Double2D((x_temp) , (y_temp) ));
            
        }
        else {
            sumForces.addIn(new Double2D((x_g - me.x) * students.GoToGoalMultiplier,
                    (y_g - me.y) * students.GoToGoalMultiplier));

        }
        sumForces.addIn(me);

        students.yard.setObjectLocation(this, new Double2D(sumForces));
    }
}
