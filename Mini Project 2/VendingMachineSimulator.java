/****
    
	The intention of this program is to build a vending machine simulator.
    The vending machine contains a variety of products, each with a cost,
    and those prodcts should be able to be changed via a file. 
    
    A) At start up, the money should already be loaded in the machine, and
    should consist of currency, like 5$, 1$, quarters, dimes, nickles,
    and pennies.
    It should be able to switch to the Euro if necessary, without changing 
    the source code.
    Money should be maintained as paper bills and coins (numbers of each).
    
    B) Menu of commands.
    Ordering Items, etc.
    
    C) Error Messaging
    
    @Author: JB @ jamdatajam
    
****/

//import everything needed
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;
import java.util.Objects;
import java.math.*;

public class VendingMachineSimulator
{
    public static void main ( String [] args )
    {
        menu(); //calls the menu at the beginning
        controlPanel( ); //goes to the controlPanel
    }
    
    /**
    * Creates a menu showing the vending machine options
    *
    * @Params: userInput - takes the user input, shows results
    *
    */
    public static void controlPanel( )
    {
        //initialize stuff, make Scanner everywhere
        Scanner input = new Scanner ( System.in );
        int userInput = 0; //this is for selecting menu items
        boolean exit = false; //when true exit
        boolean inPurchase = false; //when true, purchasing something

        while (exit == false)
        {
            System.out.print("Please make a selection: ");
            userInput = input.nextInt(); //takes the input from user
            
            //need to have a catch for in case of a letter input???
            
            switch (userInput)
            {
                case 0: menu();//shows menu
                    break;
                case 1: displayInventory();//shows inventory
                    break;
                case 2: inPurchase = false;//not buying
                    displayCurrency(inPurchase);//show money left
                    break;
                case 3: inPurchase = true;//buying
                    purchaseItem(inPurchase);//buy stuff
                    break;
                case -1: exit = true;//quit
                    break;
                default: System.out.println("---Error---");
                    //this is for abnormal inputs
                    System.out.print("Please Make Another Selection ");
                    menu();//reoutputs the menu
                    break;
            }          
        }
    }
    
    /**
    * Creates the main menu and outputs it as appropriate
    *
    * @Params: None
    *
    */
    public static void menu ()
    {//this is the menu of commands
        System.out.println("\n");
        System.out.printf("%-22s %2s \n", "Show menu:", "0");
        System.out.printf("%-22s %2s \n", "Display Inventory:", "1");
        System.out.printf("%-22s %2s \n", "Display Currency:", "2");
        System.out.printf("%-22s %2s \n", "Purchase Item:", "3");
        System.out.printf("%-22s %2s \n", "Exit", "-1");
        System.out.println("\n");
    }
    
    /**
    * Opens the inventory file and outputs it to the console
    *
    * @Params: None
    *
    */
    public static String[][] createInventory()
    {
        
        //bring in the data file
        String currencyFileName = "inventory.txt";
        String lineByLine = null;
        int arrayLength = 0; //counts the array length
        
        String[][] finalArray = new String [6][7];//makes the copy array
        
        try
        {//pull in the data from the text file, format it
            //creates a new reader object
            FileReader fileInput = new FileReader (currencyFileName);
            //creates the buffer for it
            BufferedReader fileBuffer = new BufferedReader(fileInput);
            
            //create String array to hold rows of text
            String[] rowArray = new String[16]; //max items 16
            
            while((lineByLine = fileBuffer.readLine())!= null)
            {//print the whole file out
                //splits the big String into rows
                String [] rowArrayTemp = lineByLine.split("\n"); 
                
                //assigns the rows to an external array
                rowArray[arrayLength] = rowArrayTemp[0];
                
                arrayLength ++;
            }
            //create array that holds all the info
            String[][] allArray = new String[arrayLength+1][7];
            
            for(int i = 0; i < arrayLength; i++)
            {//this splits the cleaned rows into discrete parts
                String [] columnArray = rowArray[i].split(",");
                
                for (int j = 0; j < arrayLength+1; j++)
                {//this assigns everything to a matrix
                    allArray[i][j] = columnArray[j];
                }
            }
            
            finalArray = allArray; //this array is outputted
            
            System.out.println("");
            fileBuffer.close(); //close the stream
        }
        //now catch any errors
        catch( IOException inventoryException)
        {
            System.out.println("There was an exception: " + inventoryException);
        }
        
        return finalArray; //return the array
    }
    
    /**
    * Calls the inventory method, makes a matrix, and displays its contents
    *
    * @Params: None
    *
    */
    public static void displayInventory()
    {
        String[][] finalArray = createInventory();//calls the method for array
        
        System.out.println("Current inventory: ");
        //Format = item#,name,value,size,material,total#
        System.out.printf("%-5s %-20s %-6s %-15s %-7s %-6s %-6s \n", "item#",
            "name","price","container","unit","size","#left");
            
        for(int i = 0; i < 6; i++)
            {//this prints everything out

                //this prints out the formatted data
                System.out.printf("%-5s %-20s %-6s %-15s %-7s %-6s %-6s \n", 
                    finalArray[i][0],finalArray[i][1],finalArray[i][2],
                    finalArray[i][3],finalArray[i][4],finalArray[i][5],
                    finalArray[i][6]);
                    
            }
            System.out.println(""); //spacing
    }

    /**
    * Calls the currency method, makes a matrix, and displays its contents
    *
    * @Params: inPurchase - boolean value to tell if this has been called from
    *           the menu or during purchase
    *
    */
    public static void displayCurrency( boolean inPurchase )
    {
        String[][] finalArray = createCurrency(); //this gets the array
        
        System.out.println("Available currency: ");
        //Format = item#,name,value,size,material,total#
        
        
        if (inPurchase == true)
        {//if this has been called during a purchase
            System.out.printf("%-5s %-18s %-6s %-10s %-8s \n","item#","name",
            "value","size","material");
            
            for(int i = 0; i < 11; i++)
                {//this prints everything out

                    //this only prints the first five columns
                    System.out.printf("%-5s %-18s %-6s %-10s %-8s \n", 
                        finalArray[i][0],finalArray[i][1],finalArray[i][2],
                        finalArray[i][3],finalArray[i][4]);
                }
        }
        else
        {//if this is not called during a purchase
            System.out.printf("%-5s %-18s %-6s %-10s %-8s %-5s \n","item#","name",
            "value","size","material","#left");
            for(int i = 0; i < 11; i++)
                {//this prints everything out

                    //this prints all the currency info
                    System.out.printf("%-5s %-18s %-6s %-10s %-8s %-5s \n", 
                        finalArray[i][0],finalArray[i][1],finalArray[i][2],
                        finalArray[i][3],finalArray[i][4],finalArray[i][5]);
                }
        }
        
        System.out.println(""); //spacing!
    }
        
    /**
    * Opens the stored currency file and outputs it to window
    *
    * @Params: None
    *
    */
    public static String[][] createCurrency()
    {
        //bring in the data file
        String currencyFileName = "currency.txt";
        String lineByLine = null;
        int arrayLength = 0;
        
        String[][] finalArray = new String [11][6];
        
        try
        {//pull in the data from the text file, format it
            //creates a new reader object
            FileReader fileInput = new FileReader (currencyFileName);
            //creates the buffer for it
            BufferedReader fileBuffer = new BufferedReader(fileInput);
            
            //create String array to hold rows of text
            String[] rowArray = new String[16]; // max denom =16
            
            while((lineByLine = fileBuffer.readLine())!= null)
            {//print the whole file out

                //splits the big String into rows
                String [] rowArrayTemp = lineByLine.split("\n"); 
                
                //assigns the rows to an external array
                rowArray[arrayLength] = rowArrayTemp[0];
                
                arrayLength ++; //increments 
            }
            
            //new matrix to hold the data
            String[][] allArray = new String[arrayLength+1][6];
            
            for(int i = 0; i < arrayLength; i++)
            {//this splits the cleaned rows into discrete parts
                String [] columnArray = rowArray[i].split(",");
                //System.out.println(columnArray[1]);
                //System.out.print(columnArray);
                for (int j = 0; j < 6; j++)
                {//this assigns everything to a matrix
                    //System.out.println(columnArray[j]);
                    allArray[i][j] = columnArray[j];
                }
                //this prints out the formatted data
            //    System.out.printf("%-5s %-18s %-6s %-10s %-8s %-5s \n", 
            //        columnArray[0],columnArray[1],columnArray[2],
            //        columnArray[3],columnArray[4], columnArray[5]);
                    
                finalArray = allArray;
            }
            System.out.println("");
            fileBuffer.close(); //close the stream
        }
        //now catch any errors
        catch( IOException currencyException)
        {
            System.out.println("There was an exception: " + currencyException);
        }
        
        return finalArray;
    }

    /**
    * For purchasing stuff. Brings in data, modifies it then rewrites 
    * it into the files.  Checks if overpayment, equal payment or
    * underpayment has occurred, and addresses each separately.
    *
    * Methods: brings in data from text files via createCurrency and 
    * createInventory.
    *
    * @Params: inPurchase
    *
    */
    public static void purchaseItem( boolean inPurchase )
    {
        int selection = 0;
        int numberDenoms = 0;
        double change = 0;
        double payment = 0;
        double price = 0;
        boolean inventoryChecker = false;
        String[][] currencyMatrix = createCurrency(); //pulls in the currency matrix
        String[][] inventoryMatrix = createInventory(); //pulls in inventory matrix
        Scanner input = new Scanner ( System.in ); //for taking in selections
        
        System.out.print("Please enter item # ");
        while (inventoryChecker == false)
        {//makes sure item is in stock
            selection = input.nextInt();
            if (Integer.valueOf(inventoryMatrix [selection][6]) == 0)
            {    
                inventoryChecker = false;
                System.out.println("Out of Stock");
                //break;
                return;
            }
            else
            {
                inventoryChecker = true;
            }
        }
        System.out.println("Price: " + inventoryMatrix [selection][2]);
        
        //looks up the item number (from file) and finds the cost
        //set price as a double with two decimal places
        price = Double.valueOf(inventoryMatrix [selection][2]);
        
        System.out.print("How many different currency denominations? ");
        numberDenoms = input.nextInt();//how many denominations?
        System.out.println(""); //spacing
        
        //make an array to hold the current wave of money
        int[] insertedMoney = new int [numberDenoms];
        
        displayCurrency( inPurchase );//displays the currency options
        
        
        for(int denoms = 0; denoms < numberDenoms; denoms++)
        {//loops until the number of denominations has been reached
            System.out.print("Currency type: ");
            int nextDenom = input.nextInt();//takes denomination input
            
            insertedMoney[denoms] = nextDenom; //array of payments

            //now takes the number given and looks it up
            payment = payment + 
                Double.valueOf(currencyMatrix[nextDenom][2]);//currency value
            
            System.out.println("Paid: " + payment);
            System.out.println("");
        }
        
        //for(int j = 0; j< insertedMoney.length; j++)
        //        {
        //            System.out.println(insertedMoney[j]);
        //        }

        if (payment > price)
            {//for overpayment
                change = payment - price;//finds the amount of change
                System.out.println("Dispensing");
                inventoryMatrix [selection][6] = 
                    Integer.toString((Integer.valueOf(inventoryMatrix 
                    [selection][6])-1));
                for (int moneyType = 0; moneyType < insertedMoney.length; 
                    moneyType ++)
                {//tallies up added denoms, puts them in the matrix
                    
                    currencyMatrix [Integer.valueOf(insertedMoney[moneyType])]
                        [5] = Integer.toString(Integer.valueOf(currencyMatrix 
                        [Integer.valueOf(insertedMoney[moneyType])][5])+1);
                }

                double changeRemaining = change;//money owed
                
                //needs to loop and write currency changes simultaneously
                for (int cycleThru = 0; cycleThru < 11; cycleThru++)
                {//checks that the currency fits into change and that it is there
                    if (Double.valueOf(currencyMatrix[cycleThru][5]) != 0)
                    {
                        if (changeRemaining >= Double.valueOf(currencyMatrix[cycleThru][2]))
                        {    
                            do //(changeRemaining >= Double.valueOf(currencyMatrix[cycleThru][2]))
                            {
                                changeRemaining = changeRemaining-Double.valueOf(currencyMatrix[cycleThru][2]);
                                //System.out.println("Change remaining = "+changeRemaining);
                                //System.out.println("currency was "+ currencyMatrix[cycleThru][5]);
                                currencyMatrix[cycleThru][5] = String.valueOf((Double.valueOf(currencyMatrix[cycleThru][5])-1));
                                //System.out.println("currency is now " + currencyMatrix[cycleThru][5]);
                                System.out.println(currencyMatrix[cycleThru][1]);
                            }
                            while (changeRemaining >= Double.valueOf(currencyMatrix[cycleThru][2]));
                        }
                        else if (changeRemaining == 0)
                        {
                            break;
                        }
                    }
                        
                }
                System.out.println("Please take your change");
                
                //lastly, update the inventory and currency matrices
                writeInventory( inventoryMatrix );
                writeCurrency( currencyMatrix );
                
                System.out.println("");

            }

        else if(payment == price)
            {//for exact payment
                change = payment - price;//Should be no change
                System.out.println("Dispensing");
                inventoryMatrix [selection][6] = 
                    Integer.toString((Integer.valueOf(inventoryMatrix 
                    [selection][6])-1));
                for (int moneyType = 0; moneyType < insertedMoney.length; 
                    moneyType ++)
                {
                    
                    currencyMatrix [Integer.valueOf(insertedMoney[moneyType])]
                        [5] = Integer.toString(Integer.valueOf(currencyMatrix 
                        [Integer.valueOf(insertedMoney[moneyType])][5])+1);
                    //System.out.println(currencyMatrix 
                    //    [Integer.valueOf(insertedMoney[moneyType])][5]);
                    
                }
                //lastly, update the inventory and currency matrices
                writeInventory( inventoryMatrix );
                writeCurrency( currencyMatrix );
                
                System.out.println("Thank you for your purchase!");
                System.out.println("Change = " + change);
            }
        else
            {//for underpayment
                System.out.println("Insufficient funds.  Paid: "
                        + payment +"; for item costing "
                        + price);
                System.out.println("Returning payment:");
                for (int returnCash = 0; returnCash<insertedMoney.length; 
                    returnCash++)
                {//prints out all the currency that has been added
                    System.out.print(currencyMatrix 
                        [Integer.valueOf(insertedMoney[returnCash])][1] + " ");
                }
                System.out.println("");
            }
        
        System.out.println("");

    }
    
    /**
    * For making change from overpayments
    *
    * Methods: brings in data from text files via createCurrency and 
    * createInventory.
    *
    * @Params: double change - takes the change as an input
    * @Params: String[][] currencyMatrix - outputs an array with the coin names
    *
    */
    public static String[] makeChange(double change, String[][] currencyMatrix)
    {
        
        double copyChange = change;//backup of change
        double newChange = change;
        int counter = 0;
        int countsCoins = 0;
        
        while(copyChange > 0)
        {//this loop counts how many
            for (int cycleDenom = 0; cycleDenom < 11; cycleDenom++)
            {//checks that the currency fits into change and that it is there
                if ((copyChange >= 
                    Double.valueOf(currencyMatrix[cycleDenom][2]))&&
                    (Integer.valueOf(currencyMatrix[cycleDenom][5]) != 0))
                {//subtracts the denom value
                    copyChange = copyChange 
                        - Double.valueOf(currencyMatrix[cycleDenom][2]);
                    counter++;
                }
                else if ((Integer.valueOf(currencyMatrix[cycleDenom][5]) == 0) 
                    &&(copyChange > Double.valueOf(currencyMatrix[cycleDenom+1]
                    [2])&&(Integer.valueOf(currencyMatrix[cycleDenom+1][5]) 
                    != 0)))
                {//subtracts the denom value
                    copyChange = copyChange 
                        - Double.valueOf(currencyMatrix[cycleDenom+1][2]);
                    counter++;
                }
                else if (copyChange == 0)
                {//if theres no change it 
                    break;
                    
                }
                System.out.println("Test copyChange " + copyChange);
            }
            System.out.println("testCounter "+ counter);
        }
        //create a string to hold the currency item names
        String [] changeArray = new String[counter];
        
        while(newChange > 0)
        {//this loop counts how many
            for (int cycleDenom = 0; cycleDenom < 11; cycleDenom++)
            {//checks that the currency fits into change and that it is there
                if ((newChange > Double.valueOf(currencyMatrix[cycleDenom][2])
                    &&(Integer.valueOf(currencyMatrix[cycleDenom][5]) != 0)))
                {//subtracts the denom value
                    newChange = newChange 
                        - Double.valueOf(currencyMatrix[cycleDenom][2]);
                    //adds denom to array
                    changeArray[countsCoins] = currencyMatrix[cycleDenom][1];
                    countsCoins++;
                    System.out.println("TestNewChange1 " + newChange);
                    break;
                }
                else if ((newChange > 
                    Double.valueOf(currencyMatrix[cycleDenom+1][2]) &&
                    (Integer.valueOf(currencyMatrix[cycleDenom+1][5]) != 0)))
                {//subtracts the denom value
                    newChange = newChange 
                        - Double.valueOf(currencyMatrix[cycleDenom+1][2]);
                    //adds denom to array
                    changeArray[countsCoins] = currencyMatrix[cycleDenom+1][1];
                    countsCoins++; //iterates based on coins
                    System.out.println("TestNewChange2 " + newChange);
                    break;
                }
                else if (newChange == 0)
                {
                    break;
                }

            }
            //countsCoins++;
            
        }
        
        //test
        for(int j = 0; j< changeArray.length; j++)
                {
                    System.out.println("changeArray" + changeArray[j]);
                }
        
        return changeArray;
    }
    
    /**
    * Takes the inventory data that has been modified and sends it back into
    * the inventory text file.
    *
    * @Params: String[][] inventoryArray - modified inventory array
    *
    */
    public static void writeInventory( String[][] inventoryArray )
    {
        try
        {//now put the stuff into a new textfile
            PrintWriter newTextFile = new PrintWriter("inventory.txt");
            
            for(int rows = 0; rows<6; rows++)
            {
                
                for (int columns =0; columns<inventoryArray[0].length; 
                    columns++)
                {//prints the columns one by one
                    newTextFile.print(inventoryArray[rows][columns] + ",");
                }
                newTextFile.println("");//carriage return
            }
            newTextFile.close();// don't forget to close!
        }
        catch (IOException exception2)
        {
            System.out.println("File Export Problem: " + exception2);
        }
    }
    
    /**
    * Takes the currency data that has been modified and sends it back into
    * text file.
    *
    * @Params: String[][] currencyArray - modified currency array
    *
    */
    public static void writeCurrency( String[][] currencyArray )
    {
        
        try
        {//now put the stuff into a new textfile
            PrintWriter newTextFile = new PrintWriter("currency.txt");
            
            for(int rows = 0; rows<11; rows++)
            {
                for (int columns =0; columns<currencyArray[0].length;columns++)
                {//prints the columns one by one
                    newTextFile.print(currencyArray[rows][columns] + ",");
                }
                newTextFile.println("");//carriage return
            }
            newTextFile.close();// don't forget to close!
        }
        catch (IOException exception3)
        {
            System.out.println("File Export Problem: " + exception3);
        }
    }
}