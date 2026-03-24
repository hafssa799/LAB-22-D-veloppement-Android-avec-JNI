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

            <img width="333" height="233" alt="image" src="https://github.com/user-attachments/assets/1a95432c-401a-46fe-a756-0500769b97f5" />
          👉 Capture 2 : Structure du projet (dossier cpp/, fichiers générés)

          <img width="730" height="486" alt="image" src="https://github.com/user-attachments/assets/4cd24bf8-c0b1-494b-9994-7f4fa3876a8e" />
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

- Le code natif est compilé sous forme de bibliothèque partagée : libjnidemo.so
  
🔹 Chargement dans Android

- Cette bibliothèque est chargée dans le code Java avec : System.loadLibrary("jnidemo");

🔍 Explication

.so = bibliothèque dynamique (comme .dll sur Windows)

System.loadLibrary permet de charger cette bibliothèque au démarrage

📸 Captures à insérer

<img width="390" height="187" alt="image" src="https://github.com/user-attachments/assets/7880091e-58c8-4030-b4b3-f56eee92c7bd" />
                   👉 Capture 4 : Fichier native-lib.cpp ouvert

              <img width="904" height="505" alt="image" src="https://github.com/user-attachments/assets/50860098-b0e4-417e-a068-651b6aeeba7a" />
                   👉 Capture 5 : Fichier CMakeLists.txt
                    
<img width="413" height="119" alt="image" src="https://github.com/user-attachments/assets/bbab36a5-cbcb-407f-b96e-de879c0d2b8c" />
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

<img width="479" height="343" alt="image" src="https://github.com/user-attachments/assets/22daffa6-c86e-4c13-8327-6a0f33a94325" />
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

<img width="466" height="254" alt="image" src="https://github.com/user-attachments/assets/cb12a660-0968-492e-9697-c047247ea600" />
               👉 Capture 2 : fichier CMakeLists.txt complet

<img width="562" height="435" alt="image" src="https://github.com/user-attachments/assets/ae9874e9-f783-4e02-9f8c-7a4f087b7ba0" />
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

Java ne trouvera pas la fonction
❌ erreur UnsatisfiedLinkError
🔸 JNIEXPORT et JNICALL

Macros JNI utilisées pour :

exporter la fonction
respecter la convention d’appel JNI
🔸 Signature JNI

Exemple :

Java_com_example_jnidemo_MainActivity_helloFromJNI

👉 Structure :

Java_<package>_<classe>_<méthode>
⚠️ Point critique

Si :

package ❌
nom classe ❌
nom méthode ❌

👉 alors :

❌ l’application plante
❌ méthode native introuvable
🔸 JNIEnv* env

Interface permettant :

créer des String Java
accéder aux tableaux
manipuler objets Java
🔸 Gestion des chaînes
GetStringUTFChars()
ReleaseStringUTFChars()

👉 Important :

convertir Java → C++
libérer mémoire après utilisation
🔸 Gestion des tableaux
GetIntArrayElements()
ReleaseIntArrayElements()

👉 Permet de manipuler int[] Java en C++

🔸 Logs Android
__android_log_print

👉 Permet d’afficher des logs dans Logcat

📸 Captures à insérer

<img width="386" height="152" alt="image" src="https://github.com/user-attachments/assets/81edf259-c6ad-4ce5-bf25-7554f4075b5b" />
               👉 Capture 1 : fonction helloFromJNI
               
<img width="382" height="367" alt="image" src="https://github.com/user-attachments/assets/97a00946-4d56-4811-b4aa-cad2038736cc" />
               👉 Capture 2 : fonction factorial
               
<img width="500" height="367" alt="image" src="https://github.com/user-attachments/assets/cec362a6-995e-462b-91de-59d7ff470721" />
              👉 Capture 3 : fonction reverseString
             
<img width="425" height="379" alt="image" src="https://github.com/user-attachments/assets/1b915fd5-46a4-4fcd-ac0c-4e81eb4455bf" />
              👉 Capture 4 : fonction sumArray

# Étape 6 — Déclaration des méthodes natives côté Java

🔹 Accès au fichier

app/src/main/java/com/example/jnidemo/MainActivity.java

🔹 Déclaration des méthodes

public native String helloFromJNI();
public native int factorial(int n);
public native String reverseString(String s);
public native int sumArray(int[] values);

🔍 Explication

🔸 native

Indique que la méthode est implémentée en C++.

🔹 Chargement de la bibliothèque

static {
    System.loadLibrary("native-lib");
}

⚠️ Point critique (TRÈS IMPORTANT)

👉 Le nom doit être EXACT :

native-lib

❌ Ne pas écrire :

libnative-lib.so

🔍 Pourquoi ?

Android ajoute automatiquement :

lib
.so
🔹 Appel des fonctions natives

Dans onCreate() :

appel helloFromJNI()
calcul du factoriel
inversion de chaîne
somme d’un tableau

👉 Résultats affichés dans des TextView

📸 Captures à insérer

<img width="325" height="178" alt="image" src="https://github.com/user-attachments/assets/adc1ab4a-8c6a-4da7-9cd3-bb0c790e370e" />
👉 Capture 6 : déclaration des méthodes native

<img width="266" height="65" alt="image" src="https://github.com/user-attachments/assets/31352978-b891-4dc6-8ad3-cd9206315286" />
👉 Capture 7 : bloc System.loadLibrary

<img width="481" height="347" alt="image" src="https://github.com/user-attachments/assets/ec14df24-f56d-4c1f-8877-fec7da918858" />
👉 Capture 8 : méthode onCreate()

<img width="188" height="374" alt="image" src="https://github.com/user-attachments/assets/e8d2c48b-2df2-4e9e-a489-0205efd785b2" />
👉 Capture 9 : affichage dans l’application

- Cette application démontre l’utilisation de JNI pour exécuter du code natif en C++ dans une application Android.
Les tests réalisés (factoriel, inversion de chaîne, somme de tableau) montrent la communication efficace entre Java et le code natif.

# Étape 7 — Créer le layout XML complet

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
