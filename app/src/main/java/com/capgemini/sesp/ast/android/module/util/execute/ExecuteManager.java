package com.capgemini.sesp.ast.android.module.util.execute;

import android.util.ArrayMap;

import com.skvader.rsp.cft.common.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public final class ExecuteManager {

    private final static ExecuteManager INSTANCE = new ExecuteManager();

    public static ExecuteManager getInstance() {
        return INSTANCE;
    }


    private final Map<ExecuteType, List<ExecuteTask>> tasks = new ArrayMap<ExecuteType, List<ExecuteTask>>();

    private ExecuteManager() {
    }

    public void registerTask(ExecuteTask task) {
        try {
            List<ExecuteTask> taskList = tasks.get(task.getExecuteType());
            if (taskList == null) {
                taskList = new ArrayList<ExecuteTask>();
                tasks.put(task.getExecuteType(), taskList);
            }
            taskList.add(task);
        } catch (Exception e) {
            writeLog("ExecuteManager : registerTask()", e);
        }

    }

    public void executeSafe(ExecuteType type) {
        try {
            execute(type);
        } catch (Exception e) {
            writeLog("ExecuteManager : executeSafe()", e);
        }
    }

    public void execute(ExecuteType type) throws Exception {
        try {
            for (ExecuteTask task : Utils.safeToList(tasks.get(type))) {
                task.run();
            }
        } catch (Exception e) {
            writeLog("ExecuteManager : executeSafe()", e);
        }
    }

}
