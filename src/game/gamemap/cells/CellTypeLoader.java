package game.gamemap.cells;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CellTypeLoader {

    public static List<CellType> loadCellTypesFromXml() throws Exception {
        List<CellType> cellTypes = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/game/gamemap/cells/cell_types.xml"));

        // Получаем корневой элемент
        Element root = document.getDocumentElement();
        NodeList cellNodes = root.getElementsByTagName("CellType");

        for (int i = 0; i < cellNodes.getLength(); i++) {
            Node node = cellNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element cellElement = (Element) node;

                // Получаем обязательные атрибуты
                char symbol = cellElement.getAttribute("symbol").charAt(0);
                int penalty = Integer.parseInt(cellElement.getAttribute("penalty"));
                String color = cellElement.getAttribute("color");

                // Получаем описание (если есть)
                String description = "";
                NodeList descNodes = cellElement.getElementsByTagName("description");
                if (descNodes.getLength() > 0) {
                    description = descNodes.item(0).getTextContent();
                }
                boolean is_castle = false;
                if (cellElement.getAttribute("is_castle").equals("true")) {
                    is_castle = true;
                }
                cellTypes.add(new CellType(symbol, penalty, color, description, is_castle));
            }
        }
        return cellTypes;
    }


}