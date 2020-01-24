import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Write a description of class player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    private HashMap<String, Item> playerItems; 
    private Room currentRoom;
    private int maxWeight;
    private HashMap<String, String> itemDescription;
    
    /**
     * Constructor for objects of class player
     */
    public Player(int maxWeight)
    {
        playerItems = new HashMap<String, Item>();
        itemDescription = new HashMap<String, String>(); 
        currentRoom = new Room("starting room");
        this.maxWeight = maxWeight;
    }

    public Room getCurrentRoom(){
        return currentRoom; 
    }
    
    public int restWeight(){
        int total = 0;
        for(String key : playerItems.keySet()){
            total += playerItems.get(key).getItemWieght();
        }
        if (playerItems != null){
                return maxWeight - total;
            }
                else{
                  return maxWeight;
                }
}
    

    public String returnInventory(){
        String returnString = " Items in inventory:";
        for(String key : playerItems.keySet())
            returnString += " " + key;
            return returnString; 
    }
    
    public void setCurrentRoom(Room room){
        currentRoom = room; 
    }
    
    public void setItem(String name, int weight){
        Item temp = new Item(name, weight); 
        playerItems.put(name, temp);
        //itemDescription.put(name, description);
    
    }
    
    public String printItemInformation(){
        int total = 0; 
        String itemInfo = " Items in inventory: ";
        String t1 = ". With weight: ";
       for(String key : playerItems.keySet()){
          total += playerItems.get(key).getItemWieght();
          itemInfo += ", " + playerItems.get(key).getIntemDescription();
        }
        return itemInfo + " " + t1 + total;
    }
    
    
    public Item dropInventory(String name)
    {
        for(String item : playerItems.keySet()) {
            if (item.equals(name))
            {
                Item temp = playerItems.get(name);
                playerItems.remove(name);
                return temp;
            }   
        }
        System.out.println("We haven't got one!");
        return null;

    }
    
    
}
