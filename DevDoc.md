# GITCLOUT DEV DOC

### TECHNOLOGIES
- SPRING 6 
   - backend du projet
- JPA
   - persistance du projet
- DERBY
   - Base de donnée du proejt
- VUEJS
   - Frontend du projet 
- SEMANTIC
   - L'ui lié au frontend tu projet

### Rest API
La Rest Api se trouve dans la classe  ``MessageController.java`` et à pour route:
 - ``/app/rest``
   - ``/hello``  permet d'afficher le titre du projet
   - ``/toTheBack`` permet d'envoyer les données saisis dans la barre de recherche vers le backend du projet et renvoi vrai si c'est bien un git
   - ``/percentFinished`` permet d'afficher en temps reel l'avancement des taches soumise
   - ``/getlink`` permet d'afficher les liens des git soumis qui fonctionnent 

### BACKEND
   - Blame est le coeur du projet c'est la ou on analyse le projet
   - Contributor est un record contenant le nom et le mail d'un contributeur
   - Contributor langage permet de spécifier un contributeur avec un langage pour une utilisation plus simple lors d'une analyse
   - Data est une classe qui stocke les données des contributeurs
   - Extensions est un énum des extensions de langage
   - FileExtension est un record qui permet de garder des extensions par rapport au fichier
   - GitTools sert pour tout ce qui est en rapport avec l'utilisation de git, le clonnage, les differences, les contributeurs ...
   - JGitBlame sert a préparer le blame
   - StringWork sert a faire des manipulation sur les fichier ou le chaines de caracteres, par exemple convertir un lien en un localpath
   - UtilsMethods sert pour garder les methodes annexes tels que isGitRepo qui permet de savoir si le lien entré est un git ou non.
### FRONTEND
   -  Homepage est le composant principale qui est appelé par App.vue
   -  ChartPage est un composant enfant de Homepage et affiche les Graphiques en baton (cela affiche les contribution pour un tag actuel)
   -  GitPage est un composant enfant de Homepage et affiche les leins git deja réalisé
   -  RadarChartPage est un composant enfant de Homepage et affiche les graphique en radar (cela affiche la moyenne des contributions par contributeur)
Pour certaines raisons, nous avont fait en sorte d'afficher les informations sur une même page.



### Description de la base de donnée
```
                         ---------------
                        |      TAG      |
                        |---------------|
                        | [Tag_ID]      |
                        | Nom_Tag       |
                        | Nom_projet    |
                        | Date          |
                         ---------------
                                |
                                |1,N
                                |
 ---------------        /---------------\        ---------------
| Contributeur  |       | Participation |       |    Langage    |
|---------------|       |---------------|       |---------------|
| [GIT_ID]      |  1,N  | [GIT_ID]      |  1,N  | [NomLangage]  |
| Prénom        |-------| [Tag_ID]      |-------|               |
|               |       | [NomLangage]  |       |               |
|               |       |  nbLigne      |       |               |
 ---------------        \---------------/        ---------------
```
 - La table Langage stockera les langages (les 10 plus utilisés) sur lesquels on va faire l'analyse
 - La table contributeur permettra de stocker les données des contributeurs c'est a dire leurs nom et leurs Email qui servira de clé primaire
 - La table tag sert a stocker tout les tags de chaque projet, on pourra les differencier en stockant aussi le nom du projet dedans, pour la clé primaire, on utilisera le shawan du tag.
 - La table participation est une table associative permettant de regrouper ces 3 tables et nous travaillerons majoritairement sur cette table.
