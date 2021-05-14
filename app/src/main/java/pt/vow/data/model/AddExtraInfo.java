package pt.vow.data.model;

public class AddExtraInfo {

    String children;
    String nature;
    String health;
    String houseBuilding;
    String eldery;
    String animals;
    Byte[] image;

    public AddExtraInfo(Byte[] image, String children, String nature, String health, String houseBuilding, String eldery, String animals) {
        this.image = image;
        this.children = children;
        this.animals = animals;
        this.nature = nature;
        this.eldery = eldery;
        this.health = health;
        this.houseBuilding = houseBuilding;
    }

    public String getChildren() {
        return children;
    }

    public String getHealth() {
        return health;
    }

    public String getNature() {
        return nature;
    }

    public String getAnimals() {
        return animals;
    }

    public String getEldery() {
        return eldery;
    }

    public String getHouseBuilding() {
        return houseBuilding;
    }

    public Byte[] getImage() {
        return image;
    }
}
