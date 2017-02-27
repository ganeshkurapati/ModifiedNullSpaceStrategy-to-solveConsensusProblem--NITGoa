
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
        double x_g=40.0;double y_g=10.0;
        double x_o=40.0;double y_o=40.0;
        MutableDouble2D sumForces = new MutableDouble2D();
//        double x_obst,y_obst;
//        x_obst=40+Math.atan2(40-me.y, 40-me.x)*10;
//        y_obst=40+Math.atan2(40-me.y, 40-me.x)*10;
        double Xdiff=x_o-me.x;double Ydiff=y_o-me.y;
        double dist =length(Xdiff,Ydiff);
          double dist_w1=length(x_g-me.x,y_g-me.y);
           if((dist_w1<students.dist_w)&&((x_g-me.x)*(me.x-x_o)+(y_g-me.y)*(me.y-y_o)>0))
           {
            students.wallf=0;
            students.wallf1=0;
            sumForces.addIn(new Double2D((x_g - me.x) * students.GoToGoalMultiplier, (y_g - me.y) * students.GoToGoalMultiplier));
           }
        if(dist<=10+2)
        {   
            double x_temp=0;
            double y_temp=0;
            
            if(students.wallf==0)
           {
            students.dist_w=length((x_g-me.x),(y_g-me.y));
            
            if(((x_g-me.x)*(y_o-me.y)-(y_g-me.y)*(x_o-me.x))>0)
            {
               students.wallf1=1; 
            }
            if(((y_g-me.y)*(x_o-me.x)-(x_g-me.x)*(y_o-me.y))>=0)
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
            
        sumForces.addIn(new Double2D((x_temp) * students.GoToGoalMultiplier, (y_temp) * students.GoToGoalMultiplier));
        
        }
       
        else
        {
        sumForces.addIn(new Double2D((x_g - me.x) * students.GoToGoalMultiplier, (y_g - me.y) * students.GoToGoalMultiplier));
        
        }
       sumForces.addIn(me);

        students.yard.setObjectLocation(this, new Double2D(sumForces));
        }
    } 
    
