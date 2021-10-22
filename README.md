
**Auteur: Tom Perrillat-Collomb Baptiste Lalanne**
**Date: 22/10**


***

## 1. Description


*Le but est d'implanter une application qui permettent aux clients de discuter de manière texte 
entre eux. La version basique permet à un client de communiquer avec un autre client. Le TP est 
incrémental, vous pouvez ajouter des fonctionnalités à votre application à mesure que votre 
implantation progresse : discussions de groupe, enregistrement des discussions, cryptage point à 
point, les seules limites sont celles de votre imagination et du temps que vous allez y consacrer.*



***

## 2. Execution

#### 1. Compilation 
Nous avons fait un Makefile dans notre projet pour faciliter la compilation sans utiliser des surcouches peu utile dans ce projet comme Maven

**Liste des commandes**
  * ``make clean`` : build client pour dev
  * ``make`` : build client pour prod


#### 2. Lancement du serveur

Pour lancer le serveur:
```bash
./run_server.sh 
```

Lancer le serveur lance dans l'ordre:
1. ``make clean``
2. ``make``
3. ``java -cp .:./lib/json-simple-1.1.1.jar stream.EchoServerMultiThreaded 224.0.0.251 6001``



#### 3. Lancement du client

Pour lancer le client:
```bash
./run_client.sh 
```

Lancer le client lance:
1. ``java -cp .:./lib/json-simple-1.1.1.jar stream.EchoClient 224.0.0.251 6001``

