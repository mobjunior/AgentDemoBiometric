package com.agentdemo.dao.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.agentdemo.dao.ProductItemDao;
import com.agentdemo.db.DBService;
import com.agentdemo.entity.ProductItem;

public class ProductItemDaoImpl implements ProductItemDao {
	
	private DBService dbService = null;
	private Context context = null;
	
	public ProductItemDaoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductItemDaoImpl(Context context) {
		super();
		this.context = context;
		dbService = new DBService(this.context);
	}

	@Override
	public void addProductItem(ProductItem productItem) {
		// TODO Auto-generated method stub

		if(isExist(productItem.getBarcode())){
			Toast.makeText(context, "This product already exists! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}

		String sql = "";
		Object[] args = null;
		sql = "insert into t_item_lib(name,barcode,price,quantity) values(?, ?, ?, ?)";
		args = new Object[] { productItem.getName(), productItem.getBarcode(),
				productItem.getPrice(), productItem.getQuantity() };
		SQLiteDatabase db = dbService.getWritableDatabase();
		db.execSQL(sql, args);
		db.close();
	}
	
	@Override
	public void updateQuantity(String barcode,int quantity) {
		// TODO Auto-generated method stub

		if(isExist(barcode)){
			ProductItem productItem = getProductItem(barcode);
			if((productItem.getQuantity() + quantity) < 0){
				Toast.makeText(context, "Inventory shortage! Ignore this operation!", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			String sql = "";
			Object[] args = null;
			sql = "update t_item_lib set quantity=? where barcode=?";
			args = new Object[] { productItem.getQuantity() + quantity, barcode };
			SQLiteDatabase db = dbService.getWritableDatabase();
			db.execSQL(sql, args);
			db.close();
		}else{
			Toast.makeText(context, "This product does not exist! Ignore this operation!", Toast.LENGTH_SHORT).show();
			return ;
		}
	}
	
	private boolean isExist(String barcode) {
		String[] args = new String[]{barcode};
		String sql = "select * from t_item_lib where barcode = ?";
		
		SQLiteDatabase db = dbService.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		if(cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return false;
		} else {

			cursor.close();
			db.close();
			return true;
		}
	}
	
	private ProductItem getProductItem(String barcode){
		ProductItem productItem = new ProductItem();
		String sql = "select * from t_item_lib where barcode = ?";
		
		SQLiteDatabase db = dbService.getReadableDatabase();
		String[] args = new String[]{barcode};
		Cursor cursor = db.rawQuery(sql, args);
		if(cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return null;
		} else {
			cursor.moveToFirst();
			productItem.setName(cursor.getString(cursor.getColumnIndex("name")));
			productItem.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
			productItem.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
			productItem.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));

			cursor.close();
			db.close();
			return productItem;
		}
	}
}
