package seaportproject;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.lang.reflect.Method;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

final class World extends Thing {

    private ArrayList<Thing> things;
    private ArrayList<SeaPort> ports;
    private PortTime time;
    private HashMap<Integer, SeaPort> portsMap;
    private HashMap<Integer, Dock> docksMap;
    private HashMap<Integer, Ship> shipsMap;
    private HashMap<Integer, Job> jobsMap;
    

    protected World(Scanner scannerContents, JPanel jobsPanel) {
        super(scannerContents);
        this.setThings(new ArrayList<>());
        this.setPorts(new ArrayList<>());
        this.process(scannerContents, jobsPanel);
    }

    // Setters
    private void setThings(ArrayList<Thing> allThings) {
        this.things = allThings;
    }
    private void setPorts(ArrayList<SeaPort> ports) {
        this.ports = ports;
    }
    private void setTime(PortTime time) {
        this.time = time;
    }

    // Getters
    protected ArrayList<Thing> getThings() {
        return this.things;
    }
    protected ArrayList<SeaPort> getPorts() {
        return this.ports;
    }
    protected PortTime getTime() {
        return this.time;
    }

    private void process(Scanner scannerContents, JPanel jobsPanel) {

        String lineString;
        Scanner lineContents;

        portsMap = new HashMap<>();
        docksMap = new HashMap<>();
        shipsMap = new HashMap<>();
        jobsMap = new HashMap<>();
        
        Ship parentShip;
        
        while (scannerContents.hasNextLine()) {
            lineString = scannerContents.nextLine().trim(); 

            if (lineString.length() == 0) {
                continue;
            }

            lineContents = new Scanner(lineString);

            if (lineContents.hasNext()) {
                switch(lineContents.next().trim()) {
                    case "port":
                        SeaPort port = new SeaPort(lineContents);
                        this.getThings().add(port);
                        this.getPorts().add(port);
                        portsMap.put(port.getIndex(), port);
                        break;

                    case "dock":
                        Dock dock = new Dock(lineContents);
                        this.getThings().add(dock);
                        this.addDockToPort(portsMap, dock);
                        docksMap.put(dock.getIndex(), dock);                      
                        break;

                    case "pship":
                        PassengerShip pShip = new PassengerShip(lineContents);
                        this.getThings().add(pShip);
                        this.addShipToParent(pShip, docksMap, portsMap);
                        shipsMap.put(pShip.getIndex(), pShip);
                        break;

                    case "cship":
                        CargoShip cShip = new CargoShip(lineContents);
                        this.getThings().add(cShip);
                        this.addShipToParent(cShip, docksMap, portsMap);
                        shipsMap.put(cShip.getIndex(), cShip);
                        break;

                    case "person":
                        Person person = new Person(lineContents);
                        this.getThings().add(person);
                        this.addPersonToPort(portsMap, person);
                        break;

                    case "job":
                        JPanel jobSubPanel = new JPanel();
                        jobSubPanel.setLayout(new FlowLayout());
                        jobSubPanel.setPreferredSize(new Dimension(745, 31));   
                        Job job = new Job(shipsMap, docksMap, portsMap, jobSubPanel, lineContents); 
                        jobsMap.put(job.getIndex(), job);
                        addJobsToShip(shipsMap, job);
                        jobsPanel.add(jobSubPanel);
                        jobsPanel.revalidate();
                        jobsPanel.repaint();
                    break;

                    default:
                        break;
                }
            }
        }
    }

    private void addDockToPort(HashMap<Integer, SeaPort> portsMap, Dock dock) {
        SeaPort port = portsMap.get(dock.getParent()); 
        port.getDocks().add(dock);
    }

    private void addShipToParent(Ship newShip, HashMap<Integer, Dock> docksMap,
            HashMap<Integer, SeaPort> portsMap) {

        SeaPort myPort;
        Dock myDock = docksMap.get(newShip.getParent());

        if (myDock == null) {
            myPort = portsMap.get(newShip.getParent());
            myPort.getShips().add(newShip);
            myPort.getQue().add(newShip);
        } else {
            myPort = portsMap.get(myDock.getParent());
            myDock.setShip(newShip);
            myPort.getShips().add(newShip);
        }
    }
    
    private void addJobsToShip(HashMap<Integer, Ship> shipsMap, Job job) {
        Ship ship = shipsMap.get(job.getParent());
        if(ship != null) {
            ship.getJobs().add(job);
        }
    }
    
    private void addPersonToPort(HashMap<Integer, SeaPort> portsMap, Person person) {
        SeaPort port = portsMap.get(person.getParent());
        port.getPersons().add(person);
    }

    @Override
    public String toString() {
        String output = "---------> Welcome to the world <----------\n";

        output = this.getPorts().stream().map((port) -> port.toString() + 
                "\n").reduce(output, String::concat);
        
        return output;
    }
}