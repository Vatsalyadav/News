# News
News is an Android Application to fetch News from an API and save in local storage to read later

<br>News App can:

1. Fetch News from API using Retrofit
2. Save News Articles in Local SQLite Database Storage for reading them later
3. Delete News Articles from Local SQLite Database Storage when no longer required
4. Integrated web browser to view full News article
5. Share News Articles using WhatsApp, Messaging and other Sharing apps

## Screenshots

<p>
<img src="https://github.com/Vatsalyadav/Screenshots/blob/master/newsfromapi.gif" width="250" vspace="20" hspace="5" alt="News From API" />
<img src="https://github.com/Vatsalyadav/Screenshots/blob/master/internalstorage.gif" width="250" vspace="20" hspace="5" alt="News From Internal Storage" />
</p>

## Libraries Used

* AppCompat, CardView, RecyclerView
* Glide
* Lifecycle
* Material 
* Retrofit
* RxJava
* SQLite Database
* Dagger2

## Tools and Technologies are Used :
### Android Architecture Components, Android Jetpack and RxJava :

* ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment. It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).
* LiveData is a data holder class that can be observed within a given lifecycle. This means that an Observer can be added in a pair with a LifecycleOwner, and this observer will be notified about modifications of the wrapped data only if the paired LifecycleOwner is in active state. Rather than hiding the detail of SQLite, Room tries to embrace them by providing convenient APIs to query the database and also verify such queries at compile time. This allows you to access the full power of SQLite while having the type safety provided by Java SQL query builders.

* RxJava is Java implementation of Reactive Extension (from Netflix). Basically it’s a library that composes asynchronous events by following Observer Pattern. You can create an asynchronous data stream on any thread, transform the data and consumed it by an Observer on any thread. The library offers wide range of amazing operators like map, combine, merge, filter and lot more that can be applied onto data stream.

## Architecture

### MVVM
MVVM stands for Model, View, ViewModel. MVVM facilitates a separation of development of the graphical user interface from development of the business logic or back-end logic (the data model). 

#### Model
Model holds the data of the application. Model represents domain specific data and business logic in MVC architecture. It maintains the data of the application. Model objects retrieve and store model state in the persistance store like a database. Model class holds data in public properties. It cannot directly talk to the View.
#### View
View represents the UI of the application devoid of any Application Logic. It observes the ViewModel.
#### ViewModel
ViewModel acts as a link between the Model and the View. It’s responsible for transforming the data from the Model. It provides data streams to the View. It also uses hooks or callbacks to update the View. It’ll ask for the data from the Model.
The following flow illustrates the core MVVM Pattern.
<img src="https://github.com/Vatsalyadav/NASA-pictures-app/blob/master/mvvm_architecture.png" width="750" vspace="20" hspace="5" alt="MVVM Architecture" />

## MVVM implementation in app
<b>Model</b> `News` holds Serializable News Source data and List of `Articles`

<b>Repository</b> `NewsRepository` has following purpose:
- `setNewsDatabaseHelper()` to setup NewsDatabaseHelper for communicating with local SQLite Database
- `getNewsList()` to fetch News depending upon the Internet Connectivity status and return News Flowable
- `getNewsFromUrl()` to fetch News from API using HttpURLConnection when Internet Connectivity is available
- `getNewsFromLocalStorage()` to fetch saved News from SQLite Database using NewsDatabaseHelper in absence of Internet Connectivity
- `saveNewsItem()` to add News Article in local storage
- `deleteNewsArticle()` to delete News Article from local storage

<b>ViewModel</b> 
- `NewsActivityViewModel` is a class designed to hold and manage UI-related data in a lifecycle conscious way
- `NewsActivityViewModel` will support `NewsActivity` and hold UI related data for it like `getNewsList()`, `saveNewsItem()`, `deleteNewsArticle()` and `saveWebViewCache()`
- `NewsActivityViewModel` is a lifecycle-aware and it allows data to survive configuration changes such as screen rotation.

<b>View</b> 
- `NewsActivity` interacts with ViewModel to get News Article List and observe them for any changes and shows them in list using Recycler View
- NewsActivity uses `setupRecyclerView()` method to setup Recycler View for the News Articles
- NewsActivity implements `onNewsClick()` to Navigate to `NewsDetailsActivity` for the selected News
- NewsActivity implements `onSaveClick()` to save or delete selected News Article from the Local Storage depending on its save state
<p>
<img src="https://github.com/Vatsalyadav/Screenshots/blob/master/newslist.jpg" width="250" vspace="20" hspace="5" alt="News Article List" />
<img src="https://github.com/Vatsalyadav/Screenshots/blob/master/newsfullscreen.jpg" width="250" vspace="20" hspace="5" alt="Read Full News Article" />
</p>
- Android activity layout `activity_news.xml` has RecyclerView for the News Articles and progress bar

<b>View</b> `NewsDetailsActivity` shows full News Article using WebView and inbuilt browser and provides share News feature
<p>
<img src="https://github.com/Vatsalyadav/Screenshots/blob/master/newsshare.jpg" width="250" vspace="20" hspace="5" alt="Share News" />
</p>

### Adapters
RecyclerViewAdapter `NewsListAdapter` provides a binding for News Articles, set to views that are displayed within the recyclerView for the NewsActivity. Article Images will be load using Glide, it is used for a fast image loading and caching.
RecyclerView is an efficient version of ListView which acts as a container for rendering data set of views that can be recycled and scrolled efficiently.

#### Dagger2
Dagger is a compile-time framework for dependency injection. It uses no reflection or runtime bytecode generation, does all its analysis at compile-time, and generates plain Java source code.

#### Retrofit
Retrofit is a type safe REST Client which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice. Using Retrofit, you can configure which converter is used for the data serialization.

Developed By
------------

* Vatsal Yadav  - www.linkedin.com/in/vatsalyadav 
