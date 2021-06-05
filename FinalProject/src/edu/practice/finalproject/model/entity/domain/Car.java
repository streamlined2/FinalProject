package edu.practice.finalproject.model.entity.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public class Car extends NaturalKeyEntity<String> {
	public enum Manufacturer { 
		BMW("BMW"), 
		DAIMLER("Daimler"), 
		FORD("Ford"), 
		GM("GM"), 
		HONDA("Honda"), 
		HYUNDAI("Hyundai"), 
		MAZDA("Mazda"), 
		NISSAN("Nissan"), 
		SKODA("Skoda"), 
		TESLA("Tesla"), 
		TOYOTA("Toyota"), 
		VOLKSWAGEN("Volkswagen");
		
		private final String label;
		Manufacturer(final String label) { this.label=label;}
		
		@Override public String toString() { return label;}
	}
	
	public enum QualityGrade { 
		BASIC("Basic"), 
		ECONOMY("Economy"), 
		PREMIUM("Premium"), 
		BUSINESS("Business"), 
		FIRSTCLASS("First class");
		
		private final String label;
		QualityGrade(final String label){ this.label=label;}
		
		@Override public String toString() { return label;}
	}
	
	public enum Color { 
		BLUE("Blue"), 
		BLACK("Black"), 
		GREEN("Green"), 
		RED("Red"), 
		YELLOW("Yellow"), 
		WHITE("White");
		
		private final String label;
		Color(final String label){ this.label=label;}
		
		@Override public String toString() { return label;}
	}
	
	public enum Style { 
		CONVERTIBLE("Convertible"), 
		COUPE("Coupe"), 
		HATCHBACK("Hatchback"), 
		MINIVAN("Minivan"), 
		SEDAN("Sedan"), 
		SPORTS_CAR("Sports car"), 
		STATION_WAGON("Station wagon"), 
		SUV("SUV");
		
		private final String label;
		Style(final String label){ this.label=label;}

		@Override public String toString() { return label;}
	}
	
	private String model;
	private Manufacturer manufacturer;
	private QualityGrade qualityGrade;
	private BigDecimal cost;
	private LocalDate productionDate;
	private Color color;
	private Style style;
	
	public String getModel() { return model;}
	public void setModel(final String model) { this.model=model;}
	
	public Manufacturer getManufacturer() { return manufacturer;}
	public void setManufacturer(final Manufacturer manufacturer) {this.manufacturer=manufacturer;}
	
	public QualityGrade getQualityGrade() { return qualityGrade;}
	public void setQualityGrade(final QualityGrade qualityGrade) { this.qualityGrade=qualityGrade;}
	
	public BigDecimal getCost() { return cost;}
	public void setCost(final BigDecimal cost) { this.cost=cost;}
	
	public LocalDate getProductionDate() { return productionDate;}
	public void setProductionDate(final LocalDate productionDate) { this.productionDate=productionDate;}
	
	public Color getColor() { return color;}
	public void setColor(final Color color) { this.color=color;}
	
	public Style getStyle() { return style;}
	public void setStyle(final Style style) { this.style=style;}
	
	@Override protected String getKey() { return getModel();}
	@Override protected String keyFieldGetter() { return "getModel";}
}
