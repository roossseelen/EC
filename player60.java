import mypackage.Population;
import mypackage.Individual;

import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.lang.Math;
import java.util.Random;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.lang.Object;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class player60 implements ContestSubmission {
    Random rnd_;
    private int evaluations_limit_;
    public Population currentPopulation; //create an population object but do nothing with it yet.
    private ContestEvaluation evaluation;
    public Population nextIndividuals; //population om de nieuwe babies op t slaan.
    public Population parentsAndBabyPop;
    public Population tempFittest;
    
    public int tournamentsize;
    public int nextindividualssize;
    public int popsize; 
    
    public int number;

    private static final int geneSize = 10;

    public player60() {
        rnd_ = new Random();
    }


    public void setSeed(long seed) {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation) {
        // Set evaluation problem used in the run
        this.evaluation = evaluation;

        //idn ik krijg eht ff niet in een variabele.
        //this.tournamentSize = 5;
        ///this.populationSize = 20;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
        
        if(!isMultimodal && !hasStructure && !isSeparable){     //bentsigar
        	tournamentsize = 20;
        	nextindividualssize = 100;
        	popsize = 100; 
        } 
        else if(isMultimodal && hasStructure && !isSeparable) {   //schaffers
        	tournamentsize = 10;
        	nextindividualssize = 10;
        	popsize = 100; 
        } 
        else if(isMultimodal && !hasStructure && !isSeparable) {   //katsuura
        	tournamentsize = 100;
        	nextindividualssize = 200;
        	popsize = 200; 
        }

        // Do sth with property values, e.g. specify relevant settings of your algorithm
        // if(!isMultimodal && !hasStructure && !isSeparable){  //bentcigar
        //     population pop = new population();

        // }else{
        //     // Do sth else
        // }

    }

    //TOURNAMENT SELECTION. hier komt een indiv uit voor de parentSelection
    public Individual tournamentSelection(Population pop) {
        Population tournament = new Population(tournamentsize, this.evaluation, false); //5 needs to become variable
        for (int i = 0; i < tournamentsize; i++) { //tournament size 5 needs to become variable
            int randomId = (int) (Math.random() * (pop.size() - 1)); //generate random number om te indexeren in pop
            tournament.saveIndividual(pop.getIndividual(randomId)); //get indiv met random index en voeg toe aan tournament
        }
        Individual indiv = tournament.getFittestIndividual(); //vind de fitste en return
        return indiv;
    }

    //1 POINT CROSSOVER MAKEN 2 CHILDREN = FINISHED
   public void onePointCrossOver(double[] parent1, double[] parent2, ContestEvaluation evaluation){
        //crossoverchance variable toevoegen, nu op 1 zetten
       int crossoverPoint = 5; //nu fixed value kan als random nummer maken.
       double[] head1 = Arrays.copyOfRange(parent1, 0, crossoverPoint); //syntax(OldArray,Startindex which is inclusive, endinde which is exclusive)
       double[] tail1 = Arrays.copyOfRange(parent1, crossoverPoint, parent1.length);
       double[] head2 = Arrays.copyOfRange(parent2, 0, crossoverPoint);
       double[] tail2 = Arrays.copyOfRange(parent2, crossoverPoint, parent2.length);

       Individual child1 = new Individual(false, evaluation);
       child1.setGenes(concatenate(head1, tail2, geneSize));
       number++;
       nextIndividuals.saveIndividual(child1);

       Individual child2 = new Individual(false, evaluation);
       child2.setGenes(concatenate(head2, tail1, geneSize));
       number++;
       nextIndividuals.saveIndividual(child2);
    }

    //CONCATENATE 2 ARRAYS = samen voegen van 2 arrays (deel van mama en papa) om nieuwe child te maken.
    public double[] concatenate(double[] head, double[] tail, int arrSize) {
        int hLen = head.length;
        int tLen = tail.length;

        @SuppressWarnings("unchecked")
        double[] c = (double[]) Array.newInstance(head.getClass().getComponentType(), hLen + tLen);
        System.arraycopy(head, 0, c, 0, hLen);
        System.arraycopy(tail, 0, c, hLen, tLen);

//        for (int i = 0; i < arrSize; i++) {
//            System.out.println(c[i]);
//        }
//        System.out.println("end of concatenated");
        return c;
    }

    //DIFFERENTIAL CROSSOVER with 2 parents = FINISHED
    public void diffTwoParent(double[] parent1, double[] parent2, ContestEvaluation evaluation) {
        double[] together = shuffleArray(concatenate(parent1, parent2, 20)); //parents samen en geshuffled

        double[] child1genes = new double[geneSize]; //initialise new kids
        double[] child2genes = new double[geneSize];

        int counter1 = 0; //dit anders gaat index raar doen.
        int counter2 = 1;

        for (int i = 0; i < 12; i++) {
            if (i == 10) {
                break;
            }
            child1genes[i] = together[counter1];
            child2genes[i] = together[counter2];
            counter1+=2;
            counter2+=2;
        }

        Individual child1 = new Individual(false, evaluation);
        child1.setGenes(child1genes);
        number++;
        nextIndividuals.saveIndividual(child1);

        Individual child2 = new Individual(false, evaluation);
        child2.setGenes(child2genes);
        number++;
        nextIndividuals.saveIndividual(child2);

    }

    //SHUFFLE ARRAY FUNCTION.
    public double[] shuffleArray(double[] arr) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = arr.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            double a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }

//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
//        }
//        System.out.println("end of Shuffle");
        return arr;

    }

    //2 POINT CROSSOVER MAKE 3 BABIES.
    public void twoPointCrossOver(double[] parent1, double[] parent2, double[] parent3, ContestEvaluation evaluation){
        //crossoverchance variable toevoegen, nu op 1 zetten
        int crossoverPoint1 = 3; //nu fixed value kan als random nummer maken.
        int crossoverPoint2 = 7; //nu fixed value kan als random nummer maken.

        double[] head1 = Arrays.copyOfRange(parent1, 0, crossoverPoint1); //syntax(OldArray,Startindex which is inclusive, endinde which is exclusive)
        double[] middle1 = Arrays.copyOfRange(parent1, crossoverPoint1, crossoverPoint2);
        double[] tail1 = Arrays.copyOfRange(parent1, crossoverPoint2, parent1.length);
        double[] head2 = Arrays.copyOfRange(parent2, 0, crossoverPoint1);
        double[] middle2 = Arrays.copyOfRange(parent2, crossoverPoint1,crossoverPoint2);
        double[] tail2 = Arrays.copyOfRange(parent2, crossoverPoint2, parent2.length);
        double[] head3 = Arrays.copyOfRange(parent3, 0, crossoverPoint1);
        double[] middle3 = Arrays.copyOfRange(parent3, crossoverPoint1, crossoverPoint2);
        double[] tail3 = Arrays.copyOfRange(parent3, crossoverPoint2, parent3.length);

        double[] child1genes = concatenate3(head1, middle2, tail3, head1.length+middle2.length+tail3.length); //samen
        double[] child2genes = concatenate3(middle1, tail2, head3, middle1.length+tail2.length+head3.length); //samen
        double[] child3genes = concatenate3(tail1, head2, middle3, tail1.length+head2.length+middle3.length); //samen

        Individual child1 = new Individual(false, evaluation);
        child1.setGenes(child1genes);
        number++;
        nextIndividuals.saveIndividual(child1);

        Individual child2 = new Individual(false, evaluation);
        child2.setGenes(child2genes);
        number++;
        nextIndividuals.saveIndividual(child2);

        Individual child3 = new Individual(false, evaluation);
        child2.setGenes(child3genes);
        number++;
        nextIndividuals.saveIndividual(child3);
    }

    //CONCATENATE 3 ARRAYS = samen voegen van 3 arrays (deel van de drie ouders) om nieuwe child te maken.
    public double[] concatenate3(double[] head, double[] middle,double[] tail, int arrSize) {
        int hLen = head.length;
        int mLen = middle.length;
        int tLen = tail.length;

        @SuppressWarnings("unchecked")
        double[] c = (double[]) Array.newInstance(head.getClass().getComponentType(), hLen + mLen + tLen);
        System.arraycopy(head, 0, c, 0, hLen);
        System.arraycopy(middle, 0, c, hLen, mLen);
        System.arraycopy(tail, 0, c, hLen + mLen, tLen);

        return c;
    }


    //DIFFERENTIAL CROSSOVER with 3 parents
    public void diffThreeParents(double[] parent1, double[] parent2, double[] parent3, ContestEvaluation evaluation) {
        double[] together = shuffleArray(concatenate3(parent1, parent2, parent3,30)); //parents samen en geshuffled

        double[] child1genes = new double[geneSize]; //initialise new kids
        double[] child2genes = new double[geneSize];
        double[] child3genes = new double[geneSize];

        int counter1 = 0; //dit anders gaat index raar doen.
        int counter2 = 1;
        int counter3 = 2;

        for (int i = 0; i < 12; i++) {
            if (i == 10) {
                break;
            }
            child1genes[i] = together[counter1];
            child2genes[i] = together[counter2];
            child3genes[i] = together[counter3];
            counter1+=2;
            counter2+=2;
            counter3+=2;
        }
        Individual child1 = new Individual(false, evaluation);
        child1.setGenes(child1genes);
        number++;
        nextIndividuals.saveIndividual(child1);

        Individual child2 = new Individual(false, evaluation);
        child2.setGenes(child2genes);
        number++;
        nextIndividuals.saveIndividual(child2);

        Individual child3 = new Individual(false, evaluation);
        child2.setGenes(child3genes);
        number++;
        nextIndividuals.saveIndividual(child3);
    }


    public Population evolvePopulation3Parent(String crossOverType) { //for 3 parent
        //make new population for the babies, initialise is false because we dont want random indivs.
        nextIndividuals = new Population(nextindividualssize, this.evaluation, false);

        //alle nieuwe babies maken en in een nieuwe population nextIndividuals stoppen.
        for (int i = 0; i < nextindividualssize; i++) { //popluation Size moet variable worden naar HELFT van populationSize ipv 10
            Individual parent1 = tournamentSelection(currentPopulation);
            Individual parent2 = tournamentSelection(currentPopulation);
            Individual parent3 = tournamentSelection(currentPopulation);
            if (crossOverType == "twoPoint") {
                twoPointCrossOver(parent1.genes, parent2.genes, parent3.genes, this.evaluation);
            } else if (crossOverType == "differential") {
                diffThreeParents(parent1.genes, parent2.genes, parent3.genes, this.evaluation);
            }
        }

        //aan de populatie nextIndividuals alle individuals van currentPopulation toevoegen(de parents erbij dus).
        int parentsAndBabySize = popsize + nextindividualssize;
        
        parentsAndBabyPop = new Population(parentsAndBabySize, this.evaluation, false); //20 moet populationSize x 2 zijn.

        for (int i = 0; i < popsize; i++) { //10 moet populationSize zijn.
            parentsAndBabyPop.saveIndividual(currentPopulation.getIndividual(i));
        }
        for (int i = 0; i < nextindividualssize; i++) {
            parentsAndBabyPop.saveIndividual(nextIndividuals.getIndividual(i));
        }

        //System.out.println(parentsAndBabyPop.individuals);

        //hier gaan we even tijdelijk vanuit parentsAndBabyPop de fitste in opslaan, wordt vervolgens currentPopulation
        tempFittest = new Population(popsize, this.evaluation, false); //10 is populationSize

        for (int i = 0; i <popsize; i++) { //10 moet popSize zijn
            Individual fitste = parentsAndBabyPop.getFittestIndividual();
            parentsAndBabyPop.removeFittest();
            tempFittest.saveIndividual(fitste);
        }

        //System.out.println(tempFittest.individuals);

        return tempFittest;
    }

    public Population evolvePopulation2Parent(String crossOverType) {
        //for 2 parent
        // make new population for the babies, initialise is false because we dont want random indivs.
        nextIndividuals = new Population(nextindividualssize, this.evaluation, false);

        //alle nieuwe babies maken en in een nieuwe population nextIndividuals stoppen.
        for (int i = 0; i < nextindividualssize; i++) { //popluation Size moet variable worden naar HELFT van populationSize ipv 10
            Individual parent1 = tournamentSelection(currentPopulation);
            Individual parent2 = tournamentSelection(currentPopulation);
            if (crossOverType == "onePoint") {
                onePointCrossOver(parent1.genes, parent2.genes, this.evaluation);
            } else if (crossOverType == "differential") {
                diffTwoParent(parent1.genes, parent2.genes, this.evaluation);
            }
        }

        //aan de populatie nextIndividuals alle individuals van currentPopulation toevoegen(de parents erbij dus).
        int parentsAndBabySize = popsize + nextindividualssize;
        parentsAndBabyPop = new Population(parentsAndBabySize, this.evaluation, false); //20 moet populationSize x 2 zijn.

        for (int i = 0; i < popsize; i++) { //10 moet populationSize zijn.
            parentsAndBabyPop.saveIndividual(currentPopulation.getIndividual(i));
        }
        for (int i = 0; i < nextindividualssize; i++) {
            parentsAndBabyPop.saveIndividual(nextIndividuals.getIndividual(i));
        }

        //hier gaan we even tijdelijk vanuit parentsAndBabyPop de fitste in opslaan, wordt vervolgens currentPopulation
        tempFittest = new Population(popsize, this.evaluation, false); //10 is populationSize

        for (int i = 0; i <popsize; i++) { //10 moet popSize zijn
            Individual fitste = parentsAndBabyPop.getFittestIndividual();
            parentsAndBabyPop.removeFittest();
            tempFittest.saveIndividual(fitste);
        }
        return tempFittest;
    }



    public void run() {
        //initializing first population
        if (currentPopulation == null) {
            currentPopulation = new Population(popsize, this.evaluation, true);
            number = currentPopulation.evaluations_limit; 
            //populationSize moet variable
            System.out.print(number);
        }


        // Run your algorithm here
        int evals = 0;
        System.out.println(evaluations_limit_);
        while (number < evaluations_limit_) { //5 was evaluations_limit_

            String crossOverType = "differential"; //hier specificeren welke crossover je wilt: onePoint / twoPoint / differential
            //Population tempFittest = evolvePopulation2Parent(crossOverType);
            Population tempFittest = evolvePopulation3Parent(crossOverType);
            currentPopulation = tempFittest;
            evals++;
            number++;
            System.out.println(evals);
            System.out.println(currentPopulation.size());
            System.out.println(number);
            //System.out.println(currentPopulation.evaluations_limit);


            //Individual parent1 = tournamentSelection(currentPopulation);
            //Individual parent2 = tournamentSelection(currentPopulation);

            //Individual parent3 = tournamentSelection(currentPopulation);

            //double[] x = onePointCrossOver(parent1.genes, parent2.genes, this.evaluation);
            //double[] y = shuffleArray(x);

            //double[] z = diffTwoParent(parent1.genes, parent2.genes);

            //double[] x = concatenate(parent1.genes, parent2.genes);

            //Calculate & set fitness of currentPopulation

            //Individual indiv = currentPopulation.getFittestIndividual();
            //System.out.println(indiv);

            // Select survivors
        }
    }
}


///current pop.
///new babies = new population with all the babies.
///Ouders & Kind population
///current pop = fitste van ouders&kind pop.