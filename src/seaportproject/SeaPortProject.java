package seaportproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class SeaPortProject extends JFrame {
    
    //GUI Components
    private JFrame mainFrame;
    private JTabbedPane tabbPane;
    private JTextArea mainTextArea, searchTextArea;
    private JScrollPane mainScrollPane, searchScrollPane, treeScrollPane, jobScrollPane;
    private JPanel mainPanel, optionsPanel, worldPanel, treePanel, jobsPanel;
    private JButton openBtn, searchBtn, sortBtn;
    private JLabel searchLbl, sortLbl;
    private JTextField searchTxt;
    private String[] searchComboBoxValues;
    private JComboBox<String> searchComboBox;
    private JComboBox<String> sortPortComboBox, sortTargetComboBox,
            sortTypeComboBox;
    private String[] portSortValues, targetSortValues, typeSortValues;
    private JTree tree;
    
    private JFileChooser file;
    private Scanner scan;
    
    private World world;

    
    private void buildGUI() {
        this.mainPanel = new JPanel(new BorderLayout());
        this.worldPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        this.optionsPanel = new JPanel(new GridLayout(1, 10, 5, 5));
        this.treePanel = new JPanel(new GridLayout(1, 3, 5, 5));
        this.jobsPanel = new JPanel();
        this.jobsPanel.setLayout(new BoxLayout(jobsPanel, BoxLayout.PAGE_AXIS));
        
        this.tree = new JTree();
        this.tree.setModel(null);
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        this.tabbPane = new JTabbedPane();
        this.tabbPane.setPreferredSize(new Dimension(750, 550));
        this.tabbPane.setVisible(true);
        
        this.mainTextArea = new JTextArea();
        this.mainTextArea.setEditable(false);                      
        this.mainTextArea.setLineWrap(true);                        

        this.mainScrollPane = new JScrollPane(this.mainTextArea);
        this.jobScrollPane = new JScrollPane(jobsPanel);
                        
        this.searchTextArea = new JTextArea();
        this.searchTextArea.setEditable(false);
        this.searchTextArea.setLineWrap(true);

        this.searchScrollPane = new JScrollPane(this.searchTextArea);

        this.openBtn = new JButton("Open File");
        this.searchBtn = new JButton("Search");
        this.sortBtn = new JButton("Sort");
        
        this.searchTxt = new JTextField("", 10);
        this.searchLbl = new JLabel("Search:", JLabel.RIGHT);

        this.sortLbl = new JLabel("Sort:", JLabel.RIGHT);
        
        this.portSortValues = new String[] {
            "All ports"
        };
        this.sortPortComboBox = new JComboBox<>(this.portSortValues);

        this.searchComboBoxValues = new String[] {
            "By name",
            "By index",
            "By skill"
        };
        this.searchComboBox = new JComboBox<>(this.searchComboBoxValues);

        this.targetSortValues = new String[] {
            "Que",
            "Ships",
            "Docks",
            "Persons",
            "Jobs"
        }; 
        this.sortTargetComboBox = new JComboBox<>(this.targetSortValues);

        this.typeSortValues = new String[] {
            "By name",
            "By weight",
            "By length",
            "By width",
            "By draft"
        };
        this.sortTypeComboBox = new JComboBox<>(this.typeSortValues);

        this.optionsPanel.add(this.openBtn);         
        this.optionsPanel.add(this.searchLbl);  
        this.optionsPanel.add(this.searchTxt);   
        this.optionsPanel.add(this.searchComboBox);     
        this.optionsPanel.add(this.searchBtn);       
        this.optionsPanel.add(this.sortLbl);      
        this.optionsPanel.add(this.sortPortComboBox);   
        this.optionsPanel.add(this.sortTargetComboBox); 
        this.optionsPanel.add(this.sortTypeComboBox);   
        this.optionsPanel.add(this.sortBtn);        

        this.worldPanel.add(this.mainScrollPane);
        this.worldPanel.add(this.searchScrollPane);
        
        this.treePanel.setBorder(BorderFactory.createTitledBorder("Tree"));
        this.optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        this.mainScrollPane.setBorder(BorderFactory.createTitledBorder("Raw world"));
        this.searchScrollPane
            .setBorder(BorderFactory.createTitledBorder("Search/sort results"));
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));        
        
        this.mainPanel.add(this.optionsPanel, BorderLayout.NORTH);
        this.mainPanel.add(this.worldPanel, BorderLayout.CENTER);
        this.mainPanel.add(this.treePanel, BorderLayout.WEST);
        this.tabbPane.add("World", mainPanel);
        this.tabbPane.add("Jobs", jobScrollPane);
        
        this.sortTargetComboBox.addActionListener((ActionEvent e) -> {
            this.sortOptions();
        });

        this.openBtn.addActionListener((ActionEvent e) -> {
            this.openFile();
        });

        this.searchBtn.addActionListener((ActionEvent e) -> {
            try {
                this.search();
            } catch (IllegalAccessException ex) {
                Logger.getLogger(SeaPortProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.sortBtn.addActionListener((ActionEvent e) -> {
            this.sortWorld();
        });        

        this.mainFrame = new JFrame("SeaPort Program");
        this.mainFrame.setContentPane(this.tabbPane);
        this.mainFrame.setSize(1200, 800);
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
   
    private void openFile() {
        int fileSelection;
        FileReader fileReader;
        FileNameExtensionFilter filter;
        
        this.file = new JFileChooser(".");
        
        filter = new FileNameExtensionFilter("TEXT FILES", "TXT", "text");
        this.file.setFileFilter(filter);
        
        fileSelection = this.file.showOpenDialog(new JFrame());
        
        if(fileSelection == JFileChooser.APPROVE_OPTION) {
            try {
                fileReader = new FileReader(this.file.getSelectedFile());
                this.scan = new Scanner(fileReader);                     
                this.world = new World(this.scan, this.jobsPanel);
                treeScrollPane = new JScrollPane(createTree());
                treeScrollPane.setPreferredSize(new Dimension(300, 550));
                treePanel.add(treeScrollPane);
                treePanel.revalidate();
                treePanel.repaint();
                this.mainTextArea.setText("" + this.world.toString()); 
            } catch (IOException ex) {
                JOptionPane optionPane = new JOptionPane("Error", JOptionPane.ERROR_MESSAGE);    
                optionPane.createDialog("Error: File is missing or corrupt.");
            }
        } 
    }
      
        
    private void search() throws IllegalAccessException {
        if (this.world == null || this.scan == null) {
            this.errorMsg("Error: No world initialized.");
            return;
        }

        String resultsString, searchText;
        int dropdownSelection;

        resultsString = "";
        searchText = this.searchTxt.getText().trim();
        dropdownSelection = this.searchComboBox.getSelectedIndex();

        if (searchText.equals("")) {
            this.errorMsg("Error: No search criteria entered.");
            return;
        }

        switch(dropdownSelection) {
            case 0:
            case 1: 
                resultsString = this.result(dropdownSelection, searchText);
                this.status(resultsString, searchText);
                break;
            case 2: 
                for (SeaPort port : this.world.getPorts()) {
                    for (Person person : port.getPersons()) {
                        if (person.getSkill().equals(searchText)) {
                            resultsString += person.getName() + " (id #" + person.getIndex()
                                + ")\n";
                        }
                    }
                }
                this.status(resultsString, searchText);
                break;
            default:
                break;
        }
    }
    
    private String result(int index, String target) throws IllegalAccessException {
        Method getParam;
        String parameter, methodName;
        String resultsString = "";

        methodName = (index == 0) ? "getName" : "getIndex";

        try {
            getParam = Thing.class.getDeclaredMethod(methodName);

            for (Thing item : this.world.getThings()) {
                parameter = "" + getParam.invoke(item);

                if (parameter.equals(target)) {
                    resultsString += item.getName() + " " + item.getIndex() + " ("
                        + item.getClass().getSimpleName() + ")\n";
                }
            }
        } catch (
            NoSuchMethodException |  SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println("Error: " + ex);
        }
        return resultsString;
    }

    private void status(String resultsString, String target) {
        if (resultsString.equals("")) {
            JOptionPane.showMessageDialog(
                null,
                "Error: '" + target + "' not found." , "Error", JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null,
                resultsString,
                "Search results for '" + target + "'",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    private void errorMsg(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void sortOptions() {
        this.sortTypeComboBox.removeAllItems();
        this.sortTypeComboBox.addItem("By name");
        if (this.sortTargetComboBox.getSelectedIndex() == 0) { 
            this.sortTypeComboBox.addItem("By weight");
            this.sortTypeComboBox.addItem("By width");
            this.sortTypeComboBox.addItem("By length");
            this.sortTypeComboBox.addItem("By draft");
        }
    }
     
     @SuppressWarnings("unchecked")
    private void sortWorld() {

        if (this.world == null || this.scan == null) {
            this.errorMsg("Error: No world initialized.");
            return;
        }

        String sortPort, sortTarget, sortType, result, fieldMethodName, listMethodName;
        Method getField, getList;
        ArrayList<Thing> thingsList, gottenList;
        HashMap<String, String> typeMethodMap, targetMethodMap;

        typeMethodMap = new HashMap<String, String>() {{
            put("By name", "getIndex");
            put("By weight", "getWeight");
            put("By length", "getLength");
            put("By width", "getWidth");
            put("By draft", "getDraft");
        }};

        targetMethodMap = new HashMap<String, String>() {{
            put("Que", "getQue");
            put("Ships", "getShips");
            put("Docks", "getDocks");
            put("Persons", "getPersons");
            put("Jobs", "getShips"); 
        }};

        sortPort = this.sortPortComboBox.getSelectedItem().toString();
        sortTarget = this.sortTargetComboBox.getSelectedItem().toString();
        sortType = this.sortTypeComboBox.getSelectedItem().toString();
        fieldMethodName = typeMethodMap.get(sortType);
        listMethodName = targetMethodMap.get(sortTarget);
        result = "";
        thingsList = new ArrayList<>();

        try {
            getList = SeaPort.class.getDeclaredMethod(listMethodName);

            if (sortTarget.equals("Que") && !sortType.equals("By name")) {
                getField = Ship.class.getDeclaredMethod(fieldMethodName);
            } else {
                getField = Thing.class.getDeclaredMethod(fieldMethodName);
            }

            if (sortPort.equals("All ports")) {
                sortPort = sortPort.toLowerCase();
                for (SeaPort newPort : world.getPorts()) {
                    gottenList = (ArrayList<Thing>) getList.invoke(newPort);
                    thingsList.addAll(gottenList);
                }
            } else {
                for (SeaPort newPort : this.world.getPorts()) {
                    if (newPort.getName().equals(sortPort)) {
                        gottenList = (ArrayList<Thing>) getList.invoke(newPort);
                        thingsList.addAll(gottenList);
                    }
                }
            }

            if (sortTarget.equals("Jobs")) {
                ArrayList<Job> jobsList = new ArrayList<>();

                for (Iterator<Thing> iterator = thingsList.iterator(); iterator.hasNext();) {
                    Ship newShip = (Ship) iterator.next();
                    for (Job newJob : newShip.getJobs()) {
                        jobsList.add(newJob);
                    }
                }
                thingsList.clear(); 
                thingsList.addAll(jobsList); 
            }

            if (thingsList.isEmpty()) {
                result += "No results found.\n";
            } else {
                Collections.sort(thingsList, new ThingComparator(sortType));

                for (Thing newThing : thingsList) {
                    result += " " + newThing.getName() + " (" + getField.invoke(newThing) + ")\n";
                }
            }
        } catch (
            NoSuchMethodException |
            SecurityException |
            IllegalAccessException |
            IllegalArgumentException |
            InvocationTargetException ex
        ) {
            System.out.println("Error: " + ex);
        }

        this.searchTextArea.append("Sort type: " + sortTarget + " "
            + sortType.toLowerCase() + " in " + sortPort + "\n" + result + "\n");
    }
    
    private JTree createTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("The World");
        JTree tree = new JTree(top);
        DefaultMutableTreeNode portTotal
                = new DefaultMutableTreeNode("Ports");
        top.add(portTotal);
        for (SeaPort allPorts : world.getPorts()) {
            DefaultMutableTreeNode port
                    = new DefaultMutableTreeNode(allPorts.getName());
            portTotal.add(port);
            DefaultMutableTreeNode people
                    = new DefaultMutableTreeNode("Persons");
            port.add(people);
            for (Person person : allPorts.getPersons()) {
                DefaultMutableTreeNode individual
                        = new DefaultMutableTreeNode(person.getName());
                people.add(individual);
            }
            DefaultMutableTreeNode docks
                    = new DefaultMutableTreeNode("Docks");
            port.add(docks);
            for (Dock dock : allPorts.getDocks()) {
                DefaultMutableTreeNode individualDock
                        = new DefaultMutableTreeNode(dock.getName());
                docks.add(individualDock);
                if (dock.getShip() != null) {
                    DefaultMutableTreeNode dockedShip
                            = new DefaultMutableTreeNode(dock.getShip().getName());
                    individualDock.add(dockedShip);
                    for (Job job : dock.getShip().getJobs()) {
                        DefaultMutableTreeNode dockedShipJob
                                = new DefaultMutableTreeNode(job.getName());
                        dockedShip.add(dockedShipJob); 
                    }  
                } 
            }
            DefaultMutableTreeNode que
                    = new DefaultMutableTreeNode("Queue");
            port.add(que);
            for (Ship ship : allPorts.getQue()) {
                DefaultMutableTreeNode quedShip
                        = new DefaultMutableTreeNode(ship.getName());
                que.add(quedShip);
                for (Job job : ship.getJobs()) {
                    DefaultMutableTreeNode quedShipJob
                            = new DefaultMutableTreeNode(job.getName());
                    quedShip.add(quedShipJob);
                } 
            } 
        
        }
        return tree;
    }
    
    public static void main(String[] args) {
        SeaPortProject world = new SeaPortProject();
        world.buildGUI();
    }
    
    
}
