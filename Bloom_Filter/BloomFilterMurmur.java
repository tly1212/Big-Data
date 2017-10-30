import java.util.BitSet;

public class BloomFilterMurmur {
	private static final int seed=281;
	private int counter; 				//# of string added	
	private int numHash;				//Size of input set S, |S|=N	
	private int filterSize;				//filter size for each T,|T|=M
	private BitSet filter;			//filter memory, K*|T|
	
	public BloomFilterMurmur(int setSize, int bitsPerElement){
		this.counter=0;
//		this.setSize=setSize;				// N
//		this.bitsPerElement=bitsPerElement;	//
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
			filter.set((int) ((mmhash(i*seed,s_chars)%filterSize+filterSize)%filterSize));
		}
	}
	
	public boolean appears(String s){
		s.toLowerCase();
		byte[] s_chars=s.getBytes();
		for(int i=0;i<this.numHash;i++){
			if(!filter.get((int) ((mmhash(i*seed,s_chars)%filterSize+filterSize)%filterSize))) return false;
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
	
	
	
	/*
	 * http://d3s.mff.cuni.cz/~holub/sw/javamurmurhash/MurmurHash.java
	 * 64bit hash
	 */
	public long mmhash(int seed, byte[] data) {
		int length=data.length;
		final long m = 0xc6a4a7935bd1e995L;
		final int r = 47;
		long h = (seed&0xffffffffl)^(length*m);

		int length8 = length/8;

		for (int i=0; i<length8; i++) {
			final int i8 = i*8;
			long k =  ((long)data[i8+0]&0xff)      +(((long)data[i8+1]&0xff)<<8)
					+(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
					+(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
					+(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);
			
			k *= m;
			k ^= k >>> r;
			k *= m;
			
			h ^= k;
			h *= m; 
		}
		switch (length%8) {
		case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
		case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
		case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
		case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
		case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
		case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
		case 1: h ^= (long)(data[length&~7]&0xff);
		        h *= m;
		};
	 
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		return h;
	}
}
