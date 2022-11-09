package craft.beer;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

/**
 * This class contains beer data. The @Value annotation is a project Lombok
 * annotation. It adds an all-args constructor as well as getters for each
 * instance variable but no setters. This makes the Beer class immutable. Java
 * records are similar but records do not have a builder option. The @Builder
 * annotation adds a builder that follows the Builder Design Pattern (one
 * version). This provides a fluent API that allows you to create a Beer object
 * by calling builder methods without calling the constructor. So:
 * <pre>
 Beer beer = Beer.builder()
     .ordinal(5)
     .name("A Beer")
     .brewery("Some Brewery")
     ...
     .build();
 * </pre>
 * 
 * @author Promineo
 *
 */
@Value
@Builder
public class Beer {
  private int ordinal;
  private String name;
  private String brewery;
  private String type;
  private BigDecimal abv;
  private int numRatings;
  private BigDecimal averageRating;
}
