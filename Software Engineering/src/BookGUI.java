import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

public class BookGUI extends JFrame {
	private JMenuBar menuBar;
	
	/**
	 * Sets up a GUI, creates all menus
	 * @param title
	 * @param height
	 * @param width
	 */
	public BookGUI (String title, int height, int width)
	{
		setTitle(title);
		setSize(height, width);
		setLocation(300, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		createConfigureMenu();
		createSearchMenu();
		createQuitMenu();
		setVisible(true);
	}
	
	/**
	 * Creates a "Configure" menu, which allows the user to configure the database
	 * The menu items are insert, delete, and modify
	 */
	private void createConfigureMenu()
	{
		JMenuItem item;
	    menuBar = new JMenuBar();
	    JMenu configureMenu = new JMenu("Configure");
	    ConfigureMenuHandler cmh  = new ConfigureMenuHandler(this);

	    item = new JMenuItem("Insert");
	    item.addActionListener(cmh);
	    configureMenu.add(item);

	    configureMenu.addSeparator();         
	      
	    item = new JMenuItem("Delete");
	    item.addActionListener(cmh);
	    configureMenu.add(item);
	    
	    configureMenu.addSeparator();         
	      
	    item = new JMenuItem("Modify");
	    item.addActionListener(cmh);
	    configureMenu.add(item);

	    setJMenuBar(menuBar);
	    menuBar.add(configureMenu);
	}
	
	/**
	 *Creates a "Search" menu, which allows the user to search online for a book 
	 */
	private void createSearchMenu()
	{
		JMenuItem item;
		JMenu displayMenu = new JMenu("Search");
		SearchMenuHandler dmh  = new SearchMenuHandler(this);

		item = new JMenuItem("Online Search"); 
		item.addActionListener( dmh );
		displayMenu.add(item);

		menuBar.add(displayMenu);
	}
	
	/**
	 * Creates a "Quit" menu, which allows the user to terminate the program.
	 * This is also necessary if we want the output to represent all the correct data
	 */
	private void createQuitMenu()
	{
		JMenuItem item;
		JMenu quitMenu = new JMenu("Quit");
		QuitMenuHandler qmh  = new QuitMenuHandler(this);

		item = new JMenuItem("Exit System"); 
		item.addActionListener( qmh );
		quitMenu.add(item);

		menuBar.add(quitMenu);
	}
	
	/**
	 * Refresh the GUI to make sure it represents the right data
	 */
	public void refreshGUI() {
		   JTextArea myTextArea = new JTextArea();
		   this.add(myTextArea);
		   try {
			   for(int i=0; i< (Main.localDB()).size(); i++){
				   myTextArea.append(Main.localDB().get(i) + "\n");
			   }
		   } catch (Exception e) { System.out.println(e); }
	   }
	
}