# GitHubViewer
Contians source code for code challenge


#### GitHubViewer

### Prerequisites

- AndroidStudio 3.0
- Java JDk 1.8
- Android SDK minmum API level 15 for installing the apk but the project is built using sdk version 26

#### Libraries used in the project

- Gson - Gson is a Java library that can be used to convert Java Objects into their JSON representation. 
        It can also be used to convert a JSON string to an equivalent Java object. Gson can work with arbitrary 
        Java objects including pre-existing objects that you do not have source-code of.

- RetroFit - Type-safe HTTP client for Android and Java by Square, Inc.

- Data Binding - Android offers support to write declarative layouts using data binding. 
                This minimizes the necessary code in your application logic to connect to the user interface elements.
                
- Glide - Glide is a fast and efficient image loading library for Android focused on smooth scrolling. 
          Glide offers an easy to use API, a performant and extensible resource decoding pipeline and automatic resource pooling.
          
- Android-architecture-components
  - Lifecycle-aware components

  - ViewModels

  - LiveData

-RecyclerViews

### Workflow of the application

The app nam is GitHubviewer. Once installed it can be found in All App screen on the phone with **ic-launcher icon.

When the user clicks on the launcher icon:

1. The user is taken to repository viewer screen and is prestend with a list of "android-libray" repositories.
2. The git search api is queried with topic "android-library", so the user will be presented with only repositories 
   which contains "android-libray" as topic. This is hardcoded in the code and is not configurable.
3. The list items shows the details of the car such as
    a. Repository Name
    b. Owner avatar image.
    c. Fork count
    d. Description
4. As the user scrolls the list, the list is loaded with new repositories by making new request. The progress is 
   notified to the user with a loading bar at the bottom of the screen.
5. The repositories are loading using pagination technique. The number of results per page is 30, 
   scrolling triggers pagination and loads subeseqeunt results.
6. After every requst the results are cached in the memory. Database is not used as of this implementation.
   An inmemry cache is created using Map to avoid too many network calls.
7. The user can exit from the app by pressing backbutton at any point of time.

## Error Scenarios
1. The git api has a limit of 60 requests per hour for users not using authentication. This implementation 
   does not use any authentication and hence we can get Error 403. All http errors are displayed to the user via Toast message.

### Design

1. The application is based on mvvm pattern. In mvvm architecture, Views react to changes in the ViewModel without being called 
   explicitly
2. The http request are made using retrofit.
3. The Gson is used alongside retrofit to parser json to Java POJO.
4. The ViewModel interacts with datarepository to get the data and updates the View through databiding.
5. The datarepository handles wheter the data should be fetched from network or cache.
7. The ViewModel delegates all the requests from the view to repository and vice versa.
6. The Recylerviews are used insted of normal listviews. CardView are user to diplay each item on the list.



### Future Improvements
1. Extend the solution by incorporating Database using ORM library like Room.
2. Make code more modular by using dependency injection library like Dagger 2.
