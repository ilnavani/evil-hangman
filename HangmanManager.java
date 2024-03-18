import java.util.*;

/** 
 * Class to implement the Evil Hangman game.
 * The game tricks the user by delaying picking a word until it is forced to,
 * making it harder for the user to guess the target word. 
 * 
* @author Ilina Navani and Kate Gilman
*/
public class HangmanManager {

Set<String> words = new HashSet<String>();
SortedSet<Character> sortedGuesses = new TreeSet<Character>();
String pattern;
int guessesNum;
int length;

/**
 * @param dictionary - list of words in the Scrabble dictionary
 * @param length - length of the target word 
 * @param max - maximum number of wrong guesses
 * Initializes the set of words to be used from the dictionary, the maximum number of 
 * guesses the user has, and the initial pattern of the target word (based on the given length)
 * @throws IllegalArgumentException if the length of the target word is less than 1 
 * or if max is less than 0
*/
public HangmanManager(List <String> dictionary, int length, int max){
    if(length<1) {
        throw new IllegalArgumentException();
    }
    if(max<0) {
        throw new IllegalArgumentException();
    }
    for(int i = 0; i<dictionary.size(); i++){
        if(dictionary.get(i).length()==length){
            words.add(dictionary.get(i));
        }
    }
    guessesNum = max;
    pattern = "";
    //constructs initial pattern of given length with only -'s
    for (int i=0; i<length; i++) {
        pattern += "- ";
    }
    pattern = pattern.strip();
}


/**
 * Helper function that creates a map containing a word pattern as the key and the 
 * corresponding word family as the value. It then loops through the map to find the 
 * largest word family and its pattern, or in case of a tie, it finds the word family 
 * with the least guessed characters, in order to trick the user. 
 * 
 * @param guess - the character guessed by the user
 * @param words - the current word family being considered
 * @param pattern - the corresponding pattern of the current word family
 *
 * @return - the updated pattern of the chosen word family comprising the target word
 */
private String wordCheck (char guess, Set<String>words, String pattern) {
    Map<String, Set<String>> wordsGuessed = new HashMap<String, Set<String>>();
    for(String currentWord : words) {
        String currentWordPattern = pattern;  //reassigns the original pattern for each word in the family
        Set<String> values = new HashSet<String>();
        for(int i=0; i<currentWord.length(); i++) {
            Character currentChar = currentWord.charAt(i);
                if (currentChar==guess) {  //inserts the guessed character into the pattern
                    currentWordPattern = currentWordPattern.substring(0, i*2) + currentChar 
                    + currentWordPattern.substring((i*2)+1);
                }
            }
        //checks if a pattern already exists and adds the current word to the exisiting family 
        if(wordsGuessed.containsKey(currentWordPattern)) {
            values = wordsGuessed.get(currentWordPattern);
            values.add(currentWord);
            wordsGuessed.put(currentWordPattern, values);
        } else {  //adds a new key(pattern) and value(word)
            values.add(currentWord);
            wordsGuessed.put(currentWordPattern, values);
        }
    }
    Set<String> currentSet = new HashSet<String>();
    int size = 0;
    String finalPattern = "";
    for(String currentPattern : wordsGuessed.keySet()) {
        currentSet = wordsGuessed.get(currentPattern);
        if (currentSet.size()>=size) {
        //tie: checks the number of guessed characters in the pattern being checked versus the pattern being returned
            if(currentSet.size()==size) {
                int countFinal = 0;
                int countCurrent = 0;
            for(int i =0; i<finalPattern.length(); i++){
                if(Character.isLetter(finalPattern.charAt(i))){
                    countFinal++;
                }
                if(Character.isLetter(currentPattern.charAt(i))){
                    countCurrent++;
                }
            }
            //chooses the pattern with fewer guessed letters and its corresponding word family
            if(countFinal>=countCurrent) {
                finalPattern = new String(currentPattern);
                words.clear();  
                words.addAll(currentSet); //resets the dictionary to the new word family
            }
            } else {
                size = currentSet.size();
                finalPattern = new String(currentPattern);
                words.clear();
                words.addAll(currentSet);
            }
        }
    }
    return finalPattern;
}

/**
 * 
 * @returns the word family being considered 
 */
Set<String> words() {
    return words;
}

/**
 * 
 * @returns the number of guesses the user has left 
 */
int guessesLeft(){
    return guessesNum;
}

/**
 * 
 * @returns a sorted set of the characters the user has already guessed
 */
SortedSet<Character> guesses() {
    return sortedGuesses;
}

/**
 * 
 * @returns the pattern of the word family containing the target word
 * @throws IllegalStateException if the set of words is empty  
 */
String pattern() {
    if(words.isEmpty()) {
        throw new IllegalStateException();
    }
    return pattern;
}

/**
 * 
 * @param guess the character guessed by the user
 * @returns the number of occurances of the guessed character in the pattern of the current word family 
 * @throws IllegalStateException if the set of words is empty or if the number of guesses left is fewer 
 * than 1
 * @throws IllegalArgumentException if the list of words is non-empty and the character being guessed 
 * was guessed previously
 */
int record(char guess) {
    if(words.isEmpty()) {
        throw new IllegalStateException();
    }
    SortedSet<Character> guessed = guesses();
    if((!words.isEmpty()) && (guessed.contains(guess))) {
        throw new IllegalArgumentException();
    }
    if(guessesNum<1){
        throw new IllegalStateException();
    }
    
    //calls the helper method to get the new pattern and word family everytime the user makes a new guess
    pattern = wordCheck(guess, words, pattern);

    //adds the guessed character to a sorted set
    guesses().add(guess);

    //updates the number of guesses the user has left
    guessesNum = guessesLeft() - 1;

    //finds the number of occurences of the guessed character in the pattern
    int guessCount = 0;
    for(int i=0; i<pattern.length(); i++) {
        if (pattern.charAt(i) == guess){
            guessCount += 1;
        }
    }
    return guessCount;
}
}



