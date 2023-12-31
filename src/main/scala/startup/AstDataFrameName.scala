package startup.ast

import app.SparkMain.{sparkSession}
import java.nio.file.{Paths, Files}
import upickle.default.{macroRW, Reader, Writer}
import org.apache.spark.sql.{Dataset, Row}

/** A case class that represents the name of a DataFrame.
  *
  * @param name
  *   The name of the DataFrame, which is a path to a Parquet file.
  */
case class DataFrameName(name: String)

/** Companion object for `DataFrameName` that provides several extension methods
  * and implicit instances.
  */
object DataFrameName:

  /** Provides an extension method for `Dataset[Row]` to write the dataset as a
    * Parquet file.
    *
    * The method generates a path based on the hash code of the dataset, writes
    * the dataset to this path as a Parquet file, and returns a `DataFrameName`
    * instance that represents the path.
    *
    * @param df
    *   The dataset to write.
    * @return
    *   The `DataFrameName` instance that represents the path to the Parquet
    *   file.
    */
  extension (df: Dataset[Row])

    /** Converts a DataFrame to a DataFrameName by writing it as a Parquet file.
      *
      * The method generates a DataFrameName based on the content hash of the
      * DataFrame, writes the DataFrame to a Parquet file, and returns the
      * DataFrameName.
      *
      * @return
      *   The DataFrameName that represents the DataFrame.
      */
    def toDataFrameName: DataFrameName =
      val dfId = df.contentHash
      val name = DataFrameName(dfId)
      name.writeAsParquet(df)
      name

    /** Calculates the content hash of a DataFrame.
      *
      * The method sorts the DataFrame by the first column, converts it to a
      * string, and calculates the SHA-256 hash of the string.
      *
      * @return
      *   The content hash of the DataFrame.
      */
    def contentHash: String =
      val firstColumnName = df.columns(0)
      val sortedDf = df.sort(firstColumnName)
      val contentString = sortedDf.collect().mkString
      val md = java.security.MessageDigest.getInstance("SHA-256")
      val hash = md
        .digest(contentString.getBytes("UTF-8"))
        .map("%02x".format(_))
        .mkString
      hash

  extension (name: DataFrameName)

    /** Writes a DataFrame as a Parquet file.
      *
      * The method writes the DataFrame to a Parquet file at a path based on the
      * DataFrameName. If a file already exists at the path, the method does
      * nothing.
      *
      * @param df
      *   The DataFrame to write.
      */
    def writeAsParquet(df: Dataset[Row]): Unit =
      val path = s"./dataframes/${name.name}.parquet"
      if (!Files.exists(Paths.get(path))) then
        df.coalesce(1).write.mode("overwrite").parquet(path)

    /** Reads a DataFrame from a Parquet file.
      *
      * The method reads a DataFrame from a Parquet file at a path based on the
      * DataFrameName.
      *
      * @return
      *   The DataFrame read from the Parquet file.
      */
    def readAsDataFrame: Dataset[Row] =
      val path = s"./dataframes/${name.name}.parquet"
      sparkSession.read.parquet(path)

  /** Provides an implicit `Writer[Dataset[Row]]` instance.
    *
    * This instance can write `Dataset[Row]` instances as JSON by converting
    * them to `DataFrameName` instances using the `writeAsParquet` method.
    */
  given Writer[Dataset[Row]] =
    macroRW[DataFrameName]
      .comap(_.toDataFrameName)

  /** Provides an implicit `Reader[Dataset[Row]]` instance.
    *
    * This instance can read `Dataset[Row]` instances from JSON by converting
    * `DataFrameName` instances to datasets using the `readAsDataFrame` method.
    */
  given Reader[Dataset[Row]] =
    macroRW[DataFrameName]
      .map(_.readAsDataFrame)

end DataFrameName
