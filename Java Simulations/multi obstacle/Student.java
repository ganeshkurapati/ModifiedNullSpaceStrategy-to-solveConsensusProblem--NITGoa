import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;

public class Student implements Steppable
    {
    
    
    
    public final double length(double x, double y)
        {
 
        return Math.sqrt( x*x+y*y );
        }
    public void step(SimState state)
        {
        Students students = (Students) state;
        
        double x_g=50.0;double y_g=15.0;
        double x_me=0;double y_me=0;
        double x_n=0;double y_n=0;
        double x_o=0; double y_o=0;
        double dist_o=0;
        int n_it=0;
 
        double Neb[][] = new double[3][students.numRobots];
        
        MutableDouble2D sumForces = new MutableDouble2D();
        

             
            if(students.i_dummy==0)
            {
                for(int i=0; i<2; i++) 
               {
                for(int j=0; j<students.numRobots; j++)
                 {
                   students.X_old[i][j]=students.X_int[i][j];
                    }
                   }
                 }
//            for(int k=0; k<2; k++) 
//               {
//                for(int j=0; j<students.numRobots; j++)
//                 {
//                  System.out.println(students.X_old[k][j]);
//                    }
//                   }
            x_me=students.X_old[0][students.i_imp];
            y_me=students.X_old[1][students.i_imp];
            
            for(int b = 0 ; b < students.obstInfo.length ; b++)
          {
             double x_oo=students.obstInfo[b][1]; double y_oo=students.obstInfo[b][2];
             
             double dist_oo=length(x_oo-x_me,y_oo-y_me);
           if(dist_oo<=(students.obstInfo[b][0]/2)+2)
           {
             students.i_obst=1;
             x_o=x_oo;y_o=y_oo;
             dist_o=dist_oo;
           }
          }
            
            
            
            
            
            for(int n =0 ; n <students.numRobots;n++)
            {
             
             x_n=students.X_old[0][n];
             y_n=students.X_old[1][n];
             
             
            double dist =length(x_n-x_me,y_n-y_me);
            if((dist<=3)&&(dist!=0))
             {
             Neb[0][n_it]=dist;
             Neb[1][n_it]=x_n;
             Neb[2][n_it]=y_n;
             n_it=n_it+1;
             
             } 
            }
    
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
            if((students.i_obst==1)&&(n_it==0))
            {
            students.i_obst=0;
            double x_temp_o= ((x_me-x_o)/dist_o)+(((1-(x_me-x_o)*(x_me-x_o))*(x_g-x_me)-(x_me-x_o)*(y_me-y_o)*(y_g-y_me))/(dist_o*dist_o));
            double y_temp_o= ((y_me-y_o)/dist_o)+(((1-(y_me-y_o)*(y_me-y_o))*(y_g-y_me)-(x_me-x_o)*(y_me-y_o)*(x_g-x_me))/(dist_o*dist_o));
            sumForces.addIn(new Double2D((x_temp_o) * students.GoToGoalMultiplier, (y_temp_o) * students.GoToGoalMultiplier));
            }
            
            else if((n_it!=0)&&(students.i_obst==0))
            {
            double dist=Neb[0][0];
            double x_n11=Neb[1][0];
            double y_n11=Neb[2][0];
             
            double x_temp= ((x_me-x_n11)/dist)+(((1-(x_me-x_n11)*(x_me-x_n11))*(x_g-x_me)-(x_me-x_n11)*(y_me-y_n11)*(y_g-y_me))/(dist*dist))*0.01;
            double y_temp= ((y_me-y_n11)/dist)+(((1-(y_me-y_n11)*(y_me-y_n11))*(y_g-y_me)-(x_me-x_n11)*(y_me-y_n11)*(x_g-x_me))/(dist*dist))* 0.01;
            sumForces.addIn(new Double2D((x_temp) , (y_temp) ));
            
              }
            
            else if((n_it!=0)&&(students.i_obst!=0))
             {
             students.i_obst=0;
             
             double dist2=Neb[0][0];
             double x_n2=Neb[1][0];
             double y_n2=Neb[2][0];
             double a= ((x_me-x_n2)/dist2)+(((1-(x_me-x_n2)*(x_me-x_n2))*(x_g-x_me)-(x_me-x_n2)*(y_me-y_n2)*(y_g-y_me))/(dist2*dist2));
             double b= ((y_me-y_n2)/dist2)+(((1-(y_me-y_n2)*(y_me-y_n2))*(y_g-y_me)-(x_me-x_n2)*(y_me-y_n2)*(x_g-x_me))/(dist2*dist2));
            
             double dist1=dist_o;
             double x_n1=x_o;
             double y_n1=y_o;
             double aa= ((x_me-x_n1)*1.2/dist1)+(((1-(x_me-x_n1)*(x_me-x_n1))*(a)-(x_me-x_n1)*(y_me-y_n1)*(b))/(dist1*dist1))*0.01;
             double bb= ((y_me-y_n1)*1.2/dist1)+(((1-(y_me-y_n1)*(y_me-y_n1))*(b)-(x_me-x_n1)*(y_me-y_n1)*(a))/(dist1*dist1))* 0.01;
             
             sumForces.addIn(new Double2D((aa) , (bb) ));
             }
            
            
            
       else
          {
           double x_tempg=(x_g - x_me) * students.GoToGoalMultiplier;
           double y_tempg=(y_g - y_me) * students.GoToGoalMultiplier;
            sumForces.addIn(new Double2D(x_tempg,  y_tempg));
        
          }
            
        
        
        if(students.i_imp<students.numRobots)
          {
          students.X_old[0][students.i_imp]=x_me;
          students.X_old[1][students.i_imp]=y_me;
          }
        
        sumForces.addIn(x_me,y_me);
        
         
        students.yard.setObjectLocation(this, new Double2D(sumForces));
        Double2D pp=new Double2D(sumForces);

       if(students.i_imp<students.numRobots)
          {
              
          students.X_new[0][students.i_imp]=pp.x;
          students.X_new[1][students.i_imp]=pp.y;
          }
       
       if(students.i_imp==(students.numRobots-1))
          {
            students.i_imp=-1;
              for(int i=0; i<2; i++) 
               {
                for(int j=0; j<students.numRobots; j++)
                 {
                   students.X_old[i][j]=students.X_new[i][j];
                    }
                   }
              }
       
        students.i_imp=students.i_imp+1;
        students.i_dummy=students.i_dummy+0.000001;
        }
    } 
    
