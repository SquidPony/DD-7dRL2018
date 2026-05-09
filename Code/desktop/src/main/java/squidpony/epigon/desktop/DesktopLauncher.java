package squidpony.epigon.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import squidpony.epigon.Dive;
import squidpony.epigon.Prefs;

public class DesktopLauncher {

    public static void main(String... args) {
        //start independent creators
        System.out.println("Loading...");
        
        //read in all external data files
        System.out.println("Files loaded!");

        //start independent listeners

        //load and initialize resources

        //initialize the display

        //initialize the world

        //start dependent creators

        //start dependent listeners

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(Prefs.getScreenWidth(), Prefs.getScreenHeight());
        config.setTitle(Prefs.getGameTitle());
        //uncomment if testing FPS
        //config.useVsync(false);
        config.useVsync(true);
        // to get these files to load when launching this class as a main() method (and not as a gradle task),
        // set the run configuration for "DesktopLauncher" to have a working directory that matches, on your machine,
        // Z:\path\to\project\Dive\Proto\desktop\assets
        // This is in Run -> Edit Configurations in IntelliJ IDEA and probably also Android Studio. Expect issues if you
        // use Android Studio though; there's a problem with Android and desktop modules co-existing in it.
        config.setWindowIcon(Files.FileType.Internal,
            "images/icons/libgdx16.png",
            "images/icons/libgdx32.png",
            "images/icons/libgdx64.png",
            "images/icons/libgdx128.png");

        new Lwjgl3Application(new Dive(), config);

    }
}
