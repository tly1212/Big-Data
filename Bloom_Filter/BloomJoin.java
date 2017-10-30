import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class BloomJoin {
	public static String r1;
	public String r2;
	public BloomJoin(String r1, String r2){
		this.r1=r1;
		this.r2=r2;
	}
	public void join(String r3) throws FileNotFoundException{
		String[][] s1=readWordsFile(r1);
		HashMap<String, String> s1_hash=new HashMap<String,String>();
		String[][] s2=readWordsFile(r2);
		BloomFilterFNV filter=new BloomFilterFNV(s1.length, 15);
		for(int i=0;i<s1.length;i++){
			s1_hash.put(s1[i][0], s1[i][1]);
			filter.add(s1[i][0]);
		}
		int counter=0;
		StringBuilder contend=new StringBuilder();
		for(int i=0;i<s2.length;i++){
			if(filter.appears(s2[i][0])&&s1_hash.containsKey(s2[i][0])){
				contend.append(s1_hash.get(s2[i][0]));
				contend.append("   ");	
				contend.append(s2[i][0]);
				contend.append("   ");
				contend.append(s2[i][1]);
				contend.append("\n");
				counter++;
			}
		}
		String filename=r1.substring(0, r1.lastIndexOf("Relation"))+"Relation3.txt";
		writefile(filename,contend.toString());
		System.out.println(s1.length);
		System.out.println(s2.length);
		System.out.println(counter);
		long t2=System.currentTimeMillis();
	}
	/**
	 * Write the content into fileName;
	 * @param fileName save the file in the root path 
	 * @param content 	the string you want to save.
	 */
	private static void writefile(String fileName, String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		String filepath=fileName;
		try {
			fw = new FileWriter(filepath);
			bw = new BufferedWriter(fw);
			bw.write(content.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}	
	}
	static String[][] readWordsFile(String filename) throws FileNotFoundException {
		File file = new File(filename);
		int count = 0;
		try{
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				count += 1;
			}
			scanner.close();
		}catch(FileNotFoundException e){
			e.getStackTrace();
		}
		Scanner scanner2 =new Scanner(file);
		String[][] s =new String[count][2];
		int number =0;
		while (scanner2.hasNextLine()) {
			s[number] = scanner2.nextLine().split("   ");;
			number++;
		}
		scanner2.close();
		return s;
	}
}
