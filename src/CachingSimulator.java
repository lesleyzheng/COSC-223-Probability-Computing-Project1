import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

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
		System.out.println(cache.keySet());
	}//peek
	
}//RandomSim

class LRU{ //Least recently used
	
	LinkedList<Integer> cache = new LinkedList<Integer>();
	int cacheSize;
	int cacheCount = 0;
	
	//need to reset
	int hits;
	int totalCount;
	
	//debug
	boolean debug = false;
	
	public LRU(int s) { //input size
		cacheSize = s;
	}
	
	public void addRequest(int request) {
		
		if (cache.contains(request)) {
			
			cache.remove(request);
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
		System.out.println(cache.toArray());
	}//peek
	
}//LRU

class LFU{ //use a priority queue
	
	Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
	int cacheSize;
	int cacheCount = 0;
	
	//need to reset
	int hits;
	int totalCount;
	
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
		System.out.println(cache.keySet());
	}//peek
	
}

class FIFO {
	int cacheSize;
	int totalCount;
	ArrayList<Integer> cache = new ArrayList<Integer>();
	int cacheCount=0;
	int hits=0;

	//debug
	boolean debug = false;

	public FIFO(int s) { //input size
		cacheSize = s;
	}

	public void addRequest(int request) {

		boolean inCache = false;

		for (int i=0; i<= cache.size(); i++) {
			if ((Integer)i==cache.get(i)) {
				hits+=1;
				inCache = true;

				if(debug) {
					System.out.println("A");
				}
				break;
			}
		}
		if (inCache == false) {
			if (cacheCount < cacheSize) {
			cache.add((Integer)request);
			cacheCount+=1;

			if (debug){
				System.out.println("B");
			}
			}
			else {
				cache.add((Integer)request);
				cache.remove(0);

				if(debug) {
					System.out.println("C");
					System.out.println(cache.size());
				}
			}
		}
		totalCount++;

		if(debug) {
			System.out.println("current cache: ");
			this.peek();
			System.out.println();
		}
	}
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
		System.out.println(cache.toArray());
	}//peek

}

public class CachingSimulator extends Distribution{
	
	int[] randoms = new int[30];
	boolean debug = true;
	
	public CachingSimulator() {

		System.out.println("start!");

		int[] cacheSizes = {10, 50, 100, 150, 200};

		Object[][] dataForTable = new Object[40][4];

		//Distribution: Uniform (uniformDist)

		//Policy: Random
		//Varying Cache Size

		int tableRow = 0; //row 0 to 4
//		for (int cacheS : cacheSizes) {
//
//			tableRow++;
//
//			double[] forAverage = new double[5];
//
//			for (int repeat = 0; repeat < 5; repeat++) {//repeat to get average
//				RandomSim rando = new RandomSim(cacheS);
//
//				for (int counter = 0; counter < 100; counter++) { //calculating hit rate; 100000
//
//					rando.addRequest(uniformDist());
//					if (counter == 51) {
//						rando.initialize(); //toss out first 10^4
//					}
//
//				}
//
//				double hRate = rando.hitRate();
//				forAverage[repeat] = hRate;
//
//				if (debug){
//					System.out.println("distribution uniform, random cache policy, cache size " + cacheS + ", repetition " + repeat + ", hit rate " + rando.hitRate());
//				}
//
//			}//repeats
//
//			double hitRate = average(forAverage);
//
//			dataForTable[tableRow][0] = "Uniform";
//			dataForTable[tableRow][1] = "Random";
//			dataForTable[tableRow][2] = cacheS;
//			dataForTable[tableRow][3] = hitRate;
//			//{"Uniform", "Random", cacheS, hitRate};
//		}
//
//		for (int i = 0; i < dataForTable.length; i++){
//			System.out.println(Arrays.toString(dataForTable[i]));
//		}

		//Policy: LRU
		//Varying Cache Size

		tableRow = 5; //row 5 to 9
		for (int cacheS : cacheSizes) {

			tableRow++;

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < 5; repeat++) {//repeat to get average
				LRU myLRU = new LRU(cacheS);

				for (int counter = 0; counter < 100; counter++) { //calculating hit rate; 100000

					myLRU.addRequest(uniformDist());
					if (counter == 51) {
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
		}

		for (int i = 0; i < dataForTable.length; i++){
			System.out.println(Arrays.toString(dataForTable[i]));
		}

		//Policy: LFU
		//Varying Cache Size

		tableRow = 10; //row 10 to 14
		for (int cacheS : cacheSizes) {

			tableRow++;

			double[] forAverage = new double[5];

			for (int repeat = 0; repeat < 5; repeat++) {//repeat to get average
				LFU myLFU = new LFU(cacheS);

				for (int counter = 0; counter < 100; counter++) { //calculating hit rate; 100000

					myLFU.addRequest(uniformDist());
					if (counter == 51) {
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
		}

		for (int i = 0; i < dataForTable.length; i++){
			System.out.println(Arrays.toString(dataForTable[i]));
		}

	} //constructor


	public double average(double[] myArray) {

		double sum = 0;

		for (double item:myArray) {

			sum+=item;

		}

		double value = sum/(double)myArray.length;

		return Math.round(value * 100000d) / 100000d;

	}

	public static void main(String[] args) {

		System.out.println("main");
		CachingSimulator mainInstance = new CachingSimulator();
	}//main
}
