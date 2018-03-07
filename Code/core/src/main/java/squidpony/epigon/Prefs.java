package squidpony.epigon;

import com.badlogic.gdx.Gdx;

/**
 * A static class that holds player adjustable environment wide variables.
 */
public class Prefs {

    private static int screenWidth = Dive.totalPixelWidth();
    private static int screenHeight = Dive.totalPixelHeight();
    private static final String title = "Dragons' Dive - A game about falling";
    private static final String prefName = "dive";
    private static boolean debug = true;

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
        Gdx.app.getPreferences(prefName).putInteger("screenWidth", width).flush();
    }

    public static int getScreenHeight() {
        return screenHeight;
        //return Gdx.app.getPreferences("Dive").getInteger("screenHeight", screenHeight);
    }

    public static void setScreenHeight(int height) {
        Gdx.app.getPreferences(prefName).putInteger("screenHeight", height).flush();
    }

    public static int getScreenXLocation() {
        return Gdx.app.getPreferences(prefName).getInteger("screenXLocation", 0);
    }

    public static void setScreenXLocation(int x) {
        Gdx.app.getPreferences(prefName).putInteger("screenXLocation", x).flush();
    }

    public static int getScreenYLocation() {
        return Gdx.app.getPreferences(prefName).getInteger("screenYLocation", 0);
    }

    public static void setScreenYLoaction(int y) {
        Gdx.app.getPreferences(prefName).putInteger("screenYLocation", y).flush();
    }

    public static boolean isSoundfxOn() {
        return Gdx.app.getPreferences(prefName).getBoolean("soundfxOn", true);
    }

    public static void setSoundfxOn(boolean soundfxOn) {
        Gdx.app.getPreferences(prefName).putBoolean("soundfxOn", soundfxOn).flush();
    }

    public static boolean isMusicOn() {
        return Gdx.app.getPreferences(prefName).getBoolean("musicOn", true);
    }

    public static void setMusicOn(boolean musicOn) {
        Gdx.app.getPreferences(prefName).putBoolean("musicOn", musicOn).flush();
    }

    public static float getSoundfxVolume() {
        return Gdx.app.getPreferences(prefName).getFloat("soundfxVolume", 0.5f);
    }

    public static void setSoundfxVolume(float soundfxVolume) {
        Gdx.app.getPreferences(prefName).putFloat("soundfxVolume", soundfxVolume).flush();
    }

    public static float getMusicVolume() {
        return Gdx.app.getPreferences(prefName).getFloat("musicVolume", 0.7f);
    }

    public static void setMusicVolume(float musicVolume) {
        Gdx.app.getPreferences(prefName).putFloat("musicVolume", musicVolume).flush();
    }
}
