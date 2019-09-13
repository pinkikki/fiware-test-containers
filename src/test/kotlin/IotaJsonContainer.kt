import org.testcontainers.containers.GenericContainer

class IotaJsonContainer : GenericContainer<IotaJsonContainer>("fiware/iotagent-json:1.9.0")
