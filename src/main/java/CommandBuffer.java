import org.vertx.java.core.buffer.Buffer;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by dteo on 6/03/2014.
 */
public class CommandBuffer {
  private Buffer currentBuffer = new Buffer();
  private Queue<String> commands = new LinkedList<>();

  public void append(Buffer buffer) {
    for (int i = 0; i < buffer.length(); i++) {
      byte b = buffer.getByte(i);

      if (b == '\n') {
        this.commands.add(this.currentBuffer.toString());
        this.currentBuffer = new Buffer();
      } else {
        this.currentBuffer.appendByte(b);
      }
    }
  }

  public boolean hasNext() {
    return !commands.isEmpty();
  }

  public String next() {
    return commands.poll();
  }
}
