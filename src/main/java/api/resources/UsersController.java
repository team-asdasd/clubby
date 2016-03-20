package api.resources;

import api.utilities.StringListConverter;
import api.utilities.Student;
import api.utilities.StudentWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class UsersController {

    List<Student> users = new ArrayList<Student>();
    StringListConverter converter = new StringListConverter();

    public UsersController() {
        // Populate test data. Later each WS method will retrieve data from DB.
        users.add(new Student(12344, "Mike", 17));
        users.add(new Student(12345, "Jane", 19));
        users.add(new Student(12346, "Bob", 19));
    }

    @GET
    @Produces("application/json")
    public Response getStudentList() {
        StudentWrapper wrapper = new StudentWrapper();

        wrapper.setList(users);

        return Response.status(200).entity(wrapper).build();
    }

    @GET
    @Path("overAge/{age}")
    @Produces("application/json")
    public Response getStudentOverAgeList(@PathParam("age") String anAge) {
        StudentWrapper wrapper = new StudentWrapper();

        List<Student> newList = new ArrayList<Student>();
        for (Student student : users) {
            if (student.getAge() > Integer.parseInt(anAge)) {
                newList.add(student);
            }
        }
        wrapper.setList(newList);
        return Response.status(200).entity(wrapper).build();
    }
}