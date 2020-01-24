import java.util.Set;
import java.util.HashMap;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class Room 
{
    public String description;
    private HashMap<String, Room> exits;
    private HashMap<String, Item> items; 

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
    }
    
    public Room getExit(String direction){
        return exits.get(direction); 
       }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    public void setExits(String direction, Room neighbor) 
    {
       exits.put(direction, neighbor);
    }
    
    public void setItems(String name, int weight){
        Item item = new Item(name, weight);
        items.put(name, item);
    }
    
    public Item removeItem(String name){
        for(String key : items.keySet()){
              if(key.equals(name)){
              Item temp = items.get(name);
              items.remove(name);
              return temp; 
            }
        }
        System.out.println("Item is not in the room");
            return null; 
    }
    
    
    private String getItemString()
    {
        String returnString = "Items:";
        Set<String> keys = items.keySet();
        for(String item : keys) {
            returnString += " " + item;
        }
        return returnString;
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }
    
    public String getLongDescription(){
        return "you are "+ description + ".\n " + getExitString() + ".\n "+ getItemString();
    }
    
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
}


