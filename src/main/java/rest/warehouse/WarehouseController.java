package rest.warehouse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;

import rest.model.WarehouseData;

@RestController
public class WarehouseController {

    @Autowired
    private WarehouseService service;

    /**
     * Startseite der Lagerverwaltung.
     * Zeigt eine Übersicht über die verfügbaren REST-Endpunkte für Lagerdaten.
     * @return HTML-String mit Links zu den JSON- und XML-Daten
     */
    @RequestMapping("/")
    public String warehouseMain() {
        String mainPage = "Willkommen im Lagerverwaltungssystem! (REST Warehouse Demo)<br/><br/>" +
                "<a href='/warehouse/001/json'>➡ Lager 001 als JSON anzeigen</a><br/>" +
                "<a href='/warehouse/001/xml'>➡ Lager 001 als XML anzeigen</a><br/>";
        return mainPage;
    }

    /**
     * Liefert die Lagerdaten eines bestimmten Lagers im JSON-Format.
     * @param inID ID des gewünschten Lagers
     * @return Lagerdaten im JSON-Format
     */
    @RequestMapping(value = "/warehouse/{inID}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public WarehouseData warehouseData(@PathVariable String inID) {
        return service.getWarehouseData(inID);
    }

    /**
     * Liefert die Lagerdaten eines bestimmten Lagers im XML-Format.
     * @param inID ID des gewünschten Lagers
     * @return Lagerdaten im XML-Format
     */
    @RequestMapping(value = "/warehouse/{inID}/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public WarehouseData warehouseDataXML(@PathVariable String inID) {
        return service.getWarehouseData(inID);
    }
}