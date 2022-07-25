package com.capgemini.sesp.ast.android.module.util;

import android.util.Log;

import com.capgemini.sesp.ast.android.module.util.OdvUtil.Odv;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skvader.rsp.cft.common.to.TransferObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * CREATED UTILITY CLASS FOR ABORT FUNCTIONALITY
 * samdasgu
 * 4/27/2017
 */
public class AbortUtil<T extends Serializable> {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    private Hashtable<String, T> abortTable = new Hashtable<String, T>();
    private static final String TAG = AbortUtil.class.getSimpleName();

    /**
     * Hashtable cannot contain null value,
     * should probably be used to store all values but is not currently
     */
    /*public class AbortValue implements Serializable{

		@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
		private T value;

		public AbortValue() {}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
	}*/

    /**
     * Set abortTable from SQLLITE
     * Required for workorder resume
     *
     * @param abortTable
     */
    public void setAbortTable(Hashtable abortTable) {
        if (abortTable == null) {
            abortTable = new Hashtable();
        }
        this.abortTable = abortTable;
    }

    /**
     * Fetch the abort cache to be saved in SQLlite database
     *
     * @return
     */
    public Hashtable getAbortTable() {
        return this.abortTable != null ? this.abortTable : new Hashtable();
    }

    /**
     * Saves a backup copy of a single specified field
     *
     * @param toInstance instance of the TO where a value should be backed up
     * @param fieldName  db field name from the TO
     * @return
     */
    public boolean backupField(Object toInstance, String fieldName) {
        boolean success = false;
        try {
            String methodBase = TransferObjectUtils.convertDbToJavaClassName(fieldName);
            Method getMethod = toInstance.getClass().getMethod("get" + methodBase);
            Object object = getMethod.invoke(toInstance);

			/*AbortValue abortValue = new AbortValue();
			abortValue.setValue((T)object);*/

            abortTable.put(toInstance.getClass().getName() + fieldName, (T) object);
            success = true;
        } catch (Exception e) {
            writeLog(TAG + "backupField() : Unable to backup field with fieldname " + fieldName, e);
        }
        return success;
    }

    /**
     * Restores a backup copy of a single specified field
     *
     * @param toInstance instance of the TO where a value should be restored
     * @param fieldName  db field name from the TO
     * @return
     */
    public boolean restoreField(Object toInstance, String fieldName) {
        boolean success = false;
        try {
            T abortValue = abortTable.get(toInstance.getClass().getName() + fieldName);

            String fieldBase = TransferObjectUtils.convertDbToJavaName(fieldName);
            String methodBase = TransferObjectUtils.convertDbToJavaClassName(fieldName);

            Class clazz = toInstance.getClass();
            Field f = clazz.getDeclaredField(fieldBase);
            Class[] typeParams = new Class[]{f.getType()};
            Method dstSetMethod = toInstance.getClass().getMethod("set" + methodBase, typeParams);
            dstSetMethod.invoke(toInstance, abortValue);
            success = true;
        } catch (Exception e) {
            writeLog(TAG + " :restoreField() : Unable to restore field with fieldname " + fieldName, e);
        }
        return success;
    }

    /**
     * Saves the specified ODV value from specified instance for later undoing<br><br>
     * Example usage:<br>
     * abortUtil.backupODV(woInstMepTO, WoInstMepTO.ADDITIONAL_METER_PLMT_INFO_D);
     *
     * @param toInstance   instance of the TO containing ODV values
     * @param fieldNameODV any of the ODV db field names from the instance
     * @return
     * @throws Exception
     */
    public boolean backupODV(Object toInstance, String fieldNameODV) {
        boolean success = false;
        try {
            Odv odv = OdvUtil.getODVValues(toInstance, fieldNameODV);
            abortTable.put(OdvUtil.getBaseFieldName(fieldNameODV), (T) odv);
            success = true;
        } catch (Exception e) {
            writeLog(TAG + " : backupODV(): Error backing up value", e);
        }
        return success;
    }

    /**
     * Restores a backed up ODV value to the specified instance<br><br>
     * Example usage:<br>
     * abortUtil.restoreODV(woInstMepTO, WoInstMepMTO.ADDITIONAL_METER_PLMT_INFO_D);
     *
     * @param toInstance   instance of the TO where ODV values should be restored
     * @param fieldNameODV any of the ODV db field names from the instance
     * @return
     * @throws Exception
     */
    public boolean restoreODV(Object toInstance, String fieldNameODV) {
        boolean success = false;
        try {
            Odv odv = (Odv) abortTable.get(OdvUtil.getBaseFieldName(fieldNameODV));
            OdvUtil.setODVValues(toInstance, fieldNameODV, odv);
            success = true;
        } catch (Exception e) {
            writeLog(TAG + " :restoreODV(): Error restoring value", e);
        }
        return success;
    }


    /**
     * Get a recursive copy of toInstances, but only restore TOs where the id in idFieldName matches idToMatch,<br>
     * all entries in toInstances array where idToMatch is matched will be removed and restored with previously backed up copy.<br><br>
     * Example:
     *
     * @param toInstances an array containing TOs
     * @param idFieldName
     * @param idToMatch
     * @param toClass
     * @return
     */
    public List getRestoreTOsWithId(Object toInstances, String idFieldName, Long idToMatch, Class toClass) {

        Vector tos = new Vector();
        Class toInstancesClazz = null;
        List finalResult = null;
        try {

            // First retrieve a copy of tos not matching idToMatch (including entries where id is null)
            if (toInstances != null && toInstances instanceof List) {
                List instances = (List) toInstances;
                String methodBase = TransferObjectUtils.convertDbToJavaClassName(idFieldName);
                for (int i = 0; i < instances.size(); i++) {
                    if (toInstancesClazz == null) {
                        toInstancesClazz = instances.get(i).getClass();
                    }
                    Method getMethod = instances.get(i).getClass().getMethod("get" + methodBase);
                    Long id = (Long) getMethod.invoke(instances.get(i), new Object[]{});

                    if (id == null || !id.equals(idToMatch)) {
                        // A copy should really not be needed here, might be safer?
                        tos.addElement(AndroidUtilsAstSep.copyObject(instances.get(i)));
                    }
                }
            }

            // Secondly re-add backed up entries
            Vector restores = (Vector) abortTable.get(toClass.getName() + idFieldName + idToMatch);

            if (restores != null) {
                for (int i = 0; i < restores.size(); i++) {
                    ObjectMapper mapper = new ObjectMapper();
                    tos.addElement(AndroidUtilsAstSep.copyObject(mapper.convertValue(restores.elementAt(i), toInstancesClazz)));
                }
            }


            finalResult = new ArrayList(tos.size());
            ListIterator itr = tos.listIterator();
            while (itr.hasNext()) {
                finalResult.add(itr.next());
            }
        } catch (Exception e) {
            writeLog(TAG + " :getRestoreTOsWithId(): Error while getting restore TOs", e);
        }
        return finalResult;
    }

    /**
     * Saves a recursive copy of the specified TOs where idFieldName matches idToMatch in the specified toInstances array for later undoing.
     * When undo is required, use getRestoreTOsWithId() with same idToMatch to restore.<br><br>
     * Example:<br>
     * abortUtil.backupTOsWithId(workorder.getWorkorderCustomTO().getWoUnits(), WoUnitCustomTO.ID_WO_UNIT_T, getIdWoUnitT());
     *
     * @param toInstances
     * @param idFieldName
     * @param idToMatch
     * @return
     */
    public boolean backupTOsWithId(Object toInstances, String idFieldName, Long idToMatch) {
        boolean result = false;
        if (toInstances != null) {
            try {
                if (toInstances instanceof List) {
                    List instances = (List) toInstances;

                    String methodBase = TransferObjectUtils.convertDbToJavaClassName(idFieldName);

                    Vector backups = new Vector();
                    Class clazz = null;
                    for (int i = 0; i < instances.size(); i++) {
                        clazz = instances.get(i).getClass();

                        Method getMethod = instances.get(i).getClass().getMethod("get" + methodBase);
                        Long id = (Long) getMethod.invoke(instances.get(i), new Object[]{});

                        // Make a backup copy if id match
                        if (id != null && id.equals(idToMatch)) {
                            Object copy = AndroidUtilsAstSep.copyObject(instances.get(i));
                            backups.addElement(copy);
                        }
                    }

                    abortTable.put(toInstances.getClass().getName() + idFieldName + idToMatch, (T) backups);
                    result = true;
                } else {
                    throw new Exception("backupTOsWithId(): Only list can be backed up with this method.");
                }
            } catch (Exception e) {
                writeLog(TAG + " :backupTOsWithId(): Error while backing up TOs", e);
            }
        }
        return result;
    }

    /**
     * Make a recursive backup of the specified TO instance(s)<br><br>
     * Example usages:<br>
     * abortUtil.backupTOs(woInstCustomTO.getUnits(), WoUnitCustomTO[].class);
     * abortUtil.backupTOs(woInstCustomTO, WoInstCustomTO.class);
     *
     * @param toInstances
     * @param clazz
     * @return
     */
    public boolean backupTOs(Object toInstances, Class clazz) {
        boolean success = false;

        try {
            Log.i("Inside AbortUtil: " + clazz.getName(), "inside backupTOs" + AndroidUtilsAstSep.copyObject(toInstances));
            abortTable.put(clazz.getName(), (T) AndroidUtilsAstSep.copyObject(toInstances));
            success = true;
        } catch (Exception e) {
            writeLog(TAG + " :backupMTO(): Error while backing up MTOs", e);
        }

        return success;
    }

    /**
     * Make a recursive backup of the specified TO instance(s)<br><br>
     * Example usages:<br>
     * abortUtil.backupTOs(woInstCustomTO, key);
     * Do not use this method to backup list
     *
     * @param toInstances
     * @param key
     * @return
     */
    public boolean backupTOs(Object toInstances, String key) {
        boolean success = false;

        try {
            abortTable.put(key, (T) AndroidUtilsAstSep.copyObject(toInstances));
            success = true;
        } catch (Exception e) {
            writeLog(TAG + " :backupMTO(): Error while backing up MTOs", e);
        }

        return success;
    }

    /**
     * Get a recursive copy of a backed up TO instance(s)<br><br>
     * Example usages:<br>
     * woInstCustomTO = (WoInstCustomTO) abortUtil.getRestoreTOs(WoInstCustomTO.class);<br>
     * woInstCustomTO.setUnits(((WoUnitCustomTO[]) abortUtil.getRestoreTOs(WoUnitCustomTO.class)));
     *
     * @param clazz
     * @return
     */
    public Object getRestoreTOs(Class clazz) {
        try {
            T abortValue = abortTable.get(clazz.getName());
            if (abortValue instanceof ArrayList) {
                ObjectMapper mapper = new ObjectMapper();
                List pojoList = new ArrayList();
                Iterator iterator = ((ArrayList) abortValue).iterator();
                while (iterator.hasNext()) {
                    T object = (T) mapper.convertValue(iterator.next(), clazz);
                    pojoList.add(object);
                }
                return AndroidUtilsAstSep.copyObject(pojoList);
            }
            return AndroidUtilsAstSep.copyObject(abortValue);
        } catch (Exception e) {
            writeLog(TAG + " :getRestoreTOs(): Error while restoring TOs", e);
        }
        return null;
    }


    /**
     * Get a recursive copy of a backed up TO instance(s)<br><br>
     * Example usages:<br>
     * woInstCustomTO = (WoInstCustomTO) abortUtil.getRestoreTOs(key);<br>
     * Do not use this method to restore list
     *
     * @param key
     * @return
     */
    public Object getRestoreTOs(String key) {
        try {
            T abortValue = abortTable.get(key);
            return AndroidUtilsAstSep.copyObject(abortValue);
        } catch (Exception e) {
            writeLog(TAG + " :getRestoreTOs(): Error while restoring TOs", e);
        }
        return null;
    }

}
