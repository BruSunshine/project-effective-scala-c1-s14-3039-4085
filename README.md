Effective Programming in Scala --- Final Project
------------------------------------------------

## Checklist

Checklist to facilitate the evaluation work from the reviewers:

> Which build tool is used? Give the name of the build tool.

- SBT  

> Which third-party libraries are used? Give the names of the libraries, and 
  add links to the relevant parts of the build definition.

- Cask, upickle, spark see the imports in build.sbt file lines 7-10  

> Where are the unit tests? List the unit test files.

- Testing is organised in src/test/scala/*, different test suite to adress the different business domains and operations within the project.  

> What is the domain model? Give links to the relevant Scala source files. 

- we manipulate expressions based on the types Integers, Double, or Dataset[Row] (Spark DataFrame).  

- a spark sessions is used to manipulate the type dataframe (object sparkjobs.Session located in src/main/scala/sparkjobs/SparkJob.scala). 

- some dataframes and expressions of these types are created to be used in the tests (objects sparkjobs.{SparkJob, DataFramesExemples, ExpressionsExemples} located in src/main/scala/sparkjobs/DataframesAndExpressions.scala).  

- an Abstract Syntax Tree (AST) represents the expression of some operations on those types (sealed trait startup.ast.Expression[T] located in src/main/scala/startup/AstExpression.scala).    

- a reader & writer converts the AST to a JSON format to be transfered between server and client (case class ExpressionToSerialize[T] located in src/main/scala/startup/AstExpressionSerialize.scala).
  - It uses a supporting class startup.ast.ExpressionToSerialize for correct interfacing with the cask framework.
  - Custom Reader and Writers are defined as given definitions to support the AST serialization process for the types:  
    - Dataset[Row] in object startup.ast.DataFrameName  
    - Expression[T] in object startup.ast.Expression
    - ExpressionToSerialize[T] in object startup.ast.ExpressionToSerialize.  
  - The other types, including the ones used in the validation process are supported by the upickle framework.

- a main application (trait app.MyApp located in src/main/scala/app/MyApp.scala) is used as entry point to start the web server and a spark session for processing dataframes. 


> What are the business operations? Give links to the Scala source files 
  that contain loops.

- an interpreter interpretes the AST to produce a validated result (methods in object startup.ast.Expression located in src/main/scala/startup/AstExpression.scala).

- according the concrete implementation of the operations performed in the AST (methods in trait startup.ast.ArithmeticOperation[T] located in src/main/scala/startup/AstArithmeticOperation.scala). 
  
- methods read and write available from the reader & writer convert the AST to a JSON format to be transfered between server and client (case class ExpressionToSerialize[T] located in src/main/scala/startup/AstExpressionSerialize.scala).

- a dataframe interface is used to handle local file system storage supporting the reader and writer (methods in the companion object of the case class startup.ast.DataFrameName located in src/main/scala/startup/AstDataFrameName.scala).    

- a web API is used to expose the interpreter to the client (methods in object web.JsonPost located in src/main/scala/web/WebServer.scala).


> Which collections do you use? Give links to the relevant Scala source files.

- We use basic scala.collection.immutable.List in the validation process on the left side of the Either type.  

> What type of data validation do you do? Give links to the relevant Scala 
  source files.

- a validator validates the operations: trait startup.ast.OperationValidator[T] provides validation rules as given definitions to be used in the method startup.ast.Expression.evaluateValidExpression located in src/main/scala/startup/AstExpression.scala.  

- a validator validates the input data: trait startup.ast.ArgumentValidator[T] provides validation rules as given definitions to be used in the method startup.ast.Expression.validateExpression located in src/main/scala/startup/AstExpression.scala.   

> How do you report input validation errors? Give links to the relevant 
  Scala source files.

- the validation process is started from the creation of an expression, where we generate a type Either[List[String], Expression[T]] from the method startup.ast.Expression.validateExpression. The input validation errors are accumulated in the list of strings.   

- This validation is passed through the evaluation in method startup.ast.Expression.evaluateValidExpression, where we generate again a type Either[List[String], Expression[T]]. The validation errors due to operation are accumulated in the list of strings containing the existing validation errors.  

- The validation information is carried throughout the serialization / deserialization process. This is supported by the case class startup.ast.ExpressionToSerialize[T]

- With this method:
  - a user builds an expression in the front-end form.  
  - the expression is converted to a json via the javascript script.  
    at this stage no validation occurs and any input accepted by the form is converted as a json string.  
  - the expression is send via json post request to the scala back-end.  
  - the expression is deserialized, then validated, then evaluated, then the validated result is converted as a validated expression, then serialized again.  
  - the serialized validated expression containing the result is unwrapped to be display in the front-end as an optional value.  

> How do you handle the other types of errors? Give links to the relevant 
  Scala source files.

- the other types of errors are handeled as a Failure in the cask endpoint responsible for the evaluation of the expression: web.JsonPost.  


## Project plan

Exposing a web API to run queries against a Spark dataframe.  

Roadmap for implementation:  

### Learning - familiarizing with the involved technologies

- Implement a simple AST for arithmetic operations (e.g. `2+3` is represented as `Plus(Num(2), Num(3))`).  
- Implement a function - interpreter for that AST (e.g. `f(x: Ast): Int` for some `Ast` trait you will define to be a parent of all expression case classes).  
- Expose that function using Cask to be available over web JSON API. I.e. the user would be available to submit arithmetic expressions as JSON and the server will convert them to Ast, run the interpreter on it, and return the user the result.  

### Implementation - doing the initial project idea using Spark

- Implement an AST for desired operations to be run against the dataframe.  
- Implement an interpreter to execute those operations against the dataframe.  
- Expose the interpreter using Cask for the users to be able to submit arbitrary queries via JSON API.  

### Resources

- Spark course on Coursera: https://www.coursera.org/learn/scala-spark-big-data
- Scala specialization on Coursera: https://www.coursera.org/specializations/scala
- Cask: https://com-lihaoyi.github.io/cask/
- Upickle - this library is used by Cask to convert JSON to and from case classes.: https://com-lihaoyi.github.io/upickle/

## Project front-end user interface

A script.js located in /src/main/resources/static/script.js and the associated dataAcquisitionForm.html provides the user with:  
- a convenient interface to build an AST expression of various types.  
- a trigger to converted this expresssion in a regular json acceptable for the back-end.  
- a trigger to send the json to the back-end and display the result page.  

![Alt text](/src/main/resources/astExpressionBuilder.png?raw=true "AST Expression Builder")
