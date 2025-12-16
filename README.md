# Middleware Engineering "REST and Data Formats"

## Aufgabenstellung
Entwickeln Sie einen Simulator, der die Daten eines Lagerstandortes (WHx) generiert. Es ist dabei zu achten, dass die Daten realistisch sind und im Zusammenhang mit entsprechenden Einheiten erzeugt werden. Diese Daten sollen gemeinsam mit einigen Details zum dem Standort ueber eine REST Schnittstelle veroeffentlicht werden. Die Schnittstelle verwendet standardmaessig das JSON Format und kann optional auf XML umgestellt werden.

---

## Implementierung

## Klassenübersicht

---

### 1. `WarehouseApplication`
- **Paket:** `rest`
- **Funktion:** Einstiegspunkt der Spring Boot Applikation.
- **Beschreibung:** Startet den Spring Boot Server und initialisiert alle benötigten Komponenten.

---

### 2. `WarehouseData`
- **Paket:** `rest.model`
- **Funktion:** Modellklasse für Lagerdaten.
- **Beschreibung:** Enthält Lagerinformationen (ID, Name, Adresse, Standort, Zeitstempel) und eine Liste von `ProductData`. Unterstützt JSON- und XML-Serialisierung.

Modelliert ein Lager und enthält alle Stammdaten sowie die Produktliste.

```java
private String warehouseID;
private String warehouseName;
private String warehouseAddress;
private String warehousePostalCode;
private String warehouseCity;
private String warehouseCountry;
private String timestamp;
````

```java
@JacksonXmlElementWrapper(localName = "productData")
@JacksonXmlProperty(localName = "product")
private List<ProductData> products;
```

```java
public WarehouseData() {
    this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .format(new Date());
}
```

---

### 3. `ProductData`

* **Paket:** `rest.model`
* **Funktion:** Modellklasse für Produkte im Lager.
* **Beschreibung:** Speichert Produktdetails wie ID, Name, Kategorie, Menge und Einheit. Enthält Getter/Setter und `toString()` für einfache Ausgabe.

Modelliert ein Produkt im Lager mit allen wichtigen Eigenschaften.

```java
public ProductData(String productID, String productName,
                   String productCategory, int productQuantity,
                   String productUnit) {
    this.productID = productID;
    this.productName = productName;
    this.productCategory = productCategory;
    this.productQuantity = productQuantity;
    this.productUnit = productUnit;
}
```

```java
public String getProductName() {
    return productName;
}
```

---

### 4. `WarehouseController`

* **Paket:** `rest.warehouse`
* **Funktion:** REST-Controller für Lagerdaten.
* **Beschreibung:** Bietet Endpunkte `/`, `/warehouse/{id}/json` und `/warehouse/{id}/xml`. Übergibt Anfragen an den `WarehouseService` und liefert Daten im gewünschten Format.

REST-Endpunkte für Lagerdaten, liefert JSON oder XML.

```java
@RequestMapping("/")
public String warehouseMain() {
    return "Willkommen im Lagerverwaltungssystem!<br/>" +
           "<a href='/warehouse/001/json'>➡ Lager 001 als JSON anzeigen</a><br/>" +
           "<a href='/warehouse/001/xml'>➡ Lager 001 als XML anzeigen</a><br/>";
}
```

```java
@RequestMapping(
    value = "/warehouse/{inID}/json",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public WarehouseData warehouseData(@PathVariable String inID) {
    return service.getWarehouseData(inID);
}
```

```java
@RequestMapping(
    value = "/warehouse/{inID}/xml",
    produces = MediaType.APPLICATION_XML_VALUE
)
public WarehouseData warehouseDataXML(@PathVariable String inID) {
    return service.getWarehouseData(inID);
}
```

---

### 5. `WarehouseService`

* **Paket:** `rest.warehouse`
* **Funktion:** Service-Klasse zur Geschäftslogik.
* **Beschreibung:** Liefert Lagerdaten (`getWarehouseData`) oder gefilterte Daten (`getWarehouseDataFiltered`). Nutzt `WarehouseSimulation` für die Datengenerierung.

Geschäftslogik: liefert Lagerdaten, optional gefiltert nach Kategorie oder Produktname.

```java
public WarehouseData getWarehouseData(String inID) {
    WarehouseSimulation simulator = new WarehouseSimulation();
    return simulator.getData(inID);
}
```

```java
public WarehouseData getWarehouseDataFiltered(
        String productCategorie,
        String productName) {

    WarehouseData warehouseData = getWarehouseData("001");

    if (warehouseData.getProducts() != null) {
        List<ProductData> filteredProducts =
            warehouseData.getProducts().stream()
                .filter(p -> productCategorie == null
                        || productCategorie.isEmpty()
                        || p.getProductCategory()
                             .toLowerCase()
                             .contains(productCategorie.toLowerCase()))
                .filter(p -> productName == null
                        || productName.isEmpty()
                        || p.getProductName()
                             .toLowerCase()
                             .contains(productName.toLowerCase()))
                .collect(Collectors.toList());

        warehouseData.setProducts(filteredProducts);
    }

    return warehouseData;
}
```

---

### 6. `WarehouseSimulation`

* **Paket:** `rest.warehouse`
* **Funktion:** Simulator für Lager- und Produktdaten.
* **Beschreibung:** Generiert Beispiel-Lagerdaten mit festgelegten Produkten. Liefert `WarehouseData`-Objekte basierend auf Lager-ID.

```java
public WarehouseData getData(String inID) {
    WarehouseData data = new WarehouseData();
    data.setWarehouseID(inID);
    data.setWarehouseName("Linz Bahnhof");
    data.setWarehouseAddress("Bahnhofsstrasse 27/9");
    data.setWarehousePostalCode("Linz");
    data.setWarehouseCity("Linz");
    data.setWarehouseCountry("Austria");
    data.setTimestamp("2021-09-12 08:52:39.077");

    List<ProductData> products = new ArrayList<>();
    products.add(new ProductData(
        "00-443175", "Bio Orangensaft Sonne",
        "Getraenk", 2500, "Packung 1L"));
    products.add(new ProductData(
        "00-871895", "Bio Apfelsaft Gold",
        "Getraenk", 3420, "Packung 1L"));
    products.add(new ProductData(
        "01-926885", "Ariel Waschmittel Color",
        "Waschmittel", 478, "Packung 3KG"));
    products.add(new ProductData(
        "00-316253", "Persil Discs Color",
        "Waschmittel", 1430, "Packung 700G"));

    data.setProducts(products);
    return data;
}
```

---

## Fehler

Die XML-Ausgabe funktionierte zunächst nicht, obwohl der entsprechende REST-Endpunkt vorhanden war. Ursache war eine fehlende Dependency im `build.gradle`. Nach dem Hinzufügen von

```gradle
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'
```

## Quellen

[1] J. Little, „spring boot: what dependencies are needed make a simple spring boot app with domain objects, services and web controllers (similar to grails?)“, Stack Overflow. Zugegriffen: 25. September 2025. [Online]. Verfügbar unter: https://stackoverflow.com/q/77384136

[2] M. Reiche, „Antwort auf ‚spring boot: what dependencies are needed make a simple spring boot app with domain objects, services and web controllers (similar to grails?)‘“, Stack Overflow. Zugegriffen: 25. September 2025. [Online]. Verfügbar unter: https://stackoverflow.com/a/77391138

[3] S. D’sa, „Antwort auf ‚spring boot: what dependencies are needed make a simple spring boot app with domain objects, services and web controllers (similar to grails?)‘“, Stack Overflow. Zugegriffen: 25. September 2025. [Online]. Verfügbar unter: https://stackoverflow.com/a/77384214

[4] S. Riffaldi, „Antwort auf ‚spring boot: what dependencies are needed make a simple spring boot app with domain objects, services and web controllers (similar to grails?)‘“, Stack Overflow. Zugegriffen: 25. September 2025. [Online]. Verfügbar unter: https://stackoverflow.com/a/77390421

[5] „Spring Boot Projektübersicht“. Zugegriffen: 25. September 2025. [Online]. Verfügbar unter: https://spring.io/projects/spring-boot
