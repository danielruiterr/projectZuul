import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

    public class Game {
    private Player player;
    private Parser parser;
    private Room currentRoom;
    private HashMap<String, String> itemList; 
    private Stack<Room> previousRooms; 
    private ArrayList<Item> playerInventory;
    private int timeLimit;
    private static int numberOfMoves;
    private static int limitOfMoves;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        player = new Player(10);
        createRooms();
        parser = new Parser();
        itemList = new HashMap<String, String>();
        previousRooms = new Stack<Room>(); 
        playerInventory = new ArrayList<>();
        timeLimit = 20;
    }
    /**
     * Defines all items with their descripton and their weight and adds them to the list.
     * @author Didier & Daniel
     */    
    public String setRandItems(){
        
        itemList.put("subscribe forms, with this form you can subscribe to the school", "2 grams");
        itemList.put("unsubscribe forms, with this form you can exit the school", "2 grams");
        itemList.put("Bottle of whisky, to handle the stress", "2 grams");
        itemList.put("cup of coffee, for school energy", "14 grams");
        itemList.put("key, master school key", "9 grams");
        itemList.put("Blikje energy, voor alle school problemen", "35 grams");
        itemList.put("key2, to go to the dungeon", "4 grams");
        itemList.put("Patatje mayo, for the hungry student", "21 grams");
        
        Object[] values = itemList.keySet().toArray();
        Object key = values[new Random().nextInt(values.length)];
        
        Random rand = new Random(); 
        return key+"|"+itemList.get(key);
       
        /*for(int i = 0; i < rand.nextInt(3); i++ ){
          HashMap<Object, String> randItems = new HashMap<Object, String>(); 
          randItems.put(key,itemList.get(key));
          return randItems;
        
    }
    */
}

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, cellar, basement, dungeon;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        cellar = new Room("The cellar");
        basement = new Room("A good place to solve a mystery or to find a key");
        dungeon = new Room("First you have to find the key this happens random :P");
        
        // initialise room exits
        outside.setExits("east", theater);
        outside.setExits("south", lab);
        outside.setExits("west", pub);
        
        theater.setExits("west", outside);
        
        pub.setExits("east", outside);
        pub.setExits("west", basement);
        
        lab.setExits("north", outside);
        lab.setExits("east", office);
        
        office.setExits("west", lab);
        office.setExits("down", cellar);
        
        basement.setExits("east", pub);
        basement.setExits("down", dungeon);
        
        dungeon.setExits("up", basement);
        
        
        cellar.setExits("up", office);
        //cellar.setItems(setRandItems());
        
        outside.setItems("subscribeforms", 2);
        outside.setItems("sandwich", 20);
        outside.setItems("Blikje energy", 3);
        
        basement.setItems("key", 1);
        
       
        //currentRoom = outside;  // start game outside
        player.setCurrentRoom(outside);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished && timeLimit != 0) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        if(timeLimit == 0)
    {
        finished = true;
        System.out.println("You have run out of time! You lose!\n" + "Restart to play again.");
    }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I have no idea what you mean :'(");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("look")) {
            look();
        }
        else if (commandWord.equals("back")) {
            back();
        }
        else if (commandWord.equals("pickup")){
            pickUpItem(command);
        }
        else if (commandWord.equals("drop")){
            dropItem(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.showCommands());
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);
        

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            //previousRooms.push(currentRoom);
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
            System.out.println();
        }
    }
    
    private void dropItem(Command command){
        if(!command.hasSecondWord()){
            System.out.println("drop what?");
            return;
        }
        String temp = command.getSecondWord();
        
        Item rmFromPlayer = player.dropInventory(temp);
        
        if(rmFromPlayer != null){
            player.getCurrentRoom().setItems(rmFromPlayer.getIntemDescription(), rmFromPlayer.getItemWieght());
            System.out.println("Item " + temp + " dropped");
        }
        
    }
   
    private void pickUpItem(Command command){
        if(!command.hasSecondWord()){
            System.out.println("pickup what?");
            return;
        }
        
        String temp = command.getSecondWord(); 
        Item rmFromRoom = player.getCurrentRoom().removeItem(temp);
     
        if(rmFromRoom != null){
            if(rmFromRoom.getItemWieght() < player.restWeight()){
                player.setItem(rmFromRoom.getIntemDescription(), rmFromRoom.getItemWieght());
                System.out.println("Item " + temp + " picked up." + player.returnInventory());
             
            }
            else{
                player.getCurrentRoom().setItems(rmFromRoom.getIntemDescription(), rmFromRoom.getItemWieght());
                System.out.println("This item is to heavy!");
            }
        }
    }
    
    private void back(){
        if (previousRooms.empty()){
            System.out.println("You are at the beginning of the game");
        }else{
        //currentRoom = previousRooms.pop();
        System.out.println(player.getCurrentRoom().getLongDescription());
        }
    }
    
    private void look(){
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
