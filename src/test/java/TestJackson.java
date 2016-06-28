import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

/**
 * Created by dsutedja on 6/28/16.
 */
public class TestJackson {
    public static class MyModel {
        private String name;
        private String task;
        public String getName() {
            return "Foo";
        }
        public String getTask() {
            return "Do Something";
        }
    }

    @Test
    public void testJackson() {
        ObjectMapper mapper = new ObjectMapper();
        MyModel model = new MyModel();
        try {
            String jsonString = mapper.writeValueAsString(model);
            System.out.println(jsonString);
        } catch (Exception er) {
            er.printStackTrace();
        }

    }
}
