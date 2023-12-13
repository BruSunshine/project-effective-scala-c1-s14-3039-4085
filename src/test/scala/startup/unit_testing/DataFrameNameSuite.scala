package startup.unit_testing

import java.nio.file.{Files, Paths}
import munit.FunSuite
import org.apache.spark.sql.{Dataset, Row}
import sparkjobs.DataFramesExemples
import startup.ast.DataFrameName.{toDataFrameName, contentHash}
import startup.ast.DataFrameName
import startup.ast.DataFrameName.given
import upickle.default.{read, write}

class DataFrameNameSuite extends munit.FunSuite:

  test(
    "toDataFrameName should correctly generates a DataFrameName and writes the dataset as a Parquet file"
  ) {
    val df: Dataset[Row] = DataFramesExemples.df3
    val dfName = df.toDataFrameName
    assert(dfName.isInstanceOf[DataFrameName])
    val path = s"./dataframes/${dfName.name}.parquet"
    assert(Files.exists(Paths.get(path)))
  }

  test("contentHash should correctly generates a hash of the dataset content") {
    val df: Dataset[Row] = DataFramesExemples.df3
    val hash = df.contentHash
    assert(hash.length == 64)
    assert(hash.matches("^[0-9a-f]+$"))
  }

  test("writeAsParquet should correctly writes the dataset as a Parquet file") {
    val df: Dataset[Row] = DataFramesExemples.df3
    val dfName = df.toDataFrameName
    val path = s"./dataframes/${dfName.name}.parquet"
    assert(Files.exists(Paths.get(path)))
  }

  test(
    "readAsDataFrame should correctly reads a Parquet file writen from a df with nullable schema as a dataset"
  ) {
    val df: Dataset[Row] = DataFramesExemples.df3
    val dfName = df.toDataFrameName
    val path = s"./dataframes/${dfName.name}.parquet"
    val readDf = dfName.readAsDataFrame
    assert(df.schema == readDf.schema)
    assert(df.count == readDf.count)
  }

  test(
    "readAsDataFrame can not recover schema reading a Parquet file written from df with non nullable schema as a dataset"
  ) {
    val df: Dataset[Row] = DataFramesExemples.df2
    val dfName = df.toDataFrameName
    val path = s"./dataframes/${dfName.name}.parquet"
    val readDf = dfName.readAsDataFrame
    assert(df.schema != readDf.schema)
    assert(df.count == readDf.count)
  }

  test(
    "Writer[Dataset[Row]] instance should correctly writes a Dataset[Row] as JSON"
  ) {
    val df: Dataset[Row] = DataFramesExemples.df3
    val dfName = df.toDataFrameName
    val json = write[Dataset[Row]](df)
    assert(json.nonEmpty)
    assert(json.contains(dfName.name))
  }

  test(
    "Reader[Dataset[Row]] instance should correctly reads a Dataset[Row] from JSON"
  ) {
    val df: Dataset[Row] = DataFramesExemples.df3
    val dfName = df.toDataFrameName
    val json = write[Dataset[Row]](df)
    val readDf = read[Dataset[Row]](json)
    assert(df.schema == readDf.schema)
    assert(df.count == readDf.count)
  }

end DataFrameNameSuite
