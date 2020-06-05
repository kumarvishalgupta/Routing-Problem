package com.company;

import java.util.*;

public class Individual
{
    List<Integer> chromosome;
    List<Integer> indexOfPersons,indexOfEmptyOnes;
    Map<Integer,Integer> valueIndexHashMap;
    double fitness;


    public Individual(List<Integer> chromosome) {
        this.chromosome = chromosome;
        this.valueIndexHashMap = new HashMap<>();
        createMap();createIndexes();
        fitness = cal_fitness();
    };

    Individual mate(Individual par2) {
        List<Integer> child_chromosome = this.chromosome;

        Random random = new Random();
        double p = random.nextDouble();

        int len = child_chromosome.size();

        int personIndex1= random.nextInt(indexOfPersons.size());
        int index1 = indexOfPersons.get(personIndex1);

        if(p < 0.70)
        {
            int emptyPersonIndex2= random.nextInt(par2.indexOfEmptyOnes.size());
            int index = par2.indexOfEmptyOnes.get(emptyPersonIndex2);
            int index2= this.valueIndexHashMap.get(par2.chromosome.get(index));

            swapVal(child_chromosome,index1,index2);
        } else if(p<0.90) {
            int personIndex2= random.nextInt(par2.indexOfPersons.size());
            int index = par2.indexOfPersons.get(personIndex2);
            int index2= this.valueIndexHashMap.get(par2.chromosome.get(index));

            swapVal(child_chromosome,index1,index2);
        }


        // create new Individual(offspring) using
        // generated chromosome for offspring
        return new Individual(child_chromosome);
    };

    Individual mutate() {
        // chromosome for offspring
        List<Integer> child_chromosome = this.chromosome;

        // random probability
        Random random = new Random();
        double p = random.nextDouble();

        int len = child_chromosome.size();

        if(p<0.70){
            int personIndex1= random.nextInt(indexOfPersons.size());
            int index1 = indexOfPersons.get(personIndex1);
            int emptyPersonIndex2= random.nextInt(indexOfEmptyOnes.size());
            int index2 = indexOfEmptyOnes.get(emptyPersonIndex2);

            swapVal(child_chromosome,index1,index2);
        } else if(p<0.90){
            int personIndex1= random.nextInt(indexOfPersons.size());
            int index1 = indexOfPersons.get(personIndex1);
            int personIndex2= random.nextInt(indexOfPersons.size());
            int index2 = indexOfPersons.get(personIndex2);

            swapVal(child_chromosome,index1,index2);
        }

        // create new Individual(offspring) using
        // generated chromosome for offspring
        return new Individual(child_chromosome);
    };

    Individual reverseChromosome() {
        List<Integer> chromosome = new ArrayList<>(this.chromosome);
        Collections.reverse(chromosome);
        return new Individual(chromosome);
    }

    Individual reverseHalfChromosome() {
        List<Integer> firstHalfChromosome = new ArrayList<>(this.chromosome.subList(0,this.chromosome.size()/2));
        List<Integer> secondHalfChromosome = new ArrayList<>(this.chromosome.subList((this.chromosome.size()/2),this.chromosome.size()));

        Collections.reverse(firstHalfChromosome);
        Collections.reverse(secondHalfChromosome);

        List<Integer> newChromosome = new ArrayList<>();
        newChromosome.addAll(firstHalfChromosome);
        newChromosome.addAll(secondHalfChromosome);

        return new Individual(newChromosome);
    }

    Individual scramble() {
        List<Integer> oneThirdChromosome = new ArrayList<>(this.chromosome.subList(0,this.chromosome.size()/2));
        List<Integer> secondThirdChromosome = new ArrayList<>(this.chromosome.subList((this.chromosome.size()/2),3*(this.chromosome.size()/5)));
        List<Integer> lastThirdChromosome = new ArrayList<>(this.chromosome.subList(3*(this.chromosome.size()/5),this.chromosome.size()));
        Collections.shuffle(secondThirdChromosome);

        List<Integer> newChromosome = new ArrayList<>();
        newChromosome.addAll(oneThirdChromosome);
        newChromosome.addAll(secondThirdChromosome);
        newChromosome.addAll(lastThirdChromosome);

        return new Individual(newChromosome);
    }



    // Calculate fittness score,
    double cal_fitness() {
        int len = this.chromosome.size();
        double fitness = 0.0;

        for(int i = 0;i<len;i+=3)
        {
            int last=0,flagCab=0;
            double totalDist=0.0;

            for(int j=0;j<3;j++)
                if(chromosome.get(i+j)>0){
                    flagCab=1;
                    totalDist= totalDist + CabPool.distanceMatrix.get(last).get(chromosome.get(i + j));
                    last = chromosome.get(i+j);
                }

            fitness=fitness + flagCab* CabPool.fixedPriceCab + CabPool.PerKmPrice * totalDist;

        }
        return fitness;
    };

    void createIndexes(){
        this.indexOfPersons=new ArrayList<>();
        this.indexOfEmptyOnes=new ArrayList<>();
        for(int i=0;i<this.chromosome.size();i++) {
            if (this.chromosome.get(i) > 0) {
                indexOfPersons.add(i);
            } else{
                indexOfEmptyOnes.add(i);
            }

        }
    }


    void createMap() {
        for(int i=0;i<this.chromosome.size();i++) this.valueIndexHashMap.put(chromosome.get(i),i);
    }

    void swapVal(List<Integer> child_chromosome,int index1,int index2){
        int c= child_chromosome.get(index1);
        child_chromosome.set(index1,child_chromosome.get(index2));
        child_chromosome.set(index2,c);
    }

    public double getFitness(){
        return this.fitness;
    }

};