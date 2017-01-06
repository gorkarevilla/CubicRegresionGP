package com.gorkarevilla.gp.test;

import static org.epochx.stats.StatField.*;
import com.gorkarevilla.gp.model.CubicRegression;
import org.epochx.gp.model.GPModel;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.Stats;
import org.epochx.tools.random.MersenneTwisterFast;

public class CubicRegressionTest {
	
	private static final boolean DEBUG = true;
	
	//Variables
	private static int populationSize = 500;
	private static int noGenerationsMax = 100;
	private static int maxDepth = 8;
	private static int noRun = 2;
	

    public static void main(String[] args) {
    	
    	if(DEBUG) System.out.println("Cubic Regression looking for: x + 2* x * x + x * x * x;");
    	
        // Construct the model.
        final GPModel model = new CubicRegression(4);

        // Set parameters.
        if(DEBUG) System.out.println("Population Size: "+populationSize);
        model.setPopulationSize(populationSize);
        
        if(DEBUG) System.out.println("Number of Generations: "+noGenerationsMax);
        model.setNoGenerations(noGenerationsMax);
        
        if(DEBUG) System.out.println("max Depth: "+maxDepth);
        model.setMaxDepth(maxDepth);
        
        model.setNoRuns(noRun);
        
        // Set operators and components.
        model.setProgramSelector(new TournamentSelector(model, 7));
        model.setRNG(new MersenneTwisterFast());

        // Request statistics every generation.
        Life.get().addGenerationListener(new GenerationAdapter(){
            @Override
            public void onGenerationEnd() {
            	Stats.get().print(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITTEST_PROGRAM);
            }
        });
        
        
        Life.get().addRunListener(new RunAdapter() {
        	  @Override
        	  public void onRunEnd() {
        		  Stats.get().print(RUN_TIME,RUN_NUMBER,RUN_FITNESS_MIN,RUN_FITTEST_PROGRAM);
        		  if(DEBUG) System.out.println("RUN TIME=> "+Stats.get().getStat(RUN_TIME_H)+":"+Stats.get().getStat(RUN_TIME_M)+":"+Stats.get().getStat(RUN_TIME_S)+":"+Stats.get().getStat(RUN_TIME_MS));
        	  }
        });
        
        // Run the model.
        model.run();
    }

}
