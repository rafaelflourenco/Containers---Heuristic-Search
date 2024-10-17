import java.util.Objects;

class Container {
    private char id;
    private int cost;

    public Container(char id, int cost) {
        this.id = id;
        this.cost = cost;
    }

    public char getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Check if both references are to the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Ensure the other object is not null and is the same class
        }
        Container other = (Container) obj;
        return id == other.id; // Compare based on id and cost
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, cost);
    }

    @Override
    public String toString() {
        return "Container{id=" + id + ", cost=" + cost + "}";
    }
}
