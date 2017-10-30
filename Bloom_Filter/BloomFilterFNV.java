import java.util.BitSet;

public class BloomFilterFNV {
	private static final long FNV_PRIME=1099511628211L;
	private static final long FNV_64_INIT=0xcbf29ce484222325L;
	private static final int seed=281;
	private int counter; 				//# of string added	
	private int numHash;				//Size of input set S, |S|=N					
	private int filterSize;				//filter size for each T,|T|=M
	private BitSet filter;			//filter memory, K*|T|
	
	public BloomFilterFNV(int setSize, int bitsPerElement){
		this.counter=0;
		this.filterSize=setSize*bitsPerElement;//M
		this.numHash=(int)Math.floor(Math.log(2.0)*filterSize/setSize);
		filter=new BitSet(filterSize);
	}
	public void add(String s){
		s.toLowerCase();
		if(appears(s)) return;
		counter++;
		for(int i=0;i<this.numHash;i++){
			byte[] s_chars=s.getBytes();
			filter.set((int) ((fnvhash(i*seed,s_chars)%filterSize+filterSize)%filterSize));
		}
	}
	
	public boolean appears(String s){
		s.toLowerCase();
		byte[] s_chars=s.getBytes();
		for(int i=0;i<this.numHash;i++){
			if(!filter.get((int) ((fnvhash(i*seed,s_chars)%filterSize+filterSize)%filterSize))) return false;
		}
		return true;
	}
	public int filterSize(){
		return this.filterSize;
	}
	public int dataSize() {
		return counter;
	}
	public int numHashes(){
		return this.numHash;
	}
	private long fnvhash(int k,byte[] s){
		long h=FNV_64_INIT;
		for(int i=0;i<s.length;i++){
			int s_int=s[i]^k;		//coding for each element of input string based on different fnv hash.
			h=h^s_int;
			h=h*FNV_PRIME;
		}
		return h;
	}
}
