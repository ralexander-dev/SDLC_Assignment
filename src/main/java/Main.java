/*
  * Main class for the Library Management System.
  * This class provides the main method which serves the menu for the application. 
*/

public class Main {
  public static void main(String[] args) {
    Library_Manager libraryManager = new Library_Manager();
    libraryManager.initApp(); // initialize the application and load patrons from file if selected
    libraryManager.run(); // run the main loop of the application
  }
}
