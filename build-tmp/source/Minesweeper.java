import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




public final static int NUM_ROWS = 20;//Declare and initialize NUM_ROWS and NUM_COLS = 20
public final static int NUM_COLS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton>(); //ArrayList of just the minesweeper buttons that are mined

private int numBombs = 40;
public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    
    buttons = new MSButton[NUM_ROWS][NUM_COLS];
    for (int r=0;r<NUM_ROWS;r++)
    {
        for(int c=0;c<NUM_COLS;c++)
        {
            buttons[r][c]= new MSButton(r,c);
        }
    }
    //declare and initialize buttons

    setBombs();
}
public void setBombs()
{
    //for (int k = 0; k<numBombs; k++)
    while(bombs.size()<40)
    {
        int r1 = (int)(Math.random()*20);
        int c1 = (int)(Math.random()*20);
        System.out.println(r1 + ","+c1);
        if(bombs.contains(buttons[r1][c1]) == false)
        {
            bombs.add(buttons[r1][c1]);
        }
        
    }
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    //your code here
    for (int r=0;r<NUM_ROWS;r++)
    {
        for(int c=0;c<NUM_COLS;c++)
        {
            if(!bombs.contains(buttons[r][c]) && buttons[r][c].isClicked() == false)
            {
                return false;
            }
        }
    }
    return true;
}
public void displayLosingMessage()
{
    //stroke(0);
    //text("You Lose", 200,200);
    buttons[10][6].setLabel("Y");
    buttons[10][7].setLabel("O");
    buttons[10][8].setLabel("U");
    buttons[10][9].setLabel(" ");
    buttons[10][10].setLabel("L");
    buttons[10][11].setLabel("O");
    buttons[10][12].setLabel("S");
    buttons[10][13].setLabel("E");
}
public void displayWinningMessage()
{
    buttons[10][6].setLabel("Y");
    buttons[10][7].setLabel("O");
    buttons[10][8].setLabel("U");
    buttons[10][9].setLabel(" ");
    buttons[10][10].setLabel("W");
    buttons[10][11].setLabel("I");
    buttons[10][12].setLabel("N");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        clicked = true;
        if(keyPressed == true)
        {
            marked = !marked;
        }
        else if(bombs.contains(this))
        {
            displayLosingMessage();
            for (int r=0;r<NUM_ROWS;r++)
            {
                for(int c=0;c<NUM_COLS;c++)
                {
                    if(bombs.contains(buttons[r][c]))
                    {
                        buttons[r][c].clicked = true;
                    }
                }
            }
            //System.out.println("bomb");
        }
        else if(countBombs(r,c)>0)
        {
            setLabel(""+countBombs(r,c));
        }
        else
        {
            if(isValid(r-1,c-1) && buttons[r-1][c-1].clicked == false)
            {
                buttons[r-1][c-1].mousePressed();
            }
            if(isValid(r-1,c) && buttons[r-1][c].clicked == false)
            {
                buttons[r-1][c].mousePressed();
            }
            if(isValid(r-1,c+1) && buttons[r-1][c+1].clicked == false)
            {
                buttons[r-1][c+1].mousePressed();
            }
            if(isValid(r,c-1) && buttons[r][c-1].clicked == false)
            {
                buttons[r][c-1].mousePressed();
            }
            if(isValid(r,c+1) && buttons[r][c+1].clicked == false)
            {
                buttons[r][c+1].mousePressed();
            }
            if(isValid(r+1,c-1) && buttons[r+1][c-1].clicked == false)
            {
                buttons[r+1][c-1].mousePressed();
            }
            if(isValid(r+1,c) && buttons[r+1][c].clicked == false)
            {
                buttons[r+1][c].mousePressed();
            }
            if(isValid(r+1,c+1) && buttons[r+1][c+1].clicked == false)
            {
                buttons[r+1][c+1].mousePressed();
            }
        }
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
             fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        if (r>=0 && r<NUM_ROWS && c>=0 && c<NUM_COLS)
        {
            return true;
        }
        return false;
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        if (isValid(row-1,col-1) && bombs.contains(buttons[row-1][col-1]))
        {
            numBombs ++;
        }
        if (isValid(row-1,col) && bombs.contains(buttons[row-1][col]))
        {
            numBombs ++;
        }
        if (isValid(row-1,col+1) && bombs.contains(buttons[row-1][col+1]))
        {
            numBombs ++;
        }
        if (isValid(row,col-1) && bombs.contains(buttons[row][col-1]))
        {
            numBombs ++;
        }
        if (isValid(row,col+1) && bombs.contains(buttons[row][col+1]))
        {
            numBombs ++;
        }
        if (isValid(row+1,col-1) && bombs.contains(buttons[row+1][col-1]))
        {
            numBombs ++;
        }
        if (isValid(row+1,col) && bombs.contains(buttons[row+1][col]))
        {
            numBombs ++;
        }
        if (isValid(row+1,col+1) && bombs.contains(buttons[row+1][col+1]))
        {
            numBombs ++;
        }
        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
