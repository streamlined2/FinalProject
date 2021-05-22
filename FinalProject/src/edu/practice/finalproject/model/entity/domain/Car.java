package edu.practice.finalproject.model.entity.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import edu.practice.finalproject.model.entity.NaturalKeyEntity;

public class Car extends NaturalKeyEntity<String> {
	public enum Manufacturer { 
		BMW, DAIMLER, FORD, GM, HONDA, HYUNDAI, MAZDA, NISSAN, SKODA, TESLA , TOYOTA, VOLKSWAGEN }
	public enum QualityGrade { BASIC, ECONOMY, PREMIUM, BUSINESS, FIRSTCLASS }
	public enum Color { BLUE, BLACK, GREEN, RED, YELLOW, WHITE }
	public enum Style { CONVERTIBLE, COUPE, HATCHBACK, MINIVAN, SEDAN, SPORTS_CAR, STATION_WAGON, SUV }
	
	private String model;
	private Manufacturer manufacturer;
	private QualityGrade qualityGrade;
	private BigDecimal cost;
	private LocalDate productionDate;
	private Color color;
	private Style style;
	
	public Car() {}
	
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
