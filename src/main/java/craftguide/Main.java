package craftguide;

import btw.AddonHandler;
import btw.BTWAddon;

public class Main extends BTWAddon {
    private static Main instance;

    private Main() {
        super("Craft Guide", "0.1.0", "CG");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static Main getInstance() {
        if (instance == null)
            instance = new Main();
        return instance;
    }
}
