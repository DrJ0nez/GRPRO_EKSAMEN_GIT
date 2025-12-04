import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

public class BerryBush implements Actor, NonBlocking {
    
    @Override
    public void act(World world) {
        // BerryBush skal egentlig først gøre noget, når den bliver spist (sprite ændres)
    }

    public void eatBerry(World world) {
        if (!world.contains(this)) return;

        Location loc = world.getLocation(this);
        
        // Fjern busken med bær
        world.delete(this);
        
        // Sæt en tom busk på samme plads
        world.setTile(loc, new Bush());
    }
}