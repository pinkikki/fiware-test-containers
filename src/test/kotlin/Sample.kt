import org.junit.jupiter.api.Test
import org.testcontainers.containers.Network

class Sample {

    @Test
    fun test() {
        val shared = Network.newNetwork()
        val mg = MongoDBContainer()
        mg.addExposedPort(MONGO_PORT)
        mg.network = shared
        mg.start()

        val oc = OrionContainer()
        oc.addExposedPort(1026)
        val command = "-dbhost ${mg.networkAliases[0]}:${MONGO_PORT} -logLevel DEBUG"
        oc.setCommand(command)
        oc.network = shared
        oc.start()
    }

    companion object {
        const val MONGO_PORT = 27017
    }
}

