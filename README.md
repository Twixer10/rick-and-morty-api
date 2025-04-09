# Rick and Morty - Architecture du Projet

Ce projet est une application Android développée en utilisant Kotlin Multiplatform avec une architecture "Clean Architecture". L'application permet d'explorer les personnages, épisodes et lieux de la série Rick and Morty.

## 1. Vue d'Ensemble de l'Architecture

Le projet suit une architecture Clean Architecture avec une séparation claire des responsabilités. Voici la structure détaillée :

### 1.1 Structure des Packages

```
org.mathieu.cleanrmapi/
├── common/           # Composants communs et utilitaires
├── data/            # Couche de données
│   ├── remote/      # Sources de données distantes (API)
│   ├── local/       # Sources de données locales
│   ├── repositories/ # Implémentations des repositories
│   ├── validators/  # Validateurs de données
│   └── extensions/  # Extensions Kotlin
├── domain/          # Couche métier
│   ├── character/   # Logique métier liée aux personnages
│   ├── episode/     # Logique métier liée aux épisodes
│   └── location/    # Logique métier liée aux lieux
└── ui/              # Couche présentation
    ├── core/        # Composants UI réutilisables
    └── screens/     # Écrans de l'application
```

## 2. Pattern MVI (Model-View-Intent)

Le projet utilise le pattern MVI (Model-View-Intent) pour la gestion de l'état et des interactions utilisateur. Voici comment cela fonctionne :

### 2.1 Composants MVI

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    View     │────▶│   Intent    │────▶│   Model     │
│  (UI/State) │◀────│  (Action)   │◀────│  (State)    │
└─────────────┘     └─────────────┘     └─────────────┘
```

1. **Model (État)**
   - Représente l'état de l'interface utilisateur
   - Implémenté via des classes de données ou des sealed interfaces
   - Exemple : `CharacterDetailsState`, `EpisodeDetailsState`
   - Géré par un `StateFlow` dans le ViewModel

2. **View (Vue)**
   - Composables Jetpack Compose
   - Observe l'état via `collectAsState()`
   - Envoie des intents via des callbacks
   - Exemple : `CharacterDetailsScreen`, `EpisodeDetailsScreen`

3. **Intent (Actions)**
   - Représente les actions utilisateur
   - Implémenté via des sealed interfaces
   - Exemple : `CharacterDetailsAction`, `EpisodeDetailsAction`
   - Traité par le ViewModel via `handleAction()`

### 2.2 Flux de Données

```
1. L'utilisateur interagit avec la View
2. La View émet un Intent
3. Le ViewModel reçoit l'Intent via handleAction()
4. Le ViewModel met à jour le State
5. La View observe le changement d'état et se met à jour
```

### 2.3 Exemple Concret

```kotlin
// State
sealed interface CharacterDetailsState {
    object Loading : CharacterDetailsState
    data class Error(val message: String) : CharacterDetailsState
    data class Loaded(
        val name: String,
        val avatarUrl: String,
        // ...
    ) : CharacterDetailsState
}

// Intent
sealed interface CharacterDetailsAction {
    data class SelectedEpisode(val episode: Episode) : CharacterDetailsAction
    data class SelectedLocation(val locationId: Int) : CharacterDetailsAction
}

// ViewModel
class CharacterDetailsViewModel : ViewModel<CharacterDetailsState> {
    fun handleAction(action: CharacterDetailsAction) {
        when (action) {
            is CharacterDetailsAction.SelectedEpisode -> 
                sendEvent(Destination.EpisodeDetails(action.episode.id.toString()))
            is CharacterDetailsAction.SelectedLocation ->
                sendEvent(Destination.LocationDetails(action.locationId.toString()))
        }
    }
}
```

### 2.4 Avantages du Pattern MVI

- **État Unidirectionnel** : Le flux de données est prévisible et facile à déboguer
- **Immutabilité** : Les états sont immuables, réduisant les bugs liés aux mutations
- **Testabilité** : Chaque composant peut être testé indépendamment
- **Séparation des Responsabilités** : Chaque composant a un rôle clairement défini
- **Réactivité** : Réponse immédiate aux actions utilisateur
- **Maintenabilité** : Code plus facile à maintenir et à faire évoluer

## 3. Architecture Multiplateforme (KMP)

Le projet utilise Kotlin Multiplatform (KMP) pour permettre le partage de code entre différentes plateformes. Voici comment cela fonctionne :

### 3.1 Structure des Sources
```
composeApp/src/
├── commonMain/      # Code partagé entre toutes les plateformes
├── androidMain/     # Code spécifique à Android
└── desktopMain/     # Code spécifique au Desktop
```

### 3.2 Partage de Code
- **commonMain** : Contient tout le code partagé entre les plateformes
  - Logique métier (domain)
  - Gestion des données (data)
  - Interface utilisateur commune (ui)
  - Modèles et entités
  - Repositories
  - Cas d'utilisation

- **Plateformes spécifiques** : Contiennent uniquement le code nécessaire pour chaque plateforme
  - Implémentations spécifiques des interfaces
  - Adaptateurs pour les APIs natives
  - Configuration spécifique à la plateforme

### 3.3 Avantages de cette Architecture
- Réutilisation maximale du code
- Maintenance simplifiée
- Cohérence entre les plateformes
- Performance native sur chaque plateforme
- Possibilité d'ajouter facilement de nouvelles plateformes

### 3.4 Gestion des Dépendances
Les dépendances sont gérées via Gradle avec des configurations spécifiques pour chaque plateforme :
- Dépendances communes dans `commonMain`
- Dépendances spécifiques dans les sources de chaque plateforme
- Configuration des cibles de compilation pour chaque plateforme

## 4. Couches de l'Application

### 4.1 Couche Domain
- Contient la logique métier pure
- Définit les entités et les cas d'utilisation
- Indépendante des frameworks et des détails d'implémentation
- Organisée par domaine (character, episode, location)

### 4.2 Couche Data
- Implémente les repositories définis dans le domaine
- Gère les sources de données (remote et local)
- Contient les mappers pour convertir les données
- Inclut la validation des données
- Utilise Koin pour l'injection de dépendances

### 4.3 Couche UI
- Gère l'interface utilisateur avec Compose Multiplatform
- Organisée en screens pour chaque fonctionnalité
- Utilise des ViewModels pour la gestion d'état
- Contient des composants UI réutilisables

## 5. Technologies Utilisées

- **Kotlin Multiplatform** : Pour le développement multiplateforme
- **Compose Multiplatform** : Pour l'interface utilisateur
- **Koin** : Pour l'injection de dépendances
- **Ktor** : Pour les appels réseau
- **Kotlinx Serialization** : Pour la sérialisation JSON
- **Coroutines** : Pour la programmation asynchrone
- **Flow** : Pour les flux de données réactifs

## 6. Patterns et Principes

- Clean Architecture
- Repository Pattern
- Dependency Injection
- MVI (Model-View-Intent)
- Single Responsibility Principle
- Interface Segregation
- Dependency Inversion

Cette architecture permet une maintenance facile, une testabilité accrue et une évolution simple du projet.