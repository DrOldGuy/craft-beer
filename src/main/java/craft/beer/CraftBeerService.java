package craft.beer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * This class parses the beer data file scraped from here:
 * https://www.beeradvocate.com/beer/top-rated/.
 * 
 * @Service This tells Spring to manage the lifecycle of this class as a managed
 *          bean. As such, it makes the class eligible for Dependency Injection.
 * 
 * @author Promineo
 *
 */
@Service
public class CraftBeerService {

  private static final String FILE_NAME = "beer-data.txt";

  /**
   * Parse the beer data file. This loads the beer data file from the classpath.
   * Beer data looks like this:
   * 
   * <pre>
   ordinal beer-name
   brewery
   beer-type | ABV% num-ratings average-rating
   * </pre>
   * 
   * After loading the beer data, this method compresses the 3-lines of beer
   * data into a single line like this:
   * 
   * <pre>
   ordinal beer-name | brewery | beer-type | ABV% num-ratings average-rating
   * </pre>
   * 
   * Finally, the beer data lines are parsed and Beer objects are returned.
   * 
   * @return The list of craft beers.
   */
  public List<Beer> parseBeerFile() {
    List<String> rawlines = loadBeerFile();
    List<String> lines = concatenateLines(rawlines);

    return parseLines(lines);
  }

  /**
   * Convert the 3 beer data lines into a single line.
   * 
   * @param rawlines The raw data straight from the data file.
   * @return The concatenated lines.
   */
  private List<String> concatenateLines(List<String> rawlines) {
    List<String> lines = new LinkedList<>();

    while (rawlines.size() >= 3) {
      String line = rawlines.remove(0) + " | " + rawlines.remove(0) + " | "
          + rawlines.remove(0);

      lines.add(line);
    }

    return lines;
  }

  /**
   * Parse the concatenated beer data.
   * 
   * @param lines The beer data.
   * @return A list of Beer objects.
   */
  private List<Beer> parseLines(List<String> lines) {
    List<Beer> beers = new LinkedList<>();

    /*
     * Create a single StringBuilder object that is emptied and filled with each
     * line of data. This is done to avoid creating a StringBuilder for each
     * line within the loop.
     */
    StringBuilder beer = new StringBuilder();

    for(String value : lines) {
      beer.setLength(0);
      beer.append(value);

      beers.add(parseLine(beer));
    }

    return beers;
  }

  /**
   * Parse a line of beer data and return a Beer object. This calls
   * {@link #parseToken(StringBuilder, String)} repeatedly to pull out a token
   * given a separator to search for in the data line.
   * 
   * @param beerBuilder
   * @return
   */
  private Beer parseLine(StringBuilder beerBuilder) {
    int ordinal = Integer.parseInt(parseToken(beerBuilder, " "));
    String name = parseToken(beerBuilder, "|");
    String brewery = parseToken(beerBuilder, "|");
    String type = parseToken(beerBuilder, "|");
    BigDecimal abv = new BigDecimal(parseToken(beerBuilder, "%")).setScale(2);
    int numReviewers = removeComma(parseToken(beerBuilder, " "));
    BigDecimal avg = new BigDecimal(beerBuilder.toString());

    // @formatter:off
    return Beer.builder()
        .abv(abv)
        .averageRating(avg)
        .brewery(brewery)
        .ordinal(ordinal)
        .name(name)
        .numRatings(numReviewers)
        .type(type)
        .build();
    // @formatter:on
  }

  /**
   * This method searches the beer data line from the start of the line until it
   * finds the requested separator. It then removes the token from the
   * StringBuilder and returns the trimmed token.
   * 
   * @param beerBuilder The StringBuilder that contains the tokens that will be
   *        turned into a Beer object.
   * @param separator The separator to search for.
   * @return The token.
   */
  private String parseToken(StringBuilder beerBuilder, String separator) {
    int pos = beerBuilder.indexOf(separator);

    if(pos == -1) {
      throw new IllegalStateException("Marker not found: " + separator);
    }

    String result = beerBuilder.substring(0, pos).trim();
    beerBuilder.delete(0, pos + 1);
    trim(beerBuilder);

    return result;
  }

  /**
   * Trim spaces off the start and end of the StringBuilder text.
   * 
   * @param beerBuilder The StringBuilder to trim.
   */
  private void trim(StringBuilder beerBuilder) {
    while (beerBuilder.charAt(0) == ' ') {
      beerBuilder.deleteCharAt(0);
    }

    while (beerBuilder.charAt(beerBuilder.length() - 1) == ' ') {
      beerBuilder.deleteCharAt(beerBuilder.length() - 1);
    }
  }

  /**
   * Remove all non-digit characters from the given String.
   * 
   * @param num The String to fix.
   * @return The String converted to an int.
   */
  private int removeComma(String num) {
    String value = "";

    for(int i = 0; i < num.length(); i++) {
      char ch = num.charAt(i);

      if(Character.isDigit(ch)) {
        value += Character.toString(ch);
      }
    }

    return Integer.parseInt(value);
  }

  /**
   * Load the beer data.
   * 
   * @return The beer data as a List of String.
   */
  private List<String> loadBeerFile() {
    try {
      Resource resource = new ClassPathResource(FILE_NAME);
      Path path = Paths.get(resource.getURI());

      return Files.readAllLines(path);
    }
    catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
