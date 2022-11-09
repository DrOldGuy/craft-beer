# craft-beer

Demonstrates how to read a file in the classpath and how to parse data.

# Where to start

This is a Spring Boot standalone (non-Web) project. It reads craft beer data from src/main/resources/beer-data.txt, parses the data and creates a List of Beer objects.

## The files

craft.beer.**CraftBeerApplication.java** This is the application entry point. Run as a Java application.

craft.beer.**CraftBeerService.java** This is a Spring service. It does all the work of reading and parsing.

craft.beer.**Beer.java** This object is populated from the beer data.