package com.capgemini.sesp.ast.android.module.util;

import com.skvader.rsp.cft.common.to.TransferObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class OdvUtil {

    /**
     * TODO: Add wrappers for the ODV functions in other util classes?
     */

    public static class Odv implements Serializable {
        private Object o;
        private Object d;
        private Long v;

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }

        public Object getD() {
            return d;
        }

        public void setD(Object d) {
            this.d = d;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

    }

    public static String getBaseFieldName(String fieldName) {
        boolean dstOrgDivType = (fieldName.toLowerCase().endsWith("_d") || fieldName.toLowerCase().endsWith("_v") || fieldName.toLowerCase().endsWith("_o"));
        if (dstOrgDivType) {
            return fieldName.substring(0, fieldName.length() - 2);
        }

        return fieldName;
    }

    public static Odv getODVValues(Object toInstance, String fieldName) {
        Odv odv = null;
        try {
            String baseFieldName = getBaseFieldName(fieldName);
            String methodBase = TransferObjectUtils.convertDbToJavaClassName(baseFieldName);

            if (!fieldName.equals(baseFieldName)) {
                odv = new Odv();

                Method getMethodD = toInstance.getClass().getMethod("get" + methodBase + "O");
                odv.setO(getMethodD.invoke(toInstance));

                Method getMethodO = toInstance.getClass().getMethod("get" + methodBase + "D");
                odv.setD(getMethodO.invoke(toInstance));

                Method getMethodV = toInstance.getClass().getMethod("get" + methodBase + "V");
                odv.setV((Long) getMethodV.invoke(toInstance, new Object[]{}));
            }
        } catch (Exception e) {
            writeLog("OdvUtil : getODVValues()", e);
        }
        return odv;
    }

    public static void setODVValues(Object toInstance, String fieldName, Odv odv) {
        try {
            String baseFieldName = getBaseFieldName(fieldName);
            String methodBase = TransferObjectUtils.convertDbToJavaClassName(baseFieldName);
            Method getMethod = toInstance.getClass().getMethod("get" + methodBase + "O", null);

            Class[] typeParams = new Class[]{getMethod.getReturnType()};
            Method dstSetMethod = toInstance.getClass().getMethod("set" + methodBase + "O", typeParams);
            dstSetMethod.invoke(toInstance, odv.getO());
            dstSetMethod = toInstance.getClass().getMethod("set" + methodBase + "D", typeParams);
            dstSetMethod.invoke(toInstance, odv.getD());
            dstSetMethod = toInstance.getClass().getMethod("set" + methodBase + "V", Long.class);
            dstSetMethod.invoke(toInstance, odv.getV());

        } catch (Exception e) {
            writeLog("OdvUtil : setODVValues()", e);
        }

    }
}
