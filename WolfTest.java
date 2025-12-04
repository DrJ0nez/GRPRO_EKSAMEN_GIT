
import itumulator.world.World;
import itumulator.world.Location;
import junit.framework.TestCase;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WolfTest extends junit.framework.TestCase {
    
    //Test for K2-1a (Ulve kan placeres på kortet)
    public void testWolfCanBePlacedOnWorld() {
        World w = new World(10);
        WolfPack pack = new WolfPack(1);
        Wolf wolf = new Wolf();
        wolf.setWolfPack(pack);
        pack.addWolf(wolf);

        Location loc = new Location(3, 3);
        w.setTile(loc, wolf);

        assertTrue(w.contains(wolf));
        assertEquals(wolf, w.getTile(loc));
        assertEquals(1, pack.getPackSize());
    }
    
    public void testWolfDiesFromStarvation() {
        World w = new World(5);
        WolfPack pack = new WolfPack(1);
        Wolf wolf = new Wolf();
        wolf.setWolfPack(pack);
        pack.addWolf(wolf);

        Location loc = new Location(2, 2);
        w.setTile(loc, wolf);

        // Kør mange ticks uden mad → ulven bør dø (hunger > 10)
        for (int i = 0; i < 30 && w.contains(wolf); i++) {
            wolf.act(w);
        }

        assertFalse("Ulven burde fjernes fra verdenen når dens hunger bliver for høj", w.contains(wolf));
    }
    
    public void testWolfHuntsRabbit() {
        World w = new World(10);

        WolfPack pack = new WolfPack(1);
        Wolf wolf = new Wolf();
        wolf.setWolfPack(pack);
        pack.addWolf(wolf);

        Rabbit rabbit = new Rabbit();

        Location wolfLoc = new Location(2, 2);
        Location rabbitLoc = new Location(2, 3); // tæt på ulven

        w.setTile(wolfLoc, wolf);
        w.setTile(rabbitLoc, rabbit);

        // Ulven bør finde og spise kaninen
        wolf.act(w);

        assertTrue("Ulven burde stadig eksistere", w.contains(wolf));
        assertFalse("Kaninen burde være blevet spist af ulven", w.contains(rabbit));
    }
    
    public void testWolvesMoveTowardsPackMates() {
        World w = new World(15);
    
        WolfPack pack = new WolfPack(1);
        Wolf wolf1 = new Wolf();
        Wolf wolf2 = new Wolf();
    
        wolf1.setWolfPack(pack);
        wolf2.setWolfPack(pack);
        pack.addWolf(wolf1);
        pack.addWolf(wolf2);
    
        Location loc1 = new Location(5, 5);
        Location loc2 = new Location(8, 5);
    
        w.setTile(loc1, wolf1);
        w.setTile(loc2, wolf2);
    
        // Start-afstand (Manhattan-distance)
        int initialDist =
            Math.abs(loc1.getX() - loc2.getX()) +
            Math.abs(loc1.getY() - loc2.getY());
    
        int minDist = initialDist;
    
        // Vi kører kun de første 8 "ticks" for at undgå sultedød.
        for (int i = 0; i < 8; i++) {
            wolf1.act(w);
    
            // Hvis en af ulvene er død / fjernet fra verdenen, stopper vi løkken.
            if (!w.contains(wolf1) || !w.contains(wolf2)) {
                break;
            }
    
            Location newLoc1 = w.getLocation(wolf1);
            Location newLoc2 = w.getLocation(wolf2);
    
            // Safety – burde ikke ske, men undgåer "Object does not exist...":
            if (newLoc1 == null || newLoc2 == null) {
                break;
            }
    
            int d =
                Math.abs(newLoc1.getX() - newLoc2.getX()) +
                Math.abs(newLoc1.getY() - newLoc2.getY());
    
            if (d < minDist) {
                minDist = d;
            }
        }
    
        assertTrue(
            "Ulv1 burde bevæge sig nærmere ens dens startposition ift. packmate",
            minDist < initialDist
        );
    }


    
    public void testWolfBuildsCaveAndSharesPackCave() {
        World w = new World(10);

        WolfPack pack = new WolfPack(1);
        Wolf wolf1 = new Wolf();
        Wolf wolf2 = new Wolf();

        wolf1.setWolfPack(pack);
        wolf2.setWolfPack(pack);
        pack.addWolf(wolf1);
        pack.addWolf(wolf2);

        Location loc1 = new Location(3, 3);
        Location loc2 = new Location(4, 4);

        w.setTile(loc1, wolf1);
        w.setTile(loc2, wolf2);

        // Første gang en ulv handler, bør den bygge en hule
        wolf1.act(w);

        Location caveLoc = pack.getCaveLocation();
        assertNotNull("Pack burde have en cavelocation efter første act", caveLoc);

        // Begge ulve bør have samme caveLocation som flokken
        assertEquals("Wolf1 caveLocation burde matche pack caveLocation",
                     caveLoc, wolf1.getCaveLocation());
        assertEquals("Wolf2 caveLocation burde matche pack caveLocation",
                     caveLoc, wolf2.getCaveLocation()); 
        }
        
    public void testWolfFight() {
        World w = new World(10);

        WolfPack pack = new WolfPack(1);
        Wolf wolf = new Wolf();
        wolf.setWolfPack(pack);
        pack.addWolf(wolf);
        Location wolfEtLocation = new Location(3, 3);
        w.setTile(wolfEtLocation ,wolf);

        WolfPack packTo = new WolfPack(2);
        Wolf wolfTo = new Wolf();
        wolf.setWolfPack(packTo);
        packTo.addWolf(wolf);
        Location wolfToLocation = new Location(4, 4);
        w.setTile(wolfToLocation, wolfTo);

        wolf.dealDamage(wolfTo);

        assertEquals(75, wolfTo.getCurrentHealth()); 
    }
}