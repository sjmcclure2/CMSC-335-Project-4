package seaportproject;

import java.util.Scanner;

public class Dock extends Thing {
    Ship ship;

    protected Dock(Scanner scan) {
        super(scan);
    }
    
    //Setter Method
    protected void setShip(Ship ship) {
        this.ship = ship;
    }
    
    
    //Getter Method
    protected Ship getShip() {
        return this.ship;
    }
    
    @Override
    public String toString() {
        String output = "";
        output += (ship == null) ? "EMPTY" : ship.toString();
 
        return String.format("\nDock\n %s\n %s\n", super.toString(), output);
    }
}