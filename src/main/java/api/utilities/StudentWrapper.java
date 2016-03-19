package api.utilities;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="students")
public class StudentWrapper {

	private List<Student> list;

	public StudentWrapper() {
	}

	public List<Student> getList() {
		return list;
	}

	public void setList(List<Student> list) {
		this.list = list;
	}
	
	
	
	
}
