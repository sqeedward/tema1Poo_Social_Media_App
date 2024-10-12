package TemaTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;


// Comentarii domnule, comentarii. :3
public class Comentariu implements Likeable{
    String text;
    private int likes;
    public Postare post;
    private final Utilizator account;
    private final ArrayList<Utilizator>fans;

    private final Date data;
    private final int id;
    private static int commentCount = 1;
    private static ArrayList<Comentariu>allComments = new ArrayList<Comentariu>();

    //nu o sa adaug comentarii repetitive ca seama cu restul claselor

    public Comentariu(String text, Postare post ,Utilizator account) {
        this.text = text;
        fans = new ArrayList<Utilizator>();
        this.account = account;
        this.post = post;
        this.id = commentCount++;
        this.data = new Date();
    }

    public static Comentariu findComment(int id) {
        for (Comentariu comentariu: allComments)
            if (comentariu.id == id)
                return comentariu;
        return null;
    }


    public boolean canDelete(Utilizator username) {
        return (this.post.getOwner() == username) || (this.account == username);
    }


    /**
     * Functia adauga un comentariu la o postare.
     * Trebuie apelata in primul rand de postare, astfel sa se creeze o relatie intre cele 2.
     * @param text textul comentariului
     * @param post postarea la care se adauga comentariul.
     * @param account contul care adauga comentariul.
     * @return comentariul creat.
     */
    public static Comentariu addComment(String text, Postare post, Utilizator account) {
        Comentariu comment = new Comentariu(text, post, account);
        allComments.add(comment);

        return comment;
    }

    public static void deleteComment(Comentariu comment) {
        allComments.remove(comment);
    }


     //functia reseteaza numarul de comentarii si lista de comentarii.
    public static void resetComments() {
        commentCount = 1;
        allComments = new ArrayList<Comentariu>();
    }

    public Postare getPost() {
        return post;
    }

    public int getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public String getText() {
        return text;
    }

    public Utilizator getAccount() {
        return account;
    }

    public static ArrayList<Comentariu> getAllComments() {
        return allComments;
    }

    public Date getData() {
        return data;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Functia verifica daca un utilizator a dat like la un comentariu.
     * @param username numele de utilizator
     * @return o gasca si o rata
     */
    public boolean alreadyLiked(String username) {
        for (Utilizator acc : fans) {
            if (acc.getUsername().equals(username))
                return true;
        }
        return false;
    }


    @Override
    public void giveLike(Utilizator user) {
        likes++;
        fans.add(user);
    }

    @Override
    public void giveDislike(Utilizator user) {
        likes--;
        fans.remove(user);
    }

    @Override
    public boolean canLike(String username) {
        return !alreadyLiked(username) && !account.getUsername().equals(username);
    }
}
