package com.gorkarevilla.gp.test;

import static org.epochx.stats.StatField.*;
import org.epochx.gp.model.EvenParity;
import org.epochx.gp.model.GPModel;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.Stats;
import org.epochx.tools.random.MersenneTwisterFast;

public class EvenParityTest {

    public static void main(String[] args) {
    	
        // Construct the model.
        final GPModel model = new EvenParity(4);

        // Set parameters.
        model.setPopulationSize(500);
        model.setNoGenerations(10000);
        model.setMaxDepth(8);

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
        // Run the model.
        model.run();
    }

}
