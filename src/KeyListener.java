//Ripped and adapted from https://github.com/kwhat/jnativehook/blob/2.2/doc/Keyboard.md

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener
{
    private String lastKeyPress = null;
    private String[] validKeys = {"Left", "Right", "Up", "Down", "Escape"};
    private static KeyListener keyListener = new KeyListener();

    public void nativeKeyPressed(NativeKeyEvent e)
    {
        String currentKey = NativeKeyEvent.getKeyText(e.getKeyCode());
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));


        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE)
        {
            System.exit(1);
        }
    }

    public static void start() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(keyListener);
    }

    public static void stop()
    {
        try {
            GlobalScreen.unregisterNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem unregistering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.removeNativeKeyListener(keyListener);
    }
}
