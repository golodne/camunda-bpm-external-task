import org.camunda.bpm.client.ExternalTaskClient;

import java.awt.*;
import java.util.Collections;
import java.util.logging.Logger;
import java.net.URI;

public class ChargeCardWorker {
    private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());
    public static void main(String... args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                //.baseUrl("http://localhost:8080/engine-rest/")
                .baseUrl("http://localhost:8080/rest/")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();
        client.subscribe("testOk")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    long amount = (Long) externalTask.getVariable("amount");
                    long newamount = amount + 1;
                    LOGGER.info("accept! " + amount + " new amount " + newamount);
                    try {
                        Desktop.getDesktop().browse(new URI("https://docs.camunda.org/get-started/quick-start/complete"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  externalTaskService.complete(externalTask);
                    externalTaskService.complete(externalTask,Collections.singletonMap("amount", newamount));
                })
                .open();
    }
}