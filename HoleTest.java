import junit.framework.TestCase;
import itumulator.world.World;
import itumulator.world.Location;

public class HoleTest extends TestCase {

    /**
     * K1-3a: Huller kan blive indsat når inputfiler beskriver det,
     * eller graves af kaniner.
     * Her tester vi den simple del: at et Hole kan sættes i verdenen.
     * (Selve "kaninen graver hul"-delen er dækket i RabbitTest.testRabbitCanDigHole).
     */
    public void testHoleCanBePlacedOnWorld() {
        World w = new World(10);
        Hole hole = new Hole();
        Location loc = new Location(1, 1);

        w.setTile(loc, hole);

        // Hole er NonBlocking, så getTile kan evt. returnere null eller noget andet,
        // men vi kan teste med containsNonBlocking:
        assertTrue("Der nburde være et nonblocking hole på denne tile",
                   w.containsNonBlocking(loc));
    }

    /**
     * K1-3b: Dyr kan stå på et kaninhul uden der sker noget.
     * Hole implementerer NonBlocking, så et dyr kan stå på samme felt.
     */
    public void testAnimalCanStandOnHole() {
        World w = new World(5);
        Location loc = new Location(2, 2);

        Hole hole = new Hole();
        Rabbit rabbit = new Rabbit();

        // Først hul (NonBlocking), dernæst kanin (blocking)
        w.setTile(loc, hole);
        w.setTile(loc, rabbit);

        // Kaninen er den "blocking" aktør på feltet:
        assertEquals(rabbit, w.getTile(loc));

        // Der bør stadig være et NonBlocking objekt (hullet) på feltet:
        assertTrue("Hole burde stadig eksistere som nonblocking tile på dette felt",
                   w.containsNonBlocking(loc));
    }
}
