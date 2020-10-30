package up.visulog.gitrawdata;

public class Member {
    private int id;
    private String name;
    private String username;
    public Member(int id,String name,String username){
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
