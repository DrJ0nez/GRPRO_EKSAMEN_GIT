import junit.framework.TestCase;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.Program;
import java.util.*;
import java.util.Set;

public class RabbitTest extends TestCase {

    /**
     * Kaninen kan placeres på kortet (Tema 1: kaniner kan oprettes/placeres).
     */
    public void testRabbitCanBePlacedOnWorld() {
        World w = new World(10);
        Rabbit rabbit = new Rabbit();
        Location loc = new Location(4, 4);

        w.setTile(loc, rabbit);

        assertTrue(w.contains(rabbit));
        assertEquals(rabbit, w.getTile(loc));
    }

    /**
     * Kaninen dør af sult, hvis der ikke er græs i verden.
     * Vi laver en verden uden Plant og lader Program simulere,
     * indtil kaninen sulter ihjel.
     */
    public void testRabbitDiesFromHungerWhenNoGrass() {
        Program p = new Program(5, 800, 600);
        World w = p.getWorld();

        Rabbit rabbit = new Rabbit();
        Location loc = new Location(2, 2);
        w.setTile(loc, rabbit);

        // Ingen Plant i verden → kaninen kan ikke spise.
        for (int i = 0; i < 50 && w.contains(rabbit); i++) {
            p.simulate();
        }

        assertFalse("Kanin bør dø af sult uden grass",
                    w.contains(rabbit));
    }

    /**
     * Kaninen spiser græs:
     * Vi sætter en kanin ved siden af et stykke græs (Plant) og lader
     * simuleringen køre. På et tidspunkt bør Plant på den position være væk,
     * mens kaninen stadig lever.
     */
    public void testRabbitEatsGrassAndSurvives() {
    int size = 5;
    Program p = new Program(size, 800, 600);
    World w = p.getWorld();

    Location rabbitLoc = new Location(2, 2);
    Location plantLoc  = new Location(2, 3);

    Rabbit rabbit = new Rabbit();
    Plant plant   = new Plant();

    w.setTile(rabbitLoc, rabbit);
    w.setTile(plantLoc, plant);

    // Kør simulering indtil græsset er spist, eller vi har prøvet et par gange
    for (int i = 0; i < 10; i++) {
        // Hvis kaninen dør før den når at spise, skal testen fejle – så vi breaker bare ud.
        if (!w.contains(rabbit)) {
            break;
        }

        // Hvis planten allerede er væk, er vi færdige
        Object tileAtPlantBefore = w.getTile(plantLoc);
        if (!(tileAtPlantBefore instanceof Plant)) {
            break;
        }

        p.simulate();
    }

    Object tileAtPlantAfter = w.getTile(plantLoc);

    assertFalse("Græsset på den oprindelige plads bør være væk",
                tileAtPlantAfter instanceof Plant);

    assertTrue("Kaninen bør stadig være i verden efter at have spist græs",
               w.contains(rabbit));
}


    /**
     * Kaninens alder og energi ændrer sig over tid:
     * age skal stige, energy falde, når vi simulerer nogle ticks.
     */
    public void testRabbitAgeAndEnergyChangeOverTime() {
        Program p = new Program(5, 800, 600);
        World w = p.getWorld();

        Rabbit rabbit = new Rabbit();
        Location loc = new Location(1, 1);
        w.setTile(loc, rabbit);

        int initialAge = rabbit.age;
        int initialEnergy = rabbit.energy;

        // 5 ticks er langt fra død (age <= 5, energy >= 10, hunger = 5)
        for (int i = 0; i < 5 && w.contains(rabbit); i++) {
            p.simulate();
        }

        assertTrue("Kanin alder bør increase", rabbit.age > initialAge);
        assertTrue("Kanin energy bør falde", rabbit.energy < initialEnergy);
    }

    /**
     * Kaniner kan reproducere sig.
     * Vi starter med én kanin og lidt græs, og lader simuleringen køre længe.
     * På et tidspunkt bør der være flere kaniner i verden.
     */
    public void testRabbitsCanReproduce() {
    int size = 10;
    Program p = new Program(size, 800, 600);
    World w = p.getWorld();

    // Start med flere kaniner, så sandsynligheden for reproduction er høj
    Location[] startLocs = {
        new Location(4, 4),
        new Location(5, 5),
        new Location(6, 4),
        new Location(4, 6),
        new Location(6, 6)
    };

    for (Location loc : startLocs) {
        w.setTile(loc, new Rabbit());
    }

    // Lidt græs rundt omkring, så kaninerne ikke dør alt for hurtigt af sult
    for (int x = 0; x < size; x++) {
        w.setTile(new Location(x, 0), new Plant());
        w.setTile(new Location(x, 9), new Plant());
    }

    // Sæt af alle koordinater i verden til getAll(...)
    java.util.Set<Location> allLocations = new java.util.HashSet<>();
    for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
            allLocations.add(new Location(x, y));
        }
    }

    int initialCount = w.getAll(Rabbit.class, allLocations).size();
    int maxCount = initialCount;

    // Kør simuleringen et stykke tid – kaninerne har 15 "energy ticks"
    for (int i = 0; i < 20; i++) {
        p.simulate();

        int currentCount = w.getAll(Rabbit.class, allLocations).size();
        if (currentCount > maxCount) {
            maxCount = currentCount;
        }
    }

    assertTrue("Der burde være flere kaniner nu pga reproduktion",
               maxCount > initialCount);
}


    /**
     * Kaniner kan grave huller (eller lave et hul, hvis de ikke har et i forvejen).
     * Din Rabbit har et felt holeLocation, der bliver sat når den laver et hul.
     * Vi tester, at kaninen efter noget tid har fået en holeLocation, og at
     * der findes et NonBlocking objekt (Hole) på den position.
     */
    public void testRabbitCanDigHole() {
        Program p = new Program(10, 800, 600);
        World w = p.getWorld();

        Rabbit rabbit = new Rabbit();
        Location rabbitLoc = new Location(5, 5);
        w.setTile(rabbitLoc, rabbit);

        // Lad simuleringen køre, indtil kaninen har gravet et hul (eller vi stoppe efter max ticks)
        for (int i = 0; i < 200 && w.contains(rabbit) && rabbit.holeLocation == null; i++) {
            p.simulate();
        }

        assertNotNull("Kanin burde have en holeLocation", rabbit.holeLocation);

        assertTrue("Der bør være et NonBlocking objekt (Hole) på holeLocation",
                   w.containsNonBlocking(rabbit.holeLocation));
    }

    /**
     * Når det bliver nat, skal kaninen gå i sit hul.
     * Vi lader kaninen grave et hul og venter på en nat-tick.
     * Når world.isNight() er true, og kaninen har hul, bør den stå på holeLocation.
     */
    public void testRabbitMovesToHoleAtNight() {
        Program p = new Program(10, 800, 600);
        World w = p.getWorld();

        Rabbit rabbit = new Rabbit();
        Location start = new Location(5, 5);
        w.setTile(start, rabbit);

        boolean movedToHoleAtNight = false;

        // Først skal kaninen have gravet hul, derefter skal det nå at blive nat.
        for (int i = 0; i < 500 && w.contains(rabbit); i++) {
            p.simulate();

            if (rabbit.holeLocation != null && w.isNight() && w.contains(rabbit)) {
                Location current = w.getLocation(rabbit);
                if (current != null && current.equals(rabbit.holeLocation)) {
                    movedToHoleAtNight = true;
                    break;
                }
            }
        }

        assertTrue("Kanin burde være i dens hul om natten",
                   movedToHoleAtNight);
    }
}
