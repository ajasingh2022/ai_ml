package com.capgemini.sesp.ast.android.ui.activity.material_list;

import com.capgemini.sesp.ast.android.module.others.WorkorderLiteTO;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.MaterialListKeys;

import java.util.Comparator;
import java.util.List;

class MateriaTypeCategory {
	private String detail1;
	private String detail2;
	private List<WorkorderLiteTO> workorders;
	private int count;

	public String getDetail1() {
		return detail1;
	}

	public void setDetail1(String detail1) {
		this.detail1 = detail1;
	}

	public String getDetail2() {
		return detail2;
	}

	public void setDetail2(String detail2) {
		this.detail2 = detail2;
	}

	public List<WorkorderLiteTO> getWorkorders() {
		return workorders;
	}

	public void setWorkorders(List<WorkorderLiteTO> workorders) {
		this.workorders = workorders;
	}

	public int getAmount() {
		return workorders.size();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static class SortMaterialList implements Comparator<MateriaTypeCategory> {
		private int mod = 1;

		MaterialListKeys sortOnAttribute = MaterialListKeys.UNIT_TYPE;

		public SortMaterialList(boolean isDesc, MaterialListKeys sortOnAttribute) {
			this.sortOnAttribute = sortOnAttribute;
			if (isDesc)
				mod = -1;
		}

		@Override
		public int compare(MateriaTypeCategory obj1, MateriaTypeCategory obj2) {
			switch (sortOnAttribute) {
			case UNIT_TYPE:
			case KEY_NUMBER:
				if (obj1.detail1 == null && obj2.detail1 == null)
					return mod * 0;
				else if (obj1.detail1 == null)
					return mod * -1;
				else if (obj2.detail1 == null)
					return mod * 1;
				else
					return mod * obj1.detail1.compareTo(obj2.detail1);
			case UNIT_MODEL:
			case KEY_INFO:
				if (obj1.detail2 == null && obj2.detail2 == null)
					return mod * 0;
				else if (obj1.detail2 == null)
					return mod * -1;
				else if (obj2.detail2 == null)
					return mod * 1;
				else
					return mod * obj1.detail2.compareTo(obj2.detail2);


			case UNIT_COUNT:
				if (obj1.getCount() == 0 && obj2.getCount() == 0)
					return mod * 0;
				else if (obj1.getCount() == 0)
					return mod * -1;
				else if (obj2.getCount() == 0)
					return mod * 1;
				else
					return mod * ("" + obj1.getCount()).compareTo("" + obj2.getCount());
				
			case KEY_COUNT:
				if (obj1.getAmount() == 0 && obj2.getAmount() == 0)
					return mod * 0;
				else if (obj1.getAmount() == 0)
					return mod * -1;
				else if (obj2.getAmount() == 0)
					return mod * 1;
				else
					return mod * ("" + obj1.getAmount()).compareTo("" + obj2.getAmount());				

			default:
				if (obj1.detail1 == null && obj2.detail1 == null)
					return mod * 0;
				else if (obj1.detail1 == null)
					return mod * -1;
				else if (obj2.detail1 == null)
					return mod * 1;
				else
					return mod * obj1.detail1.compareTo(obj2.detail1);
			}

		}

	}

}
