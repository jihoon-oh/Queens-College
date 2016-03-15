import java.awt.event.*;
import javax.swing.JFrame;

public class QuitMenuHandler implements ActionListener {
   JFrame jframe;

   /**
    * Constructor. Use this JFrame
    * @param jf
    */
   public QuitMenuHandler (JFrame jf) {
      jframe = jf;
   }

   /**
    * If user clicks "Exit System", output the data and exit the system.
    */
   public void actionPerformed(ActionEvent event) {
	  try {
		  String menuName = event.getActionCommand();
		  if (menuName.equals("Exit System")) {
			  Main.output();
			  System.exit(0);
		  }
	  } catch (Exception e) {System.out.println(e); }
   }
}
