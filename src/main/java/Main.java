import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data1.json");

        List<Employee> list1 = parseXML("data.xml");
        String json1 = listToJson(list1);
        writeString(json, "data1.json");


    }

    public static List parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();


        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
            writer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static List parseXML(String file) {

        List<Employee> staff = new ArrayList<>();
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));
            NodeList nodeLst = doc.getElementsByTagName("employee");

            for (int s = 0; s < nodeLst.getLength(); s++) {
                Node node = nodeLst.item(s);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    NodeList nodeListId = element.getElementsByTagName("id");
                    Element elementId = (Element) nodeListId.item(0);
                    NodeList childNodesId = elementId.getChildNodes();
                    long id = Long.parseLong((childNodesId.item(0)).getNodeValue());

                    NodeList nodeListFn = element.getElementsByTagName("firstName");
                    Element elementFn = (Element) nodeListFn.item(0);
                    NodeList childNodesFn = elementFn.getChildNodes();
                    String firstName = (childNodesFn.item(0)).getNodeValue();

                    NodeList nodeListLn = element.getElementsByTagName("lastName");
                    Element elementLn = (Element) nodeListLn.item(0);
                    NodeList childNodesLn = elementLn.getChildNodes();
                    String lastName = (childNodesLn.item(0)).getNodeValue();

                    NodeList nodeListCountry = element.getElementsByTagName("country");
                    Element elementCountry = (Element) nodeListCountry.item(0);
                    NodeList childNodesCountry = elementCountry.getChildNodes();
                    String country = (childNodesCountry.item(0)).getNodeValue();

                    NodeList nodeListAge = element.getElementsByTagName("age");
                    Element elementAge = (Element) nodeListAge.item(0);
                    NodeList childNodesAge = elementAge.getChildNodes();
                    int age = Integer.parseInt((childNodesAge.item(0)).getNodeValue());

                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    staff.add(employee);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }

}
