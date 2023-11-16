package app

import startup.runComputation

object MinimalApplication extends cask.MainRoutes:
  
  @cask.get("/")
  def hello() =
    "Hello World!" + runComputation()

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()