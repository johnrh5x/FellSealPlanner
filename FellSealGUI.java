package john.game.fellseal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class FellSealGUI extends JFrame {

	// Data
	
	private static final Font BOLD  = (new JLabel()).getFont();
	private static final Font PLAIN = BOLD.deriveFont(Font.PLAIN);
	
	FellSeal          fs;
	JComboBox<String> j1;   // Allows the user to select the primary job
	JComboBox<String> j2;   // Allows the user to select the secondary job
	JLabel            j1p1; // Shows one of the passive abilities for the primary job
	JLabel            j1p2; // Shows one of the passive abilities for the primary job
	JComboBox<String> p1;   // Allows the user to select a passive ability
	JComboBox<String> p2;   // Allows the user to select a second passive ability
	JComboBox<String> p1j;  // Shows the user the job associated with the first user-selected passive ability; allows the user to select a specific job if there is more than one job with that ability
	JComboBox<String> p2j;  // Shows the user the job associated with the second user-selected passive ability; allows the user to select a specific job if there is more than one job with that ability
	JComboBox<String> c;    // Allows the user to select a counter ability
	JComboBox<String> cj;   // Shows the user the job associated with the counter ability; allows the user to select a specific job if there is more than one job with that ability
	JTextArea         ta;   // Shows prerequisites for selected and implied jobs to the user
	JButton           b1;   // Produces an ability description dialog box for the first passive ability for the primary job
	JButton           b2;   // Produces an ability description dialog box for the second passive ability for the primary job
	JButton           b3;   // Produces an ability description dialog box for the first user-selected passive ability
	JButton           b4;   // Produces an ability description dialog box for the second user-selected passive ability
	JButton           b5;   // Produces an ability description dialog box for the counter ability
	
	// Constructor
	
	public FellSealGUI() {
	
		// Frame properties
	
		super("Fell Seal Character Planner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());
		
		// Other
		
		fs = new FellSeal();
		
	}
	
	// Methods

	private void add(JComponent component, int x, int y, int w) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.insets = new Insets(0,0,5,5);
		if (x == 0) c.insets.left = 5;
		if (y == 0) {
			c.insets.top = 5;
		} else {
			c.fill = GridBagConstraints.HORIZONTAL;
		}
		getContentPane().add(component,c);
		
	} 

	private void createAndShowGUI() {
				
		String[] jList = fs.classList();   // Items for job-selection combo boxes
		String[] pList = fs.passiveList(); // Items for passives-selection combo boxes
		String[] cList = fs.counterList(); // Items for counter-selection combo boxes
		
		//  Column headers
		
		add(new JLabel("Jobs"),1,0,1);
		add(new JLabel("Abilities"),2,0,1);
		
		// Row headers
		
		add(new JLabel("Primary job"),0,1,1);
		add(new JLabel("Secondary job"),0,2,1);
		add(new JLabel("Passive"),0,3,1);
		add(new JLabel("Passive"),0,4,1);
		add(new JLabel("Counter"),0,5,1);
		
		// Job 1
		
		j1 = new JComboBox<String>(jList);
		j1p1 = new JLabel();
		j1p2 = new JLabel();
		j1.setFont(PLAIN);
		j1.setEditable(false);
		j1p1.setFont(PLAIN);
		j1p2.setFont(PLAIN);
		j1.addActionListener(e -> onJob1Change());
		j1.addActionListener(e -> updateText());
		add(j1,1,1,1);
		add(j1p1,2,1,1);
		add(j1p2,2,2,1);

		// Job 2
		
		j2 = new JComboBox<String>(jList);
		j2.setFont(PLAIN);
		j2.setEditable(false);
		j2.addActionListener(e -> updateText());
		add(j2,1,2,1);
		
		// Passive 1
		
		p1 = new JComboBox<String>(pList);
		p1.setFont(PLAIN);
		p1.setEditable(false);
		p1j = new JComboBox<String>();
		p1j.setFont(PLAIN);
		p1j.setEditable(false);
		p1j.setEnabled(false);
		p1.addActionListener(e -> onPassive1Change());
		p1j.addActionListener(e -> updateText());
		add(p1,2,3,1);
		add(p1j,1,3,1);
		
		// Passive 2
		
		p2 = new JComboBox<String>(pList);
		p2.setFont(PLAIN);
		p2.setEditable(false);
		p2j = new JComboBox<String>();
		p2j.setFont(PLAIN);
		p2j.setEditable(false);
		p2j.setEnabled(false);
		p2.addActionListener(e -> onPassive2Change());
		p2j.addActionListener(e -> updateText());
		add(p2,2,4,1);
		add(p2j,1,4,1);

		// Counter
		
		c = new JComboBox<String>(cList);
		c.setFont(PLAIN);
		c.setEditable(false);
		cj = new JComboBox<String>();
		cj.setFont(PLAIN);
		cj.setEditable(false);
		cj.setEnabled(false);
		c.addActionListener(e -> onCounterChange());
		cj.addActionListener(e -> updateText());
		add(c,2,5,1);
		add(cj,1,5,1);
		
		// Help buttons
		
		b1 = new JButton("?");
		b1.setEnabled(false);
		b1.addActionListener(e -> onButton1Press());
		add(b1,3,1,1);
		b2 = new JButton("?");
		b2.setEnabled(false);
		b2.addActionListener(e -> onButton2Press());
		add(b2,3,2,1);
		b3 = new JButton("?");
		b3.setEnabled(false);
		b3.addActionListener(e -> onButton3Press());
		add(b3,3,3,1);
		b4 = new JButton("?");
		b4.setEnabled(false);
		b4.addActionListener(e -> onButton4Press());
		add(b4,3,4,1);
		b5 = new JButton("?");
		b5.setEnabled(false);
		b5.addActionListener(e -> onButton5Press());
		add(b5,3,5,1);
		
		// Output text area
		
		ta = new JTextArea();
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setText("No jobs selected or implied.");
		JScrollPane sp = new JScrollPane(ta);
		sp.setPreferredSize(new Dimension(300,400));
		add(sp,0,6,4);
		
		// Show on screen
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}

	public static void main(String[] args) {
		
		FellSealGUI fsg = new FellSealGUI();
		SwingUtilities.invokeLater(() -> fsg.createAndShowGUI());
		
	}

	private void onButton1Press() {
		
		// Show a description for the first passive ability for the primary job
		
		String title = j1p1.getText();
		String message = fs.passiveDescription(title);
		showMessageDialog(title,message);
		
	}

	private void onButton2Press() {
		
		// Show a description for the second passive ability for the primary job
		
		String title = j1p2.getText();
		String message = fs.passiveDescription(title);
		showMessageDialog(title,message);
		
	}

	private void onButton3Press() {
		
		// Show a description for the first user-selected passive ability
		
		String title = (String)p1.getSelectedItem();
		String message = fs.passiveDescription(title);
		showMessageDialog(title,message);
		
	}
	
	private void onButton4Press() {
		
		// Show a description for the second user-selected passive ability
		
		String title = (String)p2.getSelectedItem();
		String message = fs.passiveDescription(title);
		showMessageDialog(title,message);
		
	}

	private void onButton5Press() {
		
		// Show a description for the counter ability
		
		String title = (String)c.getSelectedItem();
		String message = fs.counterDescription(title);
		showMessageDialog(title,message);
		
	}

	private void onCounterChange() {
		
		/* Update the job associated with the counter ability and enable
		 * or disable the corresponding help button, as appropriate. */
		
		String name = (String)c.getSelectedItem();
		String[] jobs = fs.classNamesFromCounter(name);
		cj.removeAllItems();
		for (String s : jobs) cj.addItem(s);
		cj.setEnabled(jobs.length > 1);
		b5.setEnabled(!name.equals(""));
		
	}

	private void onJob1Change() {
		
		// Update passive abilities for the primary job
		
		String name = (String)j1.getSelectedItem();
		int index = fs.classIndex(name);
		switch (index) {
			case -1:
				j1p1.setText("");
				j1p2.setText("");
				b1.setEnabled(false);
				b2.setEnabled(false);
				break;
			default:
				j1p1.setText(fs.passives[index][0]);
				j1p2.setText(fs.passives[index][1]);
				b1.setEnabled(true);
				b2.setEnabled(true);
				break;
		}
		
	}

	private void onPassive1Change() {
		
		/* Update the job associated with the first user-selected 
		 * and enable or disable the corresponding help button, as
		 * appropriate. */
		
		String name = (String)p1.getSelectedItem();
		String[] jobs = fs.classNamesFromPassive(name);
		p1j.removeAllItems();
		for (String s : jobs) p1j.addItem(s);
		p1j.setEnabled(jobs.length > 1);
		b3.setEnabled(!name.equals(""));
		
	}
	
	private void onPassive2Change() {

		/* Update the job associated with the second user-selected
		 * passive ability and enable or disable the corresponding
		 * help button, as appropriate. */

		String name = (String)p2.getSelectedItem();		
		String[] jobs = fs.classNamesFromPassive(name);
		p2j.removeAllItems();
		for (String s : jobs) p2j.addItem(s);
		p2j.setEnabled(jobs.length > 1);
		b4.setEnabled(!name.equals(""));

	}

	private void showMessageDialog(String title, String message) {
		
		JOptionPane.showMessageDialog(this,message,title,JOptionPane.PLAIN_MESSAGE);
		
	}

	private void updateText() {
		
		/* Update the output text box to reflect the current set of
		 * selected and implied jobs. */
		
		// Get the index for each possible job
		
		int[] jobs = new int[5];
		jobs[0] = fs.classIndex((String)j1.getSelectedItem());
		jobs[1] = fs.classIndex((String)j2.getSelectedItem());
		jobs[2] = fs.classIndex((String)p1j.getSelectedItem());
		jobs[3] = fs.classIndex((String)p2j.getSelectedItem());
		jobs[4] = fs.classIndex((String)cj.getSelectedItem());
		
		// Count the number of unique job indices
		
		int count = 0;
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i] != -1) {
				boolean match = false;
				for (int j = 0; j < i; j++) {
					match = jobs[i] == jobs[j];
					if (match) break;
				}
				if (!match) count++;
			}
		}
				
		// Show output
		
		if (count > 0) {
						
			// Get indices for just the unique jobs
			
			int[] indices = new int[count];
			int pos = 0;
			for (int i = 0; i < jobs.length; i++) {
				if (jobs[i] != -1) {
					boolean match = false;
					for (int j = 0; j < pos; j++) {
						match = jobs[i] == indices[j];
						if (match) break;
					}
					if (!match) indices[pos++] = jobs[i];
				}
			}
			
			// Map from possible sources to jobs in the indices array
			
			int[] map = new int[5];
			for (int i = 0; i < 5; i++) map[i] = -1;
			for (int i = 0; i < jobs.length; i++) {
				if (jobs[i] != -1) {
					for (int j = 0; j < indices.length; j++) {
						if (jobs[i] == indices[j]) {
							map[i] = j;
							break;
						}
					}
				}
			}
			
			// Generate text
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < indices.length; i++) {
				if (i > 0) sb.append(System.lineSeparator());
				sb.append("To unlock ");
				sb.append(fs.classes[indices[i]]);
				sb.append(" (");
				boolean addComma = false;
				for (int j = 0; j < map.length; j++) {
					if (map[j] == i) {
						if (addComma) sb.append(", ");
						switch (j) {
							case 0:
								sb.append("Primary job");
								break;
							case 1:
								sb.append("Secondary job");
								break;
							case 2:
								sb.append((String)p1.getSelectedItem());
								break;
							case 3:
								sb.append((String)p2.getSelectedItem());
								break;
							case 4:
								sb.append((String)c.getSelectedItem());
								break;
						}
						addComma = true;
					}
				}
				sb.append(") you'll need:");
				sb.append(System.lineSeparator());
				sb.append(fs.prereqsToString(fs.classPrereqs(indices[i])));
				sb.append(System.lineSeparator());
			}
			sb.append(System.lineSeparator());
			sb.append("Overall, you'll need:");
			sb.append(System.lineSeparator());
			sb.append(fs.prereqsToString(fs.mergedPrereqs(indices)));
			ta.setText(sb.toString());
			
		} else {
			
			ta.setText("No jobs selected or implied.");
			
		}
		
	}

}
