package rest.warehouse;

import rest.model.ProductData;
import rest.model.WarehouseData;

import java.util.ArrayList;
import java.util.List;

public class WarehouseSimulation {
	
	private double getRandomDouble( int inMinimum, int inMaximum ) {

		double number = ( Math.random() * ( (inMaximum-inMinimum) + 1 )) + inMinimum; 
		double rounded = Math.round(number * 100.0) / 100.0; 
		return rounded;
		
	}

	private int getRandomInt( int inMinimum, int inMaximum ) {

		double number = ( Math.random() * ( (inMaximum-inMinimum) + 1 )) + inMinimum; 
		Long rounded = Math.round(number); 
		return rounded.intValue();

	}
	
	public WarehouseData getData( String inID ) {
		
		WarehouseData data = new WarehouseData();
		data.setWarehouseID( inID );
		data.setWarehouseName( "Linz Bahnhof" );
		data.setWarehouseAddress("Bahnhofsstrasse 27/9");
		data.setWarehousePostalCode("Linz");
		data.setWarehouseCity("Linz");
		data.setWarehouseCountry("Austria");
		data.setTimestamp("2021-09-12 08:52:39.077");

		List<ProductData> products = new ArrayList<>();
		products.add(new ProductData("00-443175", "Bio Orangensaft Sonne", "Getraenk", 2500, "Packung 1L"));
		products.add(new ProductData("00-871895", "Bio Apfelsaft Gold", "Getraenk", 3420, "Packung 1L"));
		products.add(new ProductData("01-926885", "Ariel Waschmittel Color", "Waschmittel", 478, "Packung 3KG"));
		products.add(new ProductData("00-316253", "Persil Discs Color", "Waschmittel", 1430, "Packung 700G"));

		data.setProducts(products);

		return data;
		
	}

}
