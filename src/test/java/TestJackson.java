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

    private static class TaskFormData {
        private String title;
        private String description;
        private int state;

        public TaskFormData() {

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
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

    @Test
    public void testJsonToObject() {
        String json = "{ \"title\" : \"Todo 2\", \"description\" : \"Description for todo #2\", \"state\" : \"0\" }";
        ObjectMapper mapper = new ObjectMapper();
        try {
            TaskFormData data = mapper.readValue(json, TaskFormData.class);
        } catch (Exception er) {
            er.printStackTrace();
        }


    }
}
