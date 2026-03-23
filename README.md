## LAB 22 : Développement Android avec JNI (Java Native Interface)


🎯 Objectif

Ce laboratoire vise à comprendre comment configurer un projet Android avec support natif C/C++ et maîtriser les concepts fondamentaux liés à JNI, NDK et CMake.

# Étape 1 — Création du projet Android avec support C++

🔹 Procédure de création

- Ouvrir Android Studio

Aller dans :

- File → New Project → Empty Views Activity
- Configurer le projet comme suit :
  * Name : JNIDemo
  * Language : Java
  * Minimum SDK : API 24
   ✅ Cocher : Include C++ support
  * Build system : CMake
  * Cliquer sur Finish
    
📂 Structure générée automatiquement

- Après la création, Android Studio génère automatiquement les éléments suivants :

📁 cpp/ → dossier contenant le code natif C/C++

📄 native-lib.cpp → fichier principal du code C++

📄 CMakeLists.txt → fichier de configuration de compilation

⚙️ Configuration Gradle adaptée au support du NDK

👉 Cela signifie que l’environnement est prêt pour intégrer du code natif.

🔍 Explication

# Android Studio utilise :

- NDK pour compiler le code C/C++
- CMake pour gérer le processus de compilation
- Gradle pour intégrer le tout dans l’application Android
  
🧪 Point de contrôle

✔️ Le projet doit se synchroniser sans erreur

❗ Résolution des problèmes (si erreur)

Si une erreur apparaît (NDK ou CMake) :

Aller dans :

Tools → SDK Manager → SDK Tools
Vérifier que les éléments suivants sont installés :
✅ NDK (Side by side)
✅ CMake
✅ LLDB

📸 Captures à insérer

<img width="671" height="464" alt="image" src="https://github.com/user-attachments/assets/cef6ffd5-d1b5-4eef-b533-8b949f130067" />

            👉 Capture 1 : Écran de création du projet (configuration avec Include C++)
            👉 Capture 2 : Structure du projet (dossier cpp/, fichiers générés)
            👉 Capture 3 : SDK Manager → SDK Tools avec NDK, CMake, LLDB cochés
            

# Étape 2 — Comprendre les rôles des composants

- Avant de commencer le développement, il est essentiel de comprendre les composants utilisés dans JNI.

🔗 JNI (Java Native Interface)

- JNI est une interface qui permet au code Java d’interagir avec du code natif écrit en C/C++.

📌 Rôle :

- Appeler des fonctions C/C++ depuis Java

- Utiliser des bibliothèques natives dans Android

💡 Exemple d’utilisation :

Java → appelle → fonction native → retourne résultat

🛠️ NDK (Native Development Kit)

Le NDK est un ensemble d’outils fourni par Android permettant de développer en C/C++.

📌 Rôle :

- Compiler le code natif

- Générer des bibliothèques .so

- Accéder à des fonctionnalités bas niveau
  
⚙️ CMake

CMake est un système de build utilisé pour configurer la compilation du code C/C++.

📌 Rôle :

-Décrire les fichiers à compiler

-Gérer la génération de la bibliothèque native

📦 Bibliothèque partagée (.so)

- Le code natif est compilé sous forme de bibliothèque partagée :  libnative-lib.so
  
🔹 Chargement dans Android

Cette bibliothèque est chargée dans le code Java avec : System.loadLibrary("native-lib");

🔍 Explication

.so = bibliothèque dynamique (comme .dll sur Windows)

System.loadLibrary permet de charger cette bibliothèque au démarrage

📸 Captures à insérer

                       👉 Capture 4 : Fichier native-lib.cpp ouvert
                       👉 Capture 5 : Fichier CMakeLists.txt
                       👉 Capture 6 : Code Java avec System.loadLibrary
                       
# Étape 3 — Vérification de la configuration Gradle

🔹 Accès au fichier

- Ouvrir le fichier suivant :  app/build.gradle ou (selon le projet) - app/build.gradle.kts

🔹 Configuration attendue

- Une configuration typique est la suivante :

android {
    namespace "com.example.jnidemo"
    compileSdk 35

    defaultConfig {
        applicationId "com.example.jnidemo"
        minSdk 24
        targetSdk 35
        versionCode 1
        versionName "1.0"
    }

    externalNativeBuild {
        cmake {
            path file("src/main/cpp/CMakeLists.txt")
        }
    }
}

🔍 Explication

externalNativeBuild

👉 Permet à Gradle d’intégrer la compilation du code natif

cmake

👉 Spécifie que CMake est utilisé comme système de build

path

👉 Indique l’emplacement du fichier CMakeLists.txt

⚠️ Point important

❗ Le chemin doit être correct : src/main/cpp/CMakeLists.txt

Sinon :

❌ la bibliothèque native .so ne sera pas générée
❌ l’application ne pourra pas charger le code C++

💡 Bon réflexe

👉 Ne pas modifier cette partie sans raison
👉 Toujours vérifier le chemin en cas d’erreur

📸 Capture à insérer

      👉 Capture 1 : fichier build.gradle avec la section externalNativeBuild

# Étape 4 — Configuration du fichier CMakeLists.txt
🔹 Accès au fichier

Ouvrir :

app/src/main/cpp/CMakeLists.txt
🔹 Code à utiliser

Remplacer le contenu par :

cmake_minimum_required(VERSION 3.22.1)

project("jnidemo")

add_library(
        native-lib
        SHARED
        native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        ${log-lib})
🔍 Explication ligne par ligne
🔸 cmake_minimum_required(VERSION 3.22.1)

👉 Définit la version minimale de CMake requise

🔸 project("jnidemo")

👉 Définit le nom du projet natif

🔸 add_library(...)

👉 Crée une bibliothèque :

Nom : native-lib
Type : SHARED → bibliothèque dynamique (.so)
Source : native-lib.cpp
🔸 find_library(log-lib log)

👉 Recherche la bibliothèque système Android log

➡️ utilisée pour afficher des logs en C++

🔸 target_link_libraries(...)

👉 Lie la bibliothèque native-lib avec :

log

➡️ nécessaire pour utiliser certaines fonctions Android natives

⚠️ Point critique

👉 Le nom native-lib doit être identique à :

System.loadLibrary("native-lib");

Sinon :

❌ erreur au lancement
❌ bibliothèque introuvable
💡 Important

👉 Android ajoute automatiquement :

lib (préfixe)
.so (suffixe)

Donc :

native-lib → libnative-lib.so
📸 Captures à insérer

👉 Capture 2 : fichier CMakeLists.txt complet
👉 Capture 3 : dossier cpp/ avec fichiers visibles
