
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
        double x_g = 70.0;
        double y_g = 40.0;

        MutableDouble2D sumForces = new MutableDouble2D();
        double dist1 = length(30-me.x,40-me.y);
        if ((me.x>=30)&&(me.y>=30)&&(me.y<=50)&&(dist1>=8)) 
        {
            
            double x_me=me.x;double y_me=me.y;
            double x_o=30+10*Math.cos(Math.atan((y_me-40)/(x_me-30)));
            double y_o=40+10*Math.sin(Math.atan((y_me-40)/(x_me-30)));
            double dist=length(x_me-x_o,y_me-y_o);
            double x_temp= ((x_me-x_o)/dist)+(((1-(x_me-x_o)*(x_me-x_o))*(x_g-x_me)-(x_me-x_o)*(y_me-y_o)*(y_g-y_me))/(dist*dist));
            double y_temp= ((y_me-y_o)/dist)+(((1-(y_me-y_o)*(y_me-y_o))*(y_g-y_me)-(x_me-x_o)*(y_me-y_o)*(x_g-x_me))/(dist*dist));
            sumForces.addIn(new Double2D((x_temp*0.01) , (y_temp*0.01) ));
            
        }
        else 
        {
            sumForces.addIn(new Double2D((x_g - me.x) * students.GoToGoalMultiplier,
                    (y_g - me.y) * students.GoToGoalMultiplier));

        }
        sumForces.addIn(me);

        students.yard.setObjectLocation(this, new Double2D(sumForces));
    }
}