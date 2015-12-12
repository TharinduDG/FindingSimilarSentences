package com.tharindu.mmds.sentenceMatching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EditLength1Sentences {
	
/*	I simply hashed all sentences to buckets by the first and last five words. Then sorted each list 
	(replaced all words with integers) and merged duplicates into a single record with a counter.
	 After that each list was iterated through compared each pair to each pair 
	 (which were relatively small lists as most of candidates were duplicates). 
*/
	
//	
	
	/*
	 * 
	 * Function Distance1Max(word containers: S1, S2)
		    If (Size of S1 < size of S2) Then Swap S1, S2
		    If (Size of S1 - Size of S2 > 1 Then Return FALSE
		    error <- none
		    For pos From 1 To Size of S2 Do
		        If  (error == none) Then
		            If (S1[pos] != S2[pos]) Then
		                If (Size of S1 == Size of S2) Then error <- replace
		                Else error <- delete
		            End of If / Else
		        Else If (error == replace) Then
		            If (S1[pos] != S2[pos]) Then Return FALSE
		        Else
		            If (S1[pos] != S2[pos - 1]) Then Return FALSE
		        End of If / Else
		    End of For
		    If (error == delete And S1[Size of S1] != S2[Size of S2]) Then Return FALSE
		    Return TRUE
		End of Function
	 */
	private static final int EDIT_DISTANCE = 1;
//	private static final String FILE_NAME = "sentences.txt";
	private static final String FILE_NAME = "test.txt";
	private static final int WORDS_PER_SHINGLE = 5;
	
	public static void main(String[] args) throws Exception {
		
		FileReader in = new FileReader(FILE_NAME);
		BufferedReader br = new BufferedReader(in);

		Map<String, Set<String>> shinglesPerSentence = new HashMap<>();
		
		String line = null;
		while((line = br.readLine()) != null){
			
			String[] words = line.split(" "); // first word contains the index of the sentence & it should be omitted
			
			StringBuilder shingle = null;
			for (int i = 1; i < words.length; i = i + WORDS_PER_SHINGLE) {
				if(i + WORDS_PER_SHINGLE > words.length){
					break;
				}
				
				String[] shingles = Arrays.copyOfRange(words, i, i + WORDS_PER_SHINGLE);
				shingle = new StringBuilder();
				for (String s : shingles) {
					shingle.append(s);
				}
				
				if (shinglesPerSentence.containsKey(shingle.toString())) {
					Set<String> s = shinglesPerSentence.get(shingle.toString());
					s.add(words[0]); // words[0] contains the index of the
										// sentence
//					System.out.println("Match...!!!");
				} else {
					Set<String> s = new HashSet<>();
					s.add(words[0]);
					shinglesPerSentence.put(shingle.toString(), s);
				}
				shingle = null;
				shingles = null;
			}
			words = null;
		}		
		
		System.out.println("Done Shingling....!");
		br.close();
		// done shingling		
		Collection<Set<String>> allValueSets = shinglesPerSentence.values();
		
		Set<String> candidateSentenceIndexes = new HashSet<>();
		Map<String, String[]> candidateSentences = new HashMap<>();
		
		for(Set<String> s : allValueSets){
			if(s.size() > 1){
				candidateSentenceIndexes.addAll(s);
			}
		}
		
		allValueSets = null;
		
		FileReader in2 = new FileReader(FILE_NAME);
		BufferedReader br2 = new BufferedReader(in2);
		
		String sentence = null;
		while((sentence = br2.readLine()) != null){
			String[] words = sentence.split(" ");
			if(candidateSentenceIndexes.contains(words[0])){
				candidateSentences.put(words[0], Arrays.copyOfRange(words, 1, words.length));
			}
			words = null;
		}
		
		System.out.println("Done Reading second time..!");
		br2.close();
		
		for (Iterator<String> itr = candidateSentences.keySet().iterator(); itr.hasNext();) {

			String k1 = itr.next();
			String[] s1 = candidateSentences.get(k1);

			List<String> l = new ArrayList<String>(candidateSentences.keySet());

			for (String k2 : l) {
				if (k1.equals(k2)) {
					l.remove(k2);
					break;
				}

				String[] s2 = candidateSentences.get(k2);
				int editDistance = 0;
				boolean similar = true;

				if (Math.abs(s1.length - s2.length) > EDIT_DISTANCE) {
					continue;
				}

				for (int i = 0; i < Math.min(s2.length, s1.length); i++) {
					if (!s1[i].equals(s2[i])) {
						editDistance++;
					}
					if (editDistance > EDIT_DISTANCE) {
						similar = false;
						break;
					}
				}

				if (similar) {
					System.out.println(k1 + " & " + k2);
				}
			}
		}
		
		
		System.out.println("Done...!!");
		
		
//		
//		// contains shingles for every sentence
//		List<int[]> sentenceWiseShinglesPrecense = new ArrayList<>();
//
//		// contains the whole set of unique shingles
//		Map<String,Integer> globalShinglesMap = createShinglesMap(br,sentenceWiseShinglesPrecense);
//		
//		br.close();
//		System.out.println("Done reading input!");		
//		
//		int[][] minhashArray = new int[TOTAL_HASH_FNS][sentenceWiseShinglesPrecense.size()];		
//		for (int[] is : minhashArray) {
//			Arrays.fill(is, Integer.MAX_VALUE);
//		}	
//
//		int sentenceIndex = 0;		
//		// for each sentence	
//		Integer[] shinglesArray = globalShinglesMap.values().toArray(new Integer[0]);
////		Arrays.sort(shinglesArray, Collections.reverseOrder());
//		int totalShingles = shinglesArray.length;
//		for (Integer shingleIndex : shinglesArray) {
//			for (int[] sentence : sentenceWiseShinglesPrecense) {
//				// check whether a shingle is present in the selected sentence
//				if (Arrays.binarySearch(sentence, shingleIndex) > 0) {
//					
//					int[] hashValues = new int[] { 
//							(16453 * shingleIndex + 1) % totalShingles,(15901 * shingleIndex + 1) % totalShingles,
//							(2621 * shingleIndex + 1) % totalShingles, (877 * shingleIndex + 1) % totalShingles,
//							(3041 * shingleIndex + 1) % totalShingles, (2657 * shingleIndex + 1) % totalShingles,
//							(2659 * shingleIndex + 1) % totalShingles, (2663 * shingleIndex + 1) % totalShingles,
//							(2671 * shingleIndex + 1) % totalShingles, (2677 * shingleIndex + 1) % totalShingles,
//							(2683 * shingleIndex + 1) % totalShingles, (2687 * shingleIndex + 1) % totalShingles,
//							(4723 * shingleIndex + 1) % totalShingles, (5437 * shingleIndex + 1) % totalShingles,
//							(5867 * shingleIndex + 1) % totalShingles, (1031 * shingleIndex + 1) % totalShingles,
//							(6521 * shingleIndex + 1) % totalShingles, (1483 * shingleIndex + 1) % totalShingles,
//							(6983 * shingleIndex + 1) % totalShingles, (7541 * shingleIndex + 1) % totalShingles,
//							(7001 * shingleIndex + 1) % totalShingles, (1283 * shingleIndex + 1) % totalShingles,
//							(7417 * shingleIndex + 1) % totalShingles, (863 * shingleIndex + 1) % totalShingles,
//							(7451 * shingleIndex + 1) % totalShingles, (7643 * shingleIndex + 1) % totalShingles,
//							(13417 * shingleIndex + 1) % totalShingles, (14341 * shingleIndex + 1) % totalShingles,
//							(13523 * shingleIndex + 1) % totalShingles, (14221 * shingleIndex + 1) % totalShingles,
//							(13649 * shingleIndex + 1) % totalShingles, (14537 * shingleIndex + 1) % totalShingles,
//							(13721 * shingleIndex + 1) % totalShingles, (14783 * shingleIndex + 1) % totalShingles,
//							(13883 * shingleIndex + 1) % totalShingles, (14887 * shingleIndex + 1) % totalShingles,
//							(14087 * shingleIndex + 1) % totalShingles, (15017 * shingleIndex + 1) % totalShingles,
//							(15413 * shingleIndex + 1) % totalShingles, (15401 * shingleIndex + 1) % totalShingles	};
//					            
////					System.out.println(Arrays.toString(hashValues));
//					for (int i = 0; i < TOTAL_HASH_FNS; i++) {
//						// if the hash values are smaller than the current
//						// element in the minhash array
//						if (minhashArray[i][sentenceIndex] > hashValues[i]) {
//							minhashArray[i][sentenceIndex] = hashValues[i];
//						}
//					}
//					System.out.println();
//				}			
//				sentenceIndex++;
//			}			
//			sentenceIndex = 0;
//		}
//		
//		System.out.println();
//		
//		// LSH for the minhash matrix
//		// 8 bolocks of 5 rows each
//		Map<String,List<String>> lshBuckets = new HashMap<>();
//			
//		for (int i = 0; i < minhashArray[0].length; i++) {
//			for (int k = 0; k < NUMBER_OF_BLOCKS; k++) {
//				StringBuilder key = new StringBuilder();
//				for (int j = ELEMENTS_PER_BLOCK * k; j < (k + 1) * ELEMENTS_PER_BLOCK ; j++) {
//					key.append(minhashArray[j][i]);
//				}
//				if (lshBuckets.containsKey(key)) {
//					List<String> hashes = lshBuckets.get(key);
//					hashes.add("S" + i + "" + k);
//					System.out.println("match!!");
//				} else {
//					List<String> hashes = new ArrayList<>();
//					hashes.add("S" + i + "" + k);
//					lshBuckets.put(key.toString(), hashes);
//				}
//			}
//		}		
//		System.out.println("Done...!!!");
//	}
//	
//	private static Map<String, Integer> createShinglesMap(BufferedReader br, List<int[]> sentenceWiseShinglesPrecense) throws Exception{
//		
//		String line = null;
//		Map<String, Integer> shinglesMap = new HashMap<>();
//		int shingleIndex = 0;
//		
//		while((line = br.readLine()) != null){
////			System.out.println(line);
//			StringBuilder s = new StringBuilder(line);
//			line = null;
//			s = s.replace(0, s.indexOf(" ") + 1, "");
//			int[] shinglesPresence = new int[s.length() - SHINGLE_SIZE + 1];
//			
//			for (int j = 0; j < s.length(); j++) {
//				if (j + SHINGLE_SIZE > s.length()) {
//					continue;
//				}
//				String temp = s.substring(j, j + SHINGLE_SIZE);
//				if(!shinglesMap.containsKey(temp)){
//					shinglesMap.put(temp, shingleIndex);
//					shingleIndex++;
//				}								
//				shinglesPresence[j] = shinglesMap.get(temp);
//			}			
//			Arrays.sort(shinglesPresence);
//			sentenceWiseShinglesPrecense.add(shinglesPresence);
//		}
//		
//		System.out.println("Done Shingling");
//		return shinglesMap;
	}

}

