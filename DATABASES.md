`MongoDB` is a document-oriented NoSQL database, which means it stores data in flexible, JSON-like documents,
rather than in tables with predefined columns and rows. `MongoDB` is a popular choice for modern web applications
and for use cases where data is complex, unstructured, or changes frequently.
`MongoDB` is also well-suited for large-scale, distributed systems and can handle high write throughput.


`Postgres`, on the other hand, is a traditional SQL database that stores data in tables with predefined 
columns and rows. It is known for its reliability, robustness, 
and ability to handle complex queries and transactions.
`Postgres` is often used in enterprise applications and for use cases where data is well-structured and consistent.


Here are a few key differences between the two:
<br>
1. Data Model: As mentioned, `MongoDB` uses a document-oriented data model,
while `Postgres` uses a relational data model.
   <br>
2. Schema: `MongoDB` has a flexible schema, meaning that the structure of the data can evolve over time,
whereas `Postgres` has a rigid schema, meaning that the structure of the data is predefined.
   <br>
3. Transactions: `Postgres` supports ACID transactions, which means that all transactions are
`atomic, consistent, isolated, and durable`, whereas `MongoDB`'s support for transactions is more limited.
   <br>
4. Performance: `MongoDB` is designed to handle large amounts of unstructured data,
which makes it well-suited for high write throughput and scalability,
while `Postgres` is optimized for complex queries and transactions.


The choice between `MongoDB` and `Postgres` depends on the specific needs of your application. <br>
If your data is complex and unstructured, and you need high write throughput, `MongoDB` may be the better choice. <br>
If your data is well-structured and consistency is critical, 
and you need to perform complex queries and transactions,<br> `Postgres` may be a better fit.<br>
<br>
Setting up MongoDB isn't more trouble than setting up Postgres, but it does require a slightly different approach. 
For example, in MongoDB, you would need to define the structure of your data in a document format, 
rather than a table format. 
You would need to consider the sharding and replication strategies to ensure high availability
and scalability for your application. However, many cloud providers offer managed MongoDB services that can simplify
the setup and management process.

<details>
<summary>Sharding</summary>
Sharding is a technique used to horizontally partition a database across multiple servers or nodes to distribute
the workload and improve performance and scalability. In a sharded database, each shard contains a subset of the data, 
and each node in the cluster is responsible for serving a subset of the workload.

Sharding is typically used in large-scale distributed databases, 
where a single server cannot handle the volume of data or traffic, 
or where the data needs to be geographically distributed for better performance and availability.

To shard a database, the data is typically split based on a shard key, 
which is a unique identifier that determines which shard the data belongs to. 
The shard key can be based on various criteria, such as the value of a specific field, a geographic location, 
or a hash of the data.

One of the benefits of sharding is that it allows for linear scalability, 
meaning that as the database grows, more nodes can be added to the cluster to handle the additional workload. 
However, sharding also introduces additional complexity in terms of managing data distribution, 
ensuring data consistency across shards, and handling failover and recovery in case of node failure.
</details>

Start postgres through terminal:<br>
```
docker run -d -p 9191:8080 --name test-postgres -e POSTGRES_PASSWORD=pacman -d postgres
```
Start mongodb through terminal:<br>
```
docker run -p 9292:27017 --name test-mongo -d mongo:latest
```
Recommended: Use the following to use the standard 27017 port:
```
docker run --name test-mongo -d mongo:latest
```
Connect to the container: `docker exec -it test-mongo bash`<br>
Launch the shell client: `mongosh`<br>
Show databases: `show dbs`<br>
Use/create db: `use {name}`<br>
Create data in collection 'people': `db.people.save({ firstname: "Nic", lastname: "Raboy" })`<br> 
<details>
<summary>kmongo library</summary>
kmongo is a Kotlin driver for MongoDB. It provides a type-safe API for interacting with MongoDB databases in Kotlin. 
The driver uses coroutines to allow asynchronous interactions with the database. 
With kmongo, you can perform CRUD (create, read, update, delete) operations on collections in a MongoDB database.
<details>
<summary>Sync, Async, Serialization</summary>
Since using kmongo to interact with MongoDB, recommended using the coroutine-based API provided by kmongo 
to query the database. Coroutine-based APIs allow to write asynchronous, 
non-blocking code that can help the application be more responsive and efficient.

Use coroutines to perform database operations without blocking the main thread. For example, 
when you perform a long-running database operation, you can use a coroutine to keep the UI responsive while 
the operation is running in the background. This can improve the overall performance of your application.

Serialization is typically used to convert data between different formats or representations, 
such as converting between JSON and a Kotlin data class. While it's possible to use serialization to query the database,
it may not be the most efficient or flexible way to do so. The coroutine-based API provided by kmongo is 
designed specifically for interacting with MongoDB and is likely to be more suitable for your needs.
</details>
<details>
<summary>coroutines</summary>
Coroutines are a powerful feature of Kotlin that enable asynchronous and non-blocking programming. 
They allow you to write asynchronous code that looks like synchronous code, 
making it easier to write, read, and maintain.

In a nutshell, a coroutine is a lightweight thread that can be paused and resumed at any point, 
allowing for concurrency without using multiple threads. Coroutines are cooperative, 
meaning they must explicitly yield control to other coroutines, allowing them to run.

With coroutines, you can write code that performs I/O operations, such as database queries or network requests, 
without blocking the thread that is executing the code. 
Instead, the coroutine is suspended until the I/O operation completes, and then resumed with the result.

Kotlin's coroutine support is built on top of the standard Java Thread API and offers a simpler and more efficient
way to write concurrent code. Coroutines provide a clean and expressive way to write asynchronous and non-blocking code, 
making it easier to write scalable and performant applications.
</details>
</details>

API has DTO <-> DBO, mappen <br>
MongoDB (any DB use with Database plugin)<br>
Install MongoDB library to Kotlin (kmongo)<br>

(Domainobjecten API = thema. businessobjecten) <br>