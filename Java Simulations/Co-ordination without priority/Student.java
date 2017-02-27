import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import sim.field.network.*;
public class Student implements Steppable
    {
    private static final long serialVersionUID = 1;
    public final double length(double x, double y)
        {
 
        return Math.sqrt( x*x+y*y );
        }
    public static final double MAX_FORCE = 3.0;
    public void step(SimState state)
        {
        Students students = (Students) state;
        Continuous2D yard = students.yard;
        Double2D me = students.yard.getObjectLocation(this);
        double x_g=10.0;double y_g=10.0;
//        double x_o=40.0;double y_o=40.0;

        MutableDouble2D sumForces = new MutableDouble2D();
        MutableDouble2D forceVector = new MutableDouble2D();
        Bag out = students.buddies.getEdges(this, null);
        int len = out.size();
        for(int buddy = 0 ; buddy < len; buddy++)
            {
            Edge e = (Edge)(out.get(buddy));
            double buddiness = ((Double)(e.info)).doubleValue();
            
            // I could be in the to() end or the from() end.  getOtherNode is a cute function
            // which grabs the guy at the opposite end from me.
            Double2D him = students.yard.getObjectLocation(e.getOtherNode(this));
             double x_n=0;double y_n=0;
               
             for(int j=0; j<students.numRobots; j++)
               {
                 if( ( students.X_new[0][j]==him.x)&&(students.X_new[1][j]==him.y))
                 {
                  x_n=students.X_old[0][j];
                  y_n=students.X_old[1][j];
                 }
                 else
                 {
                   x_n=him.x;
                   y_n=him.y;
                 }
                }
                   
        
            double dist =length(x_n-me.x,y_n-me.y);
            if(dist<=0.5)
             {
            
            double x_temp= ((me.x-x_n)/dist)+(((1-(me.x-x_n)*(me.x-x_n))*(x_g-me.x)-(me.x-x_n)*(me.y-y_n)*(y_g-me.y))/(dist*dist))*0.05;
            double y_temp= ((me.y-y_n)/dist)+(((1-(me.y-y_n)*(me.y-y_n))*(y_g-me.y)-(me.x-x_n)*(me.y-y_n)*(x_g-me.x))/(dist*dist))* 0.05;
            sumForces.addIn(new Double2D((x_temp) , (y_temp) ));
        
             }
            
            }
       
          {
           double x_temp1=(x_g - me.x) * students.GoToGoalMultiplier;
           double y_temp1=(y_g - me.y) * students.GoToGoalMultiplier;
            sumForces.addIn(new Double2D(x_temp1,  y_temp1));
        
          }
            
//            if (buddiness >= 0)  // the further I am from him the more I want to go to him
//                {
//                forceVector.setTo((him.x - me.x) * buddiness, (him.y - me.y) * buddiness);
//                if (forceVector.length() > MAX_FORCE)  // I'm far enough away
//                    forceVector.resize(MAX_FORCE);
//                }
//            else  // the nearer I am to him the more I want to get away from him, up to a limit
//                {
//                forceVector.setTo((him.x - me.x) * buddiness, (him.y - me.y) * buddiness);
//                if (forceVector.length() > MAX_FORCE)  // I'm far enough away
//                    forceVector.resize(0.0);
//                else if (forceVector.length() > 0)
//                    forceVector.resize(MAX_FORCE - forceVector.length());  // invert the distance
//                }
//            sumForces.addIn(forceVector);
        //    sumForces.addIn(me);
//            students.yard.setObjectLocation(this, new Double2D(sumForces));
        
       
//        sumForces.addIn(new Double2D((10.0 - me.x) * students.GoToGoalMultiplier, 
//                (10.0 - me.y) * students.GoToGoalMultiplier));
//        System.out.println(kk);
        if(students.i_imp==students.numRobots)
          {
            students.i_imp=0;
              for(int i=0; i<2; i++) 
               {
                for(int j=0; j<students.numRobots; j++)
                 {
                   students.X_old[i][j]=0;
                   students.X_new[i][j]=0;
                    }
                   }
              }
        if(students.i_imp<students.numRobots)
          {
          students.X_old[0][students.i_imp]=me.x;
          students.X_old[1][students.i_imp]=me.y;
          }
        
        sumForces.addIn(me);
        
         
        students.yard.setObjectLocation(this, new Double2D(sumForces));
        Double2D pp=new Double2D(sumForces);

       if(students.i_imp<students.numRobots)
          {
              
          students.X_new[0][students.i_imp]=pp.x;
          students.X_new[1][students.i_imp]=pp.y;
          }
       
       
       
        students.i_imp=students.i_imp+1;
        }
    } 
    
