import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import com.sun.jna.platform.win32.*;

import java.io.IOException;
import java.lang.annotation.Native;
import java.util.ArrayList;
import java.lang.String;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class Main
{
    private static boolean running = true;
    private static boolean isPaused = false;
    private final static int columns = 67;
    private final static int lines = 30;
    private final static int size = columns * lines;
    private static final String title = "Snake Game";
    private static final String[] processNames = {"cmd.exe", "powershell.exe", "WindowsTerminal.exe"};
    private static int processID = -1;

    private final static String[] windowTitles = {title, "Select " + title};
    private static String currentTitle = title;
    private static String[] field = new String[size];
    private static int headPos = (size / 2) + (columns / 2);
    private static ArrayList<Integer> snake;
    private static boolean startListener = true;
    private static EnumerateWindows enumWin = new EnumerateWindows();

    public static void main(String[] args) throws Exception
    {
        ConsoleUtilities.setConsoleWindow("55", "30", "a", "Snake Game");
        Terminal terminal = TerminalBuilder.terminal();
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        while(running)
        {
            terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
            pauseManager();
            onPause();
        }
        terminal.puts(InfoCmp.Capability.cursor_visible);
    }

    private static void onPause()
    {
        if(isPaused)
        {
//            String pauseText[] = {  "*                              | *",
//                                    "*  ___  ___       ___  ___  ___| *",
//                                    "* |   )|   )|   )|___ |___)|   ) *",
//                                    "* |__/ |__/||__/  __/ |__  |__/  *",
//                                    "* |                              *",
//                                    "*                                *"};

            String pauseText[] = {  "║║ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄ ▄▄   ▄▄ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄▄ ▄▄▄▄▄▄    ║║",
                                    "║║ █       █      █  █ █  █       █       █      █  ║║",
                                    "║║ █    ▄  █  ▄   █  █ █  █  ▄▄▄▄▄█    ▄▄▄█  ▄    █ ║║",
                                    "║║ █   █▄█ █ █▄█  █  █▄█  █ █▄▄▄▄▄█   █▄▄▄█ █ █   █ ║║",
                                    "║║ █    ▄▄▄█      █       █▄▄▄▄▄  █    ▄▄▄█ █▄█   █ ║║",
                                    "║║ █   █   █  ▄   █       █▄▄▄▄▄█ █   █▄▄▄█       █ ║║",
                                    "║║ █▄▄▄█   █▄█ █▄▄█▄▄▄▄▄▄▄█▄▄▄▄▄▄▄█▄▄▄▄▄▄▄█▄▄▄▄▄▄█  ║║"};

            for(int i = 0; i < pauseText[0].length(); i++)
            {
                if(i == 0)
                {
                    System.out.print("╔");
                }
                else if(i == pauseText[0].length())
                {
                    System.out.print("╗");
                }
                else
                {
                    System.out.print("═");
                }
            }

            System.out.println();

            for(int i = 0; i < pauseText.length; i++)
            {
                System.out.println(pauseText[i]);
            }

            for(int i = 0; i < pauseText[0].length(); i++)
            {
                if(i == 0)
                {
                    System.out.print("╚");
                }
                else if(i == pauseText[0].length())
                {
                    System.out.print("╝");
                }
                else
                {
                    System.out.print("═");
                }
            }
        }
        else
        {
            ConsoleUtilities.clearConsole();
        }
    }
    private static void pauseManager()
    {
        if(processID == -1)
        {
            setProcessID();
        }
        else
        {
            setPauseGame();
        }
    }

    private static void setPauseGame()
    {
        if(enumWin.getActiveWindowProcessID() == processID)
        {
            isPaused = false;
        }
        else
        {
            isPaused = true;
        }
    }

    private static void setProcessID()
    {
        for(String processName : processNames)
        {
            if(enumWin.getActiveWindowTitle().equals(title) && enumWin.getActiveWindowProcess().equals(processName))
            {
                processID = enumWin.getActiveWindowProcessID();
                return;
            }
        }
    }

    private void run() throws IOException, InterruptedException
    {
        setConsoleWindow();
        createSnake();
        drawSnake();
        //KeyListener.start();
        //MouseListener.start();
        while(running)
        {
//            Thread.sleep(50);
//            clearConsole();
            Thread.sleep(100);
            moveLeft();
            drawSnake();

            //keyListenerControl();
        }
    }

    private static void moveLeft()
    {
        for(int i = 0; i < snake.size(); i++)
        {
            field[snake.get(i)] = null;
            snake.set(i, snake.get(i) - 1);
        }
    }

    private static void createSnake()
    {
        int initialSize = 4;
        snake = new ArrayList<>();
        for(int i = 0; i < initialSize; i++)
        {
            snake.add(headPos + i);
        }
    }

    private static void keyListenerControl()
    {
        if((startListener && currentTitle == windowTitles[0]) || (startListener && currentTitle == windowTitles[1]))
        {
            KeyListener.start();
            startListener = false;
        }
        else if(currentTitle != windowTitles[0] && currentTitle != windowTitles[1])
        {
            KeyListener.stop();
            startListener = true;
        }
    }

    private static void drawSnake()
    {
        for(int snakePart : snake)
        {
            field[snakePart] = "@";
        }
        for(String fieldPart: field)
        {
            if(fieldPart != null)
            {
                System.out.print(fieldPart);
            }
            else
            {
                System.out.print(" ");
            }
        }
    }

    private static void setConsoleWindow() throws IOException, InterruptedException
    {
        String operatingSystem = System.getProperty("os.name"); //Check the current operating system
        String commands[] = {"mode con: cols="+ columns + " lines=" + lines, "color a", "title " + title};


        for(String command : commands)
        {
            if (operatingSystem.contains("Windows"))
            {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        }
    }

    //From https://www.delftstack.com/howto/java/java-clear-console/ with some fixes
    private static void clearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    protected void setRunning(boolean running)
    {
        this.running = running;
    }

    protected void setCurrentTitle(String title)
    {
        this.currentTitle = title;
    }

    protected String getCurrentTitle()
    {
        return this.currentTitle;
    }

    protected String[] getWindowTitles()
    {
        return this.windowTitles;
    }
}
