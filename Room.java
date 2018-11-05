import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;  // stores exits of this room.
    private String[] item;    // stores item and its description [0,1]
    private boolean hasItem;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        hasItem = false;
    }
    
    public Room(String description, String[] descriptionNWeight)
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        hasItem = true;
        item = Arrays.copyOf(descriptionNWeight, descriptionNWeight.length);
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    public boolean getHasItem()
    {
        return hasItem;
    }
    
    public String[] getItem()
    {
        return item;
    }
    
    public String getItemName()
    {
        if (item != null)
        {
            return item[0];
        }
        else 
        {
            return "0";
        }
    }
    
    public void printItemDescription()
    {
        System.out.println("Look an item!   Name; " + item[0] + "\n" + "\t\tWeight: " + item[1]);  
    }
    
    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
}

