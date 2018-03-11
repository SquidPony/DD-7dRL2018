package squidpony.epigon;

import com.badlogic.gdx.Gdx;

/**
 * A static class that holds player adjustable environment wide variables.
 */
public class Prefs {

    static private int screenWidth = Dive.totalPixelWidth();
    static private int screenHeight = Dive.totalPixelHeight();
    static private String title = "Dragon Dive - Fall To Riches";
    static private String prefKey = "DragonDive";
    static private boolean debug = true;

    /**
     * No instances of this class should be made.
     */
    private Prefs() {
    }

    public static String getGameTitle() {
        return title;
    }

    public static boolean isDebugMode() {
        return debug;
    }

    public static int getScreenWidth() {
        return screenWidth;
        //return Gdx.app.getPreferences("Dive").getInteger("screenWidth", screenWidth);
    }

    public static void setScreenWidth(int width) {
        Gdx.app.getPreferences(prefKey).putInteger("screenWidth", width).flush();
    }

    public static int getScreenHeight() {
        return screenHeight;
        //return Gdx.app.getPreferences("Dive").getInteger("screenHeight", screenHeight);
    }

    public static void setScreenHeight(int height) {
        Gdx.app.getPreferences(prefKey).putInteger("screenHeight", height).flush();
    }

    public static int getScreenXLocation() {
        return Gdx.app.getPreferences(prefKey).getInteger("screenXLocation", 0);
    }

    public static void setScreenXLocation(int x) {
        Gdx.app.getPreferences(prefKey).putInteger("screenXLocation", x).flush();
    }

    public static int getScreenYLocation() {
        return Gdx.app.getPreferences(prefKey).getInteger("screenYLocation", 0);
    }

    public static void setScreenYLoaction(int y) {
        Gdx.app.getPreferences(prefKey).putInteger("screenYLocation", y).flush();
    }

    public static boolean isSoundfxOn() {
        return Gdx.app.getPreferences(prefKey).getBoolean("soundfxOn", true);
    }

    public static void setSoundfxOn(boolean soundfxOn) {
        Gdx.app.getPreferences(prefKey).putBoolean("soundfxOn", soundfxOn).flush();
    }

    public static boolean isMusicOn() {
        return Gdx.app.getPreferences(prefKey).getBoolean("musicOn", true);
    }

    public static void setMusicOn(boolean musicOn) {
        Gdx.app.getPreferences(prefKey).putBoolean("musicOn", musicOn).flush();
    }

    public static float getSoundfxVolume() {
        return Gdx.app.getPreferences(prefKey).getFloat("soundfxVolume", 0.5f);
    }

    public static void setSoundfxVolume(float soundfxVolume) {
        Gdx.app.getPreferences(prefKey).putFloat("soundfxVolume", soundfxVolume).flush();
    }

    public static float getMusicVolume() {
        return Gdx.app.getPreferences(prefKey).getFloat("musicVolume", 0.7f);
    }

    public static void setMusicVolume(float musicVolume) {
        Gdx.app.getPreferences(prefKey).putFloat("musicVolume", musicVolume).flush();
    }
}
