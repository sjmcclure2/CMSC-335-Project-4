package seaportproject;

import java.util.Scanner;

public class Person extends Thing{
    String skill;

    protected Person(Scanner scan) {
        super(scan);
        this.setSkill(scan.next());
    }

    //Setter
    private void setSkill(String skill) {
        this.skill = skill;
    }
    
    //Getter
    protected String getSkill() {
        return skill;
    }
    
    public String toString() {
        String output = "";
        output += super.toString() + " ";
        
        output += "\nSkill: " + getSkill() + "\n";
        
        return output;
    }
}