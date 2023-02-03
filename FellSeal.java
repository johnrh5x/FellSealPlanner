package john.game.fellseal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class FellSeal {

	// Data
	
	String[]   classes;     // An array of character class (job) names
	String[][] passives;    // An array containing the passive abilities associated with each job
	String[]   counters;    // An array containing the counter abilities associated with each job
	int[][]    prereqs;     // An array of job prerequisites; prereqs[i][j] is the number of abilties in job j needed to unlock job i 
	int[]      tiers;       // An array containing the tier (or rank) for each job based on the tiers of its prerequisites
	String[][] passiveHelp; // An array containing passive ability names and their descriptions
	String[][] counterHelp; // An array containing counter ability names and their descriptions
	
	// Constructor
	
	public FellSeal() {
		
		loadJobData();
		loadPassiveDescriptions();
		loadCounterDescriptions();
		
	}
	
	// Methods

	public int classIndex(String name) {
		
		/* Returns the index in the classes array corresponding to the
		 * name argument.  If the name argument does not appear in the
		 * classes array, returns -1. */
		
		int output = -1;
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].equals(name)) {
				output = i;
				break;
			}
		}
		return output;
		
	}

	public String[] classList() {
		
		/* Returns the classes array with a blank string added as the
		 * first element.  Intended for use in job selection combo boxes
		 * in the GUI. */
		
		String[] output = new String[classes.length + 1];
		output[0] = "";
		for (int i = 0; i < classes.length; i++) output[i+1] = classes[i];
		return output;
		
	}

	public String[] classNamesFromCounter(String name) {
		
		/* Returns an array of strings containing the names of all the
		 * jobs with the counter ability given in the name argument.  In
		 * most instances, this will be a single-element array. */
		
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < counters.length; i++) {
			if (counters[i].equals(name)) l.add(Integer.valueOf(i));
		}
		String[] output = new String[l.size()];
		for (int i = 0; i < output.length; i++) {
			output[i] = classes[l.get(i).intValue()];
		}
		return output;
		
	}

	public String[] classNamesFromPassive(String name) {
		
		/* Returns an array of strings containing the names of all the
		 * jobs with the passive ability given in the name argument.  In
		 * most cases, this will be a single-element array. */
			
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < passives.length; i++) {
			for (int j = 0; j < passives[i].length; j++) {
				if (passives[i][j].equals(name)) l.add(Integer.valueOf(i));
			}
		}
		String[] output = new String[l.size()];
		for (int i = 0; i < output.length; i++) {
			output[i] = classes[l.get(i).intValue()];
		}
		return output;
		
	}

	public int[][] classPrereqs(int index) {
		
		/* Returns a two-dimensional array of integers representing the
		 * prerequisites for a class.  The [i][0] element of the output
		 * array contains the index for the ith prerequisite job.  The
		 * [i][1] element contains the number of skills required for
		 * that job. */
		
		int count = 0;
		for (int i = 0; i < prereqs[index].length; i++) {
			if (prereqs[index][i] > 0) count++;
		}
		int[][] output = new int[count][2];
		int c = 0;
		for (int i = 0; i < prereqs[index].length; i++) {
			if (prereqs[index][i] > 0) {
				output[c][0] = i;
				output[c][1] = prereqs[index][i];
				c++;
			}
		}
		return output;
		
	}

	public String counterDescription(String name) {
		
		/* Returns a string containing a short description of the 
		 * counter ability given in the name argument.  Returns null
		 * if the argument does not appear in the counterHelp arrray. */
		
		String output = null;
		for (int i = 0; i < counterHelp.length; i++) {
			if (counterHelp[i][0].equals(name)) {
				output = counterHelp[i][1];
				break;
			}
		}
		return output;
		
	}

	public String[] counterList() {
		
		/* Returns the counters array with a blank string added as the
		 * first element.  Intended for use in the counter ability
		 * selection combo box in the GUI. */
		
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < counters.length; i++) l.add(counters[i]);
		l.sort(null);
		for (int i = l.size() - 1; i > 0; i--) {
			if (l.get(i).equals(l.get(i-1))) l.remove(i);
		}
		l.add(0,"");
		String[] output = new String[l.size()];
		l.toArray(output);
		return output;
		
	}

	private void loadJobData() {

		/* Reads job data from a text file and converts that data into
		 * a useable form.  
		 * 
		 * For future reference, note that a tier 1 class has no 
		 * prerequisites.  A tier 2 class has one or more prerequisites 
		 * of tier 1.  A tier 3 class has prerequisites of at most tier 
		 * 2, and so on. */

		// Read from data file
		
		ArrayList<String> list = readFile("data.csv");
		
		// Parse the contents of the file
		
		int n = list.size();
		classes = new String[n];
		String[][] temp = new String[n][6];
		passives = new String[n][2];
		counters = new String[n];
		for (int i = 0; i < n; i++) {
			String[] parts = list.get(i).split(",");
			classes[i] = parts[0].trim();
			temp[i][0] = parts[1].trim();
			temp[i][1] = parts[2].trim();
			temp[i][2] = parts[3].trim();
			temp[i][3] = parts[4].trim();
			temp[i][4] = parts[5].trim();
			temp[i][5] = parts[6].trim();
			passives[i][0] = parts[7].trim();
			passives[i][1] = parts[8].trim();
			counters[i] = parts[9].trim();
		}
	
		// Make a table of prerequisites
		
		prereqs = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 5; j += 2) {
				for (int k = 0; k < n; k++) {
					if (temp[i][j].equals(classes[k])) {
						prereqs[i][k] = Integer.valueOf(temp[i][j+1]).intValue();
						break;
					}
				}				
			}
		}

		// Compute tiers for all classes.
		
		tiers = new int[n];
		boolean done = false;
		while (!done) {
			done = true;
			for (int i = 0; i < n; i++) {
				if (tiers[i] == 0) {
					done = false;
					int count = 0;
					for (int j = 0; j < n; j++) {
						if (prereqs[i][j] > 0) count++;
					}
					switch (count) {
						case 0:
							tiers[i] = 1;
							break;
						default:
						    boolean missingTiers = false;
						    int highestTier = 0;
						    for (int j = 0; j < n; j++) {
								if (prereqs[i][j] > 0) {
									missingTiers = tiers[j] == 0;
									if (missingTiers) break;
									if (highestTier < tiers[j]) highestTier = tiers[j];
								}
							}
							if (!missingTiers) tiers[i] = highestTier + 1;
							break;
					}
				}
			}
			
		}
	
		// Add prerequisites of prerequisites to the table of prerequisites
		
		int maxTier = 0;
		for (int i = 0; i < n; i++) {
			if (maxTier < tiers[i]) maxTier = tiers[i];
		}
		for (int t = 3; t <= maxTier; t++) {
			for (int i = 0; i < n; i++) {
				if (tiers[i] == t) {
					int[][] p = classPrereqs(i); // The prerequisites for this job
					for (int j = 0; j < p.length; j++) {
						int[][] pp = classPrereqs(p[j][0]); // The prerequisites for this prerequisite
						for (int k = 0; k < pp.length; k++) {
							if (prereqs[i][pp[k][0]] < pp[k][1]) prereqs[i][pp[k][0]] = pp[k][1]; 
						}
					}
				}
			}
		}
		
	}

	private void loadCounterDescriptions() {
		
		/* Reads descriptions of counter abilities from a text file. */
		
		ArrayList<String> list = readFile("counters.csv");
		counterHelp = new String[list.size()][2];
		for (int i = 0; i < counterHelp.length; i++) {
			int pos = list.get(i).indexOf(",");
			counterHelp[i][0] = list.get(i).substring(0,pos).trim();
			int quote = list.get(i).indexOf('"');
			switch (quote) {
				case -1:
					counterHelp[i][1] = list.get(i).substring(pos+1).trim();
					break;
				default:
					counterHelp[i][1] = list.get(i).substring(quote+1,list.get(i).length()-1).trim();
					break;
			}		
		}
		
	}
	
	private void loadPassiveDescriptions() {
		
		/* Reads descriptions of passive abilties from a text file. */
		
		ArrayList<String> list = readFile("passives.csv");
		passiveHelp = new String[list.size()][2];
		for (int i = 0; i < passiveHelp.length; i++) {
			int pos = list.get(i).indexOf(",");
			passiveHelp[i][0] = list.get(i).substring(0,pos).trim();
			int quote = list.get(i).indexOf('"');
			switch (quote) {
				case -1:
					passiveHelp[i][1] = list.get(i).substring(pos+1).trim();
					break;
				default:
				    passiveHelp[i][1] = list.get(i).substring(quote+1,list.get(i).length()-1).trim();
					break;
			}
		}
		
	}

	public int[][] mergedPrereqs(int[] indices) {
		
		/* Returns the merged set of prerequisites given for set of
		 * classes represented by the indices argument. */
		
		// Record the maximum number of skills for each job
		
		int[] maximums = new int[classes.length];
		for (int i = 0; i < indices.length; i++) {
			int[][] p = classPrereqs(indices[i]);
			for (int j = 0; j < p.length; j++) {
				int index = p[j][0];
				int level = p[j][1];
				if (maximums[index] < level) maximums[index] = level;
			}
		} 
		
		// Return the maximums as a prerequisites array
		
		int count = 0;
		for (int i = 0; i < maximums.length; i++) {
			if (maximums[i] > 0) count++;
		}
		int[][] output = new int[count][2];
		int index = 0;
		for (int i = 0; i < maximums.length; i++) {
			if (maximums[i] > 0) {
				output[index][0] = i;
				output[index][1] = maximums[i];
				index++;
			}
		}
		return output;
		
	}

	public String passiveDescription(String name) {
		
		/* Returns a string containing a short description of the
		 * passive ability given in the name argument. */
		
		String output = null;
		for (int i = 0; i < passiveHelp.length; i++) {
			if (name.equals(passiveHelp[i][0])) {
				output = passiveHelp[i][1];
				break;
			}
		}
		return output;
		
	}

	public String[] passiveList() {
		
		/* Returns an array containing the names of all passive
		 * abilities with a blank string added as the first element.
		 * Intended for use in the passive ability selection comob boxes
		 * in the GUI. */
		
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < passives.length; i++) {
			l.add(passives[i][0]);
			l.add(passives[i][1]);
		}
		l.sort(null);
		for (int i = l.size() - 1; i > 0; i--) {
			if (l.get(i).equals(l.get(i-1))) l.remove(i);
		}
		l.add(0,"");
		String[] output = new String[l.size()];
		l.toArray(output);
		return output;
		
	}

	public String prereqsToString(int[][] p) {
		
		/* Converts an array of the type produced by classPrereqs() to
		 * a String.  Classes are listed in tier order, starting with
		 * tier 1 classes. */
		
		if (p.length == 0) return System.lineSeparator() + "Nothing";
		int[] t = new int[p.length];
		for (int i = 0; i < p.length; i++) t[i] = tiers[p[i][0]];
		int maxTier = 0;
		for (int i = 0; i < t.length; i++) {
			if (maxTier < t[i]) maxTier = t[i];
		}
		//int counter = 0;
		StringBuilder sb = new StringBuilder();
		for (int tier = 1; tier <= maxTier; tier++) {
			for (int i = 0; i < p.length; i++) {
				if (t[i] == tier) {
					sb.append(System.lineSeparator());
					sb.append(String.valueOf(p[i][1]));
					sb.append(" ");
					sb.append(classes[p[i][0]]);
				}
			}
		}
		return sb.toString();
		
	}

	private ArrayList<String> readFile(String fileName) {
		
		/* Reads the contents of a text file stored in the same 
		 * directory as the class file for this class and returns the
		 * results an ArrayList<String>. */
		
		InputStream stream = FellSeal.class.getResourceAsStream(fileName);
		if (stream == null) {
			System.out.println("Could not find " + fileName + ".");
			System.exit(1);
		}
		ArrayList<String> list = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			String next = br.readLine();
			while (next != null) {
				list.add(next);
				next = br.readLine();
			}
		} catch (IOException ioe) {
			System.out.println("An exception occurred while reading from " + fileName + ".");
			System.out.println(ioe.getMessage());
			System.exit(1);
		}
		return list;
		
	}

}
