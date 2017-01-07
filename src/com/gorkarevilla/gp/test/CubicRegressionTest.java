package com.gorkarevilla.gp.test;

import static org.epochx.stats.StatField.*;

import java.io.FileWriter;
import java.io.IOException;

import com.gorkarevilla.gp.model.CubicRegression;
import org.epochx.gp.model.GPModel;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.Stats;
import org.epochx.tools.random.MersenneTwisterFast;

public class CubicRegressionTest {

	private static final boolean DEBUG = true;

	private static final String CSVFILE = "stats.csv";

	/*
	 * No Modificable Variables
	 */
	// random points to test.
	private static int noPoints = 4;
	// number of Runs (times to repeat the generations).
	private static int noRun = 4;

	/*
	 * Modificable Variables
	 */
	// number of CandidatePrograms each generation should contain.
	private static int populationSize = 500;
	// number of generations to use within a run.
	private static int noGenerationsMax = 100;
	// max depth of the tree to be constructed.
	private static int maxDepth = 8;
	// Probability to mutation 0.1 = 10%
	private static double mutationProbability = 0.1;

	/*
	 * Stats
	 */
	// Array to store the % of success
	private static double[] data = new double[noRun];
	// Average of Succesfully reach the fitness
	private static double averageHit = 0;

	/**
	 * 
	 * Can be set by parameters:
	 * @param args
	 * - Population Size
	 * - Number of Max Generations
	 * - Max Depth of the tree
	 * - Mutation Probability
	 * 
	 * If no argument is defined the default parameters are:
	 * - Population Size = 500
	 * - Max Generations = 100
	 * - Max Depth = 8
	 * - Mutation Probability = 0.1 
	 * 
	 * 
	 */
	public static void main(String[] args) {

		// If are parameters
		if (args.length == 4) {

			populationSize = Integer.valueOf(args[0]);
			noGenerationsMax = Integer.valueOf(args[1]);
			maxDepth = Integer.valueOf(args[2]);
			mutationProbability = Double.valueOf(args[3]);

		}

		if (DEBUG)
			System.out.println("Target Cubic Regression: x^3 + 2x^2 + x");

		// Construct the model with the number of random points to be tested
		// each generation to get the fitness
		final GPModel model = new CubicRegression(noPoints);

		// Set parameters.
		if (DEBUG)
			System.out.println("Population Size: " + populationSize);
		model.setPopulationSize(populationSize);

		if (DEBUG)
			System.out.println("Number of Generations: " + noGenerationsMax);
		model.setNoGenerations(noGenerationsMax);

		if (DEBUG)
			System.out.println("max Depth: " + maxDepth);
		model.setMaxDepth(maxDepth);

		if (DEBUG)
			System.out.println("Mutation Probability: " + mutationProbability);
		model.setMutationProbability(mutationProbability);

		model.setNoRuns(noRun);

		// Set operators and components.
		model.setProgramSelector(new TournamentSelector(model, 7));
		model.setRNG(new MersenneTwisterFast());

		// Request statistics every generation.
/*
		Life.get().addGenerationListener(new GenerationAdapter() {

			@Override
			public void onGenerationEnd() {
				Stats.get().print(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITTEST_PROGRAM);
			}
		});
*/
		// Request statistics every run.
		Life.get().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				Stats.get().print(GEN_NUMBER, RUN_TIME, RUN_NUMBER, RUN_FITNESS_MIN, RUN_FITTEST_PROGRAM);
				if (DEBUG)
					System.out.println("RUN TIME=> " + Stats.get().getStat(RUN_TIME_H) + ":"
							+ Stats.get().getStat(RUN_TIME_M) + ":" + Stats.get().getStat(RUN_TIME_S));

				// Store in the array
				storeData((Integer) Stats.get().getStat(RUN_NUMBER), (Integer) Stats.get().getStat(GEN_NUMBER),
						(Double) Stats.get().getStat(RUN_FITNESS_MIN));
			}
		});

		// Run the model.
		model.run();

		setAverageHit();
		writeCSV();
	}

	/**
	 * Store each execution data in the array
	 * 
	 * @param genNumber Number of Generations needed in that execution
	 * @param fitness the final Fitness value 
	 */
	private static void storeData(int runNumber, int genNumber, double fitness) {
		// If get the goal
		if (fitness == 0.0) {
			data[runNumber] = (double) ((double) genNumber / (double) noGenerationsMax);
		} else {
			data[runNumber] = 1.0;
		}

	}

	/**
	 * Store in averageHit variable the average of all the data stored in the array
	 */
	private static void setAverageHit() {
		// Get the average of all the data
		double total = 0;
		for (double d : data) {
			if (DEBUG)
				System.out.println("Data: " + d);
			total = total + d;
		}
		averageHit = total / data.length;
	}

	/**
	 * Add one line in the output CSV file with the information of the execution
	 */
	private static void writeCSV() {

		// Write in the CSV file the variables and the result
		FileWriter fw;
		try {
			if (DEBUG)
				System.out.println("Writting in the CSV...");
			fw = new FileWriter(CSVFILE, true);

			String line = populationSize + "," + noGenerationsMax + "," + maxDepth + "," + mutationProbability + ","
					+ averageHit;

			// Append to the file
			fw.write(line);

			fw.write(System.getProperty("line.separator"));
			// fw.write(System.lineSeparator()); // Only > Java 7

			fw.close();

			if (DEBUG)
				System.out.println("Writting Successful.");
		} catch (IOException e) {

			System.err.println("Error writting in the file");

			if (DEBUG)
				e.printStackTrace();
		}
	}

}
