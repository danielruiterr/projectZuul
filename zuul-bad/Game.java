import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.Scanner;
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
    * Starts the game
    * @author Daniel
    */
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

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
        numberOfMoves = 0;
    }
    /**
     * Defines all items with their descripton and their weight and adds them to the list.
     * @author Didier & Daniel
     */    
    public String setRandItems(){
        
        itemList.put("subscribe_forms, with this form you can subscribe to the school", "2 grams");
        itemList.put("unsubscribe_forms, with this form you can exit the school", "2 grams");
        itemList.put("Bottle_of_whisky, to handle the stress", "2 grams");
        itemList.put("cup_of_coffee, for school energy", "14 grams");
        itemList.put("key, master school key", "9 grams");
        itemList.put("Blikje_energy, voor alle school problemen", "35 grams");
        itemList.put("key2, to go to the dungeon", "4 grams");
        itemList.put("Patatje_mayo, for the hungry student", "21 grams");
        
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
     * @author Didier & Daniel
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the NewSchool Zuul!");
        System.out.println("NewSchool Zuul is a new, better and more innovative game");
        System.out.println("Based on the Old and original World of Zuul");
        
        chooseLevel();
        
        System.out.println("Type 'help' if you need help.");
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println();
    }
    /**
     *  Easy is for beginners 
     *  Medium is for intermidiate players who have knowledge about the game and/or have played before
     *  Hard is the make no failures way to go
     * @author Daniel
     */
    private void chooseLevel()
    {
        // Choosing a level 
        Scanner reader = new Scanner(System.in);
        System.out.println("Please choose a level : Easy for beginners(0) - Medium for intermidiate (1) - Hard for pros (2)");
        // Find the chosen level and alter the number of moves according to the chosen one
        try {
            switch (reader.nextInt()) {
            case 0:
                limitOfMoves = 20;
                System.out.println("You've chosen the easiest way to win! - Number of moves : " + limitOfMoves);
                break;
            case 1:
                limitOfMoves = 16;
                System.out.println("You've chosen the medium level, have fun! - Number of moves : " + limitOfMoves);
                break;
            case 2:
                limitOfMoves = 14;
                System.out.println("You've chosen the hard level, best of luck you need it!  - Number of moves : " + limitOfMoves);
                break;
            default:
                limitOfMoves = 20;
                System.out.println("Unkown command - The Default level : Easy - Number of moves : " + limitOfMoves);
                break;
            }
        } catch(Exception e){
            limitOfMoves = 20;
            System.out.println("Unkown command - Default level : Easy - Number of moves : " + limitOfMoves);
        }
    }

    /**
     * Counting the current move of the player
     * returns a false statement if the player used too many moves
     * @author Daniel
     */
    public static boolean countMove(){
        // Count a move
        numberOfMoves++;

        // Give some informations about the number of moves left and made
        if (numberOfMoves < limitOfMoves) {
            System.out.println("You've currently done " + numberOfMoves+ " moves");
            System.out.println("Moves left : " + (limitOfMoves - numberOfMoves));
            return false;
            // End the game if the maximimum number of moves is reached
        } else {
            System.out.println("You have reached the maximum number of moves");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("███▀▀▀██┼███▀▀▀███┼███▀█▄█▀███┼██▀▀▀");
            System.out.println("██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼█┼┼┼██┼██┼┼┼");
            System.out.println("██┼┼┼▄▄▄┼██▄▄▄▄▄██┼██┼┼┼▀┼┼┼██┼██▀▀▀");
            System.out.println("██┼┼┼┼██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██┼┼┼");
            System.out.println("███▄▄▄██┼██┼┼┼┼┼██┼██┼┼┼┼┼┼┼██┼██▄▄▄");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("███▀▀▀███┼▀███┼┼██▀┼██▀▀▀┼██▀▀▀▀██▄┼");
            System.out.println("██┼┼┼┼┼██┼┼┼██┼┼██┼┼██┼┼┼┼██┼┼┼┼┼██┼");
            System.out.println("██┼┼┼┼┼██┼┼┼██┼┼██┼┼██▀▀▀┼██▄▄▄▄▄▀▀┼");
            System.out.println("██┼┼┼┼┼██┼┼┼██┼┼█▀┼┼██┼┼┼┼██┼┼┼┼┼██┼");
            System.out.println("███▄▄▄███┼┼┼─▀█▀┼┼─┼██▄▄▄┼██┼┼┼┼┼██▄");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼██┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼██┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼████▄┼┼┼▄▄▄▄▄▄▄┼┼┼▄████┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼▀▀█▄█████████▄█▀▀┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼█████████████┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼██▀▀▀███▀▀▀██┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼██┼┼┼███┼┼┼██┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼█████▀▄▀█████┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼███████████┼┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼▄▄▄██┼┼█▀█▀█┼┼██▄▄▄┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼▀▀██┼┼┼┼┼┼┼┼┼┼┼██▀▀┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼▀▀┼┼┼┼┼┼┼┼┼┼┼▀▀┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println("┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼┼");
            System.out.println();
            System.out.println();
            return true;
        }
    }
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     * @author Didier & Daniel
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
    
    /**
     * return the numberOfMoves
     * @author Daniel
     */
    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    /**
     * return the limitOfMoves
     * @author Daniel
     */
    public int getLimitOfMoves() {
        return limitOfMoves;
    }
    
    /**
     * set param of Limit of Moves
     * @author Daniel
     */
    public void setLimitOfMoves(int lom) {
        limitOfMoves = lom;
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
     * @author Didier & Daniel
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Wait what? Where do you want to go to?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);
        

        if (nextRoom == null) {
            System.out.println("There is no door here, this just cost you a move!");
            //adding a count to each move and returning how many left
            System.out.println(Game.countMove());
        }
        else {
            //previousRooms.push(currentRoom);
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
            System.out.println();
            //adding a count to each move and returning how many left
            System.out.println(Game.countMove());
        }
    }
    
    private void dropItem(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Whoa what do you wanna drop, might wanna check again?");
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
            System.out.println("Pickup what exactly?");
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
