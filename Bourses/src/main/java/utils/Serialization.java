package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Company;
import model.CurrenciesMarket;
import model.InvestmentFund;
import model.Investor;
import model.RawMaterialsMarket;
import model.StockExchange;
import model.World;

public class Serialization {

	public static void serializeObject(Object object, String path) {

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(path);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object deserializeObject(String path) {

		try {
			FileInputStream fileInputStream = new FileInputStream(path);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			return object;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void saveStateOfSimulation() {
		SerializableWorld serializableWorld = castStaticWorldToSerializableWorld();
		serializeObject(serializableWorld, "save");
	}
	
	public static void loadStateOfSimulation() {
		SerializableWorld serializableWorld = (SerializableWorld) deserializeObject("save");
		castSerializableWorldToStaticWorld(serializableWorld);
		for(Company c : World.getCompanies()) {
			new Thread(c).start();
		}
		for(Investor inv : World.getInvestors()) {
			new Thread(inv).start();
		}
		for(InvestmentFund inv : World.getInvestmentFunds()) {
			new Thread(inv).start();
		}
		for(StockExchange stockExchange : World.getStockExchanges()) {
			new Thread(stockExchange).start();
		}
		for(CurrenciesMarket cm : World.getCurrenciesMarkets()) {
			new Thread(cm).start();
		}
		for(RawMaterialsMarket rmm : World.getRawMaterialsMarkets()) {
			new Thread(rmm).start();
		}
		
	}
	
	public static SerializableWorld castStaticWorldToSerializableWorld() {
		
		SerializableWorld serializableWorld = new SerializableWorld();

		serializableWorld.setStockExchanges(World.getStockExchanges());
		serializableWorld.setCurrenciesMarkets(World.getCurrenciesMarkets());
		serializableWorld.setRawMaterialsMarkets(World.getRawMaterialsMarkets());
		serializableWorld.setCompanies(World.getCompanies());
		serializableWorld.setCurrencies(World.getCurrencies());
		serializableWorld.setInvestmentFunds(World.getInvestmentFunds());
		serializableWorld.setInvestors(World.getInvestors());
		serializableWorld.setRawMaterials(World.getRawMaterials());
		serializableWorld.setNextAssetId(World.getNextAssetId());
		serializableWorld.setDate(World.getDate());
		serializableWorld.setNumberOfAssets(World.getNumberOfAssets());
		return serializableWorld;
	}
	
	public static void castSerializableWorldToStaticWorld(SerializableWorld serializableWorld) {
		
		World.setCompanies(serializableWorld.getCompanies());
		World.setCurrencies(serializableWorld.getCurrencies());
		World.setCurrenciesMarkets(serializableWorld.getCurrenciesMarkets());
		World.setInvestmentFunds(serializableWorld.getInvestmentFunds());
		World.setInvestors(serializableWorld.getInvestors());
		World.setRawMaterials(serializableWorld.getRawMaterials());
		World.setRawMaterialsMarkets(serializableWorld.getRawMaterialsMarkets());
		World.setStockExchanges(serializableWorld.getStockExchanges());
		World.setNextAssetId(serializableWorld.getNextAssetId());
		World.setDate(serializableWorld.getDate());
		World.setNumberOfAssets(serializableWorld.getNumberOfAssets());
	}
	
}
