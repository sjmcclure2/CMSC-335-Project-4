package seaportproject;

import java.util.Scanner;

public class Thing implements Comparable<Thing> {
    int index;
    String name;
    int parent;
    
    public Thing(Scanner scan) {
        if(scan.hasNext()) {
            this.setName(scan.next());
        }
        if(scan.hasNextInt()) {
            this.setIndex(scan.nextInt());
        }
        if(scan.hasNextInt()) {
            this.setParent(scan.nextInt());
        }
    }
    //Setter Methods
    private void setName(String name) {
        this.name = name;
    }
    private void setIndex(int index) {
        this.index = index;
    }
    private void setParent(int parent) {
        this.parent = parent;
    }
    
    //Getter Methods
    protected String getName() {
        return this.name;
    }
    protected int getIndex() {
        return this.index;
    }
    protected int getParent() {
        return this.parent;
    }
    
    @Override
    public String toString() {
        String output;
        output = "Name: " + this.getName() + "\n" +
                 "Index: " + this.getIndex();
        return output;
    }

    @Override
    public int compareTo(Thing o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}