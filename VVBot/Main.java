import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*; // I have had so many problems from this one library
import java.awt.event.*;

public class Main
{
/*
 * this program is meant to automatically mine voxels immediately to the left, right, above you, and in front of you,
 * make sure your mining method is set to toggle in the options menu, and align yourself to look perfectly ahead of you,
 * as not doing so will make you slowly drift left or right (or dont align yourself idc)
 * 
 * PROBLEMS: 
 * - the OCR is untrained and unreliable, different situations such as encountering a REALLY bright area will make it break
 * - there is no self-correction, if the bot gets misaligned or drops below the targeted depth, too bad so sad
 * 
 * CREDITS:
 * - made by me, BingChingus on github
 * - made in july/august 2023 i forgor when
 */

    public static final long serialVersionUID = 1L; // no idea what this does, was copied from a site that is cited later
                                                    
    //coordinates for the center of the screen
    final static int START_X = 960;
    final static int START_Y = 540;    

    public static void main(String[] args) throws Exception
    {
        //will most likely remove these as these have become redundant
        int current_x = START_X;
        int current_y = START_Y;

        Robot bot = new Robot(); //this will do basically all the movement
        
        bot.setAutoDelay(10);    
        bot.delay(5000); //wait 5 sec so that you can switch tabs
        bot.mouseMove(START_X, START_Y); //center the mouse + the loop order breaks without this for some reason

        while (true)
        { 
           mineRight(bot, current_x);
           mineLeft(bot, current_x);
           mineUp(bot, current_y);
           //mine(bot); //redundant (?)
           move(bot, bot.getAutoDelay());
        }
    
    }

    public static void mineRight(Robot bot, int current_x)
    {
        bot.mouseMove(current_x + 180, START_Y);
        checkRange();
        bot.mouseMove(current_x, START_Y);
    }

    public static void mineLeft(Robot bot, int current_x)
    {
        bot.mouseMove(current_x - 180, START_Y);
        checkRange();
        bot.mouseMove(current_x, START_Y);
    }

    public static void mineUp(Robot bot, int current_y)
    {
        bot.mouseMove(START_X, current_y - 200);
        checkRange();
        bot.mouseMove(START_X, current_y);
    }

    /* public static void mine(Robot bot) //also redundant (?)
    {
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    } */

    public static void move(Robot bot, int oldDelay) //moves the bot forward, is accurate by ~0.1 per 100 voxels
    {
        bot.setAutoDelay(241);
        bot.keyPress(KeyEvent.VK_W);
        bot.keyRelease(KeyEvent.VK_W);
        bot.setAutoDelay(oldDelay);
        checkRange();
    }

    public static void checkRange() 
    {
         //continually checks if the block you're mining is in range
        while (!isOutOfRange())
        {
            if (isDecade())
            {
                break;
            }

            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                System.out.println(e);
            }
        }
    }

    // this entire function was shamlessly stolen from https://www.geeksforgeeks.org/java-program-take-screenshots/
    public static boolean isOutOfRange()
    {                                    
        /*
        * takes a screenshot of a certain section of the screen 
        * that dictates whether the block you're mining is in range or not,
        * and uses OCR to determine if it is indeed out of range
        */ 
        try                              
        {
            Thread.sleep(10);
            Robot r = new Robot();
            
            // It saves screenshot to desired path
            String path = "C:\\Users\\mrsni\\OneDrive\\Desktop\\Old PC Stuff\\Code Shenanigans\\random ass projects\\VVBot\\Image\\Image.jpeg";
            
            // Used to get ScreenSize and capture image
            Rectangle capture = new Rectangle(900, 768, 120, 20);
            BufferedImage Image = r.createScreenCapture(capture);
            ImageIO.write(Image, "jpg", new File(path));
            //System.out.println("Screenshot saved"); //test code

            String result = crackImage(path);
            System.out.println(result); //test code
            if (result.strip().indexOf("Out") != -1 || result.strip().indexOf("of") != -1 || result.strip().indexOf("range") != -1)
            {                
                return true;
            }
            return false;
        }
        catch (AWTException | IOException | InterruptedException ex) 
        {
            System.out.println(ex);
        }

        return true;
    }
    
    
    public static boolean isDecade()
    {
        // modified version of isOutOfRange() but checks if it will take over a decade to mine
        // (yes, given a big enough power gap some blocks will take over a decade to mine)
        try
        {
            Thread.sleep(10);
            Robot r = new Robot();

            String decPath = "C:\\Users\\mrsni\\OneDrive\\Desktop\\Old PC Stuff\\Code Shenanigans\\random ass projects\\VVBot\\Image\\decImage.jpeg";
            Rectangle decade =  new Rectangle(890, 703, 142, 20);
            BufferedImage decImage = r.createScreenCapture(decade);
            ImageIO.write(decImage, "jpg", new File(decPath));
            
            String result = crackImage(decPath);
            System.out.println(result); //test code
            if (result.strip().indexOf("Over") != -1 || result.strip().indexOf("decade") != -1)
            {
                return true;
            }
                return false;
        }
        catch (AWTException | IOException | InterruptedException ex) 
        {
            System.out.println(ex);
        }

        return true;
    }

    //this entire function was also shamelessly stolen from https://dzone.com/articles/reading-text-from-images-using-java-1
    public static String crackImage(String filePath)
    {  
        File imageFile = new File(filePath);  
        ITesseract instance = new Tesseract();  
        try {  
            instance.setDatapath("C:\\Users\\mrsni\\OneDrive\\Desktop\\Old PC Stuff\\Code Shenanigans\\random ass projects\\Tess4jShit\\tessdata"); //put in the path to the tessdata folder
            String result = instance.doOCR(imageFile);  
            return result;  
        } catch (TesseractException e) {  
            System.err.println(e.getMessage());  
            return "Error while reading image";  
        }  
    }  
}
