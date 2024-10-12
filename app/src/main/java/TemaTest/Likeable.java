package TemaTest;

/**
 * Interfata Likeable este o interfata care este implementata
 * tipurile de obiecte care pot primi like-uri. Facut pentru
 * a avea un cod citibil dar si pentru ca, 99% din motiv, sa
 * indeplinesc cerinta.
 */
public interface Likeable {
    /**
     * Functia da un like unui obiect de tip Likeable.
     * @param user fanul care doreste sa dea like.
     */
    public void giveLike(Utilizator user);

    /**
     * Functia da un dislike unui obiect de tip Likeable.
     * @param user omul rau care scoate like.
     */
    public void  giveDislike(Utilizator user);

    /**
     * Functia verifica daca un utilizator poate da like la un obiect de tip Likeable.
     * @param username omul care doreste sa ne placa.
     * @return true daca nu e fan inca , false altfel.
     */
    public boolean canLike(String username);
}
