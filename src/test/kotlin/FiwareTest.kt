import org.junit.jupiter.api.Test
import org.testcontainers.containers.Network

class FiwareTest {

    @Test
    fun testIotaJson() {
        val shared = Network.newNetwork()

        startRabbitMQ(shared)
        startMongoDB(shared)
        startOrion(shared)
        startIotaJson(shared)
    }

    private fun startIotaJson(shared: Network) {
        val ij = IotaJsonContainer()
        ij.addExposedPorts(IOTA_JAON_PORT)
        ij.network = shared
        ij.addEnv("IOTA_CB_HOST", ORION_HOST)
        ij.addEnv("IOTA_CB_PORT", ORION_PORT.toString())
        ij.addEnv("IOTA_NORTH_PORT", IOTA_JAON_PORT.toString())
        ij.addEnv("IOTA_REGISTRY_TYPE", "mongodb")
        ij.addEnv("IOTA_MONGO_HOST", MONGO_HOST)
        ij.addEnv("IOTA_MONGO_PORT", MONGO_PORT.toString())
        ij.addEnv("IOTA_MONGO_DB", "iotagent-json")
        ij.addEnv("IOTA_PROVIDER_URL", "http://${IOTA_JSON_HOST}:${IOTA_JAON_PORT}")
        ij.addEnv("IOTA_AMQP_HOST", RABBITMQ_HOST)
        ij.addEnv("IOTA_AMQP_PORT", RABBITMQ_AMQP_PORT.toString())
        ij.addEnv("IOTA_AMQP_EXCHANGE", "amq.topic")
        ij.addEnv("IOTA_MQTT_HOST", RABBITMQ_HOST)
        ij.addEnv("IOTA_MQTT_PORT", RABBITMQ_MQTT_PORT.toString())
        ij.withCreateContainerCmdModifier { t -> t.withAliases(listOf(IOTA_JSON_HOST)) }
        ij.start()
    }

    private fun startOrion(shared: Network) {
        val oc = OrionContainer()
        oc.addExposedPort(ORION_PORT)
        val command = "-dbhost ${MONGO_HOST}:${MONGO_PORT} -logLevel DEBUG"
        oc.setCommand(command)
        oc.network = shared
        oc.withCreateContainerCmdModifier { t -> t.withAliases(listOf(ORION_HOST)) }
        oc.start()
    }

    private fun startMongoDB(shared: Network) {
        val mg = MongoDBContainer()
        mg.addExposedPort(MONGO_PORT)
        mg.network = shared
        mg.withCreateContainerCmdModifier { t -> t.withAliases(listOf(MONGO_HOST)) }
        mg.start()
    }

    private fun startRabbitMQ(shared: Network) {
        val rm = RabbitMQContainer()
        rm.addExposedPorts(RABBITMQ_AMQP_PORT, 1883)
        rm.network = shared
        rm.withCreateContainerCmdModifier { t -> t.withAliases(listOf(RABBITMQ_HOST)) }
        rm.withFileSystemBind("src/test/resources/rabbitmq.config", "/etc/rabbitmq/rabbitmq.config")
        rm.withFileSystemBind("src/test/resources/enabled_plugins", "/etc/rabbitmq/enabled_plugins")
        rm.start()
    }

    companion object {
        const val MONGO_PORT = 27017
        const val MONGO_HOST = "context-db"
        const val RABBITMQ_HOST = "rabbitmq"
        const val RABBITMQ_AMQP_PORT = 5672
        const val RABBITMQ_MQTT_PORT = 1883
        const val ORION_HOST = "orion"
        const val ORION_PORT = 1026
        const val IOTA_JSON_HOST = "iotaJson"
        const val IOTA_JAON_PORT = 4041
    }
}

