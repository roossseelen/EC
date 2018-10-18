package mypackage;

import java.lang.Math;
import java.util.Random;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import org.vu.contest.ContestEvaluation;

public class Individual {
    public double[] genes;
    private double fitness;
    private ContestEvaluation evaluation;

    //only for initializing first random genes.
    public static double getRandomDoubleBetweenDimensions() {
        double x = (Math.random() * (10)) - 5;
        if (x > 5.0) {
            x = 5.0;
        } else if (x < -5.0) {
            x = -5.0;
        }
        return x;
    }


    public Individual(boolean initialise, ContestEvaluation evaluation) {
        this.genes = new double[10];
        this.evaluation = evaluation;
        if (initialise) {
            for (int i = 0; i < 10; i++) {
                double gene = getRandomDoubleBetweenDimensions();
                this.genes[i] = gene;
                //this.fitness = calculateFitness(evaluation);
            }
            this.fitness = calculateFitness(evaluation);
        }
    }

    public double calculateFitness(ContestEvaluation evaluation) {
        double fitness = (double) evaluation.evaluate(this.genes);
        //System.out.println(fitness);
        return fitness;
    }

    public int size() {
        return genes.length;
    }

    public double[] getGenes() {
        return genes;
    }

    public void setGenes(double[] genes) {
        this.genes = genes;
        this.fitness = calculateFitness(evaluation);

    }

    public double getFitness() {
        return this.fitness;
    }
}