import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import java.awt.event.*;

public class Main
{
    public static final long serialVersionUID = 1L;
    final static int START_X = 960;
    final static int START_Y = 540;    

    public static void main(String[] args) throws Exception
    {
        int current_x = START_X;
        int current_y = START_Y;

        Robot bot = new Robot();
        
        bot.setAutoDelay(10);    
        bot.delay(5000);
        bot.mouseMove(START_X, START_Y);

        while (true)
        { 
           mineRight(bot, current_x);
           mineLeft(bot, current_x);
           mineUp(bot, current_y);
           //mine(bot);
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

    /* public static void mine(Robot bot)
    {
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    } */

    public static void move(Robot bot, int oldDelay)
    {
        bot.setAutoDelay(241);
        bot.keyPress(KeyEvent.VK_W);
        bot.keyRelease(KeyEvent.VK_W);
        bot.setAutoDelay(oldDelay);
        checkRange();
    }

    public static void checkRange()
    {
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

    public static boolean isOutOfRange()
    {
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
