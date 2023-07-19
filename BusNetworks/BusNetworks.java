/* Code for COMP103 - 2021T2, Assignment 6
 * Name: Annie Cho
 * Username: choanni
 * ID: 300575457
 */

import ecs100.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BusNetworks {

    /** Map of towns, indexed by their names */
    private Map<String,Town> busNetwork = new HashMap<String,Town>();
    boolean challenge = false;

    /** CORE
     * Loads a network of towns from a file.
     * Constructs a Set of Town objects in the busNetwork field
     * Each town has a name and a set of neighbouring towns
     * First line of file contains the names of all the towns.
     * Remaining lines have pairs of names of towns that are connected.
     */
    public void loadNetwork(String filename) {
        try {
            busNetwork.clear();
            UI.clearText();
            challenge = false;
            List<String> lines = Files.readAllLines(Path.of(filename));
            String firstLine = lines.remove(0);
            Scanner sc = new Scanner(firstLine);
            while(sc.hasNext()){
                String name = sc.next();
                busNetwork.put(name, new Town(name));
            }
            // for every other line which shows the bus connections
            for(String line: lines){
                sc = new Scanner(line);
                String name1 = sc.next();
                String name2 = sc.next();
                Town town1 = busNetwork.get(name1);
                Town town2 = busNetwork.get(name2);
                town1.addNeighbour(town2);
                town2.addNeighbour(town1);
            }

            UI.println("Loaded " + busNetwork.size() + " towns:");

        } catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }

    /**  CORE
     * Print all the towns and their neighbours:
     * Each line starts with the name of the town, followed by
     *  the names of all its immediate neighbours,
     */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        for(Town t: busNetwork.values()){
            UI.println();
            UI.print(t.getName() + "-> ");
            Set<Town> neighbours = t.getNeighbours();
            for(Town n: neighbours){ UI.print(n.getName() + " "); }
        }
    }

    /** COMPLETION
     * Return a set of all the nodes that are connected to the given node.
     * Traverse the network from this node in the standard way, using a
     * visited set, and then return the visited set
     */
    public HashSet<Town> findAllConnected(Town town, HashSet<Town> visited) {      
        visited.add(town);
        for(Town n: town.getNeighbours()){
            if(!visited.contains(n)){
                findAllConnected(n, visited);
            }
        }
        return visited;
    }
    
    

    /**  COMPLETION
     * Print all the towns that are reachable through the network from
     * the town with the given name.
     * Note, do not include the town itself in the list.
     */
    public void printReachable(String name){
        Town town = busNetwork.get(name);
        if (town==null){
            UI.println(name+" is not a recognised town");
        }
        else {
            UI.println("\nFrom "+town.getName()+" you can get to:");
            HashSet<Town> visited = new HashSet<Town>();
            Set<Town> connected = findAllConnected(town, visited);
            connected.remove(town);
            for(Town t: connected){ 
                UI.println(t.getName()); 
            }
        }
    }

    /**  COMPLETION
     * Print all the connected sets of towns in the busNetwork
     * Each line of the output should be the names of the towns in a connected set
     * Works through busNetwork, using findAllConnected on each town that hasn't
     * yet been printed out.
     */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        Set<Town> visited = new HashSet<Town>();
        for(Town t: busNetwork.values()){
            if(!visited.contains(t)){
                UI.print("Group " + groupNum + ": ");
                Set<Town> group = findAllConnected(t, new HashSet<Town>());
                for(Town t2: group){ 
                    visited.add(t2);
                    UI.print(t2.getName() + " ");
                }
                UI.println();
                groupNum++;
            }
        }
    }
    
    /**
     * Loads in the coord file for challenge
     */
    public void loadCoord(){
        try{
            busNetwork.clear();
            UI.clearText();
            String filename = UIFileChooser.open("Filename: ");
            List<String> allLines = Files.readAllLines(Path.of(filename));
            
            UI.println("WHATS THE PROBLEM.");
            
            String firstLine = allLines.remove(0);
            // all that's left in the list are the towns - same size
            Scanner sc = new Scanner(firstLine);
            
            int numTowns = sc.nextInt();
            int lineCount = 0;
            challenge = true;

            for(int i=0; i<numTowns; i++){
                // finding the long/lat
                Scanner scan = new Scanner(allLines.get(i)); 
                String name = scan.next(); 
                double lat = scan.nextDouble();
                double lon = scan.nextDouble();  
                busNetwork.put(name, new Town(name)); 
                busNetwork.get(name).setLat(lat);
                busNetwork.get(name).setLong(lon);
                lineCount++;
            }

            while(lineCount < allLines.size()){
                Scanner scan = new Scanner(allLines.get(lineCount)); 
                String name1 = scan.next();
                String name2 = scan.next(); 
                // adds eachother as direct neighbours
                busNetwork.get(name1).addNeighbour(busNetwork.get(name2));
                busNetwork.get(name2).addNeighbour(busNetwork.get(name1));
                lineCount++; 
            }
            
            UI.println("Loaded " + busNetwork.size() + " towns:");
        }catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }
    
    /**
     * Prints with the lat/long values
     */
    public void printCoord(){
        if(challenge){
            UI.println("The current network: \n====================");
            for(Town t: busNetwork.values()){
                UI.print(t.getName() + " -lat: " + t.getLat() + " -long: " + t.getLong() + " -> ");
                for(Town n: t.getNeighbours()){ UI.print(n.getName() + " "); }
                UI.println();
            }
        }
    }
    

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", ()->{loadNetwork(UIFileChooser.open());});
        UI.addButton("Print Network", this::printNetwork);
        UI.addTextField("Reachable from", this::printReachable);
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        
        UI.addButton("Load(Coord)", this::loadCoord);
        UI.addButton("Print(Coord)", this::printCoord);
        
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        BusNetworks bnw = new BusNetworks();
        bnw.setupGUI();
    }

}
