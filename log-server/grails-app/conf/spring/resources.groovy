import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.jms.connection.CachingConnectionFactory
import org.apache.activemq.command.ActiveMQTopic
import com.danveloper.realtimelogging.server.services.LoggingServiceActivator

// Place your Spring DSL code here
beans = {
    xmlns amq:"http://activemq.apache.org/schema/core"
    xmlns integration:"http://www.springframework.org/schema/integration"
    xmlns jms:"http://www.springframework.org/schema/integration/jms"

    /* Establish the broker */
    amq.broker(useJmx: false, persistent: false) {
        amq.transportConnectors() {
            amq.transportConnector(uri: "tcp://localhost:61616")
        }
    }

    /* Create the caching connection factory */
    amqConnectionFactory(ActiveMQConnectionFactory) {
        brokerURL = "vm://localhost"
    }
    connectionFactory(CachingConnectionFactory) {
        targetConnectionFactory = ref('amqConnectionFactory')
        sessionCacheSize = 10
        cacheProducers = false
    }

    /* Construct the JMS topic destination */
    logMessageDestination(ActiveMQTopic, "log.*")

    /* Attach the message adapter */
    logServiceActivator(LoggingServiceActivator)

    /* Create the log channel, for internal transport */
    integration.channel(id: "logChannel")

    /* Attach a chain to the log channel, this will delegate the message to the LoggingServiceActivator */
    integration.chain("input-channel": "logChannel") {
        integration."service-activator"(method: "log", ref:"logServiceActivator")
    }

    /* Attach the JMS channel adapter from our ActiveMQTopic "log.*" to our Spring Integration channel, "logChannel" */
    jms."message-driven-channel-adapter"(id: 'logsIn', destination: 'logMessageDestination', channel: 'logChannel')

}
