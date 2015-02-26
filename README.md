# Streaming Exercises
Welcome to the streaming_samples project!  The purpose of this project is to help you understand basic concepts in
reactive streaming.  To that end, code has been provided which queries the Foursquare API for recommended places.

## What is a stream?
By stream we mean streaming of objects.  When you think of an Iterable in Java, certain interfaces come to mind: Set, Collection, List.  A stream can be thought of as an analog to an Iterable, however where an Iterable is push-based (i.e., you receive the next element in the Iterator whenever you call the next() method on it), a Stream is pull-based.

One example is in a database query.  Some frameworks such as JdbcTemplate, Hibernate, etc., query an entire dataset and return them as Iterables of objects.  Typically they're stored entirely in memory.  In streaming, you can stream out individual elements of a dataset as an individual object, and react to it sequentially without storing the entire contents in memory.  This is a powerful concept because it allows for some elegant data processing but with less memory requirements, and potentially faster time to process.

## Reading material
These exercises included in this project involve streaming data from a remote endpoint.  The framework we're using to accomplish this is called Reactive Extensions (RX), a cross-platform library used to create asynchronous and event based streams.  Below you'll find some reading material to help you get acquainted with this framework:

* The Observable is the fundamental abstraction of object based streaming.  Below you'll find an excellent discussion on what they are and how they work in the next page.
  http://reactivex.io/documentation/observable.html
* RxJava has extensive official documentaiton:
   https://github.com/ReactiveX/RxJava/wiki
* RxMarbles--interactive diagrams of Rx interactions:
   http://rxmarbles.com/
* Intro to Rx--C# centric but lots of in depth discussion and examples
   http://www.introtorx.com/

## To build
Simply clone this project locally and run mvn install -DskipTests=true.  Next, run the tests in your IDE--they should *mostly* pass, leading us to the exercises...


## Exercises
Once you feel comfortable with the reading material, a series of exercises has been prepared.

* Four unit tests in FoursquareQueryerTest are not yet implemented.  Please find a way to complete the test using
Reactive concepts.
* MainController currently has a very simple implementation.  Please find a Reactive way to achieve a more complicated
objective: query simultaneously only the highest priced and highest rates eats and print them to the user.
