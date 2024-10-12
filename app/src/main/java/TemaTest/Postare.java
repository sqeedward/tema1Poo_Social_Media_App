package TemaTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

// Cea mai grea clasa din proiect, imi vine sa plang.
public class Postare implements Likeable{
    private final String text;
    private final Utilizator owner;
    private final int id;
    private static int postCounter = 1;
    private int likes;
    private Date data;
    private static ArrayList<Postare> allPosts = new ArrayList<Postare>();
    private final ArrayList<Utilizator> fans;
    private final ArrayList<Comentariu> comments;



    public Postare(String text, Utilizator owner) {
        this.text = text;
        this.owner = owner;
        fans = new ArrayList<Utilizator>();
        comments = new ArrayList<Comentariu>();
        this.id = postCounter;
        postCounter++;
        this.data = new Date();
        allPosts.add(this);
    }


    //ca la restul claselor, functia reseteaza numarul de postari si lista de postari.
    public static Postare findPost(int id) {
        for (Postare post: allPosts) {
            if (post.getId() == id)
                return post;
        }
        return null;
    }

    public static void deletePostEver(Postare post) {
        allPosts.remove(post);
    }


    public static void reset() {
        postCounter = 1;
        allPosts = new ArrayList<Postare>();
    }


    public Utilizator getOwner() {
        return owner;
    }


    public boolean alreadyLiked(String username) {
        for (Utilizator user: fans) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public void addComment(String text, Utilizator user) {
        Comentariu comment = Comentariu.addComment(text, this, user);
        comments.add(comment);
    }

    public void deleteComment(Comentariu comment) {
        Comentariu.deleteComment(comment);
        comments.remove(comment);
    }

    /**
     * Functia returneaza un string cu toate detaliile postarii, atat ale postarii cat si ale comentariilor.
     * O postare are format json.
     * @return urasc sa scriu commenturi in java ca e clar ce se returneaza.
     */
    public String postDetails() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(data);
        String text = "{'status' : 'ok', 'message' : [{'post_text' : '" + this.text +
                "', 'post_date' :'" + date + "', 'username' : '" +
                 this.owner.getUsername() + "', 'number_of_likes' : '" + this.likes +
                "', 'comments' : [";
        for (Comentariu comment : comments) {
            text += "{'comment_id' : '" + comment.getId() + "' , 'comment_text' : '" +
                    comment.getText() + "', 'comment_date' : '" +dateFormat.format(comment.getData())+
                    "', 'username' : '" + comment.getAccount().getUsername() +
                    "', 'number_of_likes' : '" + comment.getLikes() + "'},";
        }
        text = text.substring(0,text.length()-1);
        text += "] }] }";
        return text;
    }

    //functia returneaza un string cu toate detaliile postarii fara mesaje json si fara comentarii.
    public String postDetailsNoMessage() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(data);
        String text = "{'post_id' : '"+this.id + "','post_text' : '" + this.text +
                "', 'post_date' :'" + date + "', 'username' : '" +
                this.owner.getUsername() + "', 'number_of_likes' : '" + this.likes + "'},";
        return text;
    }

    //la fel dar cu nr de comentarii
    public String postDetailsNoMessageCommentAmmount() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(data);
        String text = "{'post_id' : '"+this.id + "','post_text' : '" + this.text +
                "', 'post_date' :'" + date + "', 'username' : '" +
                this.owner.getUsername() + "', 'number_of_comments' : '"
                +comments.size()+"' },";
        return text;
    }

    private static class comparamLikes implements Comparator<Postare> {

        @Override
        public int compare(Postare post1, Postare post2) {
            return Integer.compare(post2.likes, post1.likes);
        }
    }

    private static class comparamComments implements Comparator<Postare> {

        @Override
        public int compare(Postare post1, Postare post2) {

            return Integer.compare(post2.comments.size(), post1.comments.size());
        }
    }

    /**
     * Functia returneaza top 5 postari in ordine descrescatoare dupa numarul de like-uri.
     * @return un iepuras dragalas sub forma de json.
     */
    public static String Top5Posts() {
        ArrayList<Postare> postari = new ArrayList<Postare>(allPosts);
        Collections.sort(postari, new comparamLikes());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String data;
        String text =  "{'status' : 'ok', 'message' : [";

        Postare post = null;

        for (int i = 0 ; i < Math.min(5, allPosts.size()) ;i++) {
            post = postari.get(i);
            text += post.postDetailsNoMessage();
        }
        text = text.substring(0,text.length()-1);
        text += " ]}";

        return text;
    }

    public static String Top5PostsbyComments() {
        ArrayList<Postare> postari = new ArrayList<Postare>(allPosts);
        Collections.sort(postari, new comparamComments());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String data;
        String text =  "{'status' : 'ok', 'message' : [";

        Postare post = null;

        for (int i = 0 ; i < Math.min(5, allPosts.size()) ;i++) {
            post = postari.get(i);
            text += post.postDetailsNoMessageCommentAmmount();
        }
        text = text.substring(0,text.length()-1);
        text += "]}";

        return text;
    }

    @Override
    public boolean canLike(String username) {
        return !alreadyLiked(username) && !owner.getUsername().equals(username);
    }


    @Override
    public void giveLike(Utilizator user) {
        fans.add(user);
        likes++;
    }

    @Override
    public void giveDislike(Utilizator user) {
        fans.remove(user);
        likes--;
    }

    public int getLikes() {
        return likes;
    }

    public ArrayList<Comentariu> getComments() {
        return comments;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public Date getData() {
        return data;
    }
}
