package model;

import java.util.ArrayList;
import java.util.List;

public class RawMaterialsMarket extends Market {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4640689019008432544L;

	private List<RawMaterial> rawMaterials = new ArrayList<>();



	public List<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}

	public void setRawMaterials(List<RawMaterial> rawMaterials) {
		this.rawMaterials = rawMaterials;
	}

	public void addRawMaterial(RawMaterial rawMaterial) {
		rawMaterials.add(rawMaterial);
	}

	public void deleteRawMaterial(RawMaterial rawMaterial) {
		rawMaterials.remove(rawMaterial);
	}

	public void delete() {
		World.getRawMaterialsMarkets().remove(this);
		this.setRunning(false);
	}

	private void recalculatePrices() {
		for (RawMaterial rawMaterial : this.getRawMaterials()) {
			recalculatePrice(rawMaterial);
		}
	}

	@Override
	public void run() {
		
		try {
			Thread.sleep(random.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (isRunning()) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			realizeDeals();
			recalculatePrices();
		}
	}
}
