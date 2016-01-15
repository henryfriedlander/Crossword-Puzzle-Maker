import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LengthOrganizer {

	public static void main(String[] args) throws IOException {
		
		BufferedReader f = new BufferedReader(new FileReader("englishWords.txt"));
		
		
		int thing[]=new int[100000];
		while(true){
			String word = f.readLine();
			if(word==null)
				break;
			thing[word.length()]++;
		}
		for(int i=0;i<15;i++){
			System.out.println(i+": "+thing[i]);
		}
	}

}
