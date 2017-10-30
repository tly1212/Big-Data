
import java.util.BitSet;
import java.util.Random;
/**
 * didnot change the filter size!!!
 * @author sunmoon
 */

public class BloomFilterRan {
	private int counter; 				//# of string added	
//	private int setSize;				//Size of input set S, |S|=N					
	private int numHash;				//Size of input set S, |S|=N	
	private int filterSize;				//filter size for each T,|T|=M
	private BitSet filter;			//filter memory, K*|T|
	private int[][] seed;
	public BloomFilterRan(int setSize, int bitsPerElement){
		this.counter=0;
		this.filterSize=findnextprime(setSize*bitsPerElement);
		this.numHash=(int)Math.floor(Math.log(2.0)*filterSize/setSize);
		this.seed = new int[numHash][2];
		filter=new BitSet(filterSize);
		

		Random rand=new Random();
		for(int i=0;i<numHash;i++){
			seed[i][0]=rand.nextInt(filterSize);
			seed[i][1]=rand.nextInt(filterSize);
		}
	}

	public void add(String s){
		s.toLowerCase();
		if(appears(s)) return;
		counter++;
		for(int i=0;i<numHash;i++){
			byte[] s_chars=s.getBytes();
			filter.set((int) ((ranhash(seed[i],s_chars)+filterSize)%filterSize));
		}
	}
	
	public boolean appears(String s){
		s.toLowerCase();
		byte[] s_chars=s.getBytes();
		for(int i=0;i<numHash;i++){
			if(!filter.get((int) ((ranhash(seed[i],s_chars)+filterSize)%filterSize))) return false;
		}
		return true;
	}
	public int filterSize(){
		return this.filterSize;
	}
	public int dataSize() {
		return this.counter;
	}
	public int numHashes(){
		return this.numHash;
	}
	
	private int ranhash(int[] k,byte[] s){
//		int h=0;
		int h=0x811c9dc5;
		for(int i=0;i<s.length;i++){
			h^=s[i];
			h=(k[0]*h+k[1])%this.filterSize;
		}
		return h;
		
	}
	
	private int findnextprime(int n) {
		boolean isPrime=false;
		while(!isPrime){
		    if (n%2==0) {
		    	n++;
		    	continue;
		    }
		    int i=3;
		    for(;i*i<=n;i+=2) {
		        if(n%i==0)	break;
		    }
		    if(i*i>n) isPrime = true;
		    else n++;
		}
		return n;
	}
}
