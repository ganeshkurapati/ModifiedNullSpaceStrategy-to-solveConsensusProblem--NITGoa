
import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import java.awt.geom.*;
public class Student implements Steppable
    {
    private static final long serialVersionUID = 1;
    
    public final double length(double x, double y)
        {
        
        return Math.sqrt( x*x+y*y );
        }
    
    
    public void step(SimState state)
        {
        Students students = (Students) state;
        Continuous2D yard = students.yard;
        
        Double2D me = students.yard.getObjectLocation(this);
        double x_g=40.0;double y_g=20.0;
        double x_o=40.0;double y_o=40.0;
        MutableDouble2D sumForces = new MutableDouble2D();
//        double x_obst,y_obst;
//        x_obst=40+Math.atan2(40-me.y, 40-me.x)*10;
//        y_obst=40+Math.atan2(40-me.y, 40-me.x)*10;
        double Xdiff=x_o-me.x;double Ydiff=y_o-me.y;
        double dist =length(Xdiff,Ydiff);
        if(dist<=10+2)
        {
        double x_temp= ((me.x-x_o)/dist)+(((1-(me.x-x_o)*(me.x-x_o))*(x_g-me.x)-(me.x-x_o)*(me.y-y_o)*(y_g-me.y))/(dist*dist));
        double y_temp= ((me.y-y_o)/dist)+(((1-(me.y-y_o)*(me.y-y_o))*(y_g-me.y)-(me.x-x_o)*(me.y-y_o)*(x_g-me.x))/(dist*dist));
        sumForces.addIn(new Double2D((x_temp) * students.GoToGoalMultiplier, 
                (y_temp) * students.GoToGoalMultiplier));
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
    
