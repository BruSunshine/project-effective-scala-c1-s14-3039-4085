Effective Programming in Scala --- Final Project
------------------------------------------------

# Checklist

This readme only contains a check-list to eas the evaluation process. Your
task is to create all the content of the project while respecting the 
requirements provided in the unit “Final Project Description”.

Please edit the content of the README to facilitate the evaluation work 
from the reviewers:

- Which build tool is used? Give the name of the build tool.
- Which third-party libraries are used? Give the names of the libraries, and 
  add links to the relevant parts of the build definition.
- Where are the unit tests? List the unit test files.
- What is the domain model? Give links to the relevant Scala source files.
- What are the business operations? Give links to the Scala source files 
  that contain loops.
- Which collections do you use? Give links to the relevant Scala source files.
- What type of data validation do you do? Give links to the relevant Scala 
  source files.
- How do you report input validation errors? Give links to the relevant 
  Scala source files.
- How do you handle the other types of errors? Give links to the relevant 
  Scala source files.


# Project plan

exposing a web API to run queries against a Spark dataframe. The roadmap for implementation:

## Learning - familiarizing with the involved technologies
Implement a simple AST for arithmetic operations (e.g. `2+3` is represented as `Plus(Num(2), Num(3))`)
Implement a function - interpreter for that AST (e.g. `f(x: Ast): Int` for some `Ast` trait you will define to be a parent of all expression case classes)
Expose that function using Cask to be available over web JSON API. I.e. the user would be available to submit arithmetic expressions as JSON and the server will convert them to Ast, run the interpreter on it, and return the user the result.

## Implementation - doing the initial project idea using Spark
Implement an AST for desired operations to be run against the dataframe.
Implement an interpreter to execute those operations against the dataframe.
Expose the interpreter using Cask for the users to be able to submit arbitrary queries via JSON API.

## Resources
Spark course on Coursera: https://www.coursera.org/learn/scala-spark-big-data
Scala specialization on Coursera: https://www.coursera.org/specializations/scala
Cask: https://com-lihaoyi.github.io/cask/
Scalapy: https://github.com/scalapy/scalapy
Upickle - this library is used by Cask to convert JSON to and from case classes.: https://com-lihaoyi.github.io/upickle/

