package rest.warehouse;

import org.springframework.stereotype.Service;
import rest.model.ProductData;
import rest.model.WarehouseData;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    /**
     * Gibt einen kurzen Begrüßungstext für ein bestimmtes Modul aus.
     * @param inModule Name des Moduls
     * @return Begrüßung als String
     */
    public String getGreetings(String inModule) {
        return "Willkommen vom Modul: " + inModule;
    }

    /**
     * Liefert die Lagerinformationen für ein bestimmtes Lager basierend auf der Lager-ID.
     * @param inID Eindeutige ID des Lagers
     * @return WarehouseData-Objekt mit den Lagerinformationen
     */
    public WarehouseData getWarehouseData(String inID) {
        WarehouseSimulation simulator = new WarehouseSimulation();
        return simulator.getData(inID);
    }

    /**
     * Liefert gefilterte Lagerinformationen basierend auf Produktkategorie und/oder Produktname.
     * Es werden nur die Produkte zurückgegeben, die den Filterkriterien entsprechen.
     * @param productCategorie Kategorie des Produkts (null oder leer = keine Filterung)
     * @param productName Name oder Teilstring des Produkts (null oder leer = keine Filterung)
     * @return WarehouseData-Objekt mit den gefilterten Produkten
     */
    public WarehouseData getWarehouseDataFiltered(String productCategorie, String productName) {
        // Daten des Standardlagers abrufen (ID "001")
        WarehouseData warehouseData = getWarehouseData("001");

        if (warehouseData.getProducts() != null) {
            List<ProductData> filteredProducts = warehouseData.getProducts().stream()
                    .filter(p -> productCategorie == null
                            || productCategorie.isEmpty()
                            || p.getProductCategory().toLowerCase().contains(productCategorie.toLowerCase()))
                    .filter(p -> productName == null
                            || productName.isEmpty()
                            || p.getProductName().toLowerCase().contains(productName.toLowerCase()))
                    .collect(Collectors.toList());

            warehouseData.setProducts(filteredProducts);
        }

        return warehouseData;
    }
}
