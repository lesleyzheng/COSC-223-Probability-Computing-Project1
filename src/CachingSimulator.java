import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.io.*;


class RandomSim{
	
	Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
	int cacheSize;
	int cacheCount = 0;
	int[] keys;
	
	//need to reset
	double hits;
	double totalCount;
	
	//debug
	boolean debug = false;
	
	public RandomSim(int s) { //input size
		cacheSize = s;
		keys = new int[s];
	}//constructor
	
	public void addRequest(int request) {

		totalCount = totalCount + 1;
		
		if (cache.containsKey(request)) {
			hits = hits + 1;
			
			if(debug)
				System.out.println("A");
			
		} else { //need to add to cache: add/replace?

			if (cacheCount < cacheSize) {//can just add

				cache.put(request, -1); //value doesn't matter
				keys[cacheCount] = request;
				cacheCount++;

				if (debug)
					System.out.println("B");

			} else {//need to kick something out

				int randomNum = ThreadLocalRandom.current().nextInt(0, cacheSize);
				//https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java


				int request_to_replace = keys[randomNum];
				keys[randomNum] = request;
				cache.remove(request_to_replace);
				cache.put(request, -1);

				if (debug) {
					System.out.println("C");
					System.out.println("random num " + randomNum);
					System.out.println("keys " + Arrays.toString(keys));
					System.out.println("request to replace " + request_to_replace);
					System.out.println("cache size" + cache.size());
				}

			}
		}
		
		if (debug) {
			System.out.println("current cache: ");
			this.peek();
			System.out.println("hits and count: " + hits + "  " + totalCount);
			System.out.println("----");

		}
		
	}//addRequest
	
	public void initialize() {
		hits = 0;
		totalCount = 0;
	}//initialize
	
	public double hitRate() {

		if (totalCount==0){
			System.out.println("zero denominator");
			return 0;
		}

		double value = hits/totalCount;
		return (double)Math.round(value * 100000d) / 100000d;

	}//hitRate
	
	public void peek() {
		System.out.println("hits " + hits + " ; totalCount " + totalCount);
		System.out.println(cache.keySet());
	}//peek
	
}//RandomSim

class LRU{ //Least recently used
	
	LinkedList<Integer> cache = new LinkedList<Integer>();
	int cacheSize;
	int cacheCount = 0;
	
	//need to reset
	double hits;
	double totalCount;
	
	//debug
	boolean debug = false;
	
	public LRU(int s) { //input size
		cacheSize = s;
	}
	
	public void addRequest(int request) {
		
		if (cache.contains(request)) {

			int index = cache.indexOf(request);
			cache.remove(index);
			cache.add(request);
			hits++;
			
			if(debug)
				System.out.println("A");
			
		} else { //need to add to cache: add/replace?
			
			if (cacheCount<cacheSize) {//can just add
				
				cache.add(request);
				cacheCount++;
				
				if(debug)
					System.out.println("B");
				
			} else {//need to kick something out
				
				cache.remove();
				cache.add(request);

				if(debug) {
					System.out.println("C");
					System.out.println(cache.size());
				}
				
			}
		}
		
		totalCount++;
		
		if (debug) {
			System.out.println("current cache: ");
			this.peek();
			System.out.println();
		}
		
	}//addRequest
	
	public void initialize() {
		hits = 0;
		totalCount = 0;
	}//initialize

	public double hitRate() {

		if (totalCount==0){
			System.out.println("zero denominator");
			return 0;
		}

		double value = hits/totalCount;
		return (double)Math.round(value * 100000d) / 100000d;

	}//hitRate
	
	public void peek() {
		System.out.println("hits " + hits + " ; totalCount " + totalCount);
		System.out.println(Arrays.toString(cache.toArray()));
	}//peek
	
}//LRU

class LFU{ //use a priority queue
	
	Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
	int cacheSize;
	int cacheCount = 0;
	
	//need to reset
	double hits;
	double totalCount;
	
	//debug
	boolean debug = false;
	
	public LFU(int s) { //input size
		cacheSize = s;
	}
	
	public void addRequest(int request) {
		
		if (cache.containsKey(request)) {
			
			int current_value = cache.get(request);
			cache.replace(request, current_value + 1);
			hits++;
			
			if(debug)
				System.out.println("A");
			
		} else { //need to add to cache: add/replace?
			
			if (cacheCount<cacheSize) {//can just add
				
				cache.put(request, 1); //value doesn't matter
				cacheCount++;
				
				if(debug)
					System.out.println("B");
				
			} else {//need to kick something out
				
				Entry<Integer, Integer> min = null;
				for (Entry<Integer, Integer> entry : cache.entrySet()) {
					if (min == null || min.getValue() > entry.getValue()) {
						min = entry;
					}//if
				}//for
				
				int request_to_replace = min.getKey();
				cache.remove(request_to_replace);
				cache.put(request, 1);

				if(debug) {
					System.out.println("C");
					System.out.println("request to replace " + request_to_replace);
					System.out.println(cache.size());
				}
				
			}//else - kicking something out
		} //else
		
		totalCount++;
		
		if (debug) {
			System.out.println("current cache: ");
			this.peek();
			System.out.println();
		}
		
	}//addRequest
	
	public void initialize() {
		hits = 0;
		totalCount = 0;
	}//initialize

	public double hitRate() {

		if (totalCount==0){
			System.out.println("zero denominator");
			return 0;
		}

		double value = hits/totalCount;
		return (double)Math.round(value * 100000d) / 100000d;

	}//hitRate
	
	public void peek() {
		System.out.println("hits " + hits + " ; totalCount " + totalCount);
		System.out.println(cache.keySet());
	}//peek
	
}

class FIFO {

	int cacheSize;
	double totalCount;
	LinkedList<Integer> cache = new LinkedList<Integer>();
	int cacheCount=0;
	double hits=0;

	//debug
	boolean debug = false;

	public FIFO(int s) { //input size
		cacheSize = s;
	}

	public void addRequest(int request) {

		if (cache.contains(request)) {

			hits++;

			if(debug)
				System.out.println("A");

		} else { //need to add to cache: add/replace?

			if (cacheCount<cacheSize) {//can just add

				cache.add(request);
				cacheCount++;

				if(debug)
					System.out.println("B");

			} else {//need to kick something out

				cache.removeFirst();
				cache.add(request);

				if(debug) {
					System.out.println("C");
					System.out.println(cache.size());
				}

			}
		}

		totalCount++;

		if (debug) {
			System.out.println("current cache: ");
			this.peek();
			System.out.println();
		}
	}


	public void initialize() {
		hits = 0;
		totalCount = 0;

		if (debug){
			System.out.println("initialized!");
		}

	}//initialize

	public double hitRate() {

		if (totalCount==0){
			System.out.println("zero denominator");
			return 0;
		}

		double value = hits/totalCount;
		return (double)Math.round(value * 100000d) / 100000d;

	}//hitRate
	
	public void peek() {
        System.out.println("hits " + hits + " ; totalCount " + totalCount);
		System.out.println(Arrays.toString(cache.toArray()));
	}//peek

}

public class CachingSimulator extends Distribution{

	static boolean debug = false;
	
	public CachingSimulator(){

	} //constructor


	public static double average(double[] myArray) {

		double sum = 0;

		for (double item:myArray) {

			sum+=item;

		}

		double value = sum/(double)myArray.length;

		return Math.round(value * 100000d) / 100000d;

	}


	public static void main(String[] args) throws IOException{

		System.out.println("main");
		CachingSimulator mainInstance = new CachingSimulator();





		System.out.println("start!");

		int[] cacheSizes = {10, 50, 100, 150, 200};

		Object[][] dataForTable = new Object[60][4];

		int number_req = 1000000;
		int steadt_state_marker = 10001;

		RequestGenerator rg = new RequestGenerator();
		Distribution forZipf = new Distribution();

		int repeat_num = 1;

		//Distribution: Uniform (uniformDist)

		//Policy: Random
		//Varying Cache Size

		int tableRow = 0; //row 0 to 4
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				RandomSim rando = new RandomSim(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					rando.addRequest(uniformDist());
					if (counter == steadt_state_marker) {
						rando.initialize(); //toss out first 10^4
					}

				}

				double hRate = rando.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, random cache policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + rando.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Uniform";
			dataForTable[tableRow][1] = "Random";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LRU
		//Varying Cache Size

		tableRow = 5; //row 5 to 9
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				LRU myLRU = new LRU(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myLRU.addRequest(uniformDist());
					if (counter == steadt_state_marker) {
						myLRU.initialize(); //toss out first 10^4
					}

				}

				double hRate = myLRU.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LRU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myLRU.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Uniform";
			dataForTable[tableRow][1] = "LRU";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LFU
		//Varying Cache Size

		tableRow = 10; //row 10 to 14
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				LFU myLFU = new LFU(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myLFU.addRequest(uniformDist());
					if (counter == steadt_state_marker) {
						myLFU.initialize(); //toss out first 10^4
					}

				}

				double hRate = myLFU.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LFU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myLFU.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Uniform";
			dataForTable[tableRow][1] = "LFU";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: FIFO
		//Varying Cache Size

		tableRow = 15; //row 15 to 19
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				FIFO myFIFO = new FIFO(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myFIFO.addRequest(uniformDist());
					if (counter == steadt_state_marker) {
						myFIFO.initialize(); //toss out first 10^4
					}

				}

				double hRate = myFIFO.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LFU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myFIFO.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Uniform";
			dataForTable[tableRow][1] = "FIFO";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}


		//Distribution: Zipf (zipfDist)

		//Policy: Random
		//Varying Cache Size

		tableRow = 20; //row 20 to 24
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				RandomSim rando = new RandomSim(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					rando.addRequest(forZipf.zipfDist());
					if (counter == steadt_state_marker) {
						rando.initialize(); //toss out first 10^4
					}

				}

				double hRate = rando.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, random cache policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + rando.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Zipf";
			dataForTable[tableRow][1] = "Random";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LRU
		//Varying Cache Size

		tableRow = 25; //row 25 to 29
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				LRU myLRU = new LRU(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myLRU.addRequest(forZipf.zipfDist());
					if (counter == steadt_state_marker) {
						myLRU.initialize(); //toss out first 10^4
					}

				}

				double hRate = myLRU.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LRU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myLRU.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Zipf";
			dataForTable[tableRow][1] = "LRU";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LFU
		//Varying Cache Size

		tableRow = 30; //row 30 to 34
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				LFU myLFU = new LFU(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myLFU.addRequest(forZipf.zipfDist());
					if (counter == steadt_state_marker) {
						myLFU.initialize(); //toss out first 10^4
					}

				}

				double hRate = myLFU.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LFU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myLFU.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Zipf";
			dataForTable[tableRow][1] = "LFU";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: FIFO
		//Varying Cache Size

		tableRow = 35; //row 35 to 39
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				FIFO myFIFO = new FIFO(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myFIFO.addRequest(forZipf.zipfDist());
					if (counter == steadt_state_marker) {
						myFIFO.initialize(); //toss out first 10^4
					}

				}

				double hRate = myFIFO.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LFU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myFIFO.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Zipf";
			dataForTable[tableRow][1] = "FIFO";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}


		//Distribution: Temporal Locality (rg.generateRequest())

		//Policy: Random
		//Varying Cache Size

		tableRow = 40; //row 40 to 44
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				RandomSim rando = new RandomSim(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					rando.addRequest(rg.generateRequest());
					if (counter == steadt_state_marker) {
						rando.initialize(); //toss out first 10^4
					}

				}

				double hRate = rando.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, random cache policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + rando.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Temporal";
			dataForTable[tableRow][1] = "Random";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LRU
		//Varying Cache Size

		tableRow = 45; //row 45 to 49
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				LRU myLRU = new LRU(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myLRU.addRequest(rg.generateRequest());
					if (counter == steadt_state_marker) {
						myLRU.initialize(); //toss out first 10^4
					}

				}

				double hRate = myLRU.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LRU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myLRU.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Temporal";
			dataForTable[tableRow][1] = "LRU";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LFU
		//Varying Cache Size

		tableRow = 50; //row 50 to 54
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				LFU myLFU = new LFU(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myLFU.addRequest(rg.generateRequest());
					if (counter == steadt_state_marker) {
						myLFU.initialize(); //toss out first 10^4
					}

				}

				double hRate = myLFU.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LFU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myLFU.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Temporal";
			dataForTable[tableRow][1] = "LFU";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}

		//table
		for (int i = 0; i < dataForTable.length; i++) {
			if (dataForTable[i] != null)
				System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: FIFO
		//Varying Cache Size

		tableRow = 55; //row 55 to 59
		for (int cacheS : cacheSizes) {

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < repeat_num; repeat++) {//repeat to get average
				FIFO myFIFO = new FIFO(cacheS);

				for (int counter = 0; counter < number_req; counter++) { //calculating hit rate; 100000

					myFIFO.addRequest(rg.generateRequest());
					if (counter == steadt_state_marker) {
						myFIFO.initialize(); //toss out first 10^4
					}

				}

				double hRate = myFIFO.hitRate();
				forAverage[repeat] = hRate;

				if (debug){
					System.out.println("distribution uniform, LFU policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + myFIFO.hitRate());
				}

			}//repeats

			double hitRate = average(forAverage);

			dataForTable[tableRow][0] = "Temporal";
			dataForTable[tableRow][1] = "FIFO";
			dataForTable[tableRow][2] = cacheS;
			dataForTable[tableRow][3] = hitRate;
			//{"Uniform", "Random", cacheS, hitRate};

			tableRow++;
		}

		if (debug) {
			for (int i = 0; i < dataForTable.length; i++) {
				System.out.println(Arrays.toString(dataForTable[i]));
			}
		}


		//printing the table
		for (int i = 0; i < dataForTable.length; i++) {
			System.out.println(Arrays.toString(dataForTable[i]));
		}


		FileWriter writer = new FileWriter("results.csv");

		for (int j = 0; j < dataForTable.length; j++) {
			for (int k = 0; k < dataForTable[0].length; k++){
				writer.append(String.valueOf(dataForTable[j][k]));
				writer.append(",");
			}
			writer.append("\n");

		}
		writer.close();



	}//main
}
