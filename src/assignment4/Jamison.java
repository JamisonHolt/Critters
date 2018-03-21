package assignment4;

public class Jamison extends Critter {

    public Jamison() {
        return;
    }

    @Override
    public String toString() {return "J";}

    @Override
    public boolean fight(String not_used) {
        return true;
    }

    @Override
    public void doTimeStep() {
        return;
    }
}
