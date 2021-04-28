 /**
 *
 * @author brahm
 * @author A.L.I.C.E
 */
public class Contrat {
    private int id;
    private String description;
    private String type;
    
    public Contrat() {
      }

    public Contrat(int id, String description, String type) {
        this.id = id;
        this.description = description;
        this.type = type;
    }
    
    public Contrat(String description, String type) {
        this.description = description;
        this.type = type;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Contrat{" + "id=" + id + ", description=" + description + ", type=" + type + '}';
    }
    
    
}
