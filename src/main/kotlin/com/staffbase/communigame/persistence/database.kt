import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

abstract class Database<T : Any>(private val collectionName: String) {
    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase("communigame")
    protected val collection: CoroutineCollection<T> = database.getCollection(collectionName)

    abstract suspend fun createNew(newItem: T): Boolean
    abstract suspend fun getAll(): List<T>
    abstract suspend fun getTop(limit: Int): List<T>
    abstract suspend fun getById(id: String): T?
}