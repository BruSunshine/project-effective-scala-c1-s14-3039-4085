
package ammonite
package $file.worksheets
import _root_.ammonite.interp.api.InterpBridge.{
  value => interp
}
import _root_.ammonite.interp.api.InterpBridge.value.{
  exit,
  scalaVersion
}
import _root_.ammonite.interp.api.IvyConstructor.{
  ArtifactIdExt,
  GroupIdExt
}
import _root_.ammonite.compiler.CompilerExtensions.{
  CompilerInterpAPIExtensions,
  CompilerReplAPIExtensions
}
import _root_.ammonite.runtime.tools.{
  browse,
  grep,
  time,
  tail
}
import _root_.ammonite.compiler.tools.{
  desugar,
  source
}
import _root_.mainargs.{
  arg,
  main
}
import _root_.ammonite.repl.tools.Util.{
  PathRead
}
import _root_.ammonite.repl.ReplBridge.value.{
  codeColorsImplicit
}


object `generate-requests.worksheet`{
/*<script>*/import $ivy.$                           
import $ivy.$                          

val jsonString = os.read(os.pwd / "worksheets" / "testjson.json")
/*<amm>*/val res_3 = /*</amm>*/jsonString


import upickle.default._

sealed trait Expression[T]
case class Num[T](value: T) extends Expression[T]
case class Plus[T](left: Expression[T], right: Expression[T]) extends Expression[T]
case class Mult[T](left: Expression[T], right: Expression[T]) extends Expression[T]

val x = Num(2)


/*
import java.net.{URL, HttpURLConnection}
import java.io.{BufferedReader, InputStreamReader}


val url = new URL("http://localhost:8080/do-thing")
val connection = url.openConnection().asInstanceOf[HttpURLConnection]

connection.setRequestMethod("POST")
connection.setDoOutput(true)

connection.getOutputStream.write("hello".getBytes("UTF-8"))

val responseCode = connection.getResponseCode
val responseMessage = connection.getResponseMessage

if (responseCode == 200)
        val reader = new BufferedReader(new InputStreamReader(connection.getInputStream))
        val response = LazyList.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
        println(response)
else
  println(s"POST request not successful. Response Code: $responseCode")

connection.disconnect()

*/

/*
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets

// Read the JSON file into a String
//println(System.getProperty("user.dir"))
val jsonFilePath = Paths.get("./worksheets/testjson.json")
val jsonStr = new String(Files.readAllBytes(jsonFilePath), StandardCharsets.UTF_8)

jsonStr

val url = new URL("http://localhost:8080/json-obj")
val connection = url.openConnection().asInstanceOf[HttpURLConnection]

connection.setRequestMethod("POST")
connection.setDoOutput(true)
//connection.setRequestProperty("Content-Type", "application/json; utf-8")

// Write the JSON string to the output stream
connection.getOutputStream.write(jsonStr.getBytes(StandardCharsets.UTF_8))

// Get the response
val responseCode = connection.getResponseCode
println(s"Response Code: $responseCode")

connection.disconnect()
*/

/*
import $ivy.`com.lihaoyi::cask:0.9.1`
import $ivy.`com.lihaoyi::upickle:3.1.3`
import $ivy.`com.lihaoyi::requests:0.8.0`
import requests._

import java.nio.file.Paths
import io.undertow.util.HeaderMap

val image = cask.FormFile(fileName = "testjson.json", filePath = Paths.get("./worksheets/testjson.json"), headers = HeaderMap())
image.fileName


import io.undertow.Undertow


val server = Undertow.builder
        .addHttpListener(8080, "localhost")
        .setHandler(io.undertow.server.HttpHandler)
        .build
        
server.start()
*/
/*
connection.getOutputStream.write(???)

val responseCode = connection.getResponseCode
val responseMessage = connection.getResponseMessage

if (responseCode == 200)
        val reader = new BufferedReader(new InputStreamReader(connection.getInputStream))
        val response = LazyList.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
        println(response)
else
  println(s"POST request not successful. Response Code: $responseCode")

connection.disconnect()
*/

/*</script>*/ /*<generated>*/
def $main() = { _root_.scala.Iterator[String]() }
  override def toString = "generate$minusrequests$u002Eworksheet"
  /*</generated>*/
}
