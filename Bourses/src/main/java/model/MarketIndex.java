package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MarketIndex implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7943578728176947413L;

	private String name;
	private List<Company> companies = new ArrayList<>();
	private BigDecimal value;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		BigDecimal value = new BigDecimal(0);
		for(Company company : this.companies) {
			value = value.add(company.getCurrentPrice());
		}
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	
	
	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public void addStock(Company company) {
		companies.add(company);
	}
	
	public void deleteStock(Company company) {
		companies.remove(company);
	}
}
