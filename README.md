[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/hY8hJHQw)

# Tema1 POO

**Nume**: Bruma George-Sebastian
**Grupa**: 322 CB-a
**Tema**: tema1_POO
**Scor**: 160/160 pe checker-ul de pe github

## Implementare

In primul rand, doresc sa adresez ca nu am implementat tema cu "fisiere", asa cum a fost recomandat,
ceoarece consider ca nu este un antrenament bun pentru o tema la "programare orientata pe obiecte", am 
preferat sa folosesc clase , atribute si metode ca si cum as fance pentru un proiect in care doresc sa
folosesc mult conceptul de programare pe obiecte. Datele persista, deoarece avem o aplicatie care are un
api prin care putem comunica, anume metoda main pe care o folosim cu argumente de numar variabil in 
aceasta.

La fel cum fac in majoritatea proiectelor personale, am preferat sa am main-ul mai light, avand putin
cod in el , cu scopul de a avea un cod reutilizabil, usor de modificat si folosit in alte proiecte. Adica,
am creat o clasa speciala de input, anume: "Input handler", care are metoda principala: "scanInput", care
prelucreaza inputul dat si rezolva un task specific. Aceasta metoda a fost facuta astfel incat sa putem crea
usor noi functionalitati pentru aplicatia noastra, avand un simplu switch, in care chemam functionalitatile
in functiile de comenzile date. Aceasta ne redirectioneaza catre o functie care face un anumit lucru pentru
aplicatie. Se observa ca functiile se aseamana intre ele, acestea fiind de fapt un wrapper pentru functionalitate
create de clasele cu scop specific. De asemenea, aceasta clasa are si functii de prelucrare text, inputul venind 
intr-un format specific ca sa adresam comenzi. Puteam totusi, sa fac functiile mai scurte, ele semanand foarte mult, 
insa am zis ca daca vrem wrappere mai complexe atunci sa putem modifica pentru fiecare task in parte. Creierul
porgramului vine in sine in clasele adaugate, anume: AllUsers, Comentariu, Postare si Utilizator.

Clasa Utilizator reprezinta pur si simplu un user existent pe reteaua noastra sociala. Acesta detine atribute
pentru logare in aplicatie, oameni pe care ii urmareste si de care este urmarit si un atribut ce tine cont de 
postarile acestuia. Acesta nu contine metode foarte complexe, majoritatea fiind getter-s and setters, informtatii
de cui ii da follow, cine i-a dat follow si anumite informatii despre postarile acestuia si numarul de like-uri.
Acesta este "extins" (nu mostenit), prin clasa "AllUsers" care contine toti userii din aplicatie, astfel poate
prezenta informatii generale despre comunitate, nu doar pentru un anumit user.

Postarea este o clasa ce reprezinta o actiune a unui user pe reteaua sociala, in cazul nostru, un mesaj public
pentru followers, prin care poate primii like-uri sau comentarii. Fiecare postare de pe retea are un id unic, astfel
putem identifica postarile dupa id, fiind mai usor sa diferentiem o postare de alta. Postarea contine un vector de
comentarii, astfel putem vizualiza fiecare comentariu de pe aceasta, implementeaza LikeAble, interfata care ii
spune unei clase daca poate primii like/unlike, cat si ownerul, astfel doar acesta poate sa stearga postarea si 
comentarii de pe postarea propie. Aceasta clasa afiseaza si informatii despre postare, postari, cat si comentariile
dintr-o postare.

In final, comentariile sunt un mesaj text care se atribuie numai unei postari, precum postarea, este identificat
dupa un id unic, implementeaza Likeable si are anumiti getters si setters. Am decis ca aceasta sa contina si postarea
caruia ii apartine, astfel sa identificam mai usor cine este owner-ul postarii, astfel doar acesta sau owner-ul postarii
pot face modificari (in cazul nostru sa stearga postarea), asupra acesteia. Aceasta nu afiseaza totusi nimic, fiind doar
un atribut pentru postari, informatiile fiind vizibile doar prin acestea.

## Bonus
`EdgeCases`:
1. As verifica daca input-ul vine sub forma corect, adica, sa nu vine -u "username" -text "text" si totusi
sa fie totul corect, ar fi fost bine sa fie macar un test in checker sa verifice asta. Astfel, m-am folosit
doar de lungimea input-ului ca sa stiu daca am primt un numar corect de argumente.
2. Daca stergem o postare ar fi trebuit sa fim nevoiti sa stergem si comentariile atasate acestuia, ele 
fiind dependente de postarea in care s-au instantiat.
3. Sa nu putem comenta pe postari ale unor useri carora nu le dam follow, la fel cum nu putem da like unei
postari are caror user nu ii dam follow.

`Refractorizations`:
1. Parolele utilizatorilor sa fie hash-uite, astfel incat daca intampinam un databreach sa nu dam leak complet
la parolele oamenilor, fiind o chestie implementata in orice aplicatie in care te loghezi cu user si parola.
2. Abilitatea unui user de a modifica continutul unei postari (edit) , de asemenea sa poata posta si poze, fiind
o retea sociala cam trebuie sa ai poze in ea.
3. Optiunea de a putea decide cine poate vedea postariile e pe contul tau, conturi publice, private sau chiar
sa faci postari cu anumite permisiuni, sa existe postari publice , private, sau chiar vizibile doar de owner,
putand sa schimbe aceste set