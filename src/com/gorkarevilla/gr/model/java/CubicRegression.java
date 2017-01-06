package com.gorkarevilla.gr.model.java;

import org.epochx.gr.model.java.Regression;

/**
 * XGR model for a cubic symbolic regression problem in the Java language.
 * 
 * <p>
 * The target program is the function: x + x^2 + x^3
 */
public class CubicRegression extends Regression {

	/**
	 * Constructs an instance of the CubicRegression model with 50 input
	 * points.
	 */
	public CubicRegression() {
		super();
	}

	/**
	 * Constructs an instance of the CubicRegression model.
	 */
	public CubicRegression(final int noPoints) {
		super(noPoints);
	}

	/**
	 * The actual function we are trying to evolve.
	 */
	@Override
	public double getCorrectResult(final double x) {
		return x + 5* x * x + x * x * x;
	}

}