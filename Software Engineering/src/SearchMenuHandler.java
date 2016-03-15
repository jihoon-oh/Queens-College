import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;

public class SearchMenuHandler implements ActionListener {
   JFrame jframe;

   /**
    * Constructor, use this JFrame.
    * @param jf
    */
   public SearchMenuHandler (JFrame jf) {
      jframe = jf;
   }
   
   /**
    * If user clicks on "Online Search", create the "advanced search" GUI.
    */
   public void actionPerformed(ActionEvent event) 
   {
	  String menuName = event.getActionCommand();
      
      if (menuName.equals("Online Search")){
    	  advSearchGUI();
      }
   }
   
   /**
    * The "advanced search" GUI has three fields: author, title, and ISBN. 
    * User may type in any of these fields, to which the program will open the URL and scrape for data.
    */
   private void advSearchGUI() 
   {
	   JPanel panel = new JPanel(new GridBagLayout());
       GridBagConstraints cs = new GridBagConstraints();

       cs.fill = GridBagConstraints.HORIZONTAL;

       //Author
       JLabel author = new JLabel("Author: ");
       cs.gridx = 0;
       cs.gridy = 0;
       cs.gridwidth = 1;
       panel.add(author, cs);

       JTextField authorField = new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 0;
       cs.gridwidth = 2;
       panel.add(authorField, cs);

       //Title
       JLabel title = new JLabel("Title: ");
       cs.gridx = 0;
       cs.gridy = 1;
       cs.gridwidth = 1;
       panel.add(title, cs);

       JTextField titleField= new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 1;
       cs.gridwidth = 2;
       panel.add(titleField, cs);
       
       //ISBN
       JLabel isbn = new JLabel("ISBN: ");
       cs.gridx = 0;
       cs.gridy = 2;
       cs.gridwidth = 1;
       panel.add(isbn, cs);

       JTextField isbnField= new JTextField(20);
       cs.gridx = 1;
       cs.gridy = 2;
       cs.gridwidth = 2;
       panel.add(isbnField, cs);

       //Create a GUI where the user can type in all the information
	   int result = JOptionPane.showConfirmDialog(null, panel, 
			   "Please enter the author, title and/or ISBN", JOptionPane.OK_CANCEL_OPTION);
	   if (result == JOptionPane.OK_OPTION) {
		   Main.advSearch(concatenate(authorField.getText(), titleField.getText(), isbnField.getText()));
	   }
   }

   /**
    * Given an author, ISBN, and/or title, concatenate the URL.
    * Notice how it will still work even if more than one of these fields in left blank.
    * @param author
    * @param title
    * @param isbn
    * @return
    */
   private String concatenate(String author, String title, String isbn)
   {
	   return "http://www.abebooks.com/servlet/SearchResults?recentlyadded=all&sts=t&bx=off&bi=0"
	   		+ "&isbn=" + isbn + "&sortby=17"
	   		+ "&tn=" + title.replace(' ', '+')
	   		+ "&an=" + author.replace(' ', '+') + "&ds=30";
   }
   
}