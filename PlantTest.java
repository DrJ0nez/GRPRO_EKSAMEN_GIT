import junit.framework.TestCase;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;

import java.util.Set;
import java.util.HashSet;

public class PlantTest extends TestCase {

    public void testGrassCanBePlacedOnWorld() {
        World w = new World(10);
        Plant plant = new Plant();
        Location loc = new Location(3, 4);

        w.setTile(loc, plant);

        assertTrue(w.contains(plant));
        assertEquals(plant, w.getTile(loc));
    }

    public void testGrassSpreadsOverTime() {
        int size = 5;
        Program p = new Program(size, 800, 600);
        World w = p.getWorld();

        Location start = new Location(2, 2);
        w.setTile(start, new Plant());

        for (int i = 0; i < 50; i++) {
            p.simulate();
        }

        // Byg et sæt af ALLE koordinater i verden
        Set<Location> allLocations = new HashSet<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                allLocations.add(new Location(x, y));
            }
        }

        Set<Plant> plants = w.getAll(Plant.class, allLocations);

        assertTrue("Der burde være mere en et græs efter spredning",
                   plants.size() > 1);
    }

    public void testAnimalCanStandOnGrassWithoutRemovingIt() {
        World w = new World(5);
        Location loc = new Location(2, 2);

        Plant grass = new Plant();
        Rabbit rabbit = new Rabbit();

        w.setTile(loc, grass);
        w.setTile(loc, rabbit);

        assertEquals(rabbit, w.getTile(loc));
        assertTrue("Der burde være et nonblocking object (grass) på denne tile",
                   w.containsNonBlocking(loc));
    }
}
