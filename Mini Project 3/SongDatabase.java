/****
    
	The purpose of this program is to create a GUI that manages a database of
    songs.
    The GUI should allow suers to "add, edit, or delete" songs;
    1) Start-up: takes the path of the database file as a runtime parameter
        If there is no such file, the program will ask the user if they want 
        to create a new database: if  positive, then creates an empty one, if 
        negative, the application exits.
        Format --> java SongDB mySongDB.data
    2) The GUI will display the combo box with the first song in the set 
        selected or blank if new database.
        The fields Item Code, Description, Artist, Album, and Price fields 
        will be displayed and non-editable or blank if new database.
        For a filled database, Add, Edit, Delete, and Exit will be enabled,
        while the Accept and Cancel buttons shall be disabled.
        For empty song lists, only the Add and Exit buttons are enabled.
    3) Add button adds songs, clears the fields, enables Item Code, 
        Description, Artist, Album, and Price. Edit and Delete are disabled 
        and Accept and Cancel are enabled.  Accept adds the infor to the 
        database and combo box.  Cancel clears the entry and the frame starts over
    4) Editing an existing song requires selecting the song from the combo box, 
        then pressing edit, awhich enables Description, Artist, and Price. Item
        code cannot be changed.
        Add, Edit, Delete are disabled, accept and Cancel are enabled.
        Accept saves the changes
        Cancel cancels the transaction.
        When done it starts the menu from the beginning again.
    5) Deleting a song, select the song from combo box. Press Delete to delete.
    6) Exit to terminate.
        Then saves changes
    7) Makes sure all data fields are filled in
        Makes sure there are error messages for unfilled sections
        Makes sure price values are numerical
    8) Description = Song Title

    @Author: JB @ jamdatajam
	
****/

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * This class controls almost all the main processes
 * including buttons, as well as creating all the panels, etc.
 * @author James Brickner
 * @param song Box the comboBox that is used to switch between songs
 * @param songList ArrayList of Objects that holds songs and song info
 * @param artistNameField JTextField for artist
 * @param albumNameField JTextField for the album name
 * @param priceNameField JTextField for the price of the song
 * @param itemCodeField JTextField for itemCode
 * @param addSongTitleField JTextField when editing or adding a song
 * @param addButton JButton for adding items
 * @param editButton JButton for editing things
 * @param deleteButton JButton for deleting songs
 * @param acceptButton JButton for accepting changes
 * @param cancelButton JButton for canceling an action
 * @param exitButton JButton for exiting
 * @param startingCode the itemCode of the first item in the array
 * @param startingArtist the artist name of the first item in the array
 * @param startingAlbum the album name of the first item in the array
 * @param startingPrice the price of the first item in the array
 * @param fileName sets the filename that original data was saved to
 * @param currentSongBoxIndex tells what the index of the comboBox is
 * @param editingSong determines if the song is being edited or created
 * @param processedThrough determines if the program has done this before or 
 *  if this is the first time
*/  
class ComboBoxSongPanel extends JPanel implements ActionListener//, ItemListener
{
    //create the combobox
    private JComboBox   <String> songBox;
    private ArrayList   <Song> songList;
    private JTextField  artistNameField, 
                        albumNameField, 
                        priceNameField, 
                        itemCodeField,
                        addSongTitleField;
    private JButton     addButton,
                        editButton,
                        deleteButton,
                        acceptButton,
                        cancelButton,
                        exitButton;
    String startingCode;
    String startingArtist;
    String startingAlbum;
    String startingPrice;
    
    String fileName = "mySongDB.data";
    int currentSongBoxIndex = 0;
    boolean editingSong = false;
    boolean processedThrough = false;
    
    public ComboBoxSongPanel()
    {
    //TITLE PANEL
        JPanel titlePanel = createTitlePanel ();//creates the title panel
        
    //CODE, ARTIST, ALBUM, PRICE PANEL
        //create code label and textfield
        JLabel itemCodeLabel = new JLabel( "   Item Code: ");
        itemCodeField = new JTextField( 8 ); //space length
        itemCodeField.setText( startingCode ); //sets default
        itemCodeField.setEditable(false); //cannot edit
        
        //create the label and textfield "Artist"
        JLabel artistNameLabel = new JLabel( "            Artist: ");
        artistNameField = new JTextField( 22 ); //space length
        artistNameField.setText( startingArtist ); //sets the default
        artistNameField.setEditable(false); //cannot edit
        
        //label and textfield "Album"
        JLabel albumNameLabel = new JLabel( "          Album: ");
        albumNameField = new JTextField( 22 ); //space length
        albumNameField.setText( startingAlbum ); //sets default
        albumNameField.setEditable(false); //cannot edit
        
        //label and textfield "Price"
        JLabel priceNameLabel = new JLabel( "            Price: ");
        priceNameField = new JTextField( 8 );//space length
        priceNameField.setText( startingPrice ); //sets default
        priceNameField.setEditable(false); //cannot edit
        
        //set the border
        Border albumNameBorder = BorderFactory.createEmptyBorder();
        
        //set up the panel
        JPanel middlePanel = new JPanel();
        //JPanel middleSubPanel = new JPanel(new GridLayout(3,1));
        JPanel codePanel = new JPanel (new FlowLayout());
        JPanel artistPanel = new JPanel(new FlowLayout());
        JPanel albumPanel = new JPanel (new FlowLayout());
        JPanel pricePanel = new JPanel (new FlowLayout());
        
        middlePanel.setLayout(new FlowLayout (FlowLayout.LEFT)); //middle box
        codePanel.add( itemCodeLabel ); //"Item Code:"
        codePanel.add( itemCodeField ); //displays the item code
        artistPanel.add( artistNameLabel ); //add the artist info
        artistPanel.add( artistNameField );
        albumPanel.add( albumNameLabel ); //add the album info
        albumPanel.add( albumNameField );
        pricePanel.add( priceNameLabel ); //add the price info
        pricePanel.add( priceNameField );
        
        //now assign the subPanel to the main panel
        middlePanel.add( codePanel ); //add the code part
        middlePanel.add( artistPanel ); //add the artist part
        middlePanel.add( albumPanel ); //add the album part
        middlePanel.add( pricePanel ); //add the price part
        
        middlePanel.setBorder(albumNameBorder);//sets the border
        
    //ADD,EDIT,DELETE,ACCEPT,CANCEL, & EXIT PANEL
        JPanel bottomPanel = new JPanel(); //bottom panel
        bottomPanel.setLayout(new FlowLayout (FlowLayout.LEFT)); //bottom box
        JPanel bottomSubPanel = new JPanel ( new GridLayout(2,1)); //rows, columns
        JPanel buttonTopPanel = new JPanel( new FlowLayout());
        JPanel buttonLowPanel = new JPanel( new FlowLayout());
        
    //ADD BUTTON
        addButton = new JButton ("Add");//add button
        addButton.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent addE)
                {

                    //clear the title panel
                    titlePanel.removeAll();
                    //create a new title panel
                    titlePanel.add(createNewSongPanel());
                    //refresh the gui
                    titlePanel.revalidate();
                    titlePanel.repaint();
                    
                    //then clear the boxes, make editable
                    itemCodeField.setText( "" );
                    itemCodeField.setEditable(true);//make editable
                    artistNameField.setText( "" );
                    artistNameField.setEditable(true);//make editable
                    albumNameField.setText( "" );
                    albumNameField.setEditable(true);//make editable
                    priceNameField.setText( "" );
                    priceNameField.setEditable(true);//make editable
                    
                    //set buttons
                    addButton.setEnabled(false);
                    exitButton.setEnabled(false);
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    acceptButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    
                    //set verification step here
                    verifyFields();
                }
            }
        );
        
    //EDIT BUTTON
        editButton = new JButton ("Edit"); //the edit button
        editButton.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent editE)
                {//this removes the currently selected item
                    
                    //songBox.setEditable(true); //make editable
                    //songBox.addActionListener(this);
                    
                    //get comboBox current value
                    String currentSongBox = songBox.getSelectedItem().toString();
                    //gets the index of the edited song
                    currentSongBoxIndex = songBox.getSelectedIndex();
                    //set whether editing or not
                    editingSong = true;
                    
                    //clear the title panel
                    titlePanel.removeAll();
                    //create a new title panel
                    titlePanel.add(createNewSongPanel());
                    //refresh the gui
                    titlePanel.revalidate();
                    titlePanel.repaint();
                    
                    //set the title field
                    addSongTitleField.setText(currentSongBox);
                    
                    itemCodeField.setEditable(false);//make uneditable
                    //artistNameField.setText( "" );
                    artistNameField.setEditable(true);//make editable
                    //albumNameField.setText( "" );
                    albumNameField.setEditable(true);//make editable
                    //priceNameField.setText( "" );
                    priceNameField.setEditable(true);//make editable
                    
                    //set buttons
                    addButton.setEnabled(false);
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    exitButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    acceptButton.setEnabled(true);
                    
                    //add verification step here
                    verifyFields();
                }
            }
        );
                    
    //DELETE BUTTON
        deleteButton = new JButton ("Delete");
        deleteButton.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent deleteE)
                {//this removes the currently selected item
                    int currentSong = songBox.getSelectedIndex();//finds current song
                    //now remove that item
                    int songsLeft = songList.size();
                    //System.out.println(songsLeft);
                    if (songsLeft == 1)
                    {
                        //clear the title panel
                        titlePanel.removeAll();
                        //create new title panel
                        titlePanel.add(createNewSongPanel());
                        //refresh the gui
                        titlePanel.revalidate();
                        titlePanel.repaint();
                        
                        //then clear the boxes, make editable
                        itemCodeField.setText( "" );
                        itemCodeField.setEditable(true);//make editable
                        artistNameField.setText( "" );
                        artistNameField.setEditable(true);//make editable
                        albumNameField.setText( "" );
                        albumNameField.setEditable(true);//make editable
                        priceNameField.setText( "" );
                        priceNameField.setEditable(true);//make editable
                        
                        //set buttons
                        addButton.setEnabled(false);
                        exitButton.setEnabled(false);
                        editButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        acceptButton.setEnabled(true);
                        cancelButton.setEnabled(false);
                        
                        editingSong = true;
                    }
                    else
                    {
                        songBox.removeItemAt(currentSong);//removes from comboBox
                        songList.remove(currentSong);//removes from arrayList
                        songBox.getSelectedIndex();
                        saveToFile(songList, fileName); //save to the file
                    }
                    //set verification step here
                        verifyFields();
                        
                    
                }
            }
        );
        
    //ACCEPT BUTTON
        acceptButton = new JButton ("Accept");
        acceptButton.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent acceptE)
                {//never thought I'd get to use those 3 yrs of Mandarin
                    
                    String xinDeBiaoTi = addSongTitleField.getText();
                    //new item
                    String xinDeDongXi = itemCodeField.getText();
                    //new artist
                    String xinDeYueShi = artistNameField.getText();
                    //new album
                    String xinDeZhuanJi = albumNameField.getText();
                    //new price
                    String xinDeShouJia = priceNameField.getText();
                    
                    if ( editingSong == true )
                    {//when finished editing a song
                        songList.add( currentSongBoxIndex, new Song( xinDeBiaoTi, xinDeYueShi, xinDeZhuanJi, xinDeShouJia, xinDeDongXi ));
                        songList.remove( (currentSongBoxIndex+1));
            ////        verification of blank boxes
                    }
                    else
                    {
                        songList.add( new Song( xinDeBiaoTi, xinDeYueShi, xinDeZhuanJi, xinDeShouJia, xinDeDongXi )); //create new song
                    }
                    
                    saveToFile(songList, fileName);//save before replacing panel
                    
                    //clear the title panel
                    titlePanel.removeAll();
                    //create a new title panel
                    titlePanel.add(createTitlePanel());
                    //refresh the gui
                    titlePanel.revalidate();
                    titlePanel.repaint();
                    
                    //recall the values
                    itemCodeField.setText( startingCode );
                    artistNameField.setText( startingArtist );
                    albumNameField.setText( startingAlbum );
                    priceNameField.setText( startingPrice );
                    
                    //set buttons
                    addButton.setEnabled(true);
                    exitButton.setEnabled(true);
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    acceptButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    
                    if (songBox.getSelectedItem() == " "|| songBox.getSelectedItem() == "" ||songBox.getSelectedItem() == null )
                    {
                        int currentSongAccept = songBox.getSelectedIndex();
                        songBox.removeItemAt(currentSongAccept);
                        deleteButton.setEnabled(false);
                        editButton.setEnabled(false);
                    }
                    
                    unedit(); //makes everything uneditable
                }
            }
        );
        
    //CANCEL BUTTON
        cancelButton = new JButton ("Cancel");
        cancelButton.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent cancelE)
                {
                    //clear the title panel
                    titlePanel.removeAll();
                    //create a new title panel
                    titlePanel.add(createTitlePanel());
                    //refresh the gui
                    titlePanel.revalidate();
                    titlePanel.repaint();
                    
                    //recall the values
                    itemCodeField.setText( startingCode );
                    artistNameField.setText( startingArtist );
                    albumNameField.setText( startingAlbum );
                    priceNameField.setText( startingPrice );
                    
                    //set buttons
                    addButton.setEnabled(true);
                    exitButton.setEnabled(true);
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    acceptButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    
                    if (songBox.getSelectedItem() == " "|| songBox.getSelectedItem() == "" ||songBox.getSelectedItem() == null )
                    {
                        deleteButton.setEnabled(false);
                        editButton.setEnabled(false);
                    }
                    
                    //make uneditable
                    unedit();
                }
            }
        );
        
        //exit action
        exitButton = new JButton ("Exit");
        exitButton.addActionListener
        (
            new ActionListener()
            {//listens to the button
                public void actionPerformed(ActionEvent exitE)
                {//if button is pressed, exit the program
                    saveToFile(songList, fileName); //save to the file
                    System.exit(0);
                }
            }
        );
        
        //check if array is blank or null
        if (songBox.getSelectedItem() == " "|| songBox.getSelectedItem() == "" ||songBox.getSelectedItem() == null )
        {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        }
        
        
        //default button shading
        acceptButton.setEnabled(false);
        cancelButton.setEnabled(false);
        
        //add the buttons to their respective panels
        buttonTopPanel.add(addButton);//upper bottom panel
        buttonTopPanel.add(editButton);
        buttonTopPanel.add(deleteButton);
        buttonTopPanel.add(acceptButton);
        buttonTopPanel.add(cancelButton);
        buttonLowPanel.add(exitButton);//lower bottom panel
        
        //add the subpanels to the main bottom panel
        bottomSubPanel.add(buttonTopPanel);
        bottomSubPanel.add(buttonLowPanel);
        
        //add the subpanel to the main bottom panel
        bottomPanel.add(bottomSubPanel);
        
    //SET THE PANELS IN LAYOUT
        //set the layout within the set Borders
        this.setLayout(new BorderLayout());
        this.add( titlePanel, BorderLayout.NORTH );
        //this.add( titlePanel1, BorderLayout.NORTH );
        //this.add( titlePanel2, BorderLayout.NORTH );
        this.add( middlePanel, BorderLayout.CENTER );
        this.add( bottomPanel, BorderLayout.SOUTH );
        
        processedThrough = true;
    }
    /**
     * This checks the values of the inputted info and makes sure it is all
     * there, changes some buttons activity
     * combining them
     * @author James Brickner
     * @param acceptVerif if fields have been verified, then true
    */
    public void verifyFields()
    {
        boolean acceptVerif = false;
        //while(acceptVerif == false)
        {
            //grayed out accept button
            //acceptButton.setEnabled(false);
            //new title
            if(addSongTitleField.getText().isEmpty() == true &&
                itemCodeField.getText().isEmpty() == true &&
                artistNameField.getText().isEmpty() == true &&
                albumNameField.getText().isEmpty() == true &&
                priceNameField.getText().isEmpty() == true)
            {
                //set the verification
                acceptVerif = false;
                acceptButton.setEnabled(false);
            }
            acceptVerif = true;
            acceptButton.setEnabled(true);
        }
    }
    /**
     * This makes the panels uneditable, like a switch
     * @author James Brickner
    */
    public void unedit()
    {
        //make uneditable
        itemCodeField.setEditable(false);//make uneditable
        artistNameField.setEditable(false);//make uneditable
        albumNameField.setEditable(false);//make uneditable
        priceNameField.setEditable(false);//make uneditable
    }
    /**
     * This creates the editable title panel by creating the sub panels and
     * combining them
     * @author James Brickner
     * @param songList calls getSongs to pull in song data
     * @param songNameLabel sets the song name label for panel
     * @param titlePanel creates the new title panel
     * @param addSongPanel creates the song panel
     * @return titlePanel sends the panel back for modification
    */
    public JPanel createNewSongPanel()
    {
        //create the text box
        JLabel songNameLabel = new JLabel( "  Add Song: ");
        JPanel titlePanel = new JPanel(); //main panel
        JPanel addSongPanel = new JPanel(new FlowLayout()); //sub panel
        addSongTitleField = new JTextField( 22 ); //sets the default
        addSongTitleField.setText("");
        addSongTitleField.setEditable(true);
        
        addSongPanel.add( songNameLabel ); //"Select Song:"
        addSongPanel.add( addSongTitleField ); //adds the comboBox for song
        
        titlePanel.add( addSongPanel );//adds the subpanel to the panel
    
        return titlePanel;
    }
    /**
     * This creates the regular title panel by creating the sub panels and
     * combining them
     * @author James Brickner
     * @param songList calls getSongs to pull in song data
     * @return titlePanel sends the panel back for modification
    */
    public JPanel createTitlePanel ()
    {
        //call for the song list data
        songList = getSongs();
        
        if (songList == null )
        {
            //clicks the button
            addButton.doClick();
            
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
            //acceptButton.setEnabled(true);
            
        }
        
        int counter = 0;
        
    //TITLE PANEL
        //create the top panel
        JPanel titlePanel = new JPanel(); //main panel
        JPanel songPanel = new JPanel (new FlowLayout());
    
        //create the combo box
        JLabel songNameLabel = new JLabel( "Select Song: ");
        songBox = new JComboBox < String > ();
        songBox.setPreferredSize(new Dimension(250, 20));
        songBox.setAlignmentX( Component.LEFT_ALIGNMENT );
        for ( Song s : songList )
        {
            if (counter == 0)
            {//copies the values of the first item in the arrayList
               startingCode = s.getItemCode();
               startingArtist = s.getArtist();
               startingAlbum = s.getAlbum();
               startingPrice = s.getPrice();
            }
            
            //gets the title of the song into the comboBox
            songBox.addItem( s.getTitle() );
            counter++;
        }
        
        //get a listener
        songBox.addActionListener( this );
        
        songPanel.add( songNameLabel ); //"Select Song:"
        songPanel.add( songBox ); //adds the comboBox for song
        
        titlePanel.add( songPanel );//adds the subpanel to the panel
        
        Border titlePanelBorder = BorderFactory.createEmptyBorder();//creates an invisible border
        
        //Set the layout for the comboBox
        titlePanel.setLayout(new FlowLayout (FlowLayout.LEFT));//left align
        titlePanel.setBorder(titlePanelBorder);//get a border
    
    return titlePanel;
    }
    /**
     * This is the action listener
     * @author James Brickner
     * @param eventE action event that is used for setting text fields
     * @param index gets the comboBox index
     * @param selection gets the current index for the songList
    */
    public void actionPerformed ( ActionEvent eventE )
    {
        Object source = eventE.getSource();
        int index = songBox.getSelectedIndex();
        
        Song selection = songList.get(index);
        
        if( source == songBox )
        {
            itemCodeField.setText( selection.getItemCode() );
            artistNameField.setText( selection.getArtist() );
            albumNameField.setText( selection.getAlbum() );
            priceNameField.setText( selection.getPrice() );
        }
    }
    /**
     * This gets the songs from file
     * @author James Brickner
     * @param array sets the song array when bringing in data
     * @exception createFileE exception for when the file is created
     * @exception openException exception when we try to open a file
     *  wrong with file writing
    */
    public ArrayList <Song> getSongs()
    {
        ArrayList < Song > array = new ArrayList < Song > ();
        
        try
        {
            File fileCheck = new File(fileName);
            if (fileCheck.isFile())
            {
                Scanner scanner = new Scanner (new FileReader(fileName));
            
                while (scanner.hasNextLine())
                {
                    String [] row = scanner.nextLine().split("\n");
                    String [] values = row[0].split(",");
                    
                    if (values.length == 0 || values[0] == " " || values[0] == "" || values[0] == null)
                    {
                        array.add(new Song( "", "", "", "", ""));
                        
                        if (processedThrough == true)
                        {
                            deleteButton.setEnabled(false);
                            editButton.setEnabled(false);
                        }
                    }
                    if (values.length >=1)
                    {
                        array.add( new Song( values[0], values[1], values[2], values[3], values[4] ));
                    }
                    else
                    {
                        array.add(new Song( "", "", "", "", ""));
                    }
                }
            }
            else
            {
                try
                {
                    PrintWriter writer = new PrintWriter(fileName);
                    array.add(new Song( null, null, null, null, null));
                    
                    //deleteButton.setEnabled(false);
                    //editButton.setEnabled(false);
                    
                }
                catch(IOException createFileE)
                {
                    System.out.println("There was an exception while making a new data file: " + createFileE);
                }
            }
                    //???HOW TO DEAL WITH EMPTY ARRAY???
        }
        catch ( IOException openException)
        {
            System.out.println("There was an open file exception: " + openException);
        }

        //array.add( new Song( "Firework", "Katy Perry", "Teenage Dream", "0.99", "KP04"));
        //array.add( new Song( "Bad Romance", "Lady Gaga", "The Fame Monster", "0.85", "LG01"));
        
        return array;
    }
    
    /**
     * This saves the file
     * @author James Brickner
     * @param writer variable for the PrintWriter
     * @exception exceptionAtWriter catches the exception for if something goes
     *  wrong with file writing
    */
    public static void saveToFile(ArrayList<Song> songList, String fileName)
    {
        try
        {//now put the stuff into a new textfile
            PrintWriter writer = new PrintWriter(new FileOutputStream(fileName));
        
            for (Song str: songList)
            {
                writer.println(str.toString());//write it out
                //writer.println("\n"); //new line
            }
            
            writer.close();// don't forget to close!
            
        }
        catch (IOException exceptionAtWriter)
        {
            System.out.println("File Export Problem: " + exceptionAtWriter);
        }
    }
    
    
}
/**
 * This is the Song object class
 * @author James Brickner
 * @param title is the song title
 * @param artist is the artist name
 * @param album is the album name
 * @param price is the price
 * @param itemCode is the item code
 * @return title returns the title
 * @return artist returns the artist
 * @return album returns the album
 * @return price returns the price
 * @return itemCode returns the item code
*/
class Song
{
    public Song (String title, String artist, String album, String price, String itemCode)
    {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.price = price;
        this.itemCode = itemCode;
    }
    
    public String title;
    public String artist;
    public String album;
    public String price;
    public String itemCode;
    
    public String getTitle()
    {
        return title;
    }
    public void setTitle()
    {
        this.title = title;
    }
    public String getArtist()
    {
        return artist;
    }
    public void setArtist()
    {
        this.artist = artist;
    }
    public String getAlbum()
    {
        return album;
    }
    public void setAlbum()
    {
        this.album = album;
    }
    public String getPrice()
    {
        return price;
    }
    public void setPrice()
    {
        this.price = price;
    }
    public String getItemCode()
    {
        return itemCode;
    }
    public void setItemCode()
    {
        this.itemCode = itemCode;
    }
    public String toString()
    {
        //return ("\"" + title + "\", \"" + artist + "\", \"" + album + "\", \"" + price + "\", \"" + itemCode + "\"");
        return (title + "," + artist + "," + album + "," + price + "," + itemCode);
    }
}

/**
 * This is the main class for SongDatabase. It pulls in the command line info
 * checks it then saves it.
 * @author James Brickner
 * @version 3.0
 * @param args input from command line
 * @param array
*/
@SuppressWarnings("serial")   
public class SongDatabase extends JFrame
{//this puts the panel in the frame
    /**
     * This method sets the window size and adds the JPanel
     * @author James Brickner
     * @param panel is the new panel
    */
    public SongDatabase()
    {
        setTitle("Current Song List");
        setSize(385, 310); //this sets it to (w, h)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //when to close
        JPanel panel = new ComboBoxSongPanel(); //calls the stuff inside the panel
        //JPanel panelTest = new RadioButtonPizzaPanel ();
        //this.add(panelTest);
        this.add(panel);//adds this panel
    }
    /**
     * This is the main method, it calls everything and imports the command
     * line file.
     * @author James Brickner
     * @param args the command line argument
     * @param scanner creates a scanner for processing imported files
     * @param array ArrayList <Song> calls to the song object, creates list
     * @exception createFile catches exception if file doesn't open correctly
    */
    public static void main(String [] args)
    {
        //takes command line file and saves it into a new file
        String [] fileIn = args;//copy the args
        String fileName = "mySongDB.data";
        
        ArrayList < Song > array = new ArrayList < Song > ();
        
        try 
        {
            if(args.length == 0)
            {
                array.add(new Song("","","","",""));
            }
            else
            {
                Scanner scanner = new Scanner(new File(args[0]));
                while(scanner.hasNextLine())
                {
                    if(args[0] == null)
                    {
                    
                    array.add(new Song("","","","",""));
                    
                    }
                    else
                    {    
                    String[] fileRows = scanner.nextLine().split("\n");
                    String[] words = fileRows[0].split(",");
                    
                    array.add(new Song(words[0], words[1], words[2], words[3],words[4]));
                    }
                }
            }
            
            ComboBoxSongPanel.saveToFile(array, fileName);
            
        }
        catch( IOException createFile)
        {
            System.out.println("There was an exception while making a new data file: " + createFile);
        }
        
        //creates the GUI
        JFrame frame = new SongDatabase(); //calls the Music List Frame
        frame.setVisible(true); // makes it visible
    }
}