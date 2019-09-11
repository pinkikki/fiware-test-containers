import org.testcontainers.containers.GenericContainer

class OrionContainer : GenericContainer<OrionContainer>("fiware/orion:2.2.0")
