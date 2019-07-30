package seaportproject;

import java.util.ArrayList;
import java.util.Scanner;

public class Ship extends Thing {
    PortTime arrivalTime, dockTime;
    double draft, length, width, weight;
    ArrayList<Job> jobs;

    protected Ship(Scanner scan) {
        super(scan);  
        this.setWeight(scan.nextDouble());
        this.setLength(scan.nextDouble());
        this.setWidth(scan.nextDouble());
        this.setDraft(scan.nextDouble());
        this.jobs = new ArrayList<>();
    }
    
    //Setters
    private void setArrivalTime(PortTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    private void setDockTime(PortTime dockTime) {
        this.dockTime = dockTime;
    }
    private void setDraft(double draft) {
        this.draft = draft;
    }
    private void setLength(double length) {
        this.length = length;
    }
    private void setWidth(double width) {
        this.width = width;
    }
    private void setWeight(double weight) {
        this.weight = weight;
    }
    
    // Getters
    protected PortTime getArrivalTime() {
        return this.arrivalTime;
    }
    protected PortTime getDockTime() {
        return this.dockTime;
    }
    protected double getDraft() {
        return this.draft;
    }
    protected double getLength() {
        return this.length;
    }
    protected double getWidth() {
        return this.width;
    }
    protected double getWeight() {
        return this.weight;
    }
    public ArrayList<Job> getJobs() {
        return jobs;
    }
    
    public String toString() {
        String output = super.toString();
        output += "\nWeight: " + getWeight() +
                  "\nLength: " + getLength() +
                  "\nWidth: " + getWidth() +
                  "\nDraft: " + getDraft();
        return output;
    }
}