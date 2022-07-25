package com.capgemini.sesp.ast.android.module.util.to;

import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.skvader.rsp.cft.common.to.custom.base.IdDomainTO;
import com.skvader.rsp.cft.common.to.custom.base.InfoInterfaceTO;
import com.skvader.rsp.cft.common.to.custom.base.NameInterfaceTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Helper object when you want to wrap data. The <code>toString</code> method
 * returns the name of this object.
 * 
 * @author avallda
 * see ListItem
 */
public class DisplayItem<T extends NameInterfaceTO & InfoInterfaceTO> implements NameInterfaceTO, InfoInterfaceTO, Comparable<DisplayItem<T>> {
	private String name;
    private String info;
	private T userObject;

	public DisplayItem(){}

	public DisplayItem(T userObject) {
        this(userObject.getName(), userObject.getInfo(), userObject);
    }

    public DisplayItem(String name, T userObject) {
        this(name, null, userObject);
    }

    public DisplayItem(String name, String info, T userObject) {
        this.name = name;
        this.info = info;
        this.userObject = userObject;

		if(userObject instanceof NameInterfaceTO) {
			NameInterfaceTO nameInterfaceTO = userObject;
			if(this.name == null) {
				this.name = nameInterfaceTO.getName();
			}
		}
		if(userObject instanceof InfoInterfaceTO) {
			InfoInterfaceTO infoInterfaceTO = userObject;
			if(this.info == null) {
				this.info = infoInterfaceTO.getInfo();
			}
		}

        if(this.name == null && userObject != null) {
            this.name = userObject.getName();
        }
    }

	/**
	 * @return Always returns <code>null</code>
	 */
	@Override
	public Long getId() {
		return userObject.getId();
	}

	/**
	 * @param id Input is ignored
	 * @deprecated Use user object for this purpose.
	 */
	@Override
	@Deprecated
	public void setId(Long id) {
	}

	@Override
    public String getName() {
        return name;
    }

	@Override
	public void setName(String name) {
		this.name = name;
	}


	@Override
    public String getInfo() {
        return info;
    }

	@Override
	public void setInfo(String info) {
		this.info = info;
	}

    public T getUserObject() {
        return userObject;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DisplayItem) {
			DisplayItem listItem = (DisplayItem)o;
			if(userObject != null) {
				return userObject.equals(listItem.userObject);
			} else {
				return listItem.userObject == null && (name == null ? listItem.name == null : name.equals(listItem.name));
			}
        }
        return false;
    }

	@Override
	public int hashCode() {
		if(userObject != null) {
			return userObject.hashCode();
		} else if(name != null){
			return name.hashCode();
		} else {
			return -1;
		}
	}

    @Override
	@SuppressWarnings("unchecked")
    public int compareTo(DisplayItem<T> item) {
		if(userObject == null && item.userObject == null) {
			if(name != null) {
				return name.compareTo(item.name);
			} else { //if(item.name == null) {
				return 0;
			}
		} else if(userObject == null) {
			return -1;
		} else if(userObject instanceof Comparable) {
			Comparable comparable = (Comparable)userObject;
			return comparable.compareTo(item.userObject);
		} else if(item.userObject == null){
			return 1;
		} else if(item.userObject instanceof Comparable) {
			Comparable comparable = (Comparable)item.userObject;
			return comparable.compareTo(userObject);
		} else {
			if(name != null) {
				return name.compareTo(item.name);
			} else { //if(item.name == null) {
				return 0;
			}
		}
    }
    
    public static <T extends NameInterfaceTO & InfoInterfaceTO> List<DisplayItem<T>> getDisplayItems(List<T> items, Long idContextDomain) {
		List<DisplayItem<T>> displayItems = null;
		try{

		if(items == null)
    		return null;
    	if(items.size() == 0)
    		return new ArrayList<DisplayItem<T>>(0);
    	 displayItems = new ArrayList<DisplayItem<T>>(items.size());
    	Set<Long> idDomains = AndroidUtilsAstSep.getVisibleDomains(idContextDomain);
    	
    	for(T item : items) {
    		if(item instanceof IdDomainTO) {
    			Long idDomain = ((IdDomainTO) item).getIdDomain();
    			if(idContextDomain == null || idDomain == null || idDomains.contains(idDomain)) {
    				//OK
    			} else {
    				continue;
    			}
    		}
    		DisplayItem<T> displayItem = new DisplayItem<T>(item);
    		displayItems.add(displayItem);
    	}
		} catch (Exception e) {
			writeLog("DisplayItem : getDisplayItems()", e);
		}
			return displayItems;
    }
}
