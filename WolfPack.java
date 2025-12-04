import itumulator.world.Location;
import java.util.*;


//Ulveflok klasse - holder styr på ulve der tilhører samme flok
public class WolfPack {
    private int packId;
    private List<Wolf> wolves = new ArrayList<>();
    private Location caveLocation;

    public WolfPack(int packId) {
        this.packId = packId;
    }

    public void addWolf(Wolf w) {
        if (!wolves.contains(w)) {
            wolves.add(w);
        }
    }

    public void removeWolf(Wolf w) {
        wolves.remove(w);
    }

    public List<Wolf> getWolves() {
        return new ArrayList<>(wolves);
    }

    public int getPackId() {
        return packId;
    }

    public void setCaveLocation(Location loc) {
        this.caveLocation = loc;
    }

    public Location getCaveLocation() {
        return caveLocation;
    }

    public int getPackSize() {
        return wolves.size();
    }

    public int getWolfPack() {
        return packId;
    }
}