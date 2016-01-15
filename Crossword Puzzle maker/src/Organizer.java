import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Organizer {

	public static void main(String[] args) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader("english words.txt"));
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("englishWords.txt")));
		
		while(true){
			boolean isValid=true;
			String word = f.readLine();
			if(word==null){
				break;
			}
			if(word.length()==1 || word.length()==2){
				isValid=false;
			}
			for(int i=0;i<word.length();i++){
				if(!Character.isLetter(word.charAt(i))) {
					isValid=false;
					break;
				}
			}
			
			for (int i = 0; i < word.length(); i++) {
				if (Character.isUpperCase(word.charAt(i))) {
					isValid = false;
					break;
				}
			}
			
			if(isValid){
				out.println(word);
			}
		}
		
		out.close();
	}

}
