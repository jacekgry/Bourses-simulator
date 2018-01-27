package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AssetAmount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4671190844655092569L;
	private Asset asset;
	private BigDecimal amount;

	public AssetAmount(Asset asset, BigDecimal amount) {
		super();
		this.asset = asset;
		this.amount = amount;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public BigDecimal getAmount() {
		return amount.setScale(3, RoundingMode.HALF_UP);
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "AssetAmount [asset=" + asset + ", amount=" + amount + "]";
	}

}
