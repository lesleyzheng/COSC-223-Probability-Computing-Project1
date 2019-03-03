import java.util.*;
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
	
//	PriorityQueue<Integer>
//	//int cacheSize;
//	int cacheCount = 0;
//	
//	//need to reset
//	int hits;
//	int totalCount;
//	
//	//debug
//	boolean debug = false;
//	
//	public LFU(int s) { //input size //I AM HERE
//		cacheSize = s;
//	}
//	
//	public void addRequest(int request) {
//		
//		if (cache.contains(request)) {
//			
//			cache.remove(request);
//			cache.add(request);
//			hits++;
//			
//			if(debug)
//				System.out.println("A");
//			
//		} else { //need to add to cache: add/replace?
//			
//			if (cacheCount<cacheSize) {//can just add
//				
//				cache.add(request);
//				cacheCount++;
//				
//				if(debug)
//					System.out.println("B");
//				
//			} else {//need to kick something out
//				
//				cache.remove();
//				cache.add(request);
//
//				if(debug) {
//					System.out.println("C");
//					System.out.println(cache.size());
//				}
//				
//			}
//		}
//		
//		totalCount++;
//		
//		if (debug) {
//			System.out.println("current cache: ");
//			this.peek();
//			System.out.println();
//		}
//		
//	}//addRequest
//	
//	public void initialize() {
//		hits = 0;
//		totalCount = 0;
//	}//initialize
//	
//	public double hitRate() {
//		return hits/totalCount;
//	}//hitRate
//	
//	public void peek() {
//		this.initialize();
//		System.out.println(cache.toArray());
//	}//peek
	
}


public class CachingSimulator {
	
	int[] randoms = new int[30];
	boolean debug = true;
	
	public CachingSimulator() {
		
		System.out.println("start!");
		
		//Dummy Randoms
		for (int n = 0; n<30; n++) {
			int num = ThreadLocalRandom.current().nextInt(1, 1001);
			randoms[n] = num;
		}
		
		//Random Simulator
		for (int i = 10; i<11; i=i+10) {
			
			System.out.println("Random Simulation Cache Size " + i);
			
			RandomSim randSim = new RandomSim(i);
			
			for (int j = 0; j<20; j++) {
				randSim.addRequest(randoms[j]);
				
				if (debug)
					randSim.peek();
			}
			
			//randSim.peek();
			
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("main");
		CachingSimulator mainInstance = new CachingSimulator();
	}

}
