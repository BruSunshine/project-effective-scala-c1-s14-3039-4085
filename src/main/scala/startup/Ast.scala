/*
package startup.ast

/** Package `startup.ast` contains classes and objects that represent the
  * abstract syntax tree (AST) for the startup application.
  *
  * The AST is a hierarchical tree-like data structure that represents the
  * structure of the source code. Each node in the tree denotes a construct
  * occurring in the source code.
  *
  * The AST in this package is capable of handling various types of expressions,
  * including numeric values and arithmetic operations such as addition and
  * multiplication. It provides methods to evaluate these expressions, using the
  * operations defined by an implicit `ArithmeticOperation[T]` instance for the
  * type `T` of the values in the expression.
  *
  * The package also includes functionality for serializing and deserializing
  * the AST to and from JSON. This is done using the `upickle.default._`
  * library, which provides `ReadWriter` instances for reading and writing Scala
  * objects as JSON. These `ReadWriter` instances are implicitly available for
  * any type `T` that has a `ReadWriter[T]` instance.
  *
  * A special note on DataFrame serialization: The `DataFrameName` class is used
  * to represent the name of a DataFrame, which is a path to a Parquet file.
  * When a DataFrame is serialized, only the DataFrame name is written as JSON,
  * not the full content of the DataFrame. This is because DataFrames can be
  * very large, and it's more efficient to read and write them directly from and
  * to the file system. The `DataFrameName` class provides extension methods for
  * `Dataset[Row]` and `DataFrameName` to write a dataset as a Parquet file and
  * read a Parquet file as a dataset, respectively.
  *
  * This package is part of the `startup` application.
  */
*/