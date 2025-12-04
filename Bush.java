import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.world.NonBlocking; 
public class Bush implements Actor, NonBlocking {
    private int timer = 0;

    @Override
    public void act(World world) {
        timer++;
        
        if (timer > 20) {
            growBerries(world);
        }
    }

    private void growBerries(World world) {
        if (!world.contains(this)) return;

        Location loc = world.getLocation(this);
        
        // Fjern den tomme busk
        world.delete(this);
        
        // Indsæt busk med bær (som også er NonBlocking)
        world.setTile(loc, new BerryBush());
    }
}