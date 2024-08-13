package beans;

public class Chocolate {
    private int id;
    private String name;
    private double price;
    private String chocolateKind;
    private String chocolateType;
    private double weight;
    private String description;
    private String image;
    private String status;
    private int quantity;
    private int factoryId;

    // Empty constructor
    public Chocolate() {}

    // Constructor with parameters
    public Chocolate(int id, String name, double price, String chocolateKind, String chocolateType, double weight, String description, String image, String status, int quantity, int factoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.chocolateKind = chocolateKind;
        this.chocolateType = chocolateType;
        this.weight = weight;
        this.description = description;
        this.image = image;
        this.status = status;
        this.quantity = quantity;
        this.factoryId = factoryId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getChocolateKind() {
        return chocolateKind;
    }

    public String getChocolateType() {
        return chocolateType;
    }

    public double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getFactoryId() {
        return factoryId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setChocolateKind(String chocolateKind) {
        this.chocolateKind = chocolateKind;
    }

    public void setChocolateType(String chocolateType) {
        this.chocolateType = chocolateType;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setFactoryId(int factoryId) {
        this.factoryId = factoryId;
    }
}
