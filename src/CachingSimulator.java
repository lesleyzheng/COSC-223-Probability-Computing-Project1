import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

class RandomSim{
	
	Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
	int cacheSize;
	int cacheCount = 0;
	int[] keys;
	
	//need to reset
	int hits;
	int totalCount;
	
	//debug
	boolean debug = false;
	
	public RandomSim(int s) { //input size
		cacheSize = s;
		keys = new int[s];
	}
	
	public void addRequest(int request) {
		
		if (cache.containsKey(request)) {
			hits++;
			
			if(debug)
				System.out.println("A");
			
		} else { //need to add to cache: add/replace?
			
			if (cacheCount<cacheSize) {//can just add
				
				cache.put(request, -1); //value doesn't matter
				keys[cacheCount] = request;
				cacheCount++;
				
				if(debug)
					System.out.println("B");
				
			} else {//need to kick something out
				
				int randomNum = ThreadLocalRandom.current().nextInt(0, cacheSize);
				//https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
				
				
				int request_to_replace = keys[randomNum];
				keys[randomNum] = request;
				cache.remove(request_to_replace);
				cache.put(request, -1);

				if(debug) {
					System.out.println("C");
					System.out.println("random num " + randomNum);
					System.out.println("keys "+ Arrays.toString(keys));
					System.out.println("request to replace " + request_to_replace);
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
		return hits/totalCount;
	}//hitRate
	
	public void peek() {
		this.initialize();
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
		return hits/totalCount;
	}//hitRate
	
	public void peek() {
		this.initialize();
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
		return hits/totalCount;
	}//hitRate
	
	public void peek() {
		this.initialize();
		System.out.println(cache.keySet());
	}//peek
	
}

public class CachingSimulator extends Distribution{
	
	int[] randoms = new int[30];
	boolean debug = true;
	
	public CachingSimulator() {
		
		System.out.println("start!");
		
		//Dummy Randoms
		// for (int n = 0; n<30; n++) {
		// 	int num = ThreadLocalRandom.current().nextInt(1, 1001);
		// 	randoms[n] = num;
		// }

		//To-Do: 1) add condition for which to not count the first 10^4 iterations
		
		//Random Simulator
		for (int i = 10; i<11; i=i+10) { //change to modify varying cache size
			
			System.out.println("Random Simulation (Uniform) Cache Size " + i);
			
			RandomSim randSim = new RandomSim(i);
			
			for (int j = 0; j<100000; j++) {

				randSim.addRequest(Distribution.uniformDist());
				
				if (debug)
					randSim.peek();
			}

			System.out.println("Random Simulation (Zipf) Cache Size " + i);
			
			RandomSim randSim2 = new RandomSim(i);
			
			for (int j = 0; j<100000; j++) {

				randSim2.addRequest(Distribution.zipfDist());
				
				if (debug)
					randSim2.peek();
			}
			
		}//RandomSim

		//LRU
		for (int i = 10; i<11; i=i+10) { //change to modify varying cache size
			
			System.out.println("LRU Simulation (Uniform) Cache Size " + i);
			
			LRU LRUSim = new LRU(i);
			
			for (int j = 0; j<100000; j++) {

				LRUSim.addRequest(Distribution.uniformDist());
				
				if (debug)
					LRUSim.peek();
			}

			System.out.println("LRU Simulation (Zipf) Cache Size " + i);
			
			LRU LRUSim2 = new LRU(i);
			
			for (int j = 0; j<100000; j++) {

				LRUSim2.addRequest(Distribution.zipfDist());
				
				if (debug)
					LRUSim2.peek();
			}
			
		}//LRU

		//LFU
		for (int i = 10; i<11; i=i+10) { //change to modify varying cache size
			
			System.out.println("LFU Simulation (Uniform) Cache Size " + i);
			
			LFU LFUSim = new LFU(i);
			
			for (int j = 0; j<100000; j++) {

				LFUSim.addRequest(Distribution.uniformDist());
				
				if (debug)
					LFUSim.peek();
			}

			System.out.println("LFU Simulation (Zipf) Cache Size " + i);
			
			LFU LFUSim2 = new LFU(i);
			
			for (int j = 0; j<100000; j++) {

				LFUSim2.addRequest(Distribution.zipfDist());
				
				if (debug)
					LFUSim2.peek();
			}
			
		}//LFU

		
	}
	
	public static void main(String[] args) {
		
		System.out.println("main");
		CachingSimulator mainInstance = new CachingSimulator();
	}

}
