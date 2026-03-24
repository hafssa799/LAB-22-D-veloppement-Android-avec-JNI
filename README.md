
# LAB 22 : Développement Android avec JNI (Java Native Interface)


## 🎯 Objectif


Ce laboratoire a pour objectif de comprendre et de mettre en œuvre l’intégration du code natif C/C++ dans une application Android à l’aide de JNI (Java Native Interface).

Il vise à apprendre comment configurer un projet Android avec support natif en utilisant les outils fournis par Android, notamment le NDK (Native Development Kit) et CMake.

Plus précisément, ce laboratoire permet de :

- Comprendre le rôle de JNI dans la communication entre le code Java et le code natif C/C++.
  
- Apprendre à configurer correctement un projet Android pour supporter le développement natif.
  
- Utiliser le NDK pour compiler du code C/C++ et générer des bibliothèques partagées (.so).
  
- Utiliser CMake pour décrire et automatiser le processus de compilation du code natif.
  
- Implémenter et appeler des fonctions natives depuis une application Android.
  
- Comprendre les contraintes liées à la gestion de la mémoire et aux signatures JNI.
  
- Tester et valider le bon fonctionnement des interactions entre Java et C++.

Ce laboratoire constitue une introduction essentielle au développement natif sous Android, permettant d’améliorer les performances des applications et d’exploiter des bibliothèques existantes en C/C++.

## Étape 1 — Création du projet Android avec support C++

🔹 Procédure de création

- Ouvrir Android Studio

Aller dans :

- File → New Project → Empty Views Activity
  
- Configurer le projet comme suit :
  
  * Name : JNIDemo
    
  * Language : Java
    
  * Minimum SDK : API 24
  
  * Build system : CMake
    
  * Cliquer sur Finish
    
📂 Structure générée automatiquement : cpp/ → dossier contenant le code natif C/C++

📄 native-lib.cpp → fichier principal du code C++

📄 CMakeLists.txt → fichier de configuration de compilation

⚙️ Configuration Gradle adaptée au support du NDK

## Android Studio utilise :

- NDK pour compiler le code C/C++
  
- CMake pour gérer le processus de compilation
  
- Gradle pour intégrer le tout dans l’application Android

- Si une erreur apparaît (NDK ou CMake) :

### Aller dans :

Tools → SDK Manager → SDK Tools

Vérifier que les éléments suivants sont installés :

✅ NDK (Side by side)

✅ CMake

✅ LLDB

 
 ![](https://github.com/user-attachments/assets/be3d9b0d-e702-4620-8322-9a00e12c6803)

👉 Capture 1 : Écran de création du projet (configuration avec Include C++)

 ![](https://github.com/user-attachments/assets/24d6d1b4-1be8-47c8-b4aa-7c7e9bef3170)

👉 Capture 2 : Structure du projet (dossier cpp/, fichiers générés)

![](https://github.com/user-attachments/assets/eced42a9-b755-49a0-9877-0de860cabc02)

👉 Capture 3 : SDK Manager → SDK Tools avec NDK, CMake, LLDB cochés
            

## Étape 2 — Comprendre les rôles des composants

- Avant de commencer le développement, il est essentiel de comprendre les composants utilisés dans JNI.

### JNI (Java Native Interface)

- JNI est une interface qui permet au code Java d’interagir avec du code natif écrit en C/C++.

### Rôle :

- Appeler des fonctions C/C++ depuis Java

- Utiliser des bibliothèques natives dans Android

### NDK (Native Development Kit)

Le NDK est un ensemble d’outils fourni par Android permettant de développer en C/C++.

### Rôle :

- Compiler le code natif

- Générer des bibliothèques .so

- Accéder à des fonctionnalités bas niveau
  
### CMake

CMake est un système de build utilisé pour configurer la compilation du code C/C++.

### Rôle :

-Décrire les fichiers à compiler

-Gérer la génération de la bibliothèque native

### Bibliothèque partagée (.so)

- Le code natif est compilé sous forme de bibliothèque partagée : libjnidemo.so
  
🔹 Chargement dans Android

- Cette bibliothèque est chargée dans le code Java avec : System.loadLibrary("jnidemo");

🔹 Explication

.so = bibliothèque dynamique (comme .dll sur Windows)

System.loadLibrary permet de charger cette bibliothèque au démarrage
           
![](https://github.com/user-attachments/assets/43f9ad7d-e80a-4ff6-9506-981da7d0d96f)

👉 Capture 4 : Fichier native-lib.cpp ouvert
 
![](https://github.com/user-attachments/assets/149ae0d0-f5c4-4588-a7f1-0ad76365aed3)
                   
👉 Capture 5 : Fichier CMakeLists.txt


## Étape 3 — Vérification de la configuration Gradle

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

### Explication

externalNativeBuild

👉 Permet à Gradle d’intégrer la compilation du code natif

cmake

👉 Spécifie que CMake est utilisé comme système de build

path

👉 Indique l’emplacement du fichier CMakeLists.txt
 
![](https://github.com/user-attachments/assets/316afc3e-ea5e-42f2-ba36-9db76e2ccb72)

👉 Capture 1 : fichier build.gradle avec la section externalNativeBuild

## Étape 4 — Configuration du fichier CMakeLists.txt

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
        
### Explication ligne par ligne

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


![](https://github.com/user-attachments/assets/cb12a660-0968-492e-9697-c047247ea600)
              
👉 Capture 2 : fichier CMakeLists.txt complet

![](https://github.com/user-attachments/assets/ae9874e9-f783-4e02-9f8c-7a4f087b7ba0)
              
👉 Capture 3 : dossier cpp/ avec fichiers visibles

# Étape 5 — Implémentation du code natif en C++

🔹 Accès au fichier

- Ouvrir : app/src/main/cpp/native-lib.cpp
  
🔹 Code implémenté

👉 #include <jni.h>
#include <string>
#include <algorithm>
#include <climits>
#include <android/log.h>

#define LOG_TAG "JNI_DEMO"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// 1) Hello World natif
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_jnidemo_MainActivity_helloFromJNI(
        JNIEnv* env,
        jobject /* this */) {

    LOGI("Appel de helloFromJNI depuis le natif");
    return env->NewStringUTF("Hello from C++ via JNI !");
}

// 2) Factoriel avec gestion d'erreur
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jnidemo_MainActivity_factorial(
        JNIEnv* env,
        jobject /* this */,
        jint n) {

    if (n < 0) {
        LOGE("Erreur : n negatif");
        return -1;
    }

    long long fact = 1;
    for (int i = 1; i <= n; i++) {
        fact *= i;
        if (fact > INT_MAX) {
            LOGE("Overflow detecte pour n=%d", n);
            return -2;
        }
    }

    LOGI("Factoriel de %d calcule en natif = %lld", n, fact);
    return static_cast<jint>(fact);
}

// 3) Inversion d'une chaine Java -> C++ -> Java
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_jnidemo_MainActivity_reverseString(
        JNIEnv* env,
        jobject /* this */,
        jstring javaString) {

    if (javaString == nullptr) {
        LOGE("Chaine nulle recue");
        return env->NewStringUTF("Erreur : chaine nulle");
    }

    const char* chars = env->GetStringUTFChars(javaString, nullptr);
    if (chars == nullptr) {
        LOGE("Impossible de lire la chaine Java");
        return env->NewStringUTF("Erreur JNI");
    }

    std::string s(chars);
    env->ReleaseStringUTFChars(javaString, chars);

    std::reverse(s.begin(), s.end());

    LOGI("String inversee = %s", s.c_str());
    return env->NewStringUTF(s.c_str());
}

// 4) Somme d'un tableau int[]
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_jnidemo_MainActivity_sumArray(
        JNIEnv* env,
        jobject /* this */,
        jintArray array) {

    if (array == nullptr) {
        LOGE("Tableau nul");
        return -1;
    }

    jsize len = env->GetArrayLength(array);
    jint* elements = env->GetIntArrayElements(array, nullptr);

    if (elements == nullptr) {
        LOGE("Impossible d'acceder aux elements du tableau");
        return -2;
    }

    long long sum = 0;
    for (jsize i = 0; i < len; i++) {
        sum += elements[i];
    }

    env->ReleaseIntArrayElements(array, elements, 0);

    if (sum > INT_MAX) {
        LOGE("Overflow sur la somme");
        return -3;
    }

    LOGI("Somme du tableau = %lld", sum);
    return static_cast<jint>(sum);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_sg_vantagepoint_jnidemo_MainActivity_helloFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement helloFromJNI()
}

🔍 Explication du code

🔸 #include <jni.h>

Bibliothèque principale JNI permettant la communication entre Java et C++.

🔸 extern "C"

Permet d’éviter le name mangling du C++.

👉 Sans cela :

Java ne trouvera pas la fonction : ❌ erreur UnsatisfiedLinkError

🔸 Gestion des chaînes
GetStringUTFChars()
ReleaseStringUTFChars()

🔸 Gestion des tableaux
GetIntArrayElements()
ReleaseIntArrayElements()

👉 Permet de manipuler int[] Java en C++

🔸 Logs Android
__android_log_print

👉 Permet d’afficher des logs dans Logcat


![](https://github.com/user-attachments/assets/81edf259-c6ad-4ce5-bf25-7554f4075b5b)
              
 👉 Capture 1 : fonction helloFromJNI
               
![](https://github.com/user-attachments/assets/97a00946-4d56-4811-b4aa-cad2038736cc)
              
👉 Capture 2 : fonction factorial
               
![](https://github.com/user-attachments/assets/cec362a6-995e-462b-91de-59d7ff470721)
              
👉 Capture 3 : fonction reverseString
             
![](https://github.com/user-attachments/assets/1b915fd5-46a4-4fcd-ac0c-4e81eb4455bf)
            
👉 Capture 4 : fonction sumArray

## Étape 6 — Déclaration des méthodes natives côté Java

🔹 Accès au fichier

app/src/main/java/com/example/jnidemo/MainActivity.java

🔹 Déclaration des méthodes

![](https://github.com/user-attachments/assets/adc1ab4a-8c6a-4da7-9cd3-bb0c790e370e)

👉 Capture 6 : déclaration des méthodes native

🔍 Explication

🔸 native

Indique que la méthode est implémentée en C++.

🔹 Chargement de la bibliothèque

![](https://github.com/user-attachments/assets/31352978-b891-4dc6-8ad3-cd9206315286)

👉 Capture 7 : bloc System.loadLibrary

🔹 Appel des fonctions natives

![](https://github.com/user-attachments/assets/ec14df24-f56d-4c1f-8877-fec7da918858)

👉 Capture 8 : méthode onCreate()

## Étape 7 — Créer le layout XML complet

activity_main.xml :

<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="20dp">

        <TextView
            android:id="@+id/tvHello"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="#000000"
            android:textSize="22sp"
            android:textStyle="bold"
            android:padding="10dp" />

        <TextView
            android:id="@+id/tvFact"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="#000000"
            android:textSize="20sp"
            android:padding="10dp" />

        <TextView
            android:id="@+id/tvReverse"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="#000000"
            android:textSize="20sp"
            android:padding="10dp" />

        <TextView
            android:id="@+id/tvArray"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="#000000"
            android:textSize="20sp"
            android:padding="10dp" />

    </LinearLayout>
</ScrollView>

## Étape 8 — Compiler et exécuter

![](https://github.com/user-attachments/assets/e8d2c48b-2df2-4e9e-a489-0205efd785b2)

👉 Capture 9 : affichage dans l’application

- Cette application démontre l’utilisation de JNI pour exécuter du code natif en C++ dans une application Android.
Les tests réalisés (factoriel, inversion de chaîne, somme de tableau) montrent la communication efficace entre Java et le code natif.

## Étape 9 — Vérification des logs natifs (Logcat)

Dans Android Studio : View → Tool Windows → Logcat

![](https://github.com/user-attachments/assets/dcf170e7-e7dd-4aec-a16d-9a202c1b3722)

👉 Capture 9 : Logcat avec filtre "JNI_DEMO"

## Étape 10 — Tests fonctionnels guidés

Tester les fonctions JNI avec différents cas pour valider leur robustesse.

🧪 Test 1 : Valeur normale

int fact10 = factorial(10);

✔️ Résultat attendu

![](https://github.com/user-attachments/assets/47a3de18-ebe5-4b89-95b0-c6769741a255)

🧪 Test 2 : Valeur négative

int fact10 = factorial(-5);

✔️ Résultat attendu

![](https://github.com/user-attachments/assets/1287d4fc-581c-49c0-9516-7164949f8f3c)

🧪 Test 3 : Dépassement (overflow)

int fact10 = factorial(20);

✔️ Résultat attendu

![](https://github.com/user-attachments/assets/00b8bfe3-55cd-4704-b9ab-f3b2450a5077)

🧪 Test 4 : Chaîne vide

String reversed = reverseString("");

✔️ Résultat attendu

![](https://github.com/user-attachments/assets/610fe011-2627-44ad-afc7-c53ec62e431a)

🧪 Test 5 : Tableau vide

int[] numbers = {};

✔️ Résultat attendu

![](https://github.com/user-attachments/assets/5d34dd70-f989-4bb5-bc0d-27565c4cdbed)


📊 Analyse des résultats

Ces tests montrent que :

✔️ Les fonctions JNI fonctionnent correctement
✔️ Les erreurs sont bien gérées côté C++
✔️ L’application est robuste


## Étape 11 — Débogage des erreurs fréquentes

Lors du développement avec JNI, plusieurs erreurs peuvent apparaître.

### Erreur 1 : UnsatisfiedLinkError

Cause :

- Mauvais nom de bibliothèque
  
- Signature JNI incorrecte
  
- Différence entre package Java et C++

Solution :

Vérifier que le package correspond exactement.

### Erreur 2 : Crash de l’application

Cause :

- Fonction native non implémentée

Solution :

Vérifier que toutes les fonctions C++ sont complètes.

### Erreur 3 : Problème avec les String

Cause :

- Oubli de libérer la mémoire

Solution :

Utiliser ReleaseStringUTFChars

- Le débogage JNI nécessite une bonne correspondance entre Java et C++ 
et une gestion correcte de la mémoire.

📸 Capture : Erreur "App keeps stopping" (mauvaise bibliothèque JNI)

Par exemple change :

System.loadLibrary("jnidemo");

en 

System.loadLibrary("fake-lib");

![](https://github.com/user-attachments/assets/3b26516c-91ce-49b3-9f30-824f640a71d4)

📸 code corrigé

👉 Corrige l’erreur :

Remets :

![](https://github.com/user-attachments/assets/ff470e5a-ebee-470f-84a7-e4909a0a6189)

## Conclusion

Au cours de ce laboratoire, nous avons découvert et mis en pratique l’utilisation de JNI (Java Native Interface) dans une application Android.

Nous avons appris à intégrer du code natif C++ dans un projet Android à l’aide du NDK et de CMake, ainsi qu’à établir la communication entre le code Java et le code natif. Plusieurs fonctions ont été implémentées, notamment le calcul du factoriel, l’inversion d’une chaîne de caractères et la somme d’un tableau.

Ce laboratoire nous a également permis de comprendre l’importance de la correspondance entre les signatures Java et C++, ainsi que la gestion de la mémoire dans JNI, notamment pour les chaînes et les tableaux.

Les tests réalisés ont confirmé le bon fonctionnement des fonctions natives, y compris la gestion des cas particuliers comme les valeurs négatives, les dépassements (overflow) et les entrées vides.

Enfin, nous avons appris à utiliser Logcat pour analyser les logs natifs et faciliter le débogage, ainsi qu’à identifier et corriger des erreurs fréquentes comme les problèmes de liaison JNI.

Ce laboratoire constitue une introduction importante au développement natif sous Android, ouvrant la voie à des applications plus performantes et à l’utilisation de bibliothèques C/C++ existantes.



