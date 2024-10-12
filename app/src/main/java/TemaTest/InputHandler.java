package TemaTest;



//creierul aplicatiei, lobul frontal, creierul reptilian, whatever
public class InputHandler {
    private App app;

    public InputHandler(App app) {
        this.app = app;
    }

    public void selectApp(App myApp) {
        app = myApp;
    }


    /**
     * Functia principala a programului , putem spune ca e chiar creierul aplicatiei
     * aici se prelucreaza inputul si se obtine output-ul mult asteptat. Folosim un
     * switch pt ca arata frumos dar si pt ca e mai rapid decat un if, nu ca ar conta.
     * @param strings reprezinta argumentele din main, asa se transmite inputul.
     */
    public void scanInput(java.lang.String[] strings) {
        String command = strings[0].strip();

        switch (command) {
            case "-create-user":
                createUser(strings);
                break;
            case "-create-post":
                createPost(strings);
                break;
            case "-delete-post-by-id":
                deletePost(strings);
                break;
            case "-cleanup-all":
                cleanupApp();
                break;
            case "-follow-user-by-username":
                followUser(strings);
                break;
            case "-unfollow-user-by-username":
                unfollowUser(strings);
                break;
            case "-like-post":
                likePost(strings);
                break;
            case "-unlike-post":
                dislikePost(strings);
                break;
            case "-comment-post":
                commentPost(strings);
                break;
            case "-delete-comment-by-id":
                deleteComment(strings);
                break;
            case "-like-comment":
                likeComment(strings);
                break;
            case "-unlike-comment":
                dislikeComment(strings);
                break;
            case "-get-followings-posts":
                descendedFollowing(strings);
                break;
            case "-get-user-posts":
                descendedFollowingUser(strings);
                break;
            case "-get-post-details":
                postDetails(strings);
                break;
            case "-get-following":
                following(strings);
                break;
            case "-get-followers":
                followers(strings);
                break;
            case "-get-most-liked-posts":
                popularPosts(strings);
                break;
            case "-get-most-commented-posts":
                popularPostsbyComments(strings);
                break;
            case "-get-most-followed-users":
                popularUsers(strings);
                break;
            case "-get-most-liked-users":
                likedUsers(strings);
                break;
            default:
                System.out.println("Bye world!");
        }
    }


    /*
    Fiecare task este facut intr-o functie separata, astfel sa fie usor
    sa ne dam seama unde apar erori si astfel incat sa fie simplu sa
    adaugam feature-uri noi. Majoritatea functiilor au elemente comune,
    insa, daca era sa fac o functie generala pentru ele, pierdeam din
    flexibilitate si savam doar cateva linii de cod.
     */

    /**
     * Functia de creare a unui user, afiseaza erori daca nu e ok
     * @param strings argumentele din main
     */
    public void createUser(java.lang.String[] strings) {
        if (strings.length == 1) {
            errorMessage("Please provide username");
            return;
        }
        else if (strings.length == 2) {
            errorMessage("Please provide password");
            return;
        }

        String username = stripInput(strings[1]);
        String password = stripInput(strings[2]);


        //daca userul nu exista
        if (app.users.findUser(username) != null) {
            errorMessage("User already exists");
            return;
        }

        app.users.addUser(username,password);
        succesMessage("User created successfully");

    }

    /**
     * Functia de creare a unui post, afiseaza erori daca nu e ok
     * @param string argumentele din main
     */
    public void createPost(java.lang.String[] string) {
        Postare post;
        Utilizator account;
        String text;

        account = autentificate(string);
        if (account == null)
            return;

        if (string.length < 4){
            errorMessage("No text provided");
            return;
        }
        text = stripInput(string[3]);
        if (text.length() > 300) {
            errorMessage("Post text length exceeded");
            return;
        }

        post = new Postare(text, account);
        account.addPost(post);
        succesMessage("Post added successfully");

    }

    //nu are rost sa comentez fiecare functie in parte, sunt toate la fel
    //mici modificari in functie de output de iesire
    public void deletePost(java.lang.String[] strings) {
        Postare post;
        Utilizator account;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No identifier was provided");
            return;
        }
        id = Integer.parseInt(stripInput(strings[3]));
        post = account.findPost(id);

        if (post == null) {
            errorMessage("The identifier was not valid");
            return;
        }

        account.deletePost(post);
        succesMessage("Post deleted successfully");

    }

    public void followUser(java.lang.String[] strings) {
        Utilizator account, newFollower;
        String followerUser;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No username to follow was provided");
            return;
        }

        followerUser = stripInput(strings[3]);
        newFollower = app.users.findUser(followerUser);
        if (account.alreadyFollow(followerUser) || newFollower == null) {
            errorMessage("The username to follow was not valid");
            return;
        }

        account.followSomeone(newFollower);
        succesMessage("Operation executed successfully");

    }

    public void unfollowUser(java.lang.String[] strings) {
        Utilizator account, unfollowGuy;
        String followerUser;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No username to unfollow was provided");
            return;
        }

        followerUser = stripInput(strings[3]);
        unfollowGuy = app.users.findUser(followerUser);
        if (!account.alreadyFollow(followerUser) || unfollowGuy == null) {
            errorMessage("The username to unfollow was not valid");
            return;
        }

        account.followSomeone(unfollowGuy);
        succesMessage("Operation executed successfully");

    }

    public void likePost(java.lang.String[] strings) {
        Postare post;
        Utilizator account;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No post identifier to like was provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        post = Postare.findPost(id);

        if (post == null || !post.canLike(account.getUsername())) {
            errorMessage("The post identifier to like was not valid");
            return;
        }

        post.giveLike(account);
        succesMessage("Operation executed successfully");

    }

    public void dislikePost(java.lang.String[] strings) {
        Postare post;
        Utilizator account;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No post identifier to unlike was provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        post = Postare.findPost(id);

        if (post == null || post.canLike(account.getUsername())) {
            errorMessage("The post identifier to unlike was not valid");
            return;
        }

        post.giveDislike(account);
        succesMessage("Operation executed successfully");

    }

    public void commentPost(java.lang.String[] strings) {
        Utilizator account;
        Postare post;
        String text;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 5) {
            errorMessage("No text provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        text = stripInput(strings[4]);

        if (text.length() > 300) {
            errorMessage("Comment text length exceeded");
            return;
        }

        post = Postare.findPost(id);
        if (post == null) {
            errorMessage("error404");
            return;
        }

        post.addComment(text, account);
        succesMessage("Comment added successfully");
    }

    public void deleteComment(java.lang.String[] strings) {
        Utilizator account;
        Comentariu comment;
        Postare post;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No identifier was provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        comment = Comentariu.findComment(id);

        if (comment == null) {
            errorMessage("The identifier was not valid");
            return;
        }

        post = comment.getPost();

        if (!comment.canDelete(account)){
            errorMessage("The identifier was not valid");
            return;
        }

        post.deleteComment(comment);
        succesMessage("Operation executed successfully");

    }

    /**
     * Stiu ca nu am multe comentarii, dar daca citesti functiile astea, sunt niste
     * wrapperi pentru functiile din clasele de baza, nu am ce sa comentez aici.
     * @param strings secret.
     */

    public void likeComment(java.lang.String[] strings) {
        Utilizator account;
        Comentariu comment;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No comment identifier to like was provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        comment = Comentariu.findComment(id);

        if (comment == null || !comment.canLike(account.getUsername())) {
            errorMessage("The comment identifier to like was not valid");
            return;
        }

        comment.giveLike(account);
        succesMessage("Operation executed successfully");
    }

    public void dislikeComment(java.lang.String[] strings) {
        Utilizator account;
        Comentariu comment;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No comment identifier to unlike was provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        comment = Comentariu.findComment(id);

        if (comment == null || comment.canLike(account.getUsername())) {
            errorMessage("The comment identifier to unlike was not valid");
            return;
        }

        comment.giveDislike(account);
        succesMessage("Operation executed successfully");

    }

    public void descendedFollowing(java.lang.String[] strings) {
        Utilizator account;

        account = autentificate(strings);
        if (account == null)
            return;

        System.out.println(account.getPostsDescendent());

    }

    public void descendedFollowingUser(java.lang.String[] strings) {
        Utilizator account, followAccount;
        String followUsername;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No username to list posts was provided");
            return;
        }

        followUsername = stripInput(strings[3]);
        followAccount = app.users.findUser(followUsername);
        if (followAccount == null || !account.alreadyFollow(followUsername)) {
            errorMessage("The username to list posts was not valid");
            return;
        }

        System.out.println(account.getPostsDescendentUser(followAccount));

    }

    public void postDetails(java.lang.String[] strings) {
        Utilizator account;
        Postare post;
        int id;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No post identifier was provided");
            return;
        }

        id = Integer.parseInt(stripInput(strings[3]));
        post = Postare.findPost(id);


        if (post == null || !account.permissionToView(post)) {
            errorMessage("The post identifier was not valid");
            return;
        }

        System.out.println(post.postDetails());
    }

    public void following(java.lang.String[] strings) {
        Utilizator account;

        account = autentificate(strings);
        if (account == null)
            return;

        System.out.println(account.viewFollowing());

    }

    public void followers(java.lang.String[] strings) {
        Utilizator account;
        String followerUsername;
        Utilizator followerAccount;

        account = autentificate(strings);
        if (account == null)
            return;

        if (strings.length < 4) {
            errorMessage("No username to list followers was provided");
            return;
        }

        followerUsername = stripInput(strings[3]);
        followerAccount = app.users.findUser(followerUsername);

        if (followerAccount == null) {
            errorMessage("The username to list followers was not valid");
            return;
        }

        System.out.println(followerAccount.viewFollowers());

    }

    public void popularPosts(java.lang.String[] strings) {
        Utilizator account = autentificate(strings);

        if (account == null)
            return;

        System.out.println(Postare.Top5Posts());
    }

    public void popularPostsbyComments(java.lang.String[] strings) {
        Utilizator account = autentificate(strings);

        if (account == null)
            return;

        System.out.println(Postare.Top5PostsbyComments());
    }

    public void popularUsers(java.lang.String[] strings) {
        Utilizator account = autentificate(strings);

        if (account == null)
            return;

        System.out.println(app.users.top5FollowedUsers());
    }

    public void likedUsers(java.lang.String[] strings) {
        Utilizator account = autentificate(strings);

        if (account == null)
            return;

        System.out.println(app.users.top5LikedUsers());
    }


    /**
     * Functia de curatare a aplicatiei, practic reseteaza totul
     */
    public void cleanupApp() {
        this.app = new App();

    }

    /**
     * Functia de curatare a inputului, scoate apostrofii si spatiile
     * @param fraza inputul pe care vrem sa il curatam
     * @return inputul curatat
     */
    public String stripInput(String fraza) {
        int pos = fraza.indexOf('\'');
        return fraza.substring(pos + 1, fraza.length()-1).strip();
    }


    /**
     * Functia de autentificare, verifica daca inputul este corect
     * @param string argumentele din main, adica array de stringuri
     * @return null daca nu e ok, altfel returneaza contul
     */
    public boolean hasAutentificaton(java.lang.String[] string) {
        if (string.length < 3)
            return false;

        return string[1].contains("-u") && string[2].contains("-p");
    }


    /**
     * Functia de autentificare, verifica daca username-ul si parola exista
     * @param string argumentele din main, adica array de stringuri
     * @return null daca nu e ok, altfel returneaza contul logat
     */
    public Utilizator autentificate(java.lang.String[] string) {
        String user, pass;
        Utilizator account;

        if (!hasAutentificaton(string)) {
            errorMessage("You need to be authenticated");
            return null;
        }
        user = stripInput(string[1]);
        pass = stripInput(string[2]);
        account = app.users.loginUser(user, pass);

        if (account == null) {
            errorMessage("Login failed");
            return null;
        }
        return account;
    }


    /**
     * Functia de afisare a erorilor, afiseaza un mesaj de eroare in format json
     * @param error mesajul de eroare
     */
    public void errorMessage(String error) {
        System.out.println("{'status':'error','message':'" + error +"'}");
    }

    /**
     * Functia de afisare a succesului, afiseaza un mesaj de succes in format json
     * @param message mesajul de succes
     */
    public void succesMessage(String message) {
        System.out.println("{'status':'ok','message':'"+ message +"'}");
    }

}
