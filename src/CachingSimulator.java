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
	
	public RandomSim(int s) { //input size
		cacheSize = s;
		keys = new int[s];
	}
	
	public void addRequest(int request) {
		
		if (cache.containsKey(request)) {
			hits++;
			System.out.println("A");
		} else { //need to add to cache: add/replace?
			
			if (cacheCount<cacheSize) {//can just add
				
				cache.put(request, -1); //value doesn't matter
				keys[cacheCount] = request;
				cacheCount++;
				System.out.println("B");
				
			} else {//need to kick something out
				
				int randomNum = ThreadLocalRandom.current().nextInt(0, cacheSize);
				//https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
				int request_to_replace = keys[randomNum];
				int removed = cache.remove(request_to_replace);
				cache.put(request, -1);
				System.out.println("C");
				System.out.println(removed);
				
			}
		}
		
		totalCount++;
		this.peek();
	
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


public class CachingSimulator {
	
	int[] randoms = new int[30];
	
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
				//randSim.peek();
			}
			
			//randSim.peek();
			
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("main");
		CachingSimulator mainInstance = new CachingSimulator();
	}

}
