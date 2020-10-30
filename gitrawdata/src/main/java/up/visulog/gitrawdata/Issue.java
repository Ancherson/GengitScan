package up.visulog.gitrawdata;

public class Issue {
    private int iid;
    private Author author;
    public Issue(int iid,Author author){
        this.iid = iid;
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public int getIid() {
        return iid;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "iid=" + iid +
                ", author=" + author +
                '}';
    }
}
