import itumulator.world.World;
import itumulator.simulator.Actor;

public abstract class Animal implements Actor {
    protected int hunger;
    protected int energy;
    protected int age = 0;
    protected int maxAge;
    protected int maxEnergy;
    protected int hp = 100;
    protected int damage;

    public Animal(int maxAge, int maxEnergy, int damage) {
        this.maxAge = maxAge;
        this.maxEnergy = maxEnergy;
        this.hunger = 0;
        this.energy = maxEnergy;
        this.damage = damage;
    }

    @Override
    public void act(World world) {
        // hver tick bliver dyret mere sulten og ældre og mister energi.
        hunger++;
        age++;
        energy--;

        // her er betingelserne for hvornår et dyr dør.
        if (hunger > 100 || energy <= 0 || age >= maxAge) {
            world.delete(this);
            return;
        }
        
    }
    
    public int getCurrentHealth() {
        return hp;
    } 
    
    public void receiveDamage(int damage) {
        hp -= damage;
    }

    public void dealDamage(Animal target) {
            target.receiveDamage(this.damage);
    }
    
    /*public void eat(World world, Actor food) {
        world.delete(food);
        hunger = 0;
        energy = Math.min(energy + 20, maxEnergy);
        hp = Math.min(hp + 10, maxHp); // Man heler lidt ved at spise
    }*/

}