
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Configuration {

    private Options options = new Options();
    private CommandLine commandLine = null;

    public String getKafkaHost() {
        return kafkaHost;
    }
    private String kafkaHost =
            System.getenv().getOrDefault("KAFKA_ADVERTISED_HOST", "localhost");

    public String getKafkaPort() {
        return kafkaPort;
    }
    private String kafkaPort =
            System.getenv().getOrDefault("KAFKA_ADVERTISED_PORT", "9092");

    public String getKafkaSessionPrefix() {
        return kafkaSessionPrefix;
    }
    private String kafkaSessionPrefix =
            System.getenv().getOrDefault("KAFKA_SESSION_PREFIX", "");

    private Integer dropLimit = 300; // 300s or 5mins.

    Configuration() {
        Option dropLimitOpt = Option.builder("D")
                .longOpt("DROP_LIMIT")
                .desc("The time after which detections are dropped (s).")
                .hasArg()
                .type(String.class)
                .build();
        this.options.addOption(dropLimitOpt);
    }

    void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            this.commandLine = parser.parse(this.options, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.dropLimit = Integer.parseInt(this.commandLine.getOptionValue("DROP_LIMIT"));
    }


}

