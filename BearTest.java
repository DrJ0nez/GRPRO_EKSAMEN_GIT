import junit.framework.TestCase;
import itumulator.world.World;
import itumulator.world.Location;

public class BearTest extends TestCase {

    /**
     * K2-4a: Bjørne kan placeres på kortet når inputfilen beskriver det.
     * Her tester vi blot, at en Bear kan sættes i verdenen.
     */
    public void testBearCanBePlacedOnWorld() {
        World w = new World(10);
        Location territoryCenter = new Location(3, 5);
        Bear bear = new Bear(territoryCenter);

        Location spawn = new Location(2, 2);
        w.setTile(spawn, bear);

        assertTrue(w.contains(bear));
        assertEquals(bear, w.getTile(spawn));
    }

    /**
     * K2-4b: Bjørne jager ligesom ulve og spiser alt (dvs. andre dyr).
     * Vi tester, at en bjørn spiser en kanin i nærheden.
     */
    public void testBearHuntsRabbit() {
        World w = new World(10);
        Location territoryCenter = new Location(5, 5);
        Bear bear = new Bear(territoryCenter);
        Rabbit rabbit = new Rabbit();

        Location bearLoc = new Location(5, 5);
        Location rabbitLoc = new Location(6, 5);

        w.setTile(bearLoc, bear);
        w.setTile(rabbitLoc, rabbit);

        // Gør bjørnen lidt sulten så hunt/forage faktisk aktiveres
        for (int i = 0; i < 8; i++) {
            bear.act(w);
        }

        assertFalse("Kanin burde være spist af bjørn", w.contains(rabbit));
        assertTrue("Bjørnen burde stadig være i verdenen", w.contains(bear));
    }

    /**
     * K2-5a: Bjørnen er territoriel og bevæger sig sjældent ud af sit område.
     * moveInTerritory() holder bjørnen inden for distance < 4 fra territoriets centrum.
     * Vi tester, at den efter mange ticks stadig er inden for radius.
     */
    public void testBearStaysWithinTerritoryRadius() {
    World w = new World(20);
    Location territoryCenter = new Location(10, 10);
    Bear bear = new Bear(territoryCenter);

    Location start = new Location(10, 10);
    w.setTile(start, bear);

    for (int i = 0; i < 15 && w.contains(bear); i++) {
        bear.act(w);

        // Hvis bjørnen stadig er i verden, tjek dens distance
        if (w.contains(bear)) {
            Location current = w.getLocation(bear);
            int dist =
                Math.abs(current.getX() - territoryCenter.getX()) +
                Math.abs(current.getY() - territoryCenter.getY());

            assertTrue("Bjørn burde være indenfor radius < 4 af dens territory center", dist < 4);
        }
    }
}


    /**
     * K2-6a: Bjørne spiser bær fra buske (BerryBush) i området.
     * Bear.forage() bruger BerryBush.eatBerry(world), der laver BerryBush -> Bush.
     * Vi tester, at en BerryBush bliver til en Bush når bjørnen har fået nok hunger til at spise bær.
     */
    public void testBearEatsBerriesAndBerryBushBecomesBush() {
        World w = new World(10);
        Location territoryCenter = new Location(5, 5);
        Bear bear = new Bear(territoryCenter);

        Location bearLoc = new Location(5, 5);
        Location berryLoc = new Location(5, 6);

        BerryBush berryBush = new BerryBush();

        w.setTile(bearLoc, bear);
        w.setTile(berryLoc, berryBush);

        // Gør bjørnen sulten nok ved at kalde act flere gange.
        // For at sikre at den stadig er ved siden af bærrene, flytter vi den tilbage, hvis den flytter sig.
        for (int i = 0; i < 15; i++) {
            bear.act(w);

            Location current = w.getLocation(bear);
            if (!(current.getX() == bearLoc.getX() && current.getY() == bearLoc.getY())) {
                // Flyt tilbage til ved siden af busken
                w.move(bear, bearLoc);
            }
        }

        Object tile = w.getTile(berryLoc);
        assertTrue("BerryBush burde være blevet til bush efter den er spist",
                   tile instanceof Bush);
    }
}
