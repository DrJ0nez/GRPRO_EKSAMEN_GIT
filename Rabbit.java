import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;

public class Rabbit extends Animal {

    public Rabbit() {
        super(15,15,0);
    }
    
    // giver hver kanin et hul og en location til hullet.
    Hole hole;
    Location holeLocation;
    @Override 
    public void act(World world) {
        super.act(world);

        if (!world.contains(this)) {
            return; //Dør kaninen i animal.act, så returnér. 
        }

        Random r = new Random();
        
        //checker om der er nogle planter på en neighbour tile.
        Set<Location> neighbourTiles = world.getSurroundingTiles();

        Set<Plant> neighbourPlants = world.getAll(Plant.class, neighbourTiles);

        Set<Location> neighbourEmptyTiles = world.getEmptySurroundingTiles();

        List<Location> nearbyEmptyTiles = new ArrayList<>(neighbourEmptyTiles);

        if(!nearbyEmptyTiles.isEmpty() && world.contains(this)) {
            Location moveTo = nearbyEmptyTiles.get(r.nextInt(nearbyEmptyTiles.size()));
            world.move(this, moveTo);
        }
        
        //hvis der er græs på en neighbour tile, så går kanin til den og spiser den og så fjernes græsset.
        if(!neighbourPlants.isEmpty()) {
            List<Plant> plantList = new ArrayList<>(neighbourPlants);
            Plant target = plantList.get(r.nextInt(plantList.size()));
            
            Location plantLocation = world.getLocation(target);
            world.move(this, plantLocation);
            
            world.delete(target);
            // så nulstilles sult.
            hunger = 0;
        }
        
        
        // 15% chance for at en kanin formere sig.
        if(r.nextDouble() < 0.15) {
            Set<Location> emptyAround = world.getEmptySurroundingTiles();
            emptyAround.removeIf(loc -> world.getTile(loc) != null);

        if(!emptyAround.isEmpty()) {
            List<Location> emptyList = new ArrayList<>(emptyAround);
            Location spreadLocation = emptyList.get(r.nextInt(emptyList.size()));
            
            //Plant grass = new Plant();
            if (world.getTile(spreadLocation) == null) {
                world.setTile(spreadLocation, new Rabbit());
            }
            }
        }
        //10% chance for at en kanin laver et hul, hvis den ikke har et hul i forvejen.
        if(r.nextDouble() > 0.10 && holeLocation == null) {
            Set<Location> emptyAround = world.getEmptySurroundingTiles();
            emptyAround.removeIf(loc -> world.getTile(loc) != null);
            if(!emptyAround.isEmpty()) {
                List<Location> emptyList = new ArrayList<>(emptyAround);
                this.holeLocation = emptyList.get(r.nextInt(emptyList.size()));
                Hole h = new Hole();
                world.setTile(holeLocation, h); 
                this.hole = h;
                
            }
        }
        //hvis det bliver nat skal kaninen gå til hullet.
        if (world.isNight() && holeLocation != null && world.contains(this)) {
            Object blocking = world.getTile(holeLocation);
            if (blocking == null || blocking == this) {
                world.move(this, holeLocation);
            }
        }
    }
}