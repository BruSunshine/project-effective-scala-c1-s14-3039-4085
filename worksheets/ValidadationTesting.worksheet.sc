import startup.ast.{Expression, Num, Plus, Mult}


try
    val invalidNum = Num(-1)
catch
    case error => println(s"this is the error: $error")

Expression.validateExpression1(Num(-1))