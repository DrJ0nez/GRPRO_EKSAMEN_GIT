import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.NonBlocking;
import itumulator.simulator.Actor;
import java.util.*;

public class Plant implements Actor, NonBlocking {
    @Override
    public void act(World world) {
        Random r = new Random();
        
        
        
        // her køres ved 10% chance.
        if(r.nextDouble() < 0.10) {
            
            
            //hvis der er ledige pladser omkring, så sættes der et nyt stykke græs på en tilfældig af dem.
            Set<Location> emptyAround = world.getEmptySurroundingTiles();
            emptyAround.removeIf(loc -> world.getTile(loc) != null);
        if(!emptyAround.isEmpty()) {
            List<Location> emptyList = new ArrayList<>(emptyAround);
            Location spreadLocation = emptyList.get(r.nextInt(emptyList.size()));
            
            //Plant grass = new Plant();
            world.setTile(spreadLocation, new Plant());
        }
        
        }
    }
}