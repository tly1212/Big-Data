import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Random;

public class DynamicBloomFilters {

	private int counter; 				//# of string added	
	private int curcounter;
	private ArrayList<Integer> filterSize;				//filter size for each T,|T|=M
	private int curfilterSize;
	private int bitsPerElement;
	private int numHash;
	private int initialSize=1000;
	//1000 8!!)% 100005.9% 100000 3% 1000000 0.8%.
	private ArrayList<BitSet> filter;			//filter memory, K*|T|
	private int[][] seed;				//hash function seed.
	
	public DynamicBloomFilters(int bitsPerElement){
		this.counter=0;
		this.curcounter=0;
		this.bitsPerElement=bitsPerElement;
		this.curfilterSize=findnextprime(initialSize*bitsPerElement);
		filterSize=new ArrayList<Integer>();
		filterSize.add(curfilterSize);
		BitSet F1=new BitSet(this.curfilterSize);
		filter=new ArrayList<BitSet>();
		filter.add(F1);

		numHash=(int)Math.floor(Math.log(2.0)*bitsPerElement);
		this.seed = new int[numHash][2];
		Random rand=new Random();
		for(int i=0;i<numHash;i++){
			seed[i][0]=rand.nextInt(filterSize.get(0));
			seed[i][1]=rand.nextInt(filterSize.get(0));
		}
	}

	public void add(String s){
		s.toLowerCase();
		if(appears(s)) return;
		counter++;
		curcounter++;
		if(curcounter>=initialSize){
			this.initialSize*=2;
			this.curfilterSize=findnextprime(initialSize*bitsPerElement);
			filterSize.add(curfilterSize);
			BitSet Fn=new BitSet(this.curfilterSize);
			filter.add(Fn);
			curcounter=0;
		}
		int index=filter.size()-1;
		for(int i=0;i<numHash;i++){
			byte[] s_chars=s.getBytes();
			filter.get(index).set((int)((ranhash(seed[i],s_chars,this.curfilterSize)+this.curfilterSize)%this.curfilterSize));
		}
	}
	

	public boolean appears(String s){
		s.toLowerCase();
		byte[] s_chars=s.getBytes();
		for(int i=0;i<filter.size();i++){
			if(isappears(i,s_chars)) return true;
		}
		return false;
	}
	
	

	private boolean isappears(int i,byte[] s_chars) {
		int[] shash=new int[numHash];
		for(int j=0;j<numHash;j++){
			shash[j]=(ranhash(seed[j],s_chars,filterSize.get(i))+filterSize.get(i))%filterSize.get(i);
			if(!filter.get(i).get(shash[j])) return false;
			else if (j==numHash-1) return true;
		}
		return false;
	}

	public int filterSize(){
		return this.curfilterSize;
	}
	
	public int dataSize() {
		return this.counter;
	}
	
	public int numHashes(){
		return this.numHash;
	}

	
	private int ranhash(int[] k,byte[] s,int prime){
		int h=0;
		for(int i=0;i<s.length;i++){
			h^=s[i];
			h=(k[0]*h+k[1])%prime;
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