package truck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Точка входа.
 *
 * @author kanenkovaa
 * @version 0.1
 */
@EnableScheduling
@SpringBootApplication
public class Truckapp {

    /**
     * Запуск приложения
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        SpringApplication.run(Truckapp.class, args);
    }

}
