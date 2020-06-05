package com.company;

import java.io.File;
import java.util.*;

public class CabPool {

    static List<List<Double> > distanceMatrix ;
    static int fixedPriceCab =300, PerKmPrice=50;

    static int totalNoOfPeople = 256;
    static int individualSize = (3*totalNoOfPeople), PopulationSize=1000;

    static List<Integer> GENES = new ArrayList<>();
    static List<Individual> population = new ArrayList<>();

    static Random random = new Random();

    public static void main(String[] args) throws Exception {

        distanceMatrix = new ArrayList<>();
        File file = new File("/Users/vishal.gupta5/Desktop/matrix.txt");
        Scanner sc = new Scanner(file);

        //Input for Distance Matrix
        for(int i=0;i<=totalNoOfPeople;i++){

            List<Double> tmp = new ArrayList<>();
            for(int j=0;j<=totalNoOfPeople;j++) {
                tmp.add(Double.valueOf(sc.nextDouble()));
            }
            distanceMatrix.add(tmp);
        }

        int generation = 0;
        int counter = 0;

        // Initializing Genes
        for(int i=0;i<individualSize;i++) {
            if (i < totalNoOfPeople)
                GENES.add(i + 1);
            else
                GENES.add(i - 4 * totalNoOfPeople);
        }

        // Populating with Chromosomes
        for(int i = 0;i<PopulationSize;i++)
        {
            List<Integer> gnome;
            gnome = create_gnome();
            population.add(new Individual(gnome));
        }

        double last_fitness = Integer.MAX_VALUE , delta=0 , generationWithSameMaxFitness=100;

        while(true)
        {

            // sort the population in increasing order of fitness score
            population.sort(Comparator.comparingDouble(Individual::getFitness));

            if((last_fitness - population.get(0).fitness) <= delta) {
                counter ++;

                if(counter>=generationWithSameMaxFitness) {
                    break;
                }
            }
            else{ counter=0; }

            last_fitness=population.get(0).fitness;

            System.out.println("generation : "+generation+ " Total Cost :" +last_fitness);

            // Generate new offsprings for new generation
            List<Individual> new_generation = new ArrayList<>();

            // Perform Elitism, that mean 10% of fittest population
            // goes to the next generation
            int s = (10*PopulationSize)/100;
            for(int i = 0;i<s;i++) {
                new_generation.add(population.get(i));
            }

            // 20% Mating of next generation Population obtained from 50% of fittest population, Individuals
            // will mate to produce offspring
            s = (20*PopulationSize)/100;
            for(int i = 0;i<s;i++)
            {
                int len = population.size();
                int r = random.nextInt(len/2);
                Individual parent1 = population.get(r);
                r = random.nextInt( len/2);
                Individual parent2 = population.get(r);
                Individual offspring = parent1.mate(parent2);
                new_generation.add(offspring);
            }

            // 20% Mutation of next generation Population obtained from 50% of fittest population, Individual
            // will mutate to produce offspring
            s = (20*PopulationSize)/100;
            for(int i = 0;i<s;i++)
            {
                int len = population.size();
                int r = random.nextInt(len/2);
                Individual parent = population.get(r);
                Individual offspring = parent.mutate();
                new_generation.add(offspring);
            }

            // 20% Reversion from 50% of fittest population, Individual
            // will reversed to produce offspring
            s = (20*PopulationSize)/100;
            for(int i = 0;i<s;i++)
            {
                int len = population.size();
                int r = random.nextInt(len/2);
                Individual parent = population.get(r);
                Individual offspring = parent.reverseChromosome();
                new_generation.add(offspring);
            }

            // 15% HalfReversion from 50% of fittest population, Individual
            // will halfreversed to produce offspring
            s = (15*PopulationSize)/100;
            for(int i = 0;i<s;i++)
            {
                int len = population.size();
                int r = random.nextInt(len/2);
                Individual parent = population.get(r);
                Individual offspring = parent.reverseHalfChromosome();
                new_generation.add(offspring);
            }

            // 15% Scrambling from 50% of fittest population, Individual
            // will scrambled to produce offspring
            s = (15*PopulationSize)/100;
            for(int i = 0;i<s;i++)
            {
                int len = population.size();
                int r = random.nextInt(len/2);
                Individual parent = population.get(r);
                Individual offspring = parent.scramble();
                new_generation.add(offspring);
            }
            population = new_generation;
            generation++;

        }
        System.out.println("Fitness: "+ population.get(0).fitness);
    }

    // Create random genes for mutation
    static int mutated_genes(List<Integer> integerList) {
        int len = integerList.size();
        int r = random.nextInt(len);
        int val = integerList.get(r);
        integerList.remove(r);
        return val;
    }

    // create chromosome or string of genes
    static List<Integer> create_gnome()
    {
        int len = individualSize;
        List<Integer> gnome = new ArrayList<>();
        List<Integer> genes = new ArrayList<>(GENES);
        for(int i = 0;i<len;i++)
            gnome.add(mutated_genes(genes));
        return gnome;
    }

}