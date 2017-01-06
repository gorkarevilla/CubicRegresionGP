package com.gorkarevilla.gp.test;

public class CubicRegressionRunTests {

	public static void main(String[] args) {
		
		String[] populationSizes = {"50","100","500","1000"};
		String[] noGenerationsMaxs = {"10","40","70","100"};
		String[] maxDepths = {"-1","8","16","32"};
		String[] mutationProbabilitys = {"0.1","0.3","0.6","0.9"};
		
		for (String ps : populationSizes) {
			for (String gm : noGenerationsMaxs) {
				for (String md : maxDepths) {
					for (String mp : mutationProbabilitys) {

						String[] parameters = {ps,gm,md,mp};
						System.out.println("Running with: "+parameters[0]+","+parameters[1]+","+parameters[2]+","+parameters[3]+",");
						CubicRegressionTest.main(parameters);
						
					}
				}
			}
		}
		

	}

}
