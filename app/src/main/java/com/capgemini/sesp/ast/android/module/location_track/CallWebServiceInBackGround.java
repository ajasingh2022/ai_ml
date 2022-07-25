package com.capgemini.sesp.ast.android.module.location_track;

import android.os.AsyncTask;

import com.capgemini.sesp.ast.android.module.communication.AndroidWebserviceClientAstSepImpl;
import com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CallWebServiceInBackGround extends AsyncTask {
    public ReturnValue returnValue;


    @Override
    protected Object doInBackground(Object[] objects) {
        //0-Methodname
        //2-n -arguments --odd index type, even index value

        Object valueReturn = null;

        List<Class> methodRefereParameters = new ArrayList<>();
        List<Object> methodInvokeParameters = new ArrayList<>();
        int i=1;
        while (i<objects.length){
            methodRefereParameters.add((Class) objects[i++]);
            methodInvokeParameters.add(objects[i++]);
        }

        try {
            AndroidWebserviceClientAstSepImpl androidWebserviceClientAstSep =
                    new AndroidWebserviceClientAstSepImpl();
            Method webServiceMethod = AndroidWebserviceClientAstSepImpl.class.getMethod((String) objects[0]
                    ,methodRefereParameters.toArray(new Class[methodRefereParameters.size()]));
            valueReturn = webServiceMethod.invoke(androidWebserviceClientAstSep,
                    methodInvokeParameters.toArray(new Object[methodInvokeParameters.size()]));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            valueReturn = e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            valueReturn = e;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            valueReturn = e;
        }catch (Exception e){
            SespLogHandler.writeLog(this.getClass().getName(),e);
            valueReturn = e;
        }

        return valueReturn;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        returnValue.onReturnResult(o);
    }

    public interface ReturnValue{
        Void onReturnResult(Object o);
    }
}
