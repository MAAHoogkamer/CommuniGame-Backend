import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

abstract class Database<T : Any>(collectionName: String) {
    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase("communigame")
    protected val collection = database.getCollection<T>(collectionName)

    suspend fun findById(id: String): T? {
        return collection.findOneById(id)
    }

    suspend fun findAll(): List<T> {
        return collection.find().toList()
    }


}