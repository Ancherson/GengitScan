package up.visulog.gitrawdata;

public class Member {
    private int id;
    private String name;
    private String username;
    private String avatar_url;
    private String web_url;
    public Member(int id,String name,String username,String avatar_url,String web_url){
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatar_url = avatar_url;
        this.web_url = web_url;
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

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getWeb_url() {
        return web_url;
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
