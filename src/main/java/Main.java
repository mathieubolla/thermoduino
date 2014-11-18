import com.google.common.util.concurrent.AtomicDouble;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class Main {
    public static void main(String... args) throws IOException {
        BufferedReader controller = new BufferedReader(new FileReader("/dev/cu.usbserial-A601EQIH"));

        AtomicDouble model = new AtomicDouble(0.0);

        Container view = (request, response) -> {
            try {
                response.getPrintStream().println(model.get());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        ContainerSocketProcessor processor = new ContainerSocketProcessor(view);
        new SocketConnection(processor).connect(new InetSocketAddress(8080));

        controller
                .lines()
                .filter(Pattern.compile("[0-9]{1,3}").asPredicate())
                .map(Double::parseDouble).map(value -> ((value * 1.1 / 1023) - 0.5) * 100)
                .forEach(model::set);

    }
}
