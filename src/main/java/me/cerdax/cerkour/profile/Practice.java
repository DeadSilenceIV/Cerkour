package me.cerdax.cerkour.profile;

import me.cerdax.cerkour.map.CheckPoint;
import org.bukkit.Location;

public class Practice {

    private Location startPoint;
    private Location endPoint;
    private boolean isEnabled;
    private CheckPoint checkPoint;

    public Practice() {
        this.startPoint = null;
        this.endPoint = null;
        this.isEnabled = false;
        this.checkPoint = null;
    }

    public Location getStartPoint() {
        return this.startPoint;
    }

    public Location getEndPoint() {
        return this.endPoint;
    }

    public void setCheckPoint(CheckPoint checkPoint) {
        this.checkPoint = checkPoint;
    }

    public CheckPoint getCheckPoint() {
        return this.checkPoint;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setStartPoint(Location startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Location endPoint) {
        this.endPoint = endPoint;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}
