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
 * @version 2011.08.10
 * 
 * @author  Willen O. Leal
 * @version 2018.05.11
 */
import java.util.ArrayList;
public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private ArrayList<Room> roomList = new ArrayList<Room>();
    private int backCommandCount = 0;
    private ArrayList<String[]> itemsCollected = new ArrayList<String[]>();
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
       String[] watchTowerItem = new String[]{"bolt cutter", "15lbs"};
       String[] officeItem = new String[]{"key", "2Ibs"};
       String[] outsideTest = new String[]{"test", "0Ibs"};
       
       Room cell, solitaire, corridor1, corridor2, corridor3,office,
       commonArea, prisonYard, watchTower, kitchen, storageArea, outside;
       
       outside = new Room("outside! You have scaped!", outsideTest);
       cell = new Room("in your cell, there might be a way out...");
       solitaire = new Room("in the solitaire, kinda like your cell...");
       corridor1 = new Room("in a corridor. Where does it lead to?");
       corridor2 = new Room("in a corridor. Where does it lead to?");
       corridor3 = new Room("in a corridor. Where does it lead to?");
       office = new Room("in the sheriff's office! Quick, he might be back soon", officeItem);
       commonArea = new Room("in the common lobby");
       prisonYard = new Room("in the exercise yard. So close from the outside! If you could only cut through the fence!");
       watchTower = new Room("in the watch tower! The guards are asleep, don't wake them up!", watchTowerItem);
       kitchen = new Room("in the kitchen. How about a snack?");
       storageArea = new Room("in the storage area.");
       
       cell.setExit("north", solitaire);
       cell.setExit("south", commonArea);
       
       solitaire.setExit("west", corridor1);
       solitaire.setExit("south", cell);
       
       corridor1.setExit("east", solitaire);
       corridor1.setExit("south", office);
       
       office.setExit("north", corridor1);
       office.setExit("south", corridor2);
       
       corridor2.setExit("east", commonArea);
       corridor2.setExit("north", office);
       
       commonArea.setExit("north", cell);
       commonArea.setExit("south", prisonYard);
       commonArea.setExit("east", corridor3);
       commonArea.setExit("west", corridor2);
       
       corridor3.setExit("east", kitchen);
       corridor3.setExit("west", commonArea);
       
       kitchen.setExit("west", corridor3);
       kitchen.setExit("north", storageArea);
       
       prisonYard.setExit("north", commonArea);
       prisonYard.setExit("south", outside);
       prisonYard.setExit("west", watchTower);
       
       outside.setExit("north", prisonYard);
       
       watchTower.setExit("east", prisonYard);
       
       currentRoom = cell;
    }
    
    private void look()
    {
      System.out.println(currentRoom.getLongDescription());
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
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                backCommandCount = 0;
                goRoom(command);
                break;
                
            case LOOK:
                look();
                break;
                
            case GET:
                getRoomItem();
                break;
                
            case EAT:
                System.out.println("You just ate a snack from your pocket!");
                break;
                
            case BACK:
                backCommandCount++;
                goBack();
                break;
            
            case QUIT:
                wantToQuit = quit(command);
                break;
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
        System.out.println("around at the prison complex.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    private void goBack()
    {
        if (backCommandCount == 2)
        {
            currentRoom = roomList.get(roomList.size() - 2);
            System.out.println(currentRoom.getLongDescription());
        }
        else if (backCommandCount == 3)
        {
            currentRoom = roomList.get(roomList.size() - 3);
            System.out.println(currentRoom.getLongDescription());
        }
        
        else if (backCommandCount == 1)
        {
            currentRoom = roomList.get(roomList.size() - 1);
            System.out.println(currentRoom.getLongDescription());
        }
        
        else
        {
            System.out.println("You cannot return more than 3 times!");
            System.out.println(currentRoom.getLongDescription());
        }
            
    }
    
    public void getRoomItem()
    {
        if(currentRoom.getHasItem())
        {
           if (currentRoom.getItemName() != "test")
           {
            itemsCollected.add(currentRoom.getItem());
           
           }
        }
        
        else
        {
           System.out.println("This room has no item!");
        }
    
    
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        roomList.add(currentRoom);
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) 
        {
            System.out.println("There is no exit that way!");
            return ;
        }
      
        
           
      
        
        if (nextRoom.getItemName() == "bolt cutter" && itemsCollected.size() == 0)
        {
              System.out.println("You need the key to enter the watchTower!");
        }
        
        
        else if (nextRoom.getItemName() == "test" && (itemsCollected.size() == 0 || itemsCollected.size() == 1))
        {
             System.out.println("You need something to cut through the fence!");
        }
            
       
        else 
        {       
            currentRoom = nextRoom;
            if(currentRoom.getHasItem())
            {
                 if (currentRoom.getItemName() != "test")
                 {
                        System.out.println(currentRoom.getLongDescription());
                        currentRoom.printItemDescription(); 
                 }
                 else if (currentRoom.getItemName() == "test")
                 {
                       System.out.println(currentRoom.getLongDescription());
                 }
            }
            else
            {
              System.out.println(currentRoom.getLongDescription()); 
            
            }
        
        }
       
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
