import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class SpringForm extends JPanel{
	
	boolean enterPressed = false;
	HashMap<String, String> formVals = new HashMap<>();
	boolean submitted = false;
	JLabel[] lbls;
	JTextField[] fields;
	JButton submit;

	boolean isSubmitted(){
		return submitted;
	}
	
	HashMap getFormVals(){
		return formVals;
	}
	
	JButton getSubmitButton(){
		return submit;
	}
		
	public SpringForm(String[] labels, int padding, int fieldLength, boolean rightJustify){
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		int longestInd = 0;		// Index of longest Label
		lbls = new JLabel[labels.length];
		fields = new JTextField[labels.length];
		submit = new JButton("Submit");
		
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				for(int i = 0; i < lbls.length; i++){
					formVals.put(lbls[i].getText(), fields[i].getText());
				}
				submitted = true;
			}
			
		});
		
		// Loop through labels array to find longest label and create new JLabels and JTextFields
		for(int i = 0; i < labels.length; i++){
			
			//find longest label set longestInd to index of longest label
				if(labels[longestInd].length() < labels[i].length()){
					longestInd = i;
			}
			
			//create all labels and fields
			lbls[i] = new JLabel(labels[i]);
			fields[i] = new JTextField(fieldLength);
		}
		
		// Loop through JLabels and JTextFields, add them, and set SpringLayout constraints
		for(int i = 0; i <  lbls.length; i++){
			
			//add labels and fields
			add(lbls[i]);
			add(fields[i]);
			
			// if not longest label, set label relative to longest label for right justification
			if(i == longestInd){
				
				layout.putConstraint(SpringLayout.WEST, lbls[longestInd], padding, SpringLayout.WEST, this);
			} else {
				
				// if right justification then set label relative to longest label, else set label relative to panel
				if(rightJustify){
					layout.putConstraint(SpringLayout.EAST, lbls[i], 0, SpringLayout.EAST, lbls[longestInd]);
				} else {
					layout.putConstraint(SpringLayout.WEST, lbls[i], 0, SpringLayout.WEST, this);
				}
			}
			
			
			if(i == 0){
				// Set first label height relative to panel
				layout.putConstraint(SpringLayout.NORTH, lbls[i], padding, SpringLayout.NORTH, this);
				
				// Set first field -- same as below
				layout.putConstraint(SpringLayout.WEST, fields[i], padding, SpringLayout.EAST, lbls[longestInd]);
				layout.putConstraint(SpringLayout.NORTH, fields[i], 0, SpringLayout.NORTH, lbls[i]);
				
			} else {
				
				// Set label height relative to label above it
				layout.putConstraint(SpringLayout.NORTH, lbls[i], padding, SpringLayout.SOUTH, lbls[i-1]);
				
				// Set field height relative to label across from it
				layout.putConstraint(SpringLayout.WEST, fields[i], padding, SpringLayout.EAST, lbls[longestInd]);
				layout.putConstraint(SpringLayout.NORTH, fields[i], 0, SpringLayout.NORTH, lbls[i]);
			}
		}
		
		// Add submit button and set location relative to last JTextField
		add(submit);
		layout.putConstraint(SpringLayout.EAST, submit, -1, SpringLayout.EAST, fields[fields.length - 1]);
		layout.putConstraint(SpringLayout.NORTH, submit, padding, SpringLayout.SOUTH, fields[fields.length - 1]);
	}
}
