package TemaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Clasa AllUsers este o clasa care contine toti utilizatorii din aplicatie.
 */
public class AllUsers {
    private int nrUtilizatori;
    private ArrayList<Utilizator> users;


    public AllUsers() {
        this.nrUtilizatori = 0;
        this.users = new ArrayList<Utilizator>();
    }

    /**
     * Functia adauga un utilizator nou in lista de utilizatori.
     * @param username numele de utilizator
     * @param password  parola
     */
    public void addUser(String username, String password) {
        Utilizator user = new Utilizator(username, password);

        users.add(user);
        nrUtilizatori++;
    }

    /**
     * Functia gaseste un utilizator dupa numele de utilizator.
     * @param username numele de utilizator
     * @return Utilizatorul cu numele de utilizator dat ca parametru altfel null
     */
    public Utilizator findUser(String username) {
        for (Utilizator user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    /**
     * Functia verifica daca exista un utilizator cu numele de utilizator dat ca
     * si cu parola data ca parametru sa fie aceeasi cu parola utilizatorului.
     * @param username numele de utilizator
     * @param password parola de utilizator
     *@return Utilizatorul cu numele de utilizator dat ca parametru altfel null
     */
    public Utilizator loginUser(String username, String password) {
        Utilizator account = findUser(username);

        if (account == null || !account.getPassword().equals(password))
            return null;

        return account;
    }

    //clasa folosita pentru a compara utilizatorii dupa numarul de followers
    private static class comparamFollowers implements Comparator<Utilizator> {
        /**
         * Functia compara doi utilizatori descrescator dupa numarul de followers.
         * @param user1 primul utilizator
         * @param user2 al doilea utilizator
         * @return 0 daca user1 == user2 , >0 daca user1 > user2
         */
        @Override
        public int compare(Utilizator user1, Utilizator user2) {
            return Integer.compare(user2.getFollowersSize(), user1.getFollowersSize());
        }
    }

    //functia returneaza top 5 utilizatori cu cei mai multi followers in ordine descrescatoare
    //si formatul este sub forma de json
    public String top5FollowedUsers() {
        ArrayList<Utilizator> allusers = new ArrayList<Utilizator>(users);
        Collections.sort(allusers, new comparamFollowers());

        Utilizator currentUser = null;
        String text = "{'status' : 'ok', 'message' : [";
        for (int i = 0 ; i < Math.min(5, allusers.size()); i++) {
            currentUser = allusers.get(i);
            text += "{'username':'"+currentUser.getUsername()+"','number_of_followers':'"+currentUser.getFollowersSize()+"'},";
        }
        text = text.substring(0, text.length()-1);
        text += " ]}";

        return text;
    }

    private static class comparamLikes implements Comparator<Utilizator> {

        @Override
        public int compare(Utilizator user1, Utilizator user2) {
            return Integer.compare(user2.getLikesCount(), user1.getLikesCount());
        }
    }

    public String top5LikedUsers() {
        ArrayList<Utilizator> allusers = new ArrayList<Utilizator>(users);
        Collections.sort(allusers, new comparamLikes());

        Utilizator currentUser = null;
        String text = "{'status' : 'ok', 'message' : [";
        for (int i = 0 ; i < Math.min(5, allusers.size()); i++) {
            currentUser = allusers.get(i);
            text += "{'username' : '"+currentUser.getUsername()+"','number_of_likes' : '"+currentUser.getLikesCount()+"'},";
        }
        text = text.substring(0, text.length()-1);
        text += "]}";

        return text;
    }

    /**
     * Functia toString clasica din java care returneaza informatiile dintr-o clasa sub forma
     * de string. Am facut-o sa fie sub format json, pt ca poate o sa ma ajute pe viitor.
     * S-a dovedit a fi inutila dar am lasat-o aici pentru ca nu strica.
     * Initial creata ca sa fac debug.
     * @return String sub format json ce contine numele utilizatorilor
     */
    public String toString(){
        //folosim StringBuilder ca sa nu cream multe referinte, sa umplem memoria
        StringBuilder fullString = new StringBuilder();
        if (nrUtilizatori == 0)
            return fullString.toString();
        for (Utilizator user: this.users) {
            fullString.append("{'").append(user.getUsername()).append("':");
            fullString.append("'").append(user.getPassword()).append("'}\n");
        }
        fullString.deleteCharAt(fullString.length()-1);

        return fullString.toString();
    }

}
