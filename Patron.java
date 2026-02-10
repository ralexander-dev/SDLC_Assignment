/*
  * Patron class represents a library patron. 
*/

public class Patron {
  int patronID;
  String name;
  String address;
  double fineAmount;

  // constructor for creating a new patron with no fine amount
  Patron(int patronID, String name, String address) {
    this.patronID = patronID;
    this.name = name;
    this.address = address;
    this.fineAmount = 0.0;
  }

  // constructor for creating a new patron with a fine amount
  Patron(int patronID, String name, String address, double fineAmount) {
    this.patronID = patronID;
    this.name = name;
    this.address = address;
    this.fineAmount = fineAmount;
  }

  // getters and setters for patron attributes
  int getPatronID() {
    return patronID;
  }

  String getName() {
    return name;
  }

  String getAddress() {
    return address;
  }

  double getFineAmount() {
    return fineAmount;
  }

  void setName(String name) {
    this.name = name;
  }

  void setAddress(String address) {
    this.address = address;
  }

  void setFineAmount(double fineAmount) {
    this.fineAmount = fineAmount;
  }
}
