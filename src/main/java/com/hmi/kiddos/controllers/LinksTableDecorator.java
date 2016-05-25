/**
 * 
 */
package com.hmi.kiddos.controllers;

import java.util.Calendar;
import java.util.Date;

import org.displaytag.decorator.TableDecorator;

import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Child;
import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.Program;
import com.hmi.kiddos.model.Staff;
import com.hmi.kiddos.model.Transportation;
import com.hmi.kiddos.model.UserRole;

/**
 * @author Dell
 *
 */
public class LinksTableDecorator extends TableDecorator {

	public int getCounter() {
		return getListIndex() + 1;
	}

	public String getSecondLevelChildShowLink() {
		Child child = (Child) getCurrentRowObject();
		return " <a href=\"../children/" + child.getId() + "\">"
				+ "<img title=\"Show Child\" src=\"../resources/images/show.png\" class=\"image\" alt=\"Show Child\"/>"
				+ "</a>";
		// + " alt=${fn:escapeXml(show_label)}
		// title=${fn:escapeXml(show_label)}"> " +
		// " <img alt="${fn:escapeXml(show_label)}" class="image"
		// src="${show_image_url}" title="${fn:escapeXml(show_label)}" />
		// </a>

	}

	public String getShowLink() {
		Object currentObj = getCurrentRowObject();
		String link = "";
		if (currentObj instanceof Child) {
			Child child = (Child) getCurrentRowObject();
			link = " <a href=\"children/" + child.getId() + "\">"
					+ "<img title=\"Show Child\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Child\"/>"
					+ "</a>";
		} else if (currentObj instanceof Admission) {
			Admission admission = (Admission) getCurrentRowObject();
			link = " <a href=\"admissions/" + admission.getId() + "\">"
					+ "<img title=\"Show Admission\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Admission\"/>"
					+ "</a>";
		} else if (currentObj instanceof Program) {
			Program program = (Program) getCurrentRowObject();
			link = " <a href=\"programs/" + program.getId() + "\">"
					+ "<img title=\"Show Program\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Program\"/>"
					+ "</a>";
		} else if (currentObj instanceof Payment) {
			Payment payment = (Payment) getCurrentRowObject();
			link = " <a href=\"payments/" + payment.getId() + "\">"
					+ "<img title=\"Show Payment\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Payment\"/>"
					+ "</a>";
		} else if (currentObj instanceof Transportation) {
			Transportation transport = (Transportation) getCurrentRowObject();
			link = " <a href=\"transportations/" + transport.getId() + "\">"
					+ "<img title=\"Show Transportation\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Transportation\"/>"
					+ "</a>";
		} else if (currentObj instanceof Staff) {
			Staff staff = (Staff) getCurrentRowObject();
			link = " <a href=\"staffs/" + staff.getId() + "\">"
					+ "<img title=\"Show Staff\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Staff\"/>"
					+ "</a>";
		} else if (currentObj instanceof UserRole) {
			UserRole userroles = (UserRole) getCurrentRowObject();
			link = " <a href=\"userroles/" + userroles.getId() + "\">"
					+ "<img title=\"Show UserRole\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show UserRole\"/>"
					+ "</a>";
		}
		return link;
		// + " alt=${fn:escapeXml(show_label)}
		// title=${fn:escapeXml(show_label)}"> " +
		// " <img alt="${fn:escapeXml(show_label)}" class="image"
		// src="${show_image_url}" title="${fn:escapeXml(show_label)}" />
		// </a>

	}

	public Date getPaymentDateAsDate() {
		if (getCurrentRowObject() instanceof Payment) {
			Payment payment = (Payment) getCurrentRowObject();
			return payment.getPaymentDate().getTime();
		}
		else 
			return null;
    }

	/*
	 * public String getDeleteLink() { Child lObject= (Child)
	 * getCurrentRowObject(); int lId= lObject.getId();
	 * 
	 * return "\<a href=\"details.jsp?id=" + lId +
	 * "&amp;action=view\">View&lt;/a> | " + "&lt;a href=\"details.jsp?id=" +
	 * lId + "&amp;action=edit\">Edit\</a> | " + "\<a href=\"details.jsp?id=" +
	 * lId + "&amp;action=delete\">Delete\</a>"; }
	 */
}
