# Streaming Exercises
Welcome to the streaming_samples project!  The purpose of this project is to help you understand basic concepts in
streaming.  To that end, code has been provided which queries the Foursquare API for recommended places.

## What is a stream?
By stream we mean streaming of objects.  When you think of an Iterable in Java, certain interfaces come to mind: Set, Collection, List.  A stream can be thought of as an analog to an Iterable.

One example is in a database query.  Some frameworks such as JdbcTemplate, Hibernate, etc., query an entire dataset and return them as Iterables of objects.  Typically they're stored entirely in memory.  In streaming, you can stream out individual elements of a dataset as an individual object, and react to it sequentially without storing the entire contents in memory.  This is a powerful concept because it allows for some elegant data processing but with less memory requirements, and potentially faster time to process.

## Reading material
These exercises included in this project involve streaming data from a remote endpoint.  We are using native Java 8 Streams to accomplish this goal.  Below you'll find some reading material to help you get acquainted with this framework:

* The Stream is the main object we're going to be working with. Here is an article from Oracle about Streams:
  http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
* This blog post deals with a lot of the operations on Streams:
   http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/

## To build
Simply clone this project locally and run mvn install -DskipTests=true.  Next, run the tests in your IDE--they should *mostly* pass, leading us to the exercises...


## Exercises
Once you feel comfortable with the reading material, a series of exercises has been prepared.

* Four unit tests in FoursquareQueryerTest are not yet implemented.  Please find a way to complete the test using
Stream concepts.
* MainController currently has a very simple implementation.  Please find a streaming way to achieve a more complicated
objective: query simultaneously only the highest priced and highest rates eats and print them to the user.
