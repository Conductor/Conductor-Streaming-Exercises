# streaming_samples
Welcome to the streaming_samples project!  The purpose of this project is to help you understand basic concepts in
reactive streaming.  To that end, code has been provided which queries the Foursquare API for recommended places--you'll
stream this information to obtain insights.

## To build
Simply run mvn install -DskipTests=true 

## Reading material
These exercises involve streaming data from a remote endpoint.  The framework we're using to accomplish this is called
Reactive Extensions (RX), a cross-platform library used to create asynchronous and event based programs.  Below you'll
find some reading material to help you get acquainted with this framework:

* RxJava wiki: https://github.com/ReactiveX/RxJava/wiki
* RxMarbles--interactive diagrams of Rx interactions : http://rxmarbles.com/
* Intro to Rx--C# centric but lots of in depth discussion and examples: http://www.introtorx.com/

## Exercises
Once you feel comfortable with the reading material, a series of exercises has been prepared.

* Four unit tests in FoursquareQueryerTest are not yet implemented.  Please find a way to complete the test using
Reactive concepts.
* MainController currently has a very simple implementation.  Please find a Reactive way to achieve a more complicated
objective: query simultaneously only the highest priced and highest rates eats and print them to the user.