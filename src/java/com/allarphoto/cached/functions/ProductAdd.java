package com.allarphoto.cached.functions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.BatchUpdate;
import strategiclibrary.service.cache.Cache;
import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

import com.allarphoto.beans.Path;
import com.allarphoto.category.ProductField;
import com.allarphoto.category.ProtectedField;
import com.allarphoto.ecommerce.CommerceProduct;
import com.allarphoto.utils.Resource;

public class ProductAdd extends AbstractCacheFunction<CommerceProduct> {
	ResourceAdd resAdder = new ResourceAdd();

	ProductFieldAdd fieldAdder = new ProductFieldAdd();

	String uploadDir;

	static int counter = new Random().nextInt();

	@Override
	protected Class<CommerceProduct> getRelevantType() {
		return CommerceProduct.class;
	}

	@Override
	protected Object[] getSearchPath(CommerceProduct obj) {
		return new Object[] { "family", obj.getProductFamilyName(), "path",
				obj.getPath(), "Primary", obj.getPrimary() };
	}

	@Override
	protected boolean isValidObj(CommerceProduct obj) {
		return obj.getPrimary() != null && obj.getPath() != null
				&& obj.getValue("File Type") != null
				&& obj.getDateCataloged() != null;
	}

	public void add(CommerceProduct obj) {
		obj.setId(getId(obj.getProductFamilyName()));
		try {
			subObjects(obj);
			insertBasics(obj);
			insertValues(obj, db.createBatch());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Cache<CommerceProduct> getCache(CacheService cacheService,
			CommerceProduct i) {
		return cacheService.getCache(i.getProductFamilyName(),
				getRelevantType());
	}

	private void insertBasics(CommerceProduct obj) throws Exception {
		Map values = new HashMap();
		values.put("product", obj);
		getLog().info("product = " + obj);
		db.executeTemplateUpdate("addProduct.sql", db
				.getTemplateContext(values));
	}

	private void updateBasics(CommerceProduct obj) throws Exception {
		Map values = new HashMap();
		values.put("product", obj);
		db.executeTemplateUpdate("updateProduct.sql", db
				.getTemplateContext(values));
	}

	private void insertValues(CommerceProduct obj, BatchUpdate batch)
			throws Exception {
		for (Object fieldName : obj.getValue().keySet()) {
			ProductField field = dbUtil.getProductFamily(
					obj.getProductFamilyName()).getField(fieldName.toString());
			if (field == null)
				field = fieldAdder
						.addOrGet(ProductField.createField(obj
								.getProductFamilyName(), fieldName.toString(),
								1, 0, 0));
			for (Object value : obj.getValues(fieldName.toString())) {
				if (value != null && !value.toString().equalsIgnoreCase("null")
						&& !value.equals(DEFAULT) && value.toString().length() > 0) {
					if(value instanceof Calendar)
						value = Converter.formatCalendar((Calendar)value,"yyyy-MM-dd");
					field.addValue(obj, value, batch, mapper, db);
					addResource(obj, field, value);
				}
				if (field.getType() == ProductField.NUMERICAL)
					break;
			}
		}
		batch.execute();
	}

	private void addResource(CommerceProduct obj, ProductField field,
			Object value) {
		if (field instanceof ProtectedField) {
			resAdder.addOrGet(new Resource(obj.getProductFamilyName() + "."
					+ field.getName() + "." + value, Resource.PROTECTED_FIELD));
		}
	}

	private void updateValues(CommerceProduct obj) throws Exception {
		BatchUpdate batch = db.createBatch();
		Map values = new HashMap();
		values.put("product", obj);
		batch.addUpdate("deleteCategoryValues.sql", values);
		batch.addUpdate("deleteDescriptionValues.sql", values);
		batch.addUpdate("deleteStatValues.sql", values);
		batch.execute();
		insertValues(obj, batch);
	}

	private void subObjects(CommerceProduct obj) throws Exception {
		obj.setPath(addPath(obj.getProductFamilyName(), obj.getPathName()));
	}

	public void update(CommerceProduct obj) {
		try {
			subObjects(obj);
			updateBasics(obj);
			updateValues(obj);
			saveUpdateProperties(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void saveUpdateProperties(CommerceProduct obj) {
		if (uploadDir == null)
			return;
		Properties props = new Properties();
		props.setProperty("FILENAME", obj.getPrimary());
		props.setProperty("PATHNAME", obj.getPathName());
		props.setProperty("family", obj.getProductFamilyName());
		for (ProductField field : obj.getProductFamily().getFields()) {
			if ((field.getType() == ProductField.CATEGORY
					|| field.getType() == ProductField.PROTECTED || field
					.getType() == ProductField.TAG)
					&& obj.getValue(field.getName()) != null) {
				props.setProperty(field.getName(), obj
						.getValue(field.getName()).toString());
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(uploadDir, obj.getName() + "_"
					+ (counter++) + ".properties"));
			props.store(out, "");
		} catch (IOException e) {
			this.getLog().error(
					"Failed to save properties update for file move", e);
		} finally {
			try {
				out.close();
			} catch (IOException err) {
				getLog().error("Failed to close properties file for file move",
						err);
			}
		}
	}

	public Path addPath(String pt, String path) throws Exception {
		Map values = new HashMap();
		values.put("path", path);
		values.put("pathTable", pt + "_paths");
		Collection<Path> paths = (Collection<Path>) mapper.getObjects(
				"getPath.sql", values);
		if (paths != null && paths.size() == 1)
			return (Path) paths.iterator().next();
		else {
			Path p = new Path((int) getId(pt + "_paths"), path);
			values.put("path", p);
			db.executeTemplateUpdate("addPath.sql", db
					.getTemplateContext(values));
			return p;
		}
	}

	@CoinjemaDependency(alias = "uploadDir")
	public void setUploadDir(String dir) {
		if (dir == null || dir.length() == 0 || dir.equals("NO_DIR"))
			uploadDir = null;
		else
			uploadDir = dir;
	}
}
