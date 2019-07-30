package seaportproject;

import java.util.Scanner;

public class CargoShip extends Ship {
    double cargoValue;
    double cargoVolume;
    double cargoWeight;
    
    
    protected CargoShip(Scanner scan) {
        super(scan);
        this.setCargoWeight(scan.nextDouble());
        this.setCargoVolume(scan.nextDouble());
        this.setCargoValue(scan.nextDouble());
    }
    
    //Setters
    private void setCargoValue(double cargoValue) {
        this.cargoValue = cargoValue;
    }
    private void setCargoVolume(double cargoVolume) {
        this.cargoVolume = cargoVolume;
    }
    private void setCargoWeight(double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }
    
    //Getters
    protected double getCargoValue() {
        return cargoValue;
    }
    protected double getCargoVolume() {
        return cargoVolume;
    }
    protected double getCargoWeight() {
        return cargoWeight;
    }
    
    public String toString() {
        String output = "";
        output += "\nCargo Ship\n" + super.toString();
        output += "\nCargo Weight: " + getCargoWeight() +
                  "\nCargo Volume: " + getCargoVolume() +
                  "\nCargo Value: " + getCargoValue();
                  
        return output;
    }
}