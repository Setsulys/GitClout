## GITCLOUT USER DOC
#### DEBATS-LY_IENG
### 1 INSTALLATION
 - Placez vous dans un répertoire de votre choix
 - Dans un terminal et executez : ``git clone https://gitlab.com/Setsulys/DEBATS-LY_IENG.git``

### 2 EXECUTION
#### 2.1.A execution sur le terminal
 - Dans le répertoire racine du projet, sur un terminal ouvert dans ce répertoire:
  ```sh
  mvn clean package
  java -jar ./target/gitclout-0.0.1-SNAPSHOT.jar
  ```
#### 2.1.B execution d'un makefile
 - Dans le terminal se situant dans le répertoire du projet executez la commande:
  ```sh
  make run
  ```

#### 2.2 Nettoyage du projet
 - Pour néttoyer le projet vous pouvez executer l'une des deux lignes suivante dans le terminal:
    - ``make clean``
    - ``mvn clean``

### 3 FONCTIONNEMENT
 - Lorsque le projet sera bien executé, ouvrez une page internet en tappant sur la barre de recherche ``localhost:8080``. (cela vous amenera sur la page du site)
 - Dans la barre de recherche, mettez le lien d'un repos git en http et cliquez sur ``Gitclouting`` pour lancer la tache
 - La page se rechargera dès que les taches sont finis. Vous pourrez voir l'avancement avec l'icone de chargement.
 - lorsque la page aura rechargé, vous aurez une liste déroulante des tags contenu dans le projet, selectionnez en une pour pouvoir voir les contributions de chaque personnes dans le projet. 
### 4 FIN DE TACHE
 Pour arreter le serveur vous pouvez aller sur le terminal et faire la commande ``ctrl + c``