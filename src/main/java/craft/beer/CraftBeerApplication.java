package craft.beer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class starts Spring Boot in standalone mode (i.e., it's not a Web
 * application).
 * 
 * @SpringBootApplication Enables auto-configuration mode and starts the
 *                        component scan for this package and all subpackages.
 * 
 * @author Promineo
 *
 */
@SpringBootApplication
public class CraftBeerApplication implements CommandLineRunner {

  /**
   * The @Autowired annotation instructs Spring to inject a singleton object
   * into the instance variable.
   */
  @Autowired
  private CraftBeerService craftBeerService;

  /**
   * Entry point to the Java application. This method starts Spring Boot.
   * 
   * @param args Unused
   */
  public static void main(String[] args) {
    SpringApplication.run(CraftBeerApplication.class, args);
  }

  /**
   * Spring Boot calls this method after it has completed its startup and after
   * Dependency Injection is complete. This method asks the Spring-supplied
   * craft beer service to parse the beer data and return a list of Beer
   * objects.
   */
  @Override
  public void run(String... args) throws Exception {
    List<Beer> craftBeers = craftBeerService.parseBeerFile();

    /*
     * At this point you could persist the beers to a beer table or do something
     * else. I've just decided to print the beer objects.
     */
    craftBeers.forEach(beer -> System.out.println(beer));
  }

}
