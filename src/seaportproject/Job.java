package seaportproject;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Job extends Thing implements Runnable {

    private double duration;
    private ArrayList<String> requirements;

    private enum Status {
        RUNNING, SUSPENDED, WAITING, DONE, CANCELLED
    }

    private final JPanel parentPanel;
    private final JProgressBar progressBar;
    private final Ship parentShip;
    private JButton suspendBtn, cancelBtn;
    private Thing shipsParent;
    private SeaPort parentPort = null;
    private Dock parentDock = null;
    private boolean goFlag = false;
    private boolean noKillFlag = true;
    private boolean isComplete = false;
    public Job(HashMap<Integer, Ship> shipsMap, HashMap<Integer, Dock> docksMap, HashMap<Integer, SeaPort> portsMap, JPanel parentPanel, Scanner sc) {
        super(sc);
        if (sc.hasNextDouble()) {
            this.duration = sc.nextDouble();
        }
        requirements = new ArrayList<>();
        
        while (sc.hasNext()) {
            String line = sc.next();
            requirements.add(line);
        }
        this.parentPanel = parentPanel;

        JLabel jobLabel = new JLabel(this.toString());
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        suspendBtn = new JButton("Suspend");
        cancelBtn = new JButton("Cancel");

        this.parentPanel.add(jobLabel);
        this.parentPanel.add(progressBar);
        this.parentPanel.add(suspendBtn);
        this.parentPanel.add(cancelBtn);

        suspendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleGoFlag();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelJob();
            }
        });
        
        this.parentShip = shipsMap.get(this.getParent());
        if (docksMap.get(parentShip.getParent()) != null) {
            this.shipsParent = docksMap.get(parentShip.getParent());
            this.parentDock = docksMap.get(parentShip.getParent());
            this.parentPort = portsMap.get(parentDock.getParent());
            this.goFlag = true;
        } else {
            this.shipsParent = portsMap.get(parentShip.getParent());
            this.parentPort = portsMap.get(parentShip.getParent());
        } 

        new Thread(this, this.getName()).start();
    } 

    public double getDuration() {
        return duration;
    } 
    public ArrayList<String> getRequirements() {
        return requirements;
    } 
    public void toggleGoFlag() {
        goFlag = !goFlag;
    } 
    public void cancelJob() {
        noKillFlag = false;
    } 
    
    private void showStatus(Status status) {
        this.progressBar.setOpaque(true);
        switch (status) {
            case RUNNING:
                progressBar.setBackground(Color.BLUE);
                progressBar.setString("Running");
                this.suspendBtn.setText("Suspend");
                break;
            case SUSPENDED:
                progressBar.setBackground(Color.YELLOW);
                progressBar.setString("Suspended");
                this.suspendBtn.setText("Continue");
                break;
            case WAITING:
                progressBar.setBackground(Color.ORANGE);
                progressBar.setString("Waiting");
                break;
            case DONE:
                progressBar.setBackground(Color.GREEN);
                progressBar.setString("Done");
                break;
            case CANCELLED:
                progressBar.setBackground(Color.RED);
                progressBar.setString("Cancelled");
                break;
        }
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 100 * (long) duration;
        double timeNeeded = stopTime - time;
        
        synchronized (shipsParent) {
            while (!goFlag) {
                showStatus(Status.WAITING);
                try {
                    shipsParent.wait();
                } catch (InterruptedException e) {
                }
            }
        }

        while (time < stopTime && noKillFlag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            if (goFlag) {
                showStatus(Status.RUNNING);
                time += 100;
                progressBar.setValue((int) (((time - startTime) / timeNeeded) * 100));
            } else {
                showStatus(Status.SUSPENDED);
            }
        }
        
        if(!noKillFlag) {
            showStatus(Status.CANCELLED);
            isComplete = true;
        } else {
            progressBar.setValue(100);
            showStatus(Status.DONE);
            isComplete = true;
        } 
        try {
            for (Dock dock : parentPort.getDocks()) {
                if (dock.getShip() != null) {
                    if (dock.getShip().getJobs().isEmpty() || allJobsFinished()) {
                        dockNextShip();
                        shipsParent.notifyAll();
                    }   
                }
            }
        } catch (Exception e) {              
        }
    }
    
    
    private boolean allJobsFinished() {
        for (Job job : parentShip.getJobs()) {
            if (!job.isComplete) {
                return false;
            }
        }
        return true;
    }

    private void dockNextShip() {
            parentDock.setShip(null);
            if (!parentPort.getQue().isEmpty()) {
                for (Dock dock : parentPort.getDocks()) {
                    if (dock.getShip() == null) {
                        if (!parentPort.getQue().isEmpty()) {
                            while (parentPort.getQue().get(0).getJobs().isEmpty()) {
                                parentPort.getQue().remove(0);
                            }
                            if (!parentPort.getQue().isEmpty()) {
                                dock.setShip(parentPort.getQue().get(0));
                                parentPort.getQue().remove(0);
                            }
                            for (Job job : dock.getShip().getJobs()) {
                                job.parentDock = dock;
                                job.goFlag = true;
                                job.run();
                            }
                        }
                    }
                }
            }
    }
    

    @Override
    public String toString() {
        String output = "Job " + super.toString();
        return output;
    } 

} 