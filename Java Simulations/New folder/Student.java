import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;
import sim.field.network.*;
public class Student implements Steppable
    {
    private static final long serialVersionUID = 1;
    double Neb[][] = new double[3][300];
    
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
        int n_it=0;
        
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
            if(dist<=3)
             {
             Neb[0][n_it]=dist;
             Neb[1][n_it]=x_n;
             Neb[2][n_it]=y_n;
             n_it=n_it+1;
             
             } 
            }
            if(n_it==1)
            {
            double dist=Neb[0][0];
            double x_n=Neb[1][0];
            double y_n=Neb[2][0];
             if(dist<=0.5)
             {
              sumForces.addIn(new Double2D((0.5) , (0.5) ));
             }
             else
             {
            double x_temp= ((me.x-x_n)/dist)+(((1-(me.x-x_n)*(me.x-x_n))*(x_g-me.x)-(me.x-x_n)*(me.y-y_n)*(y_g-me.y))/(dist*dist))*0.05;
            double y_temp= ((me.y-y_n)/dist)+(((1-(me.y-y_n)*(me.y-y_n))*(y_g-me.y)-(me.x-x_n)*(me.y-y_n)*(x_g-me.x))/(dist*dist))* 0.05;
            sumForces.addIn(new Double2D((x_temp) , (y_temp) ));
             }
            }
            
            else if(n_it>=2)
             {
             double temp1=0;double temp2=0;double temp3=0;
             for(int i=0;i<n_it;i++)
             {
              for(int j=0;j<n_it-1;j++)
              {
               if(Neb[0][j+1]<Neb[0][j]) 
               {
               temp1=Neb[0][j+1];Neb[0][j+1]=Neb[0][j];Neb[0][j]=temp1;
                temp2=Neb[1][j+1];Neb[1][j+1]=Neb[1][j];Neb[1][j]=temp2;
                temp3=Neb[2][j+1];Neb[2][j+1]=Neb[2][j];Neb[2][j]=temp3;
              }
              }
              }
             
             double dist2=Neb[0][1];
             double x_n2=Neb[1][1];
             double y_n2=Neb[2][1];
             double a= ((me.x-x_n2)/dist2)+(((1-(me.x-x_n2)*(me.x-x_n2))*(x_g-me.x)-(me.x-x_n2)*(me.y-y_n2)*(y_g-me.y))/(dist2*dist2));
             double b= ((me.y-y_n2)/dist2)+(((1-(me.y-y_n2)*(me.y-y_n2))*(y_g-me.y)-(me.x-x_n2)*(me.y-y_n2)*(x_g-me.x))/(dist2*dist2));
            
             double dist1=Neb[0][0];
             double x_n1=Neb[1][0];
             double y_n1=Neb[2][0];
             double aa= ((me.x-x_n1)/dist1)+(((1-(me.x-x_n1)*(me.x-x_n1))*(a)-(me.x-x_n1)*(me.y-y_n1)*(b))/(dist1*dist1))*0.05;
             double bb= ((me.y-y_n1)/dist1)+(((1-(me.y-y_n1)*(me.y-y_n1))*(b)-(me.x-x_n1)*(me.y-y_n1)*(a))/(dist1*dist1))* 0.05;
             
             sumForces.addIn(new Double2D((aa) , (bb) ));
             }
            
            
            
       else
          {
           double x_tempg=(x_g - me.x) * students.GoToGoalMultiplier;
           double y_tempg=(y_g - me.y) * students.GoToGoalMultiplier;
            sumForces.addIn(new Double2D(x_tempg,  y_tempg));
        
          }
            

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
    
