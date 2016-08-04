/**
 * 
 */
package com.hmi.kiddos.controllers;

import java.util.Date;

import org.displaytag.decorator.MultilevelTotalTableDecorator;

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
public class LinksTableDecorator extends MultilevelTotalTableDecorator {
	{
		setGrandTotalDescription("&nbsp;Total Kids");    // optional, defaults to Grand Total
        setSubtotalLabel("&nbsp;", null);
	}
	
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
			link = link + " <a href=\"children/" + child.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
					+ "</a>";
		} else if (currentObj instanceof Admission) {
			Admission admission = (Admission) getCurrentRowObject();
			link = " <a href=\"admissions/" + admission.getId() + "\">"
					+ "<img title=\"Show Admission\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Admission\"/>"
					+ "</a>";
			link = link + " <a href=\"admissions/" + admission.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
					+ "</a>";
		} else if (currentObj instanceof Program) {
			Program program = (Program) getCurrentRowObject();
			link = " <a href=\"programs/" + program.getId() + "\">"
					+ "<img title=\"Show Program\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Program\"/>"
					+ "</a>";
			link = link + " <a href=\"programs/" + program.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
					+ "</a>";
		} else if (currentObj instanceof Payment) {
			Payment payment = (Payment) getCurrentRowObject();
			link = " <a href=\"payments/" + payment.getId() + "\">"
					+ "<img title=\"Show Payment\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Payment\"/>"
					+ "</a>";
			link = link + " <a href=\"payments/" + payment.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
					+ "</a>";
		} else if (currentObj instanceof Transportation) {
			Transportation transport = (Transportation) getCurrentRowObject();
			link = " <a href=\"transportations/" + transport.getId() + "\">"
					+ "<img title=\"Show Transportation\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Transportation\"/>"
					+ "</a>";
			link = link + " <a href=\"transportations/" + transport.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
					+ "</a>";
		} else if (currentObj instanceof Staff) {
			Staff staff = (Staff) getCurrentRowObject();
			link = " <a href=\"staffs/" + staff.getId() + "\">"
					+ "<img title=\"Show Staff\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show Staff\"/>"
					+ "</a>";
			link = link + " <a href=\"staffs/" + staff.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
					+ "</a>";
		} else if (currentObj instanceof UserRole) {
			UserRole userrole = (UserRole) getCurrentRowObject();
			link = " <a href=\"userroles/" + userrole.getId() + "\">"
					+ "<img title=\"Show UserRole\" src=\"resources/images/show.png\" class=\"image\" alt=\"Show UserRole\"/>"
					+ "</a>";
			link = link + " <a href=\"userroles/" + userrole.getId() + "?form\">"
					+ "<img title=\"Edit\" src=\"resources/images/update.png\" class=\"image\" alt=\"Edit\"/>"
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
