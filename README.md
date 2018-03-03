# Next Flick


## Overview
This Android app is using [The Movie Database's](https://www.themoviedb.org/) [API](https://developers.themoviedb.org/3/getting-started/introduction) to get the info about the movies.


## TMDB API-key is required
To get any movie info you need to create TMDB account and request an API key. When you have the key, you need to put your own to **gradle.properties** ```TMDBApiKey="YOUR_API_KEY_HERE"```


## Purpose
This repository is for Android Developer Nanodegree program I'm participating and for learning purposes.


## Libraries
* [Picasso](http://square.github.io/picasso/) to download images
* [Retrofit](http://square.github.io/retrofit/) +[Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) to make API requests and converting response to objects


## Links
* To get rounded images I made custom implementation of the [Picasso.transformation](https://square.github.io/picasso/2.x/picasso/com/squareup/picasso/Transformation.html) with the help of [this post](https://stackoverflow.com/a/26112408/649474) on Stack Overflow.
