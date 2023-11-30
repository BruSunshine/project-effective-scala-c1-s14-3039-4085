/*
package sparkdata.dataframeapi

/* SimpleApp.scala */
import org.apache.spark.sql.SparkSession

object SimpleApp {
  def main(args: Array[String]): Unit = {
    val logFile =
      "YOUR_SPARK_HOME/README.md" // Should be some file on your system
    val spark = SparkSession.builder
      .appName("Simple Application")
      .master("local[*]") // Use all available cores local[*] ap4y5wmc.bd.wcp spark://???.wuerth.com:7136
      //.config("spark.yarn.principal", "your_principal") // Replace with your Kerberos principal
      //.config("spark.yarn.keytab", "path/to/your/keytab") // Replace with the path to your keytab file
      .config("spark.executor.memory", "1g") // Set executor memory to 2 GB
      .config("spark.executor.cores", "2") // Set number of cores per executor to 4
      // ANY NECESSARY CONFIG typically your spark_config_defaults and spark_config_credentials 
      .getOrCreate()
    val logData = spark.read.textFile(logFile).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()
  }
}
*/