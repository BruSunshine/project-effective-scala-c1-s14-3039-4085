package startup.integration_testing

import munit.FunSuite

trait IntegrationSuite extends munit.FunSuite
/*
class httpServerSuite extends IntegrationSuite:
    
    val httpServer = startup.HttpServer()
    
    override def beforeAll(): Unit = httpServer.start(8888)
    
    override def afterAll(): Unit = httpServer.stop()
    
    test("server is running") {
        // Perform HTTP request here
    }

    test("server is doing ...") {
        // Perform HTTP request here
    }

end httpServerSuite

*/