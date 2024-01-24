# Chatop API

ChaTop est un portail en ligne qui permet de mettre en contact locataires et propriétaires pour de la location saisonnière.
L'application est composée d'une partie frontend Angular et backend en Java, présentée ici.


## Technologies utilisées

* Java 17
* Spring Boot 3
* Spring Security avec authentification JWT
* Maven
* MySQL 8
* Springdoc openAPI
* Cloudinary pour la sauvegarde de fichier

## Installation et déploiement

### Frontend

Récupérer le code depuis ce [dépôt](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring.git) et y suivre les instructions.

Il sera accessible à l'adresse [localhost:4200](http://localhost:4200/)

### Backend

#### Base de données

* S'assurer d'avoir MySQL Server d'installé.
* Créer une nouvelle base de données :
  ```
  CREATE DATABASE your_database_name;
  ```
* Utiliser ensuite le script SQL fournit à `/src/main/resources/sql/script.sql`

#### Configuration

* Ouvrir le fichier `application.properties` situé dans `/src/main/resources`
* Mettre à jour les propriétés suivantes avec votre configuration de MySQL :
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
  spring.datasource.username=your_database_username
  spring.datasource.password=your_database_password
  ```
* Lancer le build :
  ```
  mvn clean install
  ```
* Démarrer le serveur :
  ```
  mvn spring-boot:run
  ```
  
  Il sera accessible à l'adresse [localhost:3001](http://localhost:3001)

## Documentation

Accès à la documentation des diverses routes de l'API à l'adresse [localhost:3001/doc/swagger-ui](http://localhost:3001/doc/swagger-ui/index.html)
