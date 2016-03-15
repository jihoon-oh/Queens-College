import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


public class Main {
	static PrintStream out;
	static PrintStream log;
	static BookGUI myGUI;

	public static void main(String[] args) throws Exception 
	{
		out = new PrintStream(new FileOutputStream(args[1])); //user provides location of output file 
		log = new PrintStream(new FileOutputStream(args[2])); //user provides location of log file
		createTable();
		readInput(args[0]);
		myGUI = new BookGUI("ISBN Database", 800, 500);
		showDB(myGUI);
	}
	
	/**
	 * Provide all the information from database into output file
	 */
	public static void output(){
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM ISBN_TABLE");
			ResultSet result = statement.executeQuery();
			while(result.next()){
				int id = Integer.parseInt(result.getString("id"));
				String title = result.getString("title");
				String isbn = result.getString("isbn");
				String author = result.getString("author");
				String publisher = result.getString("publisher");
				String price = result.getString("price");
				out.println(id + " | " + title + " | " + isbn + " | " + author + " | " + publisher + " | " + price);
			}
		} catch (Exception e) {System.out.println(e);}
	}

	/**
	 * Given a URL, scrapes all the necessary information via abebooks.com
	 * @param urlString
	 */
	public static void advSearch(String urlString) {
		try {
			String isbn = null;
			String title = null;
			String author = null;
			String price = null;
			String publisher = null;
			Pattern pattern;
			Matcher matcher;
			URL url = new URL(urlString);
			BufferedReader website = new BufferedReader(new InputStreamReader(url.openStream()));
			
			for(String line = website.readLine(); line != null; line = website.readLine()) {
				if(checkRegex("<div id=\"book.*", line)) {
					while(!checkRegex("\\s*<meta itemprop=\"isbn\" content=\"", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("[0-9]{13}");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						isbn = matcher.group(0);
					}
					
					while(!checkRegex("\\s*<meta itemprop=\"name\" content=\"", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(\".*\")");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						title = (matcher.group(0).substring(16,matcher.group(0).length()-1));
					}
					
					while(!checkRegex("\\s*<meta itemprop=\"author\" content=\"", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(\".*\")");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						author = (matcher.group(0).substring(18,matcher.group(0).length()-1));
					}
					
					while(!checkRegex("\\s*<meta itemprop=\"publisher\" content=\"", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(\".*\")");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						publisher = (matcher.group(0).substring(21,matcher.group(0).length()-1));
					}
					
					while(!checkRegex("\\s*<meta itemprop=\"price\" content=\"", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(\".*\")");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						price = (matcher.group(0).substring(17,matcher.group(0).length()-1));
					}
					
					line = null; //Once all info is found, the rest of the for-loop is unnecessary
				}
			}
			
			//Confirm with the user if this is the right book
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, 
					"Would you like to store " 
							+ "\nTitle: "+ title 
							+ "\nISBN: " + isbn 
							+ "\nAuthor: " + author 
							+ "\nPublisher: " + publisher 
							+ "\nPrice: $" + price
							+ " into the database?" ,
					"Confirm message",
					dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
				insert(title, isbn, author, publisher, price); 
				myGUI.refreshGUI();
			}
		} catch (MalformedURLException e) { System.out.println(e);
		} catch (IOException e) { System.out.println(e); }
	}

	/**
	 * Given a ISBN, scrapes all the necessary information via infosearch.org
	 * @param isbn
	 */
	public static void getInfo(String isbn){
		try{
			Pattern pattern;
			Matcher matcher;
			String title = "No title available";
			String author = "No author available";
			String publisher = "No publisher available";
			String price = "No price available";
			URL url = new URL("http://isbnsearch.org/isbn/" + isbn);
			
			BufferedReader website = new BufferedReader(new InputStreamReader(url.openStream()));
			for(String line = website.readLine(); line != null; line = website.readLine()) {

				if(checkRegex("\\s*<div class=\"bookinfo", line)) {
				
					while(!checkRegex("<h2>", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(<h2>.*</h2>)");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						title = (matcher.group(0).substring(4, matcher.group(0).length()-5));
					}
				
			
					while(!checkRegex("\\s*<p><strong>Author:</strong>", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(</strong>.*</p>)");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						author = (matcher.group(0).substring(10,matcher.group(0).length()-4));
					}
				
					while(!checkRegex("\\s*<p><strong>Publisher:</strong>", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(</strong>.*</p>)");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						publisher = (matcher.group(0).substring(10,matcher.group(0).length()-4));
					}
				
					while(!checkRegex("\\s*<p><strong>List Price:</strong>", line)){
						line = website.readLine();
					}
					pattern = Pattern.compile("(</strong>.*</p>)");
					matcher = pattern.matcher(line);
					if (matcher.find()){
						price = (matcher.group(0).substring(10,matcher.group(0).length()-4));
					}
				}
			}
			
			insert(title, isbn, author, publisher, price);
		} catch (Exception e) { System.out.println(e); }
		
	}
	
	/**
	 * Checks for the regex within a string
	 * @param regexPattern
	 * @param strCheck
	 * @return
	 */
	public static boolean checkRegex(String regexPattern, String strCheck)
	{
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(strCheck);
		return matcher.find();    	    
	}

	/**
	 * Show the database on the GUI
	 * @param GUI
	 */
	public static void showDB(BookGUI GUI) 
	{
		JTextArea myTextArea = new JTextArea();
		GUI.add(myTextArea);
		ArrayList<String> a = new ArrayList<String>();
		a = localDB();
		try {
			for(int i=0; i< a.size(); i++){
				  myTextArea.append(a.get(i) + "\n");
			}
		} catch (Exception e) { System.out.println(e); }
	}
	
	/**
	 * Read all the ISBN's in the input file
	 * @param file
	 */
	public static void readInput(String file)
	{
		TextFileInput in = new TextFileInput(file);
		try {
			for(String line = in.readLine(); line != null ; line = in.readLine()){
				getInfo(line);
			} 
		} catch (Exception e) { System.out.println(e); }
	}
	
	/**
	 * Allow other classes to access the database as an array list
	 * @return
	 */
	public static ArrayList<String> localDB()  
	{
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM ISBN_TABLE");
			ResultSet result = statement.executeQuery();
			ArrayList<String> array = new ArrayList<String>(); //temporary array to load all the data
			while(result.next()){
				int id = Integer.parseInt(result.getString("id"));
				String title = result.getString("title");
				String isbn = result.getString("isbn");
				String author = result.getString("author");
				String publisher = result.getString("publisher");
				String price = result.getString("price");
				array.add(id + " " + title + " " + isbn + " " + author + " " + publisher + " " + price);
			}
			return array;
		} catch (Exception e) {System.out.println(e);}
		return null;
	}

	/**
	 * Insert title, ISBN, author, publisher, and price into the database
	 * @param t
	 * @param i
	 * @param a
	 * @param pu
	 * @param pr
	 */
	public static void insert(String t, String i, String a, String pu, String pr)  
	{
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO ISBN_TABLE(title, isbn, author, publisher, price) "
					+ "VALUES ('"+t+"', '"+i+"', '"+a+"', '"+pu+"', '"+pr+"')");
			statement.executeUpdate();
			
			//log the insert into the log file
			log.println("INSERT: " + t + ", " + i + ", " + a + ", " 
					+ pu + ", " + pr + " at "
					+ new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) { System.out.println(e); }
		finally {
			System.out.println("Insert Complete");
		}
	}
	
	/**
	 * Delete a given row from the database
	 * @param n
	 */
	public static void delete(int n) 
	{
		try{
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("DELETE FROM ISBN_Table WHERE id = " + n);
		    statement.execute();
		    
		    //log the delete
		    log.println("DELETE : " + n);
		} catch (Exception e) { System.out.println(e); }
		finally {
			System.out.println("Delete Complete");
		}
	
	}
	
	/**
	 * Given an id, modify the title, isbn, author, publisher, and price
	 * @param id
	 * @param newTitle
	 * @param newISBN
	 * @param newAuthor
	 * @param newPublisher
	 * @param newPrice
	 */
	public static void modify(int id, String newTitle, String newISBN, String newAuthor, String newPublisher, String newPrice) {
		try{
			Connection conn = getConnection();
			String query = "UPDATE ISBN_Table SET title = ?, isbn = ?,  author = ?, publisher = ? , price = ? WHERE id = ?";
		    PreparedStatement statement = conn.prepareStatement(query);
		    statement.setString(1, newTitle);
		    statement.setString(2, newISBN);
		    statement.setString(3, newAuthor);
		    statement.setString(4, newPublisher);
		    statement.setString(5, newPrice);
		    statement.setInt(6, id);
			statement.executeUpdate();
			
			//log the modify
			log.println("MODIFY: " + id + " to " + newTitle 
					+ newISBN + " " 
					+ newAuthor + " " 
					+ newPublisher + " " 
					+ newPrice);
		} catch(Exception e) { System.out.println(e); }
	}
	
	/**
	 * If there is no table, create it
	 */
	public static void createTable() 
	{
		try {
			Connection conn = getConnection();
			PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS "
					+ "ISBN_Table(id int NOT NULL AUTO_INCREMENT, "
					+ "title varchar(255), "
					+ "isbn varchar(255), "
					+ "author varchar(255), "
					+ "publisher varchar(255), "
					+ "price varchar(255), "
					+ "PRIMARY KEY(ID))");
			create.executeUpdate();
		} catch (Exception e){ System.out.println(e); }
		finally {System.out.println("Creation complete");}
	}
	
	/**
	 * Establish a connection to the database
	 * @return
	 */
	public static Connection getConnection()  
	{
		try{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/370_Final_Project";
			String username = "root"; //Very important to type in your username
			String password = ""; //Very important to type in your password
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(url,username,password);
			return conn;
		} catch (Exception e){
			System.out.println(e);
		}
		
		
		return null;
	}

	
}
