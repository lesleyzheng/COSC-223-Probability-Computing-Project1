import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Distribution {

	public static int uniformDist(){
		int d = ThreadLocalRandom.current().nextInt(1,10001);
		return d;
	}

	public static int zipfDist(){
		//creates an ArrayList that has 1000 values of 1, 500 values of 2, ... etc. such that choosing a 
		//random element of the ArrayList will give you a value with probability according to Dipf distribution
		ArrayList<Integer> dist = new ArrayList<Integer>();
		for (int i=1; i<=1000; i++){
			int p = (int)(1000.0*(1.0/i)); 
			for (int j=1; j<=p; j++) {
				dist.add(i);
			}
		}
		int d = dist.get(ThreadLocalRandom.current().nextInt(0,dist.size()+1));
		return d;
	}
}
