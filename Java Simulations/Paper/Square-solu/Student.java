
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
        
        double x_o=0;double y_o=0;
        
        if((28 <= me.x) && (me.x <= 52)){
          double dist11=length(0,me.y-50);
          if(dist11<=2){
            x_o=me.x;y_o=50;}
                         
          double dist22=length(0,me.y-30);
          if(dist22<=2){
            x_o=me.x;y_o=30;}
           }
        
        if((29 <= me.y) && (me.y <= 51)){
          double dist11=length(me.x-50,0);
          if(dist11<=2){
            x_o=50;y_o=me.y;}
                         
          double dist22=length(me.x-30,0);
          if(dist22<=2){
            x_o=30;y_o=me.y;}
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
        if ((dist1<=2)) 
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
