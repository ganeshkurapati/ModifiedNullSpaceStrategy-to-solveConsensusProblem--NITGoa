import sim.engine.*;
import static sim.engine.SimState.doLoop;
import sim.util.*;
import sim.field.continuous.*;
import sim.field.network.*;
public class Students extends SimState
    {
    private static final long serialVersionUID = 1;
    public int i_imp=0;
    public int numRobots = 5;
    double X_old[][] = new double[2][numRobots];
    double  X_new[][] = new double[2][numRobots];
    public Continuous2D yard = new Continuous2D(1.0,100,100);
    
    

    double GoToGoalMultiplier = 0.01;
   public Network buddies = new Network(false);
    public Students(long seed)
        {
        super(seed);
        }

    public void start()
        {
        super.start();
        
        // clear the yard
        yard.clear();
        
        // add some students to the yard
        for(int i = 0; i < numRobots; i++)
            {
            Student student = new Student();
            yard.setObjectLocation(student, 
                new Double2D(yard.getWidth() * 0.5 + random.nextDouble()*25.0,
                    yard.getHeight() * 0.5 + random.nextDouble()*25.0));
            
            buddies.addNode(student);
            schedule.scheduleRepeating(student);
            }
        Bag students = buddies.getAllNodes();
        for(int i = 0; i < students.size(); i++)
            {
            Object student = students.get(i);
            
            // who does he like?
            Object studentB = null;
            do
                {
                studentB = students.get(random.nextInt(students.numObjs));
                } while (student == studentB);
            double buddiness = random.nextDouble();
            buddies.addEdge(student, studentB, new Double(buddiness*0));

            // who does he dislike?
            do
                {
                studentB = students.get(random.nextInt(students.numObjs));
                } while (student == studentB);
            buddiness = random.nextDouble();
            buddies.addEdge(student, studentB, new Double( -buddiness*0));
            }
        }
        
    public static void main(String[] args)
        {
        doLoop(Students.class, args);
        System.exit(0);
        }    
    }
