/*
    Copyright (c) 2012,2013 Mirco Attocchi
	
    This file is part of WebAppCommon.

    WebAppCommon is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WebAppCommon is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with WebAppCommon.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.webappcommon.lib.jsf;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * <p>JsfConverter class.</p>
 *
 * @author Mirco
 * @version $Id: $Id
 */
public class JsfConverter {

	/**
	 * Creates a new instance of JsfConverter
	 */
	public JsfConverter() {
	}

	/**
	 * Method that convert a <tt>List</tt> of <tt>Object</tt> to an
	 * <tt>ArrayList</tt> of <tt>SelectedItem</tt>.
	 *
	 * @param list a {@link java.util.List} object.
	 * @param itemValue a {@link java.lang.String} object.
	 * @param itemLabel a {@link java.lang.String} object.
	 * @return a {@link java.util.ArrayList} object.
	 * @throws java.lang.Exception if any.
	 */
	public static ArrayList<SelectItem> listToSelectItems(List list, String itemValue, String itemLabel) throws Exception {
		ArrayList<SelectItem> retVal = null;
		try {
			retVal = new ArrayList<SelectItem>();
			for (Object obj : list) {
				PropertyDescriptor pdValue = new PropertyDescriptor(itemValue, obj.getClass());
				Method methodValue = pdValue.getReadMethod();
				Object methodValueRes = methodValue.invoke(obj);

				PropertyDescriptor pdLabel = new PropertyDescriptor(itemLabel, obj.getClass());
				Method methodLabel = pdLabel.getReadMethod();
				Object methodLabelRes = methodLabel.invoke(obj);
				if (methodValueRes == null) {
					methodValueRes = "";
				}
				if (methodLabelRes == null) {
					methodLabelRes = "";
				}
				retVal.add(new SelectItem(methodValueRes.toString(), methodLabelRes.toString()));

				pdValue = null;
				methodValue = null;
				methodValueRes = null;

				pdLabel = null;
				methodLabel = null;
				methodLabelRes = null;
			}
		} catch (Exception e) {
			throw e;
		}
		return retVal;
	}

	/**
	 * Method that convert a <tt>List</tt> of <tt>Object</tt> to an
	 * <tt>ArrayList</tt> of <tt>SelectedItem</tt>. This method allow to
	 * concatenate more than one Object Property.
	 *
	 * @param list a {@link java.util.List} object.
	 * @param itemValue a {@link java.lang.String} object.
	 * @param itemLabel an array of {@link java.lang.String} objects.
	 * @param separatorForLabel a {@link java.lang.String} object.
	 * @return a {@link java.util.ArrayList} object.
	 * @throws java.lang.Exception if any.
	 */
	public static ArrayList<SelectItem> listToSelectItems(List list, String itemValue, String[] itemLabel, String separatorForLabel) throws Exception {
		ArrayList<SelectItem> retVal = null;
		StringBuilder sumLabel = null;

		try {
			retVal = new ArrayList<SelectItem>();
			for (Object obj : list) {

				PropertyDescriptor pdValue = new PropertyDescriptor(itemValue, obj.getClass());
				Method methodValue = pdValue.getReadMethod();
				Object methodValueRes = methodValue.invoke(obj);

				sumLabel = new StringBuilder();
				for (String str : itemLabel) {
					PropertyDescriptor pdLabel = new PropertyDescriptor(str, obj.getClass());
					Method methodLabel = pdLabel.getReadMethod();
					Object methodLabelRes = methodLabel.invoke(obj);
					if (methodLabelRes == null) {
						methodLabelRes = "";
					}
					if (sumLabel.length() > 0) {
						sumLabel.append(separatorForLabel);
						sumLabel.append(methodLabelRes.toString());
					} else {
						sumLabel.append(methodLabelRes.toString());
					}

					str = null;

					pdLabel = null;
					methodLabel = null;
					methodLabelRes = null;
				}
				if (methodValueRes == null) {
					methodValueRes = "";
				}

				retVal.add(new SelectItem(methodValueRes.toString(), sumLabel.toString()));

				sumLabel = null;

				pdValue = null;
				methodValue = null;
				methodValueRes = null;
			}
		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}

	/**
	 * Method that convert a <tt>List</tt> of <tt>Object</tt> to an
	 * <tt>ArrayList</tt> of <tt>SelectedItem</tt>.
	 *
	 * @param list a {@link java.util.List} object.
	 * @param itemLabel a {@link java.lang.String} object.
	 * @return a {@link java.util.ArrayList} object.
	 * @throws java.lang.Exception if any.
	 */
	public static ArrayList<SelectItem> listToSelectItems(List list, String itemLabel) throws Exception {
		ArrayList<SelectItem> retVal = null;
		try {
			retVal = new ArrayList<SelectItem>();
			for (Object obj : list) {
				// PropertyDescriptor pdValue = new
				// PropertyDescriptor(itemValue, obj.getClass());
				// Method methodValue = pdValue.getReadMethod();
				// Object methodValueRes = methodValue.invoke(obj);

				PropertyDescriptor pdLabel = new PropertyDescriptor(itemLabel, obj.getClass());
				Method methodLabel = pdLabel.getReadMethod();
				Object methodLabelRes = methodLabel.invoke(obj);
				// if (methodValueRes == null) {
				// methodValueRes = "";
				// }
				if (methodLabelRes == null) {
					methodLabelRes = "";
				}
				retVal.add(new SelectItem(obj, methodLabelRes.toString()));

				// pdValue = null;
				// methodValue = null;
				// methodValueRes = null;

				pdLabel = null;
				methodLabel = null;
				methodLabelRes = null;
			}
		} catch (Exception e) {
			throw e;
		}
		return retVal;
	}

	/**
	 * Method that convert a <tt>List</tt> of <tt>Object</tt> to an
	 * <tt>ArrayList</tt> of <tt>SelectedItem</tt>. This method allow to
	 * concatenate more than one Object Property.
	 *
	 * @param list a {@link java.util.List} object.
	 * @param itemLabel an array of {@link java.lang.String} objects.
	 * @param separatorForLabel a {@link java.lang.String} object.
	 * @return a {@link java.util.ArrayList} object.
	 * @throws java.lang.Exception if any.
	 */
	public static ArrayList<SelectItem> listToSelectItems(List list, String[] itemLabel, String separatorForLabel) throws Exception {
		ArrayList<SelectItem> retVal = null;
		StringBuilder sumLabel = null;

		try {
			retVal = new ArrayList<SelectItem>();
			for (Object obj : list) {

				sumLabel = new StringBuilder();
				for (String str : itemLabel) {
					PropertyDescriptor pdLabel = new PropertyDescriptor(str, obj.getClass());
					Method methodLabel = pdLabel.getReadMethod();
					Object methodLabelRes = methodLabel.invoke(obj);
					if (methodLabelRes == null) {
						methodLabelRes = "";
					}
					if (sumLabel.length() > 0) {
						sumLabel.append(separatorForLabel);
						sumLabel.append(methodLabelRes.toString());
					} else {
						sumLabel.append(methodLabelRes.toString());
					}

					str = null;

					pdLabel = null;
					methodLabel = null;
					methodLabelRes = null;
				}

				retVal.add(new SelectItem(obj, sumLabel.toString()));

				sumLabel = null;

			}
		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}

	/**
	 * <p>getSelectOptionFromText.</p>
	 *
	 * @param text a {@link java.lang.String} object.
	 * @param splitter a {@link java.lang.String} object.
	 * @return a {@link java.util.ArrayList} object.
	 * @throws java.lang.Exception if any.
	 */
	public static ArrayList<SelectItem> getSelectOptionFromText(String text, String splitter) throws Exception {
		ArrayList<SelectItem> returnValue = null;
		String[] valori = null;
		String string = null;
		try {
			if (text != null) {
				// valori = text.split("\n");
				valori = text.split(splitter);
			}
			returnValue = new ArrayList<SelectItem>();
			for (int i = 0; i < valori.length; i++) {
				string = valori[i];
				if (string != null && string.length() > 1) {
					string = string.trim();
					returnValue.add(new SelectItem(string, string));
				}
			}
		} catch (Exception exception) {
			throw exception;
		} finally {
			valori = null;
			string = null;
		}
		return returnValue;

	}

}
