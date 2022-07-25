package com.capgemini.sesp.ast.android.module.util.execute;


public abstract class ExecuteTask {
    private final ExecuteType type;
    
    public ExecuteTask(ExecuteType type) {
    	this.type = type;
    }
    
    public ExecuteType getExecuteType() {
    	return type;
    }

	public abstract void run() throws Exception;

}
