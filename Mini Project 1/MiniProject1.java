/****
    
	The intention of this program is to make a Pacman program, tentatively 
    refferred to as "PackDude."
    It displays a 2D grid using standard arrays, the dimensions of which are
    set by the user.
    PacDude starts in the top left corner facing left.
    all grid cells have "." in them except the cell with PacDude.
    15% of the grid should randomly contain cookies (rounded down), except for the spot
    PacDude starts on.
    
    @Author: JB at jamdatajam
    
****/

//import all these header files
import java.util.Scanner;
import java.util.Arrays;

public class MiniProject1
{
    public static void main ( String [] args )
    {
        //Initialize Scanner/variables
        Scanner input = new Scanner ( System.in );
        int heightSelection = 0;
        int widthSelection = 0;
        
        //introduce the game, ask for height and width info
        System.out.println("\n" + " Hello and welcome to PackDude!");
        System.out.println(" The game that is similar to another famous game");
        System.out.println(" from the 80s, but is just enough different to");
        System.out.println(" avoid legal repercussions!" +"\n");
        System.out.println(" How big of a field would you like to play in?");
        System.out.print(" Height? "); //asks for height
        heightSelection = input.nextInt(); //inputs map height
        System.out.print(" How about width? "); //asks for width
        widthSelection = input.nextInt(); //inputs map width
        System.out.println("\n" + " Rad!!! Let's do this!" + "\n\n");
        
        shellState( heightSelection, widthSelection ); //calls shell state
    }
    
    /****

    This method takes the entered height and width and uses them to assemble
    the initial map; it also calls all the other fucntions as commands are
    issued.  This method also contains the while loop that maintains the
    game state.

    Pre-con: height
             width
             
    Post-con: calls everything
              outputs the entire map, cookies, PackDude, etc.
              contains the exit printouts as well
    
    ****/
    public static void shellState (int height, int width)
    {//this is the loop for the entire game state
     //this calls all the functions and organizes everything
        Scanner input = new Scanner ( System.in ); //new Scanner
        
        //make the map matrix
        char [][] mapMatrix = new char[height][width]; //sets the map matrix
        mapMatrix = makeMapMatrix(height, width); //makes starter map

        int [] posXY = {0, 0}; //sets the position on the x axis and the y-axis
        int [] newPosXY = {0, 0}; //(x,y)
        int cookieCounter = 0; //this adds up cookies
        char direction = 62; //starting direction for dood >
        char newDirection = 62; //set to default as the starting direction
        mapMatrix = cookie(height, width, mapMatrix); //cookie matrix positions
        mapMatrix[posXY[1]][posXY[0]] = direction; //[y][x]top left corner
        mapPrint (mapMatrix, height, width, cookieCounter); //prints full map
        boolean continuation = true; //if true, keep playing, else quit
        int command = 0; //this is for the switch choices
        int moveTotal = 0; //Starting number of moves taken
        double avgSteps = 0.0; //averages the number of steps

        //dump it into loop
        while ( continuation == true )
        {//keeps the map and PackDude game components running until quit

            System.out.print("\n" + " Command: ");
            command = input.nextInt(); //takes keyboard input as ints
            
            switch (command)
            {//main switchboard for game commands
                case 0: menu();//calls the menu method, then prints the map
                        mapPrint (mapMatrix, height, width, cookieCounter);
                        break;
                case 1: newDirection = turnCCW( direction );//turns CCW
                        //set the PackDude direction
                        mapMatrix[posXY[1]][posXY[0]] = newDirection;
                        direction = newDirection;
                        //map the changes
                        mapPrint (mapMatrix, height, width, cookieCounter);
                        break;
                case 2: newDirection = turnCW( direction );//turns CW
                        //set the PackDude direction
                        mapMatrix[posXY[1]][posXY[0]] = newDirection;
                        direction = newDirection;
                        //map the changes
                        mapPrint (mapMatrix, height, width, cookieCounter);
                        break;
                case 3: cookieCounter = moveGuy( posXY, direction, width, height, mapMatrix, cookieCounter );
                        //calls to the moveGuy method, which inc. move + map
                        moveTotal = ++moveTotal;//move total
                        break;
                case 4: continuation = false;//if false, exit game
                        break;
                        
                default: System.out.println(" No that's not quite right, ");
                         System.out.print(" try a different selection: ");
                         menu();//reoutputs the menu
                         break;
            }
            
        }  
        
        //After the player selects "4" to quit, the stats are conveyed
        System.out.println("\n" + " Thanks for playing!" + "\n");
        System.out.println(" You collected " + cookieCounter + " cookies!");
        System.out.println(" And you took " + moveTotal + " steps!");
        
        //custom outputs based on player behavior
        if (moveTotal == 0)
        {//if the user didn't move then quit
            System.out.println("\n" + " You didn't walk anywhere.");
            System.out.println(" Your PackDude must not be hungry ... ");
        }
        else if (cookieCounter == 0)
        {//if the user moved, but no cookies
            System.out.println("\n" + " So sad! Where did your cookies go?");
            System.out.println(" You're steps/cookie ratio is infinite!");
        }

        else
        {//if user moved and ate cookies
            avgSteps = moveTotal/cookieCounter; //calculates steps per cookie
            System.out.print(" That's an average of " + avgSteps);
            System.out.println(" steps per cookie, Great Job!");
        }
        
    }
    
    /****

    This method geenrates the cookies and plces them inside the map grid
    Takes the map and its size as inputs and then uses the dimensions as 
    limits to cookie placement.

    Pre-con: height
             width
             mapMatrix[][]
             
    Post-con: returns modified mapMatrix [][]
    
    ****/
    public static char[][] cookie ( int height, int width, char mapMatrix[][] )
    {
        //find total number of spaces
        //take 15% and round down as the number of cookies
        int cookieBatter = (int)(height * width * 0.15);
        int yPosition = 0;
        int xPosition = 0;
        //int [][] cookieBatter = new int [1][1];
        for (int bakingCookies = 0; bakingCookies < cookieBatter; bakingCookies ++)
        {
            yPosition = (int) (height*Math.random()); //random cookie y locate
            xPosition = (int) (width*Math.random()); //random cookie x locate
            if ((yPosition == 0)&&(xPosition == 0))
            {//makes sure the cookies aren't starting under PackDude
                yPosition = (int) (height*Math.random());
                xPosition = (int) (width*Math.random());
            }//then moves the cookie if it is
            mapMatrix[yPosition][xPosition] = 79; //place cookie
        }
        
        return mapMatrix;
    }
        
    /****

    This method generates the initial map matrix.  It takes height and width 
    as inputs and sends uses them as loop iterators to build the map, with
    "." as the base filler.

    Pre-con: height
             width
             
    Post-con: returns the initial map, mapper
    
    ****/   
    public static char [][] makeMapMatrix ( int height, int width )
    {
        char [][] mapper = new char[height][width]; //sets the map matrix
        
        //create map basis
        for (int h = 0; h < height; h++)
        {//loops through height values
            for (int w = 0; w < width; w++)
            {//loops through width values
                mapper[h][w] = 46; //sets the values to "."
            }
        }

        return mapper;
    }
    
    /****

    This method prints the menu to the terminal

    Pre-con: N/A
             
    Post-con: N/A
    
    ****/
    public static void menu ()
    {//this method shows the menu commands
        System.out.println("\n" + " Here are the available options:" + "\n");
        System.out.println(" 0 = Show this menu of commands");
        System.out.println(" 1 = Turn left");
        System.out.println(" 2 = Turn right");
        System.out.println(" 3 = Move forward");
        System.out.println(" 4 = Exit");
    }
    
    /****

    This method rotates the PackDude by 90 degrees in the counter clockwise
    direction by reassigning the PackDude's appearance.

    Pre-con: direction
             
    Post-con: returns the newDirection of PackDude
    
    ****/
    public static char turnCCW ( char direction )
    {
        char newDirection = 62; //this initializes the char
        
        switch (direction)
        {// > = 62; ^ = 94; < = 60; and v = 118;
            case 62: newDirection = 94;
                    break;
            case 94: newDirection = 60;
                    break;
            case 60: newDirection = 118;
                    break;
            case 118: newDirection = 62;
                    break;
        }

        return newDirection; //sends out the new direction of PackDude
    }
    
    /****

    This method rotates the PackDude by 90 degrees in the clockwise
    direction by reassigning the PackDude's appearance.

    Pre-con: direction
             
    Post-con: returns the newDirection of PackDude
    
    ****/
    public static char turnCW ( char direction )
    {
        char newDirection = 62; //this initializes the char
        
        switch (direction)
        {// > = 62; ^ = 94; < = 60; and v = 118;
            case 62: newDirection = 118;
                    break;
            case 118: newDirection = 60;
                    break;
            case 60: newDirection = 94;
                    break;
            case 94: newDirection = 62;
                    break;
        }

        return newDirection; //sends out the new direction of PackDude
    }
    
    /****

    This method moves PackDude in the forward direction based on what his
    currently assigned direction is.  Plots out his new direction, replaces
    the previous locations dot with a blank, and counts cookies

    Pre-con: posXY, direction, width, height, char[][] mapMatrix, cookieCounter
             
    Post-con: returns the number of cookies eaten so far.
    
    ****/
    public static int moveGuy ( int [] posXY, char direction, int width, int height, char[][] mapMatrix, int cookieCounter)
    {//each item in the switch covers a different facing direction
        switch (direction)
        {// > = 62; ^ = 94; < = 60; and v = 118;
            case 62: if (posXY[0]-1 >= 0)
                        {//checks if PackDude has tried to leave the map edge
                            int currentXY = posXY[0];
                            //if he's facing left, and there is map spot open, move him left
                            currentXY = --currentXY;
                            if (mapMatrix[posXY[1]][currentXY] == 79)
                            {//if lands on a cookie, eat it
                                cookieCounter = ++cookieCounter;
                            }
                            
                            //next clear previous space
                            mapMatrix[posXY[1]][posXY[0]] = 32; 
                            //set the new location of PackDude
                            posXY[0] = currentXY;

                        } 
                    break;//break if case is met
            case 94: if (posXY[1]+1 < height)
                        {//checks if PackDude has tried to leave the map edge
                            int currentXY = posXY[1];
                            //if he's facing down, and there is map spot below, move him down
                            currentXY = ++currentXY;
                            if (mapMatrix[currentXY][posXY[0]] == 79)
                            {//if lands on a cookie, eat it
                                cookieCounter = ++cookieCounter;
                            }
                            
                            //next clear previous space
                            mapMatrix[posXY[1]][posXY[0]] = 32;
                            //set the new location of PackDude
                            posXY[1] = currentXY;   
                        } 
                    break;
            case 60: if (posXY[0]+1 < width)
                        {//checks if PackDude has tried to leave the map edge
                            int currentXY = posXY[0];
                            //if he's facing right, and there is an open spot in 
                            currentXY = ++currentXY;
                            if (mapMatrix[posXY[1]][currentXY] == 79)
                            {//if lands on a cookie, eat it
                                cookieCounter = ++cookieCounter;
                            }
                            
                            //next clear previous space
                            mapMatrix[posXY[1]][posXY[0]] = 32;
                            //set the new location of PackDude
                            posXY[0] = currentXY; 
                        }
                    break;
            case 118: if (posXY[1]-1 >= 0)
                        {//checks if PackDude has tried to leave the map edge
                            int currentXY = posXY[1];
                            //if he's facing up, and there's a space above him
                            currentXY = --currentXY;
                            //move PackDude into the space
                            if (mapMatrix[currentXY][posXY[0]] == 79)
                            {//if lands on a cookie, eat it
                                cookieCounter = ++cookieCounter;
                            }
                            
                            //next clear previous space
                            mapMatrix[posXY[1]][posXY[0]] = 32;
                            //set the new location of PackDude
                            posXY[1] = currentXY;
                        } 
                    break;
        }
        mapMatrix[posXY[1]][posXY[0]] = direction; //move to the new spot

        mapPrint (mapMatrix, height, width, cookieCounter); //print the map
        
        return cookieCounter; //send back the cookies
    }
    
    /****

    This method prints the new map after changes/commands have happened.

    Pre-con: mapper, height, width, cookieCounter
             
    Post-con: returns the number of cookies eaten so far.
    
    ****/    
    public static void mapPrint ( char [][] mapper, int height, int width, int cookieCounter )
    {   
        System.out.println("\n" + " 0 = show menu" + "\n"); //starter menu
        
        for (int printH = 0; printH < height; printH ++)
        {
            System.out.print("  "); //spaces the map out a bit
            
            for ( int printW = 0; printW < width; printW ++ )
            {//prints out the matrix line by line and spot by spot
                System.out.print( mapper [printH][printW] );
            }
            System.out.println("\n"); //makes new lines
        }
        
        System.out.print("\n");
        System.out.println("Cookies Eaten: " + cookieCounter);
    }
}   
                
        