/**
 * Created by dteo on 6/03/2014.
 */
@FunctionalInterface
public interface CommandHandler {
  public void handle(String command);
}
