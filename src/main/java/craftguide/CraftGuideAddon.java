package craftguide;

import btw.AddonHandler;
import btw.BTWAddon;

public class CraftGuideAddon extends BTWAddon {
    private static CraftGuideAddon instance;

    private CraftGuideAddon() {
        super("Craft Guide", "3.0.1", "CG");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static CraftGuideAddon getInstance() {
        if (instance == null)
            instance = new CraftGuideAddon();
        return instance;
    }
}
