
/**
 * Defines the all the items
 *
 * @author Didier & Daniël
 * @version 26-12-2019
 */
public class Item
{
   private String description;
   private int wieght; 
   

    /**
     * Constructor for objects of class Item
     */
    public Item(String description, int wieght)
    {
        this.description = description; 
        this.wieght = wieght; 
    }

    public String getIntemDescription(){
        return description; 
    }
    
    public int getItemWieght(){
        return wieght; 
    }
   
}
