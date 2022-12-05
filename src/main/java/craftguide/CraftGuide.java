package craftguide;

import btw.AddonHandler;
import btw.BTWAddon;

public class CraftGuide extends BTWAddon {
    private static CraftGuide instance;

    private CraftGuide() {
        super("Craft Guide", "0.1.0", "CG");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static CraftGuide getInstance() {
        if (instance == null)
            instance = new CraftGuide();
        return instance;
    }
}
