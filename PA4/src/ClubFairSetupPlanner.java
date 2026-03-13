import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class ClubFairSetupPlanner implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for(Project project : projectList) {
            int[] schedule = project.getEarliestSchedule();
            project.printSchedule(schedule);
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        try{
            File xmlFile = new File(filename);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            Document projects =builder.parse(xmlFile);
            projects.getDocumentElement().normalize();
            NodeList projectNodes = projects.getElementsByTagName("Project");
            for (int i=0; i < projectNodes.getLength(); i++) {
                Element projectElement = (Element) projectNodes.item(i);
                String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                List<Task> projectTasks = new ArrayList<>();
                NodeList taskNodes = projectElement.getElementsByTagName("Task");
                for (int j = 0; j < taskNodes.getLength(); j++) {
                    Element taskElement = (Element) taskNodes.item(j);
                    int taskID= Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                    String taskDescription = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                    int taskDuration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());
                    List<Integer> taskDependencies = new ArrayList<>();
                    NodeList dependencyNodes = taskElement.getElementsByTagName("DependsOnTaskID");
                    for (int k = 0; k < dependencyNodes.getLength(); k++) {
                        taskDependencies.add(Integer.parseInt(dependencyNodes.item(k).getTextContent()));
                    }
                    projectTasks.add(new Task(taskID, taskDescription, taskDuration, taskDependencies));
                }
                projectList.add(new Project(projectName, projectTasks));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }
}
