import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.File;

import javax.swing.JFrame;

public class ConfigureMenuHandler implements ActionListener {
   JFrame jframe;

   /**
    * Constructor, use this JFrame
    * @param jf
    */
   public ConfigureMenuHandler (JFrame jf) {
      jframe = jf;
   }
   

   /**
    * If user clicks insert, he/she opens a file to insert into database. Refresh afterwards.
    * If user clicks delete, he/she deletes a row from the database. Refresh afterwards.
    * If user clicks modify, he/she can modify a certain row. Refresh afterwards.
    */
   public void actionPerformed(ActionEvent event) {
	  try {
		  String menuName = event.getActionCommand();
		  if (menuName.equals("Insert")) { 
			  Main.readInput(openFile()); 
			  refreshGUI();
		  }
		  else if (menuName.equals("Delete")) {
			  int n = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the ID to delete:"));
			  Main.delete(n);
			  refreshGUI();
			  
		  }
		  else if (menuName.equals("Modify")) {
			  int n = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the ID to modify:"));
			  modifyGUI(n);
			  refreshGUI();
		  }
	  } catch (Exception e) { System.out.println(e); }
    	  
   }
   
   /**
    * Take the chosen file, convert it into an actual class File.
    * Return the path of the file.
    */
   private String openFile() {
	   JFileChooser chooser = new JFileChooser(); 
	   chooser.showOpenDialog(null);
	   File f = chooser.getSelectedFile();
	   return f.getAbsolutePath();
   }
   
   /**
    * Refresh the GUI
    */
   private void refreshGUI() {
	   JTextArea myTextArea = new JTextArea();
	   jframe.add(myTextArea);
	   try {
		   for(int i=0; i< (Main.localDB()).size(); i++){
			   myTextArea.append(Main.localDB().get(i) + "\n");
		   }
	   } catch (Exception e) { System.out.println(e); }
   }
   
   /**
    * The modify GUI contains all the fields and an input box where the user can modify all the fields
    * @param id
    */
   private void modifyGUI(int id) {
	   JPanel panel = new JPanel(new GridBagLayout());
       GridBagConstraints cs = new GridBagConstraints();

       cs.fill = GridBagConstraints.HORIZONTAL;

       //Title
       JLabel title = new JLabel("Title: ");
       cs.gridx = 0;
       cs.gridy = 0;
       cs.gridwidth = 1;
       panel.add(title, cs);

       JTextField titleField = new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 0;
       cs.gridwidth = 2;
       panel.add(titleField, cs);

       //ISBN 
       JLabel isbn = new JLabel("ISBN: ");
       cs.gridx = 0;
       cs.gridy = 1;
       cs.gridwidth = 1;
       panel.add(isbn, cs);

       JTextField isbnField= new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 1;
       cs.gridwidth = 2;
       panel.add(isbnField, cs);
       
       //Author
       JLabel author = new JLabel("Author: ");
       cs.gridx = 0;
       cs.gridy = 2;
       cs.gridwidth = 1;
       panel.add(author, cs);

       JTextField authorField= new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 2;
       cs.gridwidth = 2;
       panel.add(authorField, cs);
       
       //Publisher
       JLabel publisher = new JLabel("Publisher: ");
       cs.gridx = 0;
       cs.gridy = 3;
       cs.gridwidth = 1;
       panel.add(publisher, cs);

       JTextField publisherField= new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 3;
       cs.gridwidth = 2;
       panel.add(publisherField, cs);
       
       //Price
       JLabel price = new JLabel("Price: ");
       cs.gridx = 0;
       cs.gridy = 4;
       cs.gridwidth = 1;
       panel.add(price, cs);

       JTextField priceField= new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 4;
       cs.gridwidth = 2;
       panel.add(priceField, cs);

       //Create this GUI and show it to the user
	   int result = JOptionPane.showConfirmDialog(null, panel, 
			   "Please enter the new fields", JOptionPane.OK_CANCEL_OPTION);
	   if (result == JOptionPane.OK_OPTION) {
		   Main.modify(id, titleField.getText(), isbnField.getText()
				   ,authorField.getText(), publisherField.getText(), priceField.getText());
	   }
   }

}