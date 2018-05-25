import common.types.HelloWorld
import io.javalin.Javalin
import kotlinx.serialization.json.JSON

fun main(args: Array<String>) {

    /*
    Create our server app a little like we would with an Express server app.
     */
    val app = Javalin.create().apply {
        enableCorsForOrigin("*")
    }.start()

    /*
    Routes should look very very similar to an Express app.
    HelloWorld() is a serializable data class common to both the front end and the back end.
    I'm using the serialization library's stringify method for now but will eventually change it up
    so that javalin uses the library which would let us use the much nicer ctx.json syntax.
     */

    app.get("/") { ctx -> ctx.result(JSON.stringify(HelloWorld())) }

    app.post("/") { ctx -> println(ctx.body()) }

}

