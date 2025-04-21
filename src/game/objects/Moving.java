package game.objects;

public interface Moving {

    int getEnergy();
    void setEnergy(int energy);
    int getDefEnergy();

    default boolean haveEnergyToMove(int penalty) {
        return getEnergy() - penalty >= 0;
    }

    default void changeEnergy(int penalty) {
        setEnergy(getEnergy() - penalty);
    }

    default void setDefaultEnergy() {
        setEnergy(getDefEnergy());
    }
}