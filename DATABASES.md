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
Additionally, you would need to consider the sharding and replication strategies to ensure high availability
and scalability for your application. However, many cloud providers offer managed MongoDB services that can simplify
the setup and management process.


Start postgres through terminal:<br>
```
docker run --name test-postgres -e POSTGRES_PASSWORD=pacman -d postgres
```
Start mongodb through terminal:<br>
```
docker run --name test-mongo -d mongo
```
