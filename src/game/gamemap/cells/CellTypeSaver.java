package game.gamemap.cells;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class CellTypeSaver {
    public static void saveCellTypesToXml(List<CellType> cellTypes) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Создаем XML документ
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("CellTypes");
        doc.appendChild(rootElement);

        // Добавляем каждый тип клетки как элемент в XML
        for (CellType cellType : cellTypes) {
            Element cellElement = doc.createElement("CellType");
            rootElement.appendChild(cellElement);

            // Добавляем обязательные атрибуты
            cellElement.setAttribute("symbol", String.valueOf(cellType.getSymbol()));
            cellElement.setAttribute("penalty", String.valueOf(cellType.getPenalty()));
            cellElement.setAttribute("color", cellType.getColor());

            // Добавляем атрибут is_castle (по умолчанию false)
            cellElement.setAttribute("is_castle", String.valueOf(cellType.isCastle()));

            // Описание добавляем только если оно есть
            if (cellType.getDescription() != null && !cellType.getDescription().isEmpty()) {
                Element descElement = doc.createElement("description");
                descElement.appendChild(doc.createTextNode(cellType.getDescription()));
                cellElement.appendChild(descElement);
            }
        }

        // Записываем содержимое в XML файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/game/gamemap/cells/cell_types.xml"));
        transformer.transform(source, result);
    }
}