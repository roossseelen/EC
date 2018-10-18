package mypackage;

import mypackage.Individual;

import java.lang.Math;
import java.util.Random;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import org.vu.contest.ContestEvaluation;

public class Population {
    public List<Individual> individuals;
    //[test$Individual@e6ea0c6, test$Individual@6a38e57f, test$Individual@5577140b, test$Individual@1c6b6478, test$Individual@67f89fa3, test$Individual@4ac68d3e, test$Individual@277c0f21, test$Individual@6073f712, test$Individual@43556938, test$Individual@3d04a311]test$Populatio

    public Individual theFittest;
    public int populationSize;
    
    public int evaluations_limit;

    //create an INITIAL population (so 1st time only)
    public Population(int populationSize, ContestEvaluation evaluation, boolean initialise) {
    	evaluations_limit =0;
    	
        this.individuals = new ArrayList<Individual>(populationSize);
        this.populationSize = populationSize;
        if (initialise) {
            for (int i = 0; i < populationSize; i++) {
                individuals.add(new Individual(true, evaluation));
                
                evaluations_limit++;
            }
        }
    }

    //hier returned die de firste individual, maar verwijderd nog niet! (ivm index problems anders)
    public Individual getFittestIndividual(){
        double[] allFitnesses = new double[populationSize];
        int k =0;

        for (int i = 0; i < populationSize; i++) {
            allFitnesses[i] = individuals.get(i).getFitness();
        }
        double best_fit = 0;
        int parent_index = 0;
        for (int i = 0; i < populationSize; i++) {
            if (allFitnesses[i] > best_fit && allFitnesses[i] > 0) {
                best_fit = allFitnesses[i];
                parent_index = i;
            }
        }
        //System.out.println("de fitste");
        //System.out.println(individuals.get(parent_index));

        return individuals.get(parent_index);
    }

    public void removeFittest(){ //hier gaat die de fitste verwijderen van de individual list
        double[] allFitnesses = new double[populationSize];
        int k =0;
        for(int i = 0; i<populationSize; i++) {
            allFitnesses[i] = individuals.get(i).getFitness();
        }

        double best_fit = 0;
        int parent_index = 0;
        for (int i = 0; i < populationSize; i++) {
            if (allFitnesses[i] > best_fit && allFitnesses[i] > 0) {
                best_fit = allFitnesses[i];
                parent_index = i;
            }
        }

        individuals.remove(parent_index);
        this.populationSize--;
    }


    //dit is nodig want je kan niet .size() op popuation object doen.
    public int size() {
        return individuals.size();
    }

    //Pak een individual om aan de tournament toe te voegen voor parentSelection later.
    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public void saveIndividual(Individual indiv) {
        individuals.add(indiv);
    }

    public void removeIndiv(int index){
        individuals.remove(index);
    }
}