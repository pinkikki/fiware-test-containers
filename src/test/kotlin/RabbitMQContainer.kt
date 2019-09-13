import org.testcontainers.containers.GenericContainer

class RabbitMQContainer : GenericContainer<RabbitMQContainer>("rabbitmq:3.7.14")
