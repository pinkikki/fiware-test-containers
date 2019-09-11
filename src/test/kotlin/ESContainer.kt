import org.testcontainers.containers.GenericContainer

class ESContainer : GenericContainer<ESContainer>("elasticsearch:7.2.0")
