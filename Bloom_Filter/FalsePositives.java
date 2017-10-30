import java.io.FileNotFoundException;
import java.util.HashMap;

public class FalsePositives {
	public static void main(String[] args) throws FileNotFoundException {
		int bitsize=16;
		String[][] s1=BloomJoin.readWordsFile("/Users/sunmoon/2017_fall_courses/535/Relation1.txt");
		HashMap<String, String> s1_hash=new HashMap<String,String>();
		String[][] s2=BloomJoin.readWordsFile("/Users/sunmoon/2017_fall_courses/535/Relation2.txt");
		BloomFilterFNV filter1=new BloomFilterFNV(s1.length, bitsize);
		BloomFilterMurmur filter2=new BloomFilterMurmur(s1.length, bitsize);
		BloomFilterRan filter3=new BloomFilterRan(s1.length, bitsize);
		DynamicBloomFilters filter4=new DynamicBloomFilters(bitsize);
		for(int i=0;i<s1.length;i++){
			s1_hash.put(s1[i][0], s1[i][1]);
			filter1.add(s1[i][0]);
			filter2.add(s1[i][0]);
			filter3.add(s1[i][0]);
			filter4.add(s1[i][0]);
		}
		int counter=0;
		int counter0=s2.length;
		int counter1=0;
		int counter2=0;
		int counter3=0;
		int counter4=0;
		StringBuilder contend=new StringBuilder();
		for(int i=0;i<s2.length;i++){
			if(filter1.appears(s2[i][0])) counter1++;
			if(filter2.appears(s2[i][0])) counter2++;
			if(filter3.appears(s2[i][0])) counter3++;
			if(filter4.appears(s2[i][0])) counter4++;
			if(filter1.appears(s2[i][0])&&filter2.appears(s2[i][0])&&filter3.appears(s2[i][0])&&filter4.appears(s2[i][0])){
				counter++;
			}
		}
		System.out.println(counter);
		System.out.println("Bloom Filter      False Positive");
		System.out.printf("fnv     %15.2f %%\n",(counter1-counter)*100.0/counter0);
		System.out.printf("mur     %15.2f %%\n",(counter2-counter)*100.0/counter0);
		System.out.printf("Ran     %15.2f %%\n",(counter3-counter)*100.0/counter0);
		System.out.printf("dyn     %15.2f %%\n",(counter4-counter)*100.0/counter0);
		System.out.println(counter1+" "+counter2+" "+counter3+" "+counter4);
		System.out.println(Math.pow(0.618, 4)*100);
		System.out.println(Math.pow(0.618, 8)*100);
		System.out.println(Math.pow(0.618, 16)*100);
//		System.out.println(Math.pow(0.618, 15)*100);
//		System.out.println(Math.pow(0.618, 16)*100);
	}
}
