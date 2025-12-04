import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;

public class Bear extends Animal {
    private Location territoryCenter;

    public Bear(Location territoryCenter) {
        super(400, 200, 40); 
        this.territoryCenter = territoryCenter;
    }

    @Override
    public void act(World world) {
        super.act(world);
        if (!world.contains(this)) return;

        if (hunger > 5) {
            if (hunt(world)) return;
            if (forage(world)) return;
        }

        moveInTerritory(world);
    }

    private boolean hunt(World world) {
        Location currentLoc = world.getLocation(this);
        Set<Location> surrounding = world.getSurroundingTiles(currentLoc, 2);
        
        for (Location loc : surrounding) {
            Object obj = world.getTile(loc);
            if (obj instanceof Animal && !(obj instanceof Bear)) {
                world.delete(obj);
                hunger = Math.max(0, hunger - 20);
                energy = Math.min(maxEnergy, energy + 40);
                return true;
            }
        }
        return false;
    }

    private boolean forage(World world) {
        // Find bær i nærheden
        Location currentLoc = world.getLocation(this);
        Set<Location> surrounding = world.getSurroundingTiles(currentLoc, 1);
        
        for (Location loc : surrounding) {
            Object obj = world.getTile(loc);
            if (obj instanceof BerryBush) {
            BerryBush bush = (BerryBush) obj;
    
            bush.eatBerry(world);
            
            hunger = Math.max(0, hunger - 5); 
            energy = Math.min(maxEnergy, energy + 10);
            return true;
            }
        }
        return false;
    }

    private void moveInTerritory(World world) {
        Location myLoc = world.getLocation(this);
        if (myLoc == null) {
            return; //Slettes bjørnen fra kortet = gør ingenting/return.
        }
        //Bevægelse i sit territorium 
        Set<Location> emptyTiles = world.getEmptySurroundingTiles(myLoc);
        List<Location> validMoves = new ArrayList<>();

        for (Location l : emptyTiles) {
            if (territoryCenter == null) {
                territoryCenter = myLoc;
            }

            int distance = Math.abs(l.getX() - territoryCenter.getX()) + Math.abs(l.getY() - territoryCenter.getY());
            if (distance < 4) { 
                validMoves.add(l);
            }
        }

        if (!validMoves.isEmpty()) {
            Random r = new Random();
            world.move(this, validMoves.get(r.nextInt(validMoves.size())));
        }
    }
}