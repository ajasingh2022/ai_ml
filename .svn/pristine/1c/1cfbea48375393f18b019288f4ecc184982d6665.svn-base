package com.capgemini.sesp.ast.android.module.cache;

import android.util.ArrayMap;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.comparators.TypeDataComparator;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.AndroidDynamicDataAstSepTO;
import com.skvader.rsp.ast_sep.webservice_client.bean.to.custom.AndroidStaticDataAstSepTO;
import com.skvader.rsp.cft.common.to.custom.base.BaseTO;
import com.skvader.rsp.cft.common.to.custom.base.CodeTO;
import com.skvader.rsp.cft.common.to.custom.base.IdTO;
import com.skvader.rsp.cft.common.to.custom.base.IuTimestampTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.StaticDataTO;
import com.skvader.rsp.cft.common.to.custom.base.TypeTO;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * This class contains semi static data. That is, static data like pallet
 * <p>
 * type, but also some rarely changed information like case type.
 * <p>
 * All data registered is put in the database and restored to memory only
 * upon access.
 * <p>
 * All static methods are backed by a default instance, which is initialised
 * at startup.
 */
public final class ObjectCache implements BaseTO {
	private static final long serialVersionUID = 8515072899553807626L;

	private static ObjectCache defaultInstance = new ObjectCache();
	private final static String TAG = "ObjectCache";

	private final Map<Class<? extends BaseTO>, Map<Long, ? extends BaseTO>> allTypesMap = new ArrayMap<Class<? extends BaseTO>, Map<Long, ? extends BaseTO>>();
	private final Map<Class<? extends IdTO>, Map<Long, ? extends IdTO>> allIdObjectsMap = new ArrayMap<Class<? extends IdTO>, Map<Long, ? extends IdTO>>();

	private final Map<Class<? extends BaseTO>, List<? extends BaseTO>> allTypesList = new ArrayMap<Class<? extends BaseTO>, List<? extends BaseTO>>();
	private final Map<Class<? extends IdTO>, List<? extends IdTO>> allIdObjectsList = new ArrayMap<Class<? extends IdTO>, List<? extends IdTO>>();

	private ObjectCache() {

	}

	private static boolean cacheCleared = false;

	public static void clear() {
		if (!cacheCleared) {
			defaultInstance.allTypesMap.clear();
			defaultInstance.allIdObjectsMap.clear();

			defaultInstance.allTypesList.clear();
			defaultInstance.allIdObjectsList.clear();
		}
	}

	public static void toggleClearState() {
		cacheCleared = !cacheCleared;
	}

	public static ObjectCache getDefaultInstance() {
		return defaultInstance;
	}

	public Map<Class<? extends BaseTO>, List<? extends BaseTO>> getAllTypesList() {
		return allTypesList;
	}

	@SuppressWarnings("unchecked")
	public static <I extends IdTO, T extends IdTO & StaticDataTO> void registerObjects(BaseTO typesTO) {
		try {
		Method[] methods = typesTO.getClass().getDeclaredMethods();

		for (Method method : methods) { //!(method.getGenericReturnType() instanceof Class) &&
			if (method.getName().startsWith("get")) {
				Type returnType = method.getGenericReturnType();
				// assuming its a list
				String subType = returnType.toString().substring(returnType.toString().indexOf("<") + 1).replace(">", "");

					Class<I> subTypeClass = (Class<I>) Thread.currentThread().getContextClassLoader().loadClass(subType);
					if (StaticDataTO.class.isAssignableFrom(subTypeClass)) {
						Class<T> typeClass = (Class<T>) subTypeClass;
						List<T> typeList = (List<T>) method.invoke(typesTO);
						if (typeList == null) {
							typeList = new ArrayList<T>(0);
						}
						defaultInstance.registerTypes(typeClass, typeList);
					} else {
						List<I> objectList = (List<I>) method.invoke(typesTO);
						if (objectList == null) {
							objectList = new ArrayList<I>(0);
						}
						defaultInstance.registerIdObjects(subTypeClass, objectList);
					}

			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :registerObjects() ", ex);
		}
	}

	/**
	 * Load Static Type Data for Android
	 *
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public <T extends IdTO & StaticDataTO> void loadStaticTypeDataForAndroid() {
		try {
		Method[] methods = AndroidStaticDataAstSepTO.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get")) {
				Type returnType = method.getGenericReturnType();
				String subType = returnType.toString().substring(returnType.toString().indexOf("<") + 1).replace(">", "");
				if (subType.equals("com.skvader.rsp.ast_sep.common.to.ast.table.UnitStatusReasonTCTO")) {
					Log.d(TAG, subType);
				}

					Class<T> typeClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(subType);
					List<? extends BaseTO> list = allTypesList.get(typeClass);
					if (list == null || list.isEmpty()) {
						DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
						list = db.getAll(typeClass);
						allTypesList.put(typeClass, list);
					}

			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :loadStaticTypeDataForAndroid() ", ex);
		}
	}

	/**
	 * Load Dynamic Type Data for Android
	 *
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public <T extends IdTO & IuTimestampTO> void loadDynamicTypeDataForAndroid() {
		try {
		Method[] methods = AndroidDynamicDataAstSepTO.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get")) {
				Type returnType = method.getGenericReturnType();
				String subType = returnType.toString().substring(returnType.toString().indexOf("<") + 1).replace(">", "");

					Class<T> typeClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(subType);
					List<? extends IdTO> list = allIdObjectsList.get(typeClass);
					if (list == null || list.isEmpty()) {
						DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
						list = db.getAll(typeClass);
						allIdObjectsList.put(typeClass, list);
					}

			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :loadDynamicTypeDataForAndroid() ", ex);
		}
	}

	private <T extends BaseTO> void registerTypes(Class<T> clazz, List<? extends T> valueList) {
		try{
		DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
		db.delete(clazz); // delete previous objects
		for (T type : valueList) {
			db.create(type);
		}
		} catch (Exception ex) {
			writeLog(TAG + " :registerTypes() ", ex);
		}
	}

	private <I extends IdTO> void registerIdObjects(Class<I> clazz, List<? extends I> valueList) {
		try{
		DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
		//db.delete(clazz); // delete previous objects
		for (I idObject : valueList) {
			db.delete(idObject.getId(), clazz);
			db.create(idObject);
		}
		} catch (Exception ex) {
			writeLog(TAG + " :registerIdObjects() ", ex);
		}
	}

	public static <I extends IdTO> void registerIdObject(I idObject) {
		try{
		DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
		db.delete(idObject.getClass()); // delete previous object
		db.create(idObject);
		} catch (Exception ex) {
			writeLog(TAG + " :registerIdObjects() ", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static <I extends IdTO> List<I> getAllIdObjects(Class<I> clazz) {
		List<? extends IdTO> list = defaultInstance.allIdObjectsList.get(clazz);
		try{
		if (list == null || list.isEmpty()) {
			DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
			list = db.getAll(clazz);
			defaultInstance.allIdObjectsList.put(clazz, list);
		}

		Collections.sort(list, new TypeDataComparator());
		} catch (Exception ex) {
			writeLog(TAG + " :getAllIdObjects() ", ex);
		}
		return (List<I>) list;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseTO> List<T> getAllTypes(Class<T> clazz) {
		List<? extends BaseTO> list = defaultInstance.allTypesList.get(clazz);
		try{
		if (list == null || list.isEmpty()) {
			DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
			list = db.getAll(clazz);
			defaultInstance.allTypesList.put(clazz, list);
		}

		Collections.sort(list, new TypeDataComparator());
	} catch (Exception ex) {
		writeLog(TAG + " :getAllTypes() ", ex);
	}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseTO> List<T> getAllTypesUnsorted(Class<T> clazz) {
		List<? extends BaseTO> list = defaultInstance.allTypesList.get(clazz);
		try{
		if (list == null || list.isEmpty()) {
			DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
			list = db.getAll(clazz);
			defaultInstance.allTypesList.put(clazz, list);
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getAllTypesUnsorted() ", ex);
		}
		return (List<T>) list;
	}

	public static <T extends BaseTO & NameInterfaceTO> String getTypeName(Class<T> clazz, Long id) {
		try{
		if (id != null) {
			T type = getType(clazz, id);
			if (type != null) {
				return type.getName();
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getTypeName() ", ex);
		}
		return null;
	}

	public static <T extends BaseTO & CodeTO> String getTypeCode(Class<T> clazz, Long id) {
		try{
		if (id != null) {
			T type = getType(clazz, id);
			if (type != null) {
				return type.getCode();
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getTypeCode() ", ex);
		}
		return null;
	}

	public static <T extends TypeTO> String getTypeNames(Class<T> clazz, Collection<Long> ids) {
		StringBuilder sb = new StringBuilder();
		try{
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				String name = getTypeName(clazz, id);
				if (name != null) {
					sb.append(name).append(", ");
				}
			}
			sb.delete(sb.length() - 2, sb.length());
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getTypeNames() ", ex);
		}
		return sb.toString();
	}

	public static <T extends TypeTO> String getTypeNameSagg(Class<T> clazz, String ids) {
		try{
		if (ids != null) {
			StringBuilder sb = new StringBuilder();
			for (String id : ids.split(",")) {
				String typeName = getTypeName(clazz, Long.valueOf(id));
				if (typeName != null) {
					sb.append(typeName).append(", ");
				}
			}
			if (sb.length() > 0) {
				sb.delete(sb.length() - 2, sb.length());
			}
			return sb.toString();
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getTypeNameSagg() ", ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseTO> T getType(Class<T> clazz, Long id) {

		Map<Long, T> map = (Map<Long, T>) defaultInstance.allTypesMap.get(clazz);
		try{
		if (map == null) {
			map = new ArrayMap<Long, T>();
			defaultInstance.allTypesMap.put(clazz, map);
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getType() ", ex);
		}

		T type = map.get(id);
		try{
		if (type == null) {
			DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
			type = db.load(id, clazz);
			if (type != null) {
				map.put(id, type);
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getType() ", ex);
		}
		return type;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseTO> T getType(Class<T> clazz, String code) {
		try{
		if (code != null) {
			for (T typeObject : getAllTypes(clazz)) {
				if (typeObject instanceof CodeTO) {
					if (code.equals(((CodeTO) typeObject).getCode())) {
						return typeObject;
					}
				}
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getType() ", ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <I extends IdTO> I getIdObject(Class<I> clazz, Long id) {
		I idObject = null;
		try{
		// The if else is added for temporary purpose only
		if (clazz != null && clazz.isAssignableFrom(WorkorderCustomWrapperTO.class)) {
			return DatabaseHandler.createDatabaseHandler().getSpecificWorkorder(id, clazz);
		} else {
			Map<Long, I> map = (Map<Long, I>) defaultInstance.allIdObjectsMap.get(clazz);
			if (map == null) {
				map = new ArrayMap<Long, I>();
				defaultInstance.allIdObjectsMap.put(clazz, map);
			}
			idObject = map.get(id);
			if (idObject == null) {
				DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
				try {
					idObject = db.load(id, clazz);
					if (idObject != null) {
						map.put(id, idObject);
					}
				} catch (Exception e) {
					writeLog(TAG + " :getIdObject() ", e);
				}
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getType() ", ex);
		}
			return idObject;
		}

	

	public static <I extends CodeTO> I getIdObject(Class<I> clazz, String code) {
		try{
		if (code != null) {
			for (I idObject : getAllIdObjects(clazz)) {
				if (code.equals(idObject.getCode())) {
					return idObject;
				}
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getIdObject() ", ex);
		}
		return null;
	}

	public static <I extends NameInterfaceTO & IdTO> String getIdObjectName(Class<I> clazz, Long id) {
		try{
		if (id != null) {
			I idObject = getIdObject(clazz, id);
			if (idObject != null) {
				return idObject.getName();
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getIdObjectName() ", ex);
		}
		return null;
	}

	public static <I extends NameInterfaceTO & CodeTO> String getIdObjectName(Class<I> clazz, String code) {
		try{
		if (code != null) {
			I idObject = getIdObject(clazz, code);
			if (idObject != null) {
				return idObject.getName();
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getIdObjectName() ", ex);
		}
		return null;
	}

	public static <T extends TypeTO> List<T> getTypes(Class<T> clazz, Collection<Long> ids) {
		List<T> types =null;
		try{
		if (ids == null) {
			return new ArrayList<T>(0);
		}

		 types = new ArrayList<T>(ids.size());
		for (Long id : ids) {
			T type = getType(clazz, id);
			if (type != null) {
				types.add(type);
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getTypes() ", ex);
		}
		return types;
	}

	public static <I extends IdTO> List<I> getIdObjects(Class<I> clazz, Collection<Long> ids) {
		List<I> idObjects = null;
		try{
		if (ids == null) {
			return new ArrayList<I>(0);
		}

	 idObjects = new ArrayList<I>(ids.size());
		for (Long id : ids) {
			I idObject = getIdObject(clazz, id);
			if (idObject != null) {
				idObjects.add(idObject);
			}
		}
		} catch (Exception ex) {
			writeLog(TAG + " :getIdObjects() ", ex);
		}
		return idObjects;
	}

	public static List<String> materialControlPalletList = new ArrayList<String>();

	public static List<String> getMaterialControlPallets() {
		return materialControlPalletList;
	}

}
