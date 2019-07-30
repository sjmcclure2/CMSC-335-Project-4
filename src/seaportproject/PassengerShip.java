package seaportproject;

import java.util.Scanner;

public class PassengerShip extends Ship {
    int numberOfOccupiedRooms;
    int numberOfPassengers;
    int numberOfRooms;


    protected PassengerShip(Scanner scan) {
        super(scan);
        this.setNumberOfPassengers(scan.nextInt());
        this.setNumberOfRooms(scan.nextInt());
        this.setNumberOfOccupiedRooms(scan.nextInt());
    }
    
    //Setters
    private void setNumberOfOccupiedRooms(int numOccRooms) {
        this.numberOfOccupiedRooms = numOccRooms;
    }
    private void setNumberOfPassengers(int numPass) {
        this.numberOfPassengers = numPass;
    }
    private void setNumberOfRooms(int numRooms) {
        this.numberOfRooms = numRooms;
    }
    
    //Getters
    protected int getNumberOfOccupiedRooms() {
        return numberOfOccupiedRooms;
    }
    protected int getNumberOfPassengers() {
        return numberOfPassengers;
    }
    protected int getNumberOfRooms() {
        return numberOfRooms;
    }
    
    public String toString() {
        String output = "";
        output += "\nPassenger Ship\n" + super.toString();
        output += "\nNumber of Passengers: " + getNumberOfPassengers() + 
                  "\nNumber of Rooms: " + getNumberOfRooms() +
                  "\nNumber of Occupied Rooms: " + getNumberOfOccupiedRooms(); 
                 
        return output;
    }
}