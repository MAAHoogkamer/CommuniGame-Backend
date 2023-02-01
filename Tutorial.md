### Ktor 2.2.2 Help

[Original tutorial link](https://ktor.io/docs/creating-http-apis.html)

Creating HTTP APIs
==================

In this tutorial, we're going to create an HTTP API that can serve as a backend for any application, be it mobile, web, desktop, or even a B2B service. We will see how routes are defined and structured, how serialization plugins help simplify tedious tasks, and how we can test parts of our application both manually and automated.

Throughout the tutorial, we'll build a simple JSON API that allows us to query information about the customers of our fictitious business, as well as the orders we currently want to fulfill. We will build a convenient way of listing all customers and orders in our system, get information for individual customers and orders, and provide the functionality to add new entries and remove old entries.

Prerequisites
-------------

Before starting this tutorial:

*   [Install IntelliJ IDEA Ultimate](https://www.jetbrains.com/help/idea/installation-guide.html).

    If you use IntelliJ IDEA Community or other IDE, you can generate a Ktor project using a [web-based project generator](https://start.ktor.io/).

*   Make sure the [Ktor plugin](https://www.jetbrains.com/help/idea/ktor.html) is installed and enabled.


Create a new Ktor project
-------------------------

To create a base project for our application using the Ktor plugin, [open IntelliJ IDEA](https://www.jetbrains.com/help/idea/run-for-the-first-time.html) and follow the steps below:

1.  On the Welcome screen, click New Project.

    Otherwise, from the main menu, select File | New | Project.

2.  In the New Project wizard, choose Ktor from the list on the left. On the right pane, specify the following settings:

    ![New Ktor project](images/tutorial_http_api_new_project.png "New Ktor project")

    *   Name: Specify a project name.

    *   Location: Specify a directory for your project.

    *   Build System: Make sure that Gradle Kotlin is selected as a [build system](server-dependencies.html).

    *   Website: Leave the default `example.com` value as a domain used to generate a package name.

    *   Artifact: This field shows a generated artifact name.

    *   Ktor version: Choose the latest Ktor version.

    *   Engine: Leave the default Netty [engine](engines.html).

    *   Configuration in: Choose HOCON file to specify server parameters in a [dedicated configuration file](create-server.html).

    *   Add sample code: Disable this option to skip adding sample code for plugins.


    Click Next.

3.  On the next page, add the Routing, Content Negotiation, and kotlinx.serialization plugins:

    ![Ktor plugins](images/tutorial_http_api_new_project_plugins.png "Ktor plugins")

    Click Create and wait until IntelliJ IDEA generates a project and installs the dependencies.


Examine the project
-------------------

To look at the structure of the [generated project](#create_ktor_project), let's invoke the [Project view](https://www.jetbrains.com/help/idea/project-tool-window.html):

![Initial project structure](images/tutorial_http_api_project_structure.png "Initial project structure")

*   The `build.gradle.kts` file contains [dependencies](#dependencies) required for a Ktor server and plugins.

*   The `main/resources` folder includes [configuration files](#configurations).

*   The `main/kotlin` folder contains the generated [source code](#source_code).


### Dependencies

First, let's open the `build.gradle.kts` file and examine added dependencies:

dependencies { implementation("io.ktor:ktor-server-core:$ktor\_version") implementation("io.ktor:ktor-server-netty:$ktor\_version") implementation("io.ktor:ktor-server-content-negotiation:$ktor\_version") implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor\_version") implementation("ch.qos.logback:logback-classic:$logback\_version") testImplementation("io.ktor:ktor-server-test-host:$ktor\_version") testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin\_version") }

Let's briefly go through these dependencies one by one:

*   `ktor-server-core` adds Ktor's core components to our project.

*   `ktor-server-netty` adds the Netty [engine](engines.html) to our project, allowing us to use server functionality without having to rely on an external application container.

*   `ktor-server-content-negotiation` and `ktor-serialization-kotlinx-json` provide a convenient mechanism for converting Kotlin objects into a [serialized form](serialization.html) like JSON, and vice versa. We will use it to format our APIs output, and to consume user input that is structured in JSON. In order to use `ktor-serialization-kotlinx-json`, we also have to apply the `plugin.serialization` plugin.

    plugins { id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0" }

*   `logback-classic` provides an implementation of SLF4J, allowing us to see nicely formatted [logs](logging.html) in a console.

*   `ktor-server-test-host` and `kotlin-test-junit` allow us to [test](testing.html) parts of our Ktor application without having to use the whole HTTP stack in the process. We will use this to define unit tests for our project.


### Configurations: application.conf and logback.xml

The generated project also includes the `application.conf` and `logback.xml` configuration files located in the `resources` folder:

*   `application.conf` is a configuration file in [HOCON](https://en.wikipedia.org/wiki/HOCON) format. Ktor uses this file to determine the port on which it should run, and it also defines the entry point of our application.

    ktor { deployment { port = 8080 port = ${?PORT} } application { modules = \[ com.example.ApplicationKt.module \] } }

    If you'd like to learn more about how a Ktor server is configured, check out the [Configuration in a file](configuration-file.html) help topic.

*   `logback.xml` sets up the basic logging structure for our server. If you'd like to learn more about logging in Ktor, check out the [Logging](logging.html) topic.


### Source code

The [application.conf](#configurations) file configures the entry point of our application to be `com.example.ApplicationKt.module`. This corresponds to the `Application.module()` function in `Application.kt`, which is an application [module](modules.html):

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args) fun Application.module() { configureRouting() configureSerialization() }

This module, in turn, calls the following extension functions:

*   `configureRouting` is a function defined in `plugins/Routing.kt`, which currently doesn't do anything:

    fun Application.configureRouting() { routing { } }

    We'll define the routes for customers and orders in the next chapters.

*   `configureSerialization` is a function defined in `plugins/Serialization.kt`, which installs `ContentNegotiation` and enables the `json` serializer:

    fun Application.configureSerialization() { install(ContentNegotiation) { json() } }


Customer routes
---------------

First, let's tackle the `Customer` side of our application. We need to create a model which defines the data that's associated with a customer. We also need to create a series of endpoints to allow Customers to be added, listed, and deleted.

### Create the Customer model

For our case, a customer should store some basic information in the form of text: a customer should have an `id` by which we can identify them, a first and last name, and an email address. An easy way to model this in Kotlin is by using a data class:

1.  Create a [new package](https://www.jetbrains.com/help/idea/add-items-to-project.html#new-package) named `models` inside `com.example`.

2.  Create a `Customer.kt` file in the `models` package and add the following code:

    package com.example.models import kotlinx.serialization.Serializable @Serializable data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)

    Note that we are using the `@Serializable` annotation from [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization). Together with its Ktor integration, this will allow us to generate the JSON representation we need for our API responses automatically – as we will see in just a bit.


### Create the Customer storage

To not complicate the code, for this tutorial, we'll be using an in-memory storage (i.e. a mutable list of `Customer`s) – in a real application, we would be storing this information in a database so that it doesn't get lost after restarting our application. We can add this line right after the data class declaration in `Customer.kt` file:

val customerStorage = mutableListOf<Customer>()

Now that we have a well-defined `Customer` class and a storage for our customer objects, it's time we create endpoints and expose them via our API!

### Define the routing for customers

We want to respond to `GET`, `POST`, and `DELETE` requests on the `/customer` endpoint. As such, let's define our routes with the corresponding HTTP methods.

1.  Create a new package named `routes` inside `com.example`.

2.  Create a `CustomerRoutes.kt` file called in the `routes` package, and fill it with the following:

    package com.example.routes import io.ktor.server.routing.\* fun Route.customerRouting() { route("/customer") { get { } get("{id?}") { } post { } delete("{id?}") { } } }

    In this case, we're using the `route` function to group everything that falls under the `/customer` endpoint. We then create a block for each HTTP method. This is just one approach how we can structure our routes – when we tackle the `Order` routes in the next chapter, we will see another approach.

    Notice also how we actually have two entries for `get`: one without a path parameter, and the other with `{id?}`. We'll use the first entry to list all customers, and the second entry to display a specific one.


#### List all customers

To list all customers, we can return the `customerStorage` list by using the `call.respond` function in Ktor, which can take a Kotlin object and return it serialized in a specified format. For the `get` handler, it looks like this:

package com.example.routes import com.example.models.\* import io.ktor.http.\* import io.ktor.server.application.\* import io.ktor.server.request.\* import io.ktor.server.response.\* import io.ktor.server.routing.\* fun Route.customerRouting() { route("/customer") { get { if (customerStorage.isNotEmpty()) { call.respond(customerStorage) } else { call.respondText("No customers found", status = HttpStatusCode.OK) } } } }

In order for this to work, we need the `ContentNegotiation` plugin, which is already installed with the `json` serializer in `plugins/Serialization.kt`. What does content negotiation do? Let us consider the following request:

GET http://127.0.0.1:8080/customer Accept: application/json

When a client makes such a request, content negotiation allows the server to examine the `Accept` header, see if it can serve this specific type of content, and if so, return the result.

JSON support is powered by [kotlinx.serialization](serialization.html). We previously used its annotation `@Serializable` to annotate our `Customer` data class, meaning that Ktor now knows how to serialize `Customer`s (and collections of `Customer`s!)

#### Return a specific customer

Another route we want to support is one that returns a specific customer based on their ID (in this case, `200`):

GET http://127.0.0.1:8080/customer/200 Accept: application/json

In Ktor, paths can also contain [parameters](routing-in-ktor.html#match_url) that match specific path segments. We can access their value using the indexed access operator (`call.parameters["myParamName"]`). Let's add the following code to the `get("{id?}")` entry:

get("{id?}") { val id = call.parameters\["id"\] ?: return@get call.respondText( "Missing id", status = HttpStatusCode.BadRequest ) val customer = customerStorage.find { it.id == id } ?: return@get call.respondText( "No customer with id $id", status = HttpStatusCode.NotFound ) call.respond(customer) }

First, we check whether the parameter `id` exists in the request. If it does not exist, we respond with a `400 Bad Request` status code and an error message, and are done. If the parameter exists, we try to `find` the corresponding record in our `customerStorage`. If we find it, we'll respond with the object. Otherwise, we'll return a 404 "Not Found" status code with an error message.

#### Create a customer

Next, we implement the option for a client to `POST` a JSON representation of a client object, which then gets put into our customer storage. Its implementation looks like this:

post { val customer = call.receive<Customer>() customerStorage.add(customer) call.respondText("Customer stored correctly", status = HttpStatusCode.Created) }

`call.receive` integrates with the [configured Content Negotiation](#source_code) plugin. Calling it with the generic parameter `Customer` automatically deserializes the JSON request body into a Kotlin `Customer` object. We can then add the customer to our storage and respond with a status code of `201 Created`.

At this point, it is worth highlighting again that in this tutorial, we are also intentionally glancing over issues that could arise from, for example, multiple requests accessing the storage simultaneously. In production, data structures and code that can be accessed from multiple requests/threads at the same time should account for these cases – something that is out of the scope of this tutorial.

#### Delete a customer

The implementation for deleting a customer follows a similar procedure as we have used for listing a specific customer. We first get the `id` and then modify our `customerStorage` accordingly:

delete("{id?}") { val id = call.parameters\["id"\] ?: return@delete call.respond(HttpStatusCode.BadRequest) if (customerStorage.removeIf { it.id == id }) { call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted) } else { call.respondText("Not Found", status = HttpStatusCode.NotFound) } }

Similar to the definition of our `get` request, we make sure that the `id` is not null. If the `id` is absent, we respond with a `400 Bad Request` error.

### Register the routes

Up until now, we have only defined our routes inside an extension function on `Route` – so Ktor doesn't know about our routes yet, and we need to register them. Open the `plugins/Routing.kt` file and add the following code:

package com.example.plugins import com.example.routes.\* import io.ktor.server.application.\* import io.ktor.server.routing.\* fun Application.configureRouting() { routing { customerRouting() } }

As you might remember, the `configureRouting` function is already [invoked](#source_code) in our `Application.module()` function in `Application.kt`.

We've now completed the implementation for the customer-related routes in our API. If you would like to validate that everything works right away, you can skip ahead to the chapter [Test HTTP endpoints manually](#manual_test). If you can still bear the suspense, we can move on to the implementation of order-related routes.

Order routes
------------

Now that we have API endpoints for `Customer`s done, let's move on to `Orders`. While some of the implementation is rather similar, we will be using a different way of structuring our application routes, and include routes that sum up the price of individual items in an order.

### Create the Order model

The orders we want to store in our system should be identifiable by an order number (which might contain dashes), and should contain a list of order items. These order items should have a textual description, the number of how often this item appears in the order, as well as the price for the individual item (so that we can compute the total price of an order on demand).

Inside the `models` package, create a new file called `Order.kt` and fill it with the definition of the two data classes:

package com.example.models import kotlinx.serialization.Serializable @Serializable data class Order(val number: String, val contents: List<OrderItem>) @Serializable data class OrderItem(val item: String, val amount: Int, val price: Double)

We also once again need a place to store our orders. To skip having to define a `POST` route – something you're more than welcome to attempt on your own using the knowledge from the `Customer` routes – we will prepopulate our `orderStorage` with some sample orders. We can again define it as a top-level declaration inside the `Order.kt` file.

val orderStorage = listOf(Order( "2020-04-06-01", listOf( OrderItem("Ham Sandwich", 2, 5.50), OrderItem("Water", 1, 1.50), OrderItem("Beer", 3, 2.30), OrderItem("Cheesecake", 1, 3.75) )), Order("2020-04-03-01", listOf( OrderItem("Cheeseburger", 1, 8.50), OrderItem("Water", 2, 1.50), OrderItem("Coke", 2, 1.76), OrderItem("Ice Cream", 1, 2.35) )) )

### Define the routing for orders

We respond to a set of `GET` requests with three different patterns:

GET http://127.0.0.1:8080/order Content-Type: application/json ### GET http://127.0.0.1:8080/order/2020-04-06-01 Content-Type: application/json ### GET http://127.0.0.1:8080/order/2020-04-06-01/total Content-Type: application/json

The first will return all orders, the second will return an order given the `id`, and the third will return the total of an order (prices of individual `OrderItems` multiplied by number of each item).

With orders, we're going to follow a different pattern when it comes to defining routes. Instead of grouping all routes under a single `route` function with different HTTP methods, we'll use individual functions.

#### List all and individual orders

For listing all orders, we'll follow the same pattern as with customers – the difference being that we're defining it in its own function. Let's create a file called `OrderRoutes.kt` inside the `routes` package, and start with the implementation of the route inside a function called `listOrdersRoute()`.

package com.example.routes import com.example.models.\* import io.ktor.server.application.\* import io.ktor.http.\* import io.ktor.server.response.\* import io.ktor.server.routing.\* fun Route.listOrdersRoute() { get("/order") { if (orderStorage.isNotEmpty()) { call.respond(orderStorage) } } }

We apply the same structure to individual orders – with a similar implementation to customers, but encapsulated in its own function:

fun Route.getOrderRoute() { get("/order/{id?}") { val id = call.parameters\["id"\] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest) val order = orderStorage.find { it.number == id } ?: return@get call.respondText( "Not Found", status = HttpStatusCode.NotFound ) call.respond(order) } }

#### Totalize an order

Getting the total amount of an order consists of iterating over the items of an order and totalizing this. Implemented as a `totalizeOrderRoute` function, it looks like this, which besides the summing process should already look familiar:

fun Route.totalizeOrderRoute() { get("/order/{id?}/total") { val id = call.parameters\["id"\] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest) val order = orderStorage.find { it.number == id } ?: return@get call.respondText( "Not Found", status = HttpStatusCode.NotFound ) val total = order.contents.sumOf { it.price \* it.amount } call.respond(total) } }

A small thing to note here is that we are not limited to suffixes of routes for parameters – as we can see, it's absolutely possible to have a section in the middle be a path parameter (`/order/{id}/total`).

### Register the routes

Finally, much like the case of customers, we need to register the routes. Hopefully, this makes it clear why grouping routes makes more sense as the number of routes grows. Still in `plugins/Routing.kt`, add the routes for orders as follows:

package com.example.plugins import com.example.routes.\* import io.ktor.server.application.\* import io.ktor.server.routing.\* fun Application.configureRouting() { routing { customerRouting() listOrdersRoute() getOrderRoute() totalizeOrderRoute() } }

Now that we have everything wired up, we can finally start testing our application, and see if everything works as we would expect it to!

Test HTTP endpoints manually
----------------------------

Now that we have all the endpoints ready, it's time to test our application. While we can use any browser to test `GET` requests, we'll need a separate tool to test the other HTTP methods:

*   If you use IntelliJ IDEA Ultimate, you already have a [client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html) that supports `.http` files, allowing you to specify and execute requests.

*   If you use IntelliJ IDEA Community or another IDE, you can test your API using `curl`.


### Create a customer HTTP test file

Let's create a new `CustomerTest.http` file in the project root:

POST http://127.0.0.1:8080/customer Content-Type: application/json { "id": "100", "firstName": "Jane", "lastName": "Smith", "email": "jane.smith@company.com" } ### POST http://127.0.0.1:8080/customer Content-Type: application/json { "id": "200", "firstName": "John", "lastName": "Smith", "email": "john.smith@company.com" } ### POST http://127.0.0.1:8080/customer Content-Type: application/json { "id": "300", "firstName": "Mary", "lastName": "Smith", "email": "mary.smith@company.com" } ### GET http://127.0.0.1:8080/customer Accept: application/json ### GET http://127.0.0.1:8080/customer/200 Accept: application/json ### GET http://127.0.0.1:8080/customer/500 Accept: application/json ### DELETE http://127.0.0.1:8080/customer/100 ### DELETE http://127.0.0.1:8080/customer/500

curl -X POST --location "http://127.0.0.1:8080/customer" \\ -H "Content-Type: application/json" \\ -d "{ \\"id\\": \\"100\\", \\"firstName\\": \\"Jane\\", \\"lastName\\": \\"Smith\\", \\"email\\": \\"jane.smith@company.com\\" }" curl -X POST --location "http://127.0.0.1:8080/customer" \\ -H "Content-Type: application/json" \\ -d "{ \\"id\\": \\"200\\", \\"firstName\\": \\"John\\", \\"lastName\\": \\"Smith\\", \\"email\\": \\"john.smith@company.com\\" }" curl -X POST --location "http://127.0.0.1:8080/customer" \\ -H "Content-Type: application/json" \\ -d "{ \\"id\\": \\"300\\", \\"firstName\\": \\"Mary\\", \\"lastName\\": \\"Smith\\", \\"email\\": \\"mary.smith@company.com\\" }" curl -X GET --location "http://127.0.0.1:8080/customer" \\ -H "Accept: application/json" curl -X GET --location "http://127.0.0.1:8080/customer/200" \\ -H "Accept: application/json" curl -X GET --location "http://127.0.0.1:8080/customer/500" \\ -H "Accept: application/json" curl -X DELETE --location "http://127.0.0.1:8080/customer/100" curl -X DELETE --location "http://127.0.0.1:8080/customer/500"

Inside this file, we have now specified a bunch of HTTP requests using all the supported HTTP methods of our API. IntelliJ IDEA now allows us to run each of these requests individually or all together. To really see what's going on, let's run them individually. But first, we need to make sure our API is actually reachable!

### Run API server

Before we can run a request, we need to first start our API server. Open the `Application.kt` file and click on the Run icon in the gutter:

![Run Server](images/run-app.png "Run Server")

Once the server is up and running, we can execute each request by using the Run icon in the gutter:

![Run POST Request](images/run-post-request.png "Run POST Request")

If everything is correct, we should see the output in the Run tool window:

![Run Output](images/run-output.png "Run Output")

### Order endpoints

For the order endpoints, we can follow the same procedure: we create a new file called `OrderTest.http` in the root of our project and fill it with some HTTP requests:

GET http://127.0.0.1:8080/order Content-Type: application/json ### GET http://127.0.0.1:8080/order/2020-04-06-01 Content-Type: application/json ### GET http://127.0.0.1:8080/order/2020-04-06-01/total Content-Type: application/json

curl -X GET --location "http://127.0.0.1:8080/order" \\ -H "Content-Type: application/json" curl -X GET --location "http://127.0.0.1:8080/order/2020-04-06-01" \\ -H "Content-Type: application/json" curl -X GET --location "http://127.0.0.1:8080/order/2020-04-06-01/total" \\ -H "Content-Type: application/json"

Running these requests just as the ones before, we should see the expected output – list of orders, information about one order, and the total of the order.

Automated testing
-----------------

While manual testing is great and necessary, it also makes sense to have [automated testing](testing.html) of endpoints. Ktor allows us to test endpoints without having to start up the entire underlying engine (such as Netty) using the `testApplication` function. Inside this function, you need to use the existing Ktor client instance to make requests to your server and verify the results.

Let's write a unit test to ensure that our order route returns properly formatted JSON content. Open the `src/test/kotlin/com/example/ApplicationTest.kt` file and replace the existing test class with `OrderRouteTests`:

package com.example import io.ktor.client.request.\* import io.ktor.client.statement.\* import io.ktor.http.\* import io.ktor.server.testing.\* import kotlin.test.\* class OrderRouteTests { @Test fun testGetOrder() = testApplication { val response = client.get("/order/2020-04-06-01") assertEquals( """{"number":"2020-04-06-01","contents":\[{"item":"Ham Sandwich","amount":2,"price":5.5},{"item":"Water","amount":1,"price":1.5},{"item":"Beer","amount":3,"price":2.3},{"item":"Cheesecake","amount":1,"price":3.75}\]}""", response.bodyAsText() ) assertEquals(HttpStatusCode.OK, response.status) } }

If you set the Configuration in option to Code when [creating a project](#create_ktor_project), you need to add modules to a test application [manually](testing.html#manual):

application { configureRouting() configureSerialization() }

Note that since our string contains a lot of quotation marks around keys and values (like `"number"`), this is a great place to use [raw strings](https://kotlinlang.org/docs/basic-types.html#string-literals) using triple-quotes (`"""`), saving us the hassle of individually escaping every special character inside the string.

We can now [run our unit test from the IDE](https://www.jetbrains.com/help/idea/performing-tests.html) and see the results. Much like we've done for this endpoint, we can add all other endpoints as tests and automate the testing of our HTTP API.

And just like that, we have finished building our small JSON-based HTTP API. Of course, there are tons of topics you can still explore around Ktor and building APIs with it, so your learning journey doesn't have to stop here!

You can find the resulting project for this tutorial here: [tutorial-http-api](https://github.com/ktorio/ktor-documentation/tree/2.2.2/codeSnippets/snippets/tutorial-http-api).

What's next
-----------

With this step, we've finalized our HTTP API application. From here on, we can add other plugins such as Authentication, etc.

### Feature requests

*   Authentication: currently, the API is open to whomever would like to access it. If you want to restrict access, have a look at Ktor's support for [JWT](jwt.html) and other authentication methods.

*   Learn more about route organization! If you'd like to learn about different ways to organize your routes with Ktor, check out the [Routing](routing-in-ktor.html) topic.

*   Persistence! Currently, all customers and orders vanish when we stop our application, as we are only storing them in an in-memory storage. You could try integrating your application with a database like PostgreSQL or MongoDB, using one of the plenty projects that allow database access from Kotlin, like [Exposed](interactive-website-add-persistence.html) or [KMongo](https://litote.org/kmongo/).

*   Integrate with a client! Now that we are exposing data, it would make sense to explore how this data can be consumed again! Try writing an API client using the [Ktor HTTP client](create-client.html), for example, or try accessing it from a website using JavaScript or Kotlin/JS!

    To make sure your API works nicely with browser clients, you should also set up a policy for [Cross-Origin Resource Sharing (CORS)](cors.html). The simplest and most permissive way to do this with Ktor would be by adding the following snippet to the top of `Application.module()`:

    install(CORS) { anyHost() }


Last modified: 14 December 2022

[Creating a new Ktor project](intellij-idea.html) [Creating a static website](creating-static-website.html)