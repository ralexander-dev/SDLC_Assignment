/*
  * Library Manager class to manage library patrons
  * Provides functionality to create, add, remove, and list patrons
*/

// imports
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class Library_Manager {
  ArrayList<Patron> patrons = new ArrayList<Patron>(); // list to store patrons
  Scanner scanner = new Scanner(System.in); // scanner for user input

  // display the initial application menu and load patrons from file if selected
  void initApp() {
    // print menu header
    System.out.println("-------------------------------");
    System.out.println("Library Management System");
    System.out.println("-------------------------------");
    
    // prompt user to load patrons from file
    System.out.println("Would you like to load patrons from a file? (yes/no)");
    if (scanner.nextLine().equalsIgnoreCase("yes")) {
      System.out.print("Enter filename: "); // prompt user for filename
      String filename = scanner.nextLine(); // ingest filename
      loadPatronsFromFile(filename); // attempt to load patrons from the provided file
    }
  }

  // display the main menu options
  void displayMenu() {
    System.out.println("Library Management System");
    System.out.println("1. Add Patron");
    System.out.println("2. Remove Patron");
    System.out.println("3. View All Patrons");
    System.out.println("4. Exit");
  }

  // main application loop
  void run() {
    while (true) {
      displayMenu(); // display menu options

      int choice = scanner.nextInt(); // get user choice as integer
      scanner.nextLine(); // consume newline left by nextInt()

      switch (choice) {
        case 1:
          createPatron(); // allow user to enter patron details
          break;
        case 2:
          boolean status = removePatron(); // prompt for patron ID and attempt to remove patron

          // print removal status
          if (status) {
            System.out.println("Patron removed successfully.");
          } else {
            System.out.println("Patron not found.");
          }
          break;
        case 3:
          this.getAllPatrons(); // display all patrons
          break;
        case 4:
          System.out.println("Exiting the program."); // print exit message
          scanner.close();
          System.exit(0);
          break;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
      System.out.flush(); // clear console after each iteration of the main loop
    }
  }

  // attempt to read patrons from a file and add them to the patrons list
  void loadPatronsFromFile(String filename) {

    // file validation checks
    if (filename == null || filename.isEmpty()) {
      throw new RuntimeException("Filename cannot be null or empty.");
    }
    if (!filename.contains(".txt")) {
      throw new RuntimeException("Invalid file type. Only .txt files are supported.");
    }

    String content = ""; // variable to store file content

    // attempt to read file content, if an error occurs print message and exit
    try {
      content = Files.readString(Paths.get(filename));
    } catch (Exception e) {
      throw new RuntimeException("Error reading file: " + e.getMessage());
    }

    // split content into lines
    String[] lines = content.split("\n");

    // for each line...
    for (String line : lines) {
      String[] parts = line.split("-"); // split line into parts on hyphen

      // if there are 4 parts, attempt to parse them and create a new patron
      if (parts.length == 4) {
        try {
          int patronID = Integer.parseInt(parts[0].trim()); // extract patronID
          // validate patronID is a 7 digit number and not a duplicate
          if (patronID < 1000000 || patronID > 9999999) {
            throw new RuntimeException("Invalid patron ID: " + patronID + ". Must be a 7 digit number.");
          }
          String name = parts[1].trim(); // extract name
          String address = parts[2].trim(); // extract address
          double fineAmount = Double.parseDouble(parts[3].trim()); // extract fine amount
          Patron patron = new Patron(patronID, name, address, fineAmount); // create new patron with extracted information
          boolean status = addPatron(patron); // attempt to add a new patron to the list

          // if adding the patron failed, throw a runtime error
          if (!status) {
            throw new RuntimeException("Failed to add patron with ID: " + patronID);
          }
        }
        // throw runtime error on any exception during file parsing or patron creation 
        catch (Exception e) {
          throw new RuntimeException("Invalid data format in file: " + e.getMessage());
        }
      }
      // else (not exactly 4 parts) throw a runtime error for invalid line format
      else {
        throw new RuntimeException("Invalid line format in file: " + line);
      }
    }
  }

  // create a new patron by prompting user for information
  boolean createPatron() {
    int patronID = (int) ThreadLocalRandom.current().nextInt(1000000, 9999999); // generate random 7 digit patron ID

    System.out.print("Enter name: "); // prompt user for name
    String name = scanner.nextLine();
    
    System.out.print("Enter address: "); // prompt user for address
    String address = scanner.nextLine();

    String fineAmountInput; // store fine amount input as string
    double fineAmount = 0.0; // initialize fine amount to 0.0
    System.out.print("Enter fine amount (or leave blank for 0): "); // prompt user for fine amount
    fineAmountInput = scanner.nextLine(); // input fine amount as string
    try {
      fineAmount = fineAmountInput.isEmpty() ? 0.0 : Double.parseDouble(fineAmountInput); // if not blank, parse fine amount to double
    } catch (Exception e) {
      throw new RuntimeException("Invalid fine amount: " + e.getMessage()); // throw runtime error if fine amount is invalid
    }

    try {
      Patron newPatron = new Patron(patronID, name, address, fineAmount); // create new patron
      boolean status = addPatron(newPatron); // add new patron to the list
      if (!status) {
        System.out.println("A patron with the same ID already exists. Try again? (yes/no)");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
          return createPatron(); // if user wants to try again, call createPatron() recursively
        } else {
          return false; // else return false
        }
      }
      System.out.println("Patron created successfully with ID: " + patronID); // confirm creation of patron
      return true; // return true to indicate successful creation
    } catch (Exception e) {
      throw new RuntimeException("Error creating patron: " + e.getMessage()); // throw runtime error if there was an issue creating the patron
    }
  }

  // add a patron to the patrons list
  boolean addPatron(Patron patron) {
    for (Patron existingPatron : patrons) {
      if (existingPatron.getPatronID() == patron.getPatronID()) {
        return false; // if a patron with the same ID already exists, return false
      }
    }
    patrons.add(patron);
    return true;
  }

  // remove a patron from the patrons list by their ID, return true if successful, false if not found
  boolean removePatron() {
    System.out.print("Enter Patron ID to remove: ");
    int patronID = scanner.nextInt();
    scanner.nextLine();

    // for patrons...
    for (int i = 0; i < patrons.size(); i++) {
      // if patronID matches provided ID, remove patron and return true
      if (patrons.get(i).getPatronID() == patronID) {
        patrons.remove(i); 
        return true;
      }
    }
    // if no patron with provided ID was found, return false
    return false;
  }

  // print all patrons
  void getAllPatrons() {
    for (Patron patron : patrons) {
      System.out.println("ID: " + patron.getPatronID() + ", Name: " + patron.getName() + ", Address: " + patron.getAddress() + ", Fine Amount: " + patron.getFineAmount());
    }
  }
}

    