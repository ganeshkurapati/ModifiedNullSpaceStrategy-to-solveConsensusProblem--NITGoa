
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
        
        double x_o=0;double y_o=0;
        double dist10= length(30-me.x,40-me.y);
        if ((me.x>=30)&&(me.y>=30)&&(me.y<=50)&&(dist10>=8)) 
        {   
            students.i_obst=1;
            x_o=30+10*Math.cos(Math.atan((me.y-40)/(me.x-30)));
            y_o=40+10*Math.sin(Math.atan((me.y-40)/(me.x-30)));
        }
        MutableDouble2D sumForces = new MutableDouble2D();
        
        double dist_w1=length(x_g-me.x,y_g-me.y);
           if((dist_w1<students.dist_w)&&((x_g-me.x)*(me.x-x_o)+(y_g-me.y)*(me.y-y_o)>0))
           {
            students.wallf=0;
            students.wallf1=0;
            sumForces.addIn(new Double2D((x_g - me.x) * students.GoToGoalMultiplier, (y_g - me.y) * students.GoToGoalMultiplier));
           }
        double dist1=length(me.x-x_o,me.y-y_o);
        if (students.i_obst==1) 
        {
            
        double x_temp=0;
            double y_temp=0;
            
            if(students.wallf==0)
           {
            students.dist_w=length((x_g-me.x),(y_g-me.y));
            
            if(((x_g-me.x)*(me.y-y_o)-(y_g-me.y)*(me.x-x_o))>0)
            {
               students.wallf1=1; 
            }
            if(((y_g-me.y)*(me.x-x_o)-(x_g-me.x)*(me.y-y_o))>=0)
            {
                students.wallf1=2;
            }
            students.wallf=1;
           }
           
           if(students.wallf1==1){
                x_temp=y_o-me.y;
                y_temp=-(x_o-me.x);}
            if(students.wallf1==2){
               x_temp=-(y_o-me.y);
                y_temp=(x_o-me.x);}
            
        sumForces.addIn(new Double2D((x_temp) *0.02, (y_temp) * 0.02));
            
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
