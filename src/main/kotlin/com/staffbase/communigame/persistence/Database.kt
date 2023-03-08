import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.reflect.KClass

abstract class Database(val dbName: String) {
    private val client = KMongo.createClient().coroutine // create a client connection
    val database = client.getDatabase(dbName) // get a reference to the database

    abstract fun <T : Any> getCollection(): CoroutineCollection<T>
}
