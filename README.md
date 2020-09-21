# SmartWeather
This is a simple application that allows search weather based on  https://openweathermap.org/

<br>

# Requirements
- **Language**: Kotlin
- **IDE**: Android Studio 4.0.1
- **Android version**: Android 5.0 Lollipop or higher.

<br>

# Architecture

This project implemented MVVM architecture using Koin, RxJava, Retrofit, LiveData. 

![ArchDiagram1](https://user-images.githubusercontent.com/7032500/93716800-d7f6cd80-fb9b-11ea-9f4d-da38895569ce.png)

## Code Structure:

### View
- It's the Activity/Fragment 
- It's handle all actions/inputs from user
- It can reference to multiple ViewModel to handle business, but less is better.

### ViewModel
- It's handle all UI's business, such as proceed an user's action, control loading flow,...
- It's contain LiveData that will be register by View(Activity/Fragment)

### Interactor:
- It's the main layer to handle all data business
- All interactor **MUST** be defined with an interface, ViewModels communicate with interactor through by the interface

### Repository
- The main data source of app that is used by Interactor layer
- It contains a little bit business rules to branch data source that should be used, from remove or local database
- All repositories **MUST** be defined with an interface, interactor communicate with repository through by the interface 

### Remote datasource (Api)
- It's data source layer that data is fetched from RestFul API
- It's use Retrofit to turn HTTP API into a Kotlin interface.

### Local datasource(DAO)
- It's data source layer that data is fetched from local storage (file, sqlite, ...)
- All DAOs **MUST** be defined with an interface, repository communicate with Dao through by the interface 

### Caching
- It's a cache mechanism provide by OKHttp library
- It's support cache for offline mode and reduce network request connection

Example:
```
  class CacheInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (isNetworkAvailable(context)) {
            /*
            *  If there is Internet, get the cache that was stored 60 seconds ago.
            *  If the cache is older than 60 seconds, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-age' attribute is responsible for this behavior.
            */
            request = request.newBuilder().header(
                "Cache-Control",
                "public, max-age=" + 60
            ).build()
        } else {
            /*
             *  If there is no Internet, get the cache that was stored 7 days ago.
             *  If the cache is older than 7 days, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-stale' attribute is responsible for this behavior.
             *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
             */
            request = request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()
        }

        return chain.proceed(request)
    }
}
```

### Dependency Injection
- It's use Koin as a Dependency Injection framework.
- It's include 4 modules:
    - **networkModule**        *Provide instance of HTTP Client, Retrofit, Caching, Json parser*
    - **apiModule**            *Provide Api interface*
    - **repositoryModule**     *Provide Repository instance*
    - **interactorModule**     *Provide Interactor instance*
    - **viewModelModule**      *Provide ViewModel instance*


<br>

# Main Libraries Used

## Development
- **OkHttp**: HTTP is the way modern applications network. It’s how we exchange data & media. Doing HTTP efficiently makes your stuff load faster and saves bandwidth.
More detail at: https://github.com/square/okhttp/

- **Moshi**: Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects
More detail at: https://github.com/square/moshi/

- **Retrofit** -  https://square.github.io/retrofit/

- **RxJava3** - https://github.com/ReactiveX/RxJava

- **Koin**: A pragmatic lightweight dependency injection framework for Kotlin developers. Written in pure Kotlin, using functional resolution only: no proxy, no code generation, no reflection.
More detail at: https://insert-koin.io/

- **Rxbinding**: RxJava binding APIs for Android UI widgets from the platform and support libraries.
More detail at: https://github.com/JakeWharton/RxBinding

- **Material Dialogs** - https://github.com/afollestad/material-dialogs

- **Root checker**: The library for checking is a device has been rooted or not.
More detail at: https://github.com/scottyab/rootbeer

## Testing

- **MockWebServer** - https://github.com/square/okhttp/tree/master/mockwebserver

- **Mockito** - https://github.com/mockito/mockito

- **Espresso**

- **JUnit**

<br>

# Checklists:
- [x] Programming language: Kotlin is required, Java is optional.
- [x] Design app's architecture (suggest MVVM)
- [x] Apply LiveData mechanism
- [x] UI should be looks like in attachment.
- [x] Write UnitTests
- [x] Acceptance Tests
- [x] Exception handling
- [x] Caching handling
- [ ] Secure Android app from:
    - [x] Decompile APK                          - *Configuration in Gradle build*
    - [x] Rooted device                          - *Using RootBeer library*
    - [x] Data transmission via network          - *Using Https*
    - [ ] Encryption for sensitive information   - *Just put secret key into gradle properties file*
- [ ] Accessibility for Disability Supports:
    - [x] Talkback: *Use a screen reader.*
    - [x] Scaling Text: *Display size and font size: To change the size of items on your screen, adjust the display size or font size.*