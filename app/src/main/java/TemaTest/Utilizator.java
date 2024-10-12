package TemaTest;

import org.checkerframework.checker.units.qual.A;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//clasa Utilizator reperezinta un utilizator al aplicatiei
public class Utilizator {
    private String username;
    private String password;
    private ArrayList<Postare> posts;
    private ArrayList<Utilizator> following;
    private ArrayList<Utilizator> followers;

    public Utilizator() {
        /*Empty users*/
        posts = new ArrayList<Postare>();
        following = new ArrayList<Utilizator>();
    }

    /**
     * Constructorul clasei Utilizator initializeaza un utilizator cu
     * numele de utilizator si parola date ca parametru dar si cu
     * o lista de postari si o lista de utilizatori pe care ii urmareste.
     * @param username numele de utilizator
     * @param password parola de utilizator
     */
    public Utilizator(String username, String password) {
        this.username = username;
        this.password = password;
        posts = new ArrayList<Postare>();
        following = new ArrayList<Utilizator>();
        followers = new ArrayList<Utilizator>();
    }

    public void addPost(Postare post) {
        posts.add(post);
    }

    public void followSomeone(Utilizator user) {
        following.add(user);
        user.getFollower(this);
    }

    public void getFollower(Utilizator user) {
        followers.add(user);
    }


    //functia verifica daca un utilizator urmareste un alt utilizator
    public boolean alreadyFollow(String username) {
        for (Utilizator user: following) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    /**
     * Functia verifica daca un utilizator are permisiunea de a vedea
     * @param post postarea pe care vrem sa o vedem daca avem permisiunea.
     * @return true daca avem permisiunea de a vedea postarea altfel false.
     */
    public boolean permissionToView(Postare post) {
        return this.alreadyFollow(post.getOwner().getUsername()) || this == post.getOwner();
    }


    /**
     * Functie care gaseste o postare dupa id.
     * @param id id-ul postarii
     *@return postarea cu id-ul dat ca parametru altfel null
     */
    public Postare findPost(int id) {
        for (Postare post: posts) {
            if (post.getId() == id)
                return post;
        }
        return null;
    }

    public void deletePost(Postare postare) {
        Postare.deletePostEver(postare);
        boolean ret = posts.remove(postare);
        if (!ret)
            System.out.println("An error occurred when deleting a post.");
    }


    private class comparamDate implements Comparator<Postare> {
        /**
         * Pentru ca datele pot fi trimise in aceasi secunda ne
         * luam dupa id ca sa facem referinta care postare vine
         * inaintea carei postari. Altfel reusim sa sortam corect.
         * @param post1 data1
         * @param post2 data2
         * @return 0 daca post1 == post2 , >0 daca post1 > post2
         */
        @Override
        public int compare(Postare post1, Postare post2) {
            int dateTime = post2.getData().compareTo(post1.getData());
            if (dateTime == 0) {
                return post2.getId() - post1.getId();
            }
            return  dateTime;
        }
    }

    /**
     * Functia returneaza toate postarile utilizatorului in ordine
     * descrescatoare dupa data postarii si id in caz ca sunt trimise
     * in aceeasi secunda.
     * @return un string cu toate postarile utilizatorului in ordine
     * descrescatoare dupa data postarii.
     */
    public String getPostsDescendent() {
        ArrayList<Postare> postari = new ArrayList<Postare>();
        for (Utilizator user : this.following) {
            postari.addAll(user.posts);
        }
        Collections.sort(postari, new comparamDate());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String data;
        String text = "{ 'status' : 'ok', 'message' : [ ";

        for (Postare post : postari) {
            data = dateFormat.format(post.getData());
            text += "{'post_id' : '" + post.getId()+"', 'post_text' : '" +
                    post.getText() + "', 'post_date' : '" + data +
                    "', 'username' : '" + post.getOwner().getUsername()+"'},";
        }
        text = text.substring(0,text.length()-1);
        text += "]}";
        return text;
    }

    //aceasi functie ca si cea de mai sus doar ca returneaza postarile in ordine descrescatoare
    //dupa numarul de like-uri
    public String getPostsDescendentUser(Utilizator account) {
        ArrayList<Postare> postari = new ArrayList<Postare>();
            postari.addAll(account.posts);

            Collections.sort(postari, new comparamDate());

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String data;
            String text = "{ 'status' : 'ok', 'message' : [";

            for (Postare post : postari) {
                data = dateFormat.format(post.getData());
                text += "{'post_id' : '" + post.getId()+"', 'post_text' : '" +
                        post.getText() + "', 'post_date' : '" + data+"'},";
            }
            text = text.substring(0,text.length()-1);
            text += "]}";
            return text;
    }


    /**
     * Functia retureaza un string cu toti utilizatorii pe care ii urmareste.
     * @return un string cu asta.
     */
    public String viewFollowing() {
        String text = "{ 'status' : 'ok', 'message' : [";
        for (Utilizator follow : following) {
            text += " '" + follow.getUsername() + "',";
        }
        text = text.substring(0, text.length()-1);
        text += " ]}";
        return text;
    }

    /**
     * Functia retureaza un string cu toti utilizatorii care il urmaresc.
     * @return un string cu asta dar cu fani adevarati.
     */
    public String viewFollowers() {
        String text = "{ 'status' : 'ok', 'message' : [";
        for (Utilizator follow : followers) {
            text += " '" + follow.getUsername() + "',";
        }
        text = text.substring(0, text.length()-1);
        text += " ]}";
        return text;
    }

    /**
     * Functia returneaza numarul de like-uri pe care le are un utilizator.
     * In functie de likeurile de la postari si de la comentarii pe care
     * le detine, nu neaparat pe postarea curenta.
     * @return nr like-uri.
     */
    public int getLikesCount() {
        int likes = 0;

        for (Postare post : posts) {
            likes += post.getLikes();
        }
        for (Comentariu comment : Comentariu.getAllComments()) {
           if (comment.getAccount() == this) {
               likes += comment.getLikes();
           }
        }
        return likes;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



    public int getFollowersSize() {
        return followers.size();
    }
}
