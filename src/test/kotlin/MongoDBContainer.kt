import org.testcontainers.containers.GenericContainer

class MongoDBContainer : GenericContainer<MongoDBContainer>("mongo:3.6")
