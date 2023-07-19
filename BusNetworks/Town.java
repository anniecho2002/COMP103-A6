/* Code for COMP103 - 2021T2, Assignment 6
 * Name: Annie Cho
 * Username: choanni
 * ID: 300575457
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ecs100.*;

public class Town {

    private String name;
    private Set<Town> neighbours = new HashSet<Town>();
    private boolean visited = false;
    private double latitude, longitude;

    public Town(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Set<Town> getNeighbours() {
        return Collections.unmodifiableSet(neighbours);
    }

    public void addNeighbour(Town node) {
        neighbours.add(node);
    }

    public String toString(){
        return name+" ("+neighbours.size()+" connections)";
    }
    
    /**
     * Getting and setting latitude/longitude
     */
    public void setLat(double lat){ latitude = lat; }
    public void setLong(double lon){ longitude = lon; }
    public double getLat(){ return latitude; }
    public double getLong(){ return longitude; }
    
    

}
