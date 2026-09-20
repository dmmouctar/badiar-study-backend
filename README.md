# 🎓 BadiarStudy — Backend API

Application de gestion académique pour les lycées et universités en Guinée Conakry.  
Gère les étudiants, matières, examens, notes et bulletins.

---

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Spring Boot 3.5.1, Java 17, Maven |
| Sécurité | Spring Security + JWT (jjwt 0.12.6) |
| Base de données | MySQL 8 |
| Utilitaires | Lombok, Spring Validation, Spring Data JPA |

---

## Prérequis

- Java 17+
- Maven 3.8+
- MySQL 8 (ou MariaDB 10.4+)

---

## Installation

### 1. Cloner le projet

```bash
git clone https://github.com/dmmouctar/badiar-study.git
cd badiar-study
```

### 2. Créer la base de données

Importer le schéma SQL dans MySQL :

```bash
mysql -u root -p < database/student_management.sql
```

Ou via phpMyAdmin : importer le fichier `database/student_management.sql`.

### 3. Configurer les variables d'environnement

Copier `application.properties.example` et adapter :

```bash
# Les valeurs par défaut fonctionnent pour le développement local
# En production, définir les variables d'environnement suivantes :
DB_URL, DB_USERNAME, DB_PASSWORD
JWT_SECRET, JWT_EXPIRATION
CORS_ORIGINS
SUPER_ADMIN_EMAIL, SUPER_ADMIN_PASSWORD
```

### 4. Lancer l'application

```bash
mvn spring-boot:run
```

L'API démarre sur `http://localhost:8080`.

---

## Compte par défaut

Au premier démarrage, un compte **SUPER_ADMIN** est automatiquement créé :

| Champ | Valeur |
|---|---|
| Email | `superadmin@badiar.com` |
| Mot de passe | `BadiarAdmin@2025!` |

> Changer ces valeurs en production via les variables d'environnement.

---

## Authentification

Toutes les routes (sauf login) nécessitent un token JWT dans le header :