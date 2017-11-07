import java.util.Random;
import java.util.ArrayList;

public class Simulation {

	/*Settings*/
	private double mutationRate = 0.001;
	private double populationSize = 200;
	private int maxNumGenerations = 100000;
	private Creature desiredCreature;

	/*State*/
	private int numGenerations = 0;
	private Population lastGeneration;
	private Population currentGeneration;
	private boolean isSimulationComplete = false;

	/*Data*/
	private ArrayList< Population > previousGenerations;

	/*RNG*/
	private Random rng;
	private CreatureRoulette roulette;

	public Simulation() {

		/*Init RNG*/
		rng = new Random();
		roulette = new CreatureRoulette();

		/*Init Solution*/
		String desiredGenomeString = "AAABCCBBAAGGAABBCCBBBBABCCBBrrAAGGrr";
		char[] desiredGenome = desiredGenomeString.toCharArray();

		desiredCreature = new Creature( this );
		desiredCreature.setGenome( desiredGenome );

	}	

	public void start () {

		/*Desired Creature*/
		System.out.println("This is the creature we want:");

		desiredCreature.printGenome();
		desiredCreature.printNotes();

		/*Initialize first Population*/
		int populationSize = 200;
		currentGeneration = new Population(this, populationSize);
		this.numGenerations++;

		/*Step Simulation*/
		while (!this.isSimulationComplete) {

			this.nextGeneration();

		}
	}

	public boolean getRandomBoolean () {

		return rng.nextBoolean();

	}

	public int getRandomInteger () {

		return rng.nextInt();

	}

	public Creature chooseCreature ( Population pop ) {

		return roulette.chooseCreature( pop );

	}

	private void nextGeneration() {

		currentGeneration.testCreatures();
		//previousGenerations.add( currentGeneration );

		if ( numGenerations == maxNumGenerations ) {
			this.isSimulationComplete = true;
			return;
		}

		lastGeneration = currentGeneration;
		currentGeneration = new Population( lastGeneration );
		this.numGenerations++;
	}

	public void testCreature( Creature c ) {

		/*Compare bits with desired creature*/
		boolean[] genomeUnderTest = c.getGenome();
		boolean[] desiredGenome = this.desiredCreature.getGenome();

		int fitness = 0;
		int index = 0;
		int bitsPerGene = c.getBitsPerGene();

		for (int i = 0; i < genomeUnderTest.length; i += bitsPerGene) {

			boolean geneMatches = true;
			for (int j = 0; j < bitsPerGene; j++) {
				if ( genomeUnderTest[i+j] != desiredGenome[i+j] ) {
					geneMatches = false;
					break;
				}
			}
			
			if (geneMatches) {
				fitness++;
			}
		}
				


		c.setFitness( fitness );

		

	}

}
