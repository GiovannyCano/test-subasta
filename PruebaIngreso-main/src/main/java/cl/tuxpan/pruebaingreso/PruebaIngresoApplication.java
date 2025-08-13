package cl.tuxpan.pruebaingreso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PruebaIngresoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaIngresoApplication.class, args);
    }

}
