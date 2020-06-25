import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import rita.*;

public class EmotionLexicon {
	private HashMap<String, HashMap<String, Integer>> emotionLexicon; 
	
	public EmotionLexicon() {
		try {
			parseLexicon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseLexicon() throws Exception {
		emotionLexicon = new HashMap<String, HashMap<String, Integer>>();
		String file = "C:\\Users\\Vera\\git\\GeneticPoetry\\GeneticPoemsV1\\src\\files\\NRC-Emotion-Lexicon-Wordlevel-v0.92.txt";
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if (line.trim().isEmpty()) { continue; }
		       String[] tokenizedLine = RiTa.tokenize(line);
		       if (!emotionLexicon.containsKey(tokenizedLine[0])) {
		    	   emotionLexicon.put(tokenizedLine[0], new HashMap<String, Integer>());
		    	   emotionLexicon.get(tokenizedLine[0]).put(tokenizedLine[1], Integer.parseInt(tokenizedLine[2]));
		       } else {
		    	   emotionLexicon.get(tokenizedLine[0]).put(tokenizedLine[1], Integer.parseInt(tokenizedLine[2]));
		       }
		    }
		}
	}
	
	public boolean isEmotion(String word, String emotion) {
		if (!emotionLexicon.containsKey(word)) {
			return false;
		} 
		if (emotionLexicon.get(word).containsKey(emotion)) {
			if (emotionLexicon.get(word).get(emotion) == 1) {
				return true;
			}
		}
		return false;
	}
	
	
}
