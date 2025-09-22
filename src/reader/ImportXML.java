package reader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportXML extends Component  {
    public static void main(String[] args) throws SQLException, IOException, SAXException {
       try {
           String url = "jdbc:mysql://localhost:3306/teste?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
           String user = "root";
           String password = "Aluno123qwer#";

           File xmlFile = new File("equipment.xml");

           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder builder = factory.newDocumentBuilder();
           Document doc = builder.parse(xmlFile);

           doc.getDocumentElement().normalize();

           NodeList list = doc.getElementsByTagName("equipamento");
           try(Connection conn = DriverManager.getConnection(url, user, password)){
               PreparedStatement smts = conn.prepareStatement("INSERT INTO equipment (sector, size, quantity, isDefective) VALUES (?, ?, ?, ?)");
               for(int i = 0; i < list.getLength(); i++){
                   Node node = list.item(i);
                   if(node.getNodeType() == Node.ELEMENT_NODE){
                       Element equipment = (Element) node;

                       String name = equipment.getElementsByTagName("setor").item(0).getTextContent();
                       int quantity = Integer.parseInt(equipment.getElementsByTagName("quantidade").item(0).getTextContent());
                       int size = Integer.parseInt(equipment.getElementsByTagName("tamanho").item(0).getTextContent());
                       boolean isDefective = Boolean.parseBoolean(equipment.getElementsByTagName("defeituoso").item(0).getTextContent());
                       smts.setString(1,name);
                       smts.setInt(3,quantity);
                       smts.setInt(2,size);
                       smts.setBoolean(4,isDefective);
                       smts.executeUpdate();
                   }
               }
           }
           JOptionPane.showInternalMessageDialog(null,"Importação concluída com sucesso!");
           System.exit(0);
       } catch (ParserConfigurationException e) {
           throw new RuntimeException(e);
       }

    }
}
