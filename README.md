MinionAndroidHttp
=================

An Asynchronous OkHttp Library for Android


*Features
    - OkHttp
    - Some of Android Async Http Features
    - Object GSON convertible task
    - Structure easier to understand than Async Http Features.
    - Not as scalable as their code, but not needed, because GSON conversion.
    - Arquitecture improved. Refactoring done --> Simpler  --> TOTALLY CONTROL TO WHICH INTERFACE METHODS ARE CALLED.  --> MAYBE A LITTLE MORE DANGEROUS(casting) methods

- Unique ambiguous method, mabe is onFailure, the callback could be of string content or json content...

*Wish List
    - Connections got by tag, so you can handle them together listening the same interface,
    - Parsing data in background thread
    - SyncHttpClient (and all Android Async Http features)