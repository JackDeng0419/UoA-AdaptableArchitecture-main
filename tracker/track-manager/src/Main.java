
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static Logger log = LogManager.getLogger(Main.class);
    private static final String KAFKA_HOST_OPTION = "KafkaHost";
    private static final String KAFKA_PORT_OPTION = "KafkaPort";
    private static final String KAFKA_PORT_SESSION_PREFIX_OPTION = "KafkaSessionPrefix";

    private static void start(String[] args) throws ParseException {

        Option databaseOpt = Option.builder("D")
                .longOpt("DatabaseName")
                .desc("The database to write to.")
                .hasArg()
                .type(String.class)
                .build();
        Option databaseHostOpt = Option.builder("H")
                .longOpt("DatabaseHost")
                .desc("The database host.")
                .hasArg()
                .type(String.class)
                .build();
        Option databasePortOpt = Option.builder("P")
                .longOpt("DatabasePort")
                .desc("The database port.")
                .hasArg()
                .type(String.class)
                .build();
        Option kafkaHostOpt = Option.builder("K")
                .longOpt(KAFKA_HOST_OPTION)
                .desc("The Kafka hostname.")
                .hasArg()
                .type(String.class)
                .build();
        Option kafkaPortOpt = Option.builder("S")
                .longOpt(KAFKA_PORT_OPTION)
                .desc("The Kafka port.")
                .hasArg()
                .type(int.class)
                .build();
        Option kafkaSessionPrefixOpt = Option.builder("R")
                .longOpt(KAFKA_PORT_SESSION_PREFIX_OPTION)
                .desc("The prefix for the Kafka session.")
                .hasArg()
                .type(String.class)
                .build();

        Options opts = new Options();

        opts.addOption(databaseHostOpt);
        opts.addOption(databasePortOpt);
        opts.addOption(databaseOpt);
        opts.addOption(kafkaSessionPrefixOpt);
        opts.addOption(kafkaHostOpt);
        opts.addOption(kafkaPortOpt);

        CommandLine cmd = new DefaultParser().parse(opts, args);

        final String kafkaHost = cmd.getOptionValue(
                KAFKA_HOST_OPTION,
                System.getenv().getOrDefault(("KAFKA_ADVERTISED_HOST"), "localhost"));
        final String kafkaPort = cmd.getOptionValue(
                KAFKA_PORT_OPTION,
                System.getenv().getOrDefault(("KAFKA_ADVERTISED_PORT"), "9092"));
        final String kafkaSessionPrefix = cmd.getOptionValue(
                KAFKA_PORT_SESSION_PREFIX_OPTION,
                System.getenv().getOrDefault(("KAFKA_SESSION_PREFIX"), ""));

        final String databaseHost = cmd.getOptionValue("DatabaseHost","localhost");
        final String databasePort = cmd.getOptionValue("DatabaseHost","4000");
        final String databaseName = cmd.getOptionValue("DatabaseHost","trackdb");

        DatabaseActions myActions =
                new DatabaseActions(databaseHost,databasePort, databaseName,
                        kafkaSessionPrefix, kafkaHost, kafkaPort);

        while(true) {
            myActions.run();
        }

    }

    public static void main(String[] args) {
        try {
            start (args);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("context", e);
            }
        }
    }
}