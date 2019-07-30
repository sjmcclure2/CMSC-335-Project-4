package seaportproject;

import java.util.ArrayList;
import java.util.Scanner;

class SeaPort extends Thing {
    ArrayList<Dock> docks;
    ArrayList<Ship> ships;
    ArrayList<Ship> que;
    ArrayList<Person> persons;

    protected SeaPort(Scanner scan) {
        super(scan);
        this.setDocks(new ArrayList<>());
        this.setShips(new ArrayList<>());
        this.setQue(new ArrayList<>());
        this.setPerson(new ArrayList<>());
    }
    
    //Setter Methods
    private void setDocks(ArrayList<Dock> docks) {
        this.docks = docks;
    }
    private void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }
    private void setQue(ArrayList<Ship> que) {
        this.que = que;
    }
    private void setPerson(ArrayList<Person> people) {
        this.persons = people;
    }
    
    //Getter Methods
    protected ArrayList<Dock> getDocks() {
        return docks;
    }
    protected ArrayList<Ship> getShips() {
        return ships;
    }
    protected ArrayList<Ship> getQue() {
        return que;
    }
    protected ArrayList<Person> getPersons() {
        return persons;
    }
   
    
    @Override
    public String toString() {
        String output;
        
        output = "\n\nSeaPort\n" + super.toString() + "\n";
        for (Dock dock: this.getDocks()) {
            output += "\n " + dock.toString();
        }

        output += "\n\n ****** List of all ships in que ******";
        for (Ship shipQue: this.getQue()) {
            output += "\n " + shipQue.toString();
        }

        output += "\n\n ****** List of all ships ******";
        for (Ship shipAll: this.getShips()) {
            output += "\n " + shipAll.getName() + " " + shipAll.getIndex() + " ("
                + shipAll.getClass().getSimpleName() + ")";
        }
        
        output += "\n\n****** List of all Jobs ******";
        for(Ship ship : ships) {
            if(ship.getJobs() != null) {        
                for(Job job : ship.getJobs()) {
                    output += job;
                }
            }
        }
        output += "\n\n ****** List of all persons ******";
        for (Person person: this.getPersons()) {
            output += "\n " + person.toString();
        }

        return output;
    } 
}