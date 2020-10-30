package up.visulog.gitrawdata;

public class Author {
    private final int id;
    private final String name;
    private final String username;
    public Author(int id,String name,String username){
        this.id = id;
        this.name = name;
        this.username = username;
    }
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
            return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
