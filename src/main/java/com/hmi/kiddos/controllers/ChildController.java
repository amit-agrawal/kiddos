package com.hmi.kiddos.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.hmi.kiddos.dao.AdmissionDao;
import com.hmi.kiddos.dao.ChildDao;
import com.hmi.kiddos.model.Child;
import com.hmi.kiddos.model.Gender;
import com.hmi.kiddos.model.Transportation;

@RequestMapping("/children")
@Controller
public class ChildController {

	@Autowired
	private ChildDao childDao;
	
	@Autowired
	private AdmissionDao admissionDao;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Child child, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, child);
			return "children/create";
		}
		uiModel.asMap().clear();
		childDao.persist(child);
		return "redirect:/children/" + encodeUrlPathSegment(child.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Child());
		return "children/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("child", childDao.findChild(id));
		uiModel.addAttribute("itemId", id);
		return "children/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "type", required = false) String types, 
			Model uiModel) {
		if (types != null) {
			uiModel.addAttribute("children", childDao.findAllChildren(types));
		} else if (page != null || size != null) {
			int sizeNo = size == null ? 1000 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("children",
					childDao.findChildEntries(firstResult, sizeNo, sortFieldName, sortOrder, types));
			float nrOfPages = (float) childDao.countChildren() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("children", childDao.findAllChildren(sortFieldName, sortOrder, types));
		}
		addDateTimeFormatPatterns(uiModel);
		return "children/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Child child, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, child);
			return "children/update";
		}
		uiModel.asMap().clear();
		childDao.merge(child);
		return "redirect:/children/" + encodeUrlPathSegment(child.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, childDao.findChild(id));
		return "children/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Child child = childDao.findChild(id);
		childDao.remove(child);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/children";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("child_dob_date_format",
				DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, Child child) {
		uiModel.addAttribute("child", child);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("admissions", admissionDao.findAllAdmissions());
		uiModel.addAttribute("genders", Arrays.asList(Gender.values()));
        uiModel.addAttribute("pickupTransportations", Transportation.findAllActivePickupTransportations());
        uiModel.addAttribute("dropTransportations", Transportation.findAllActiveDropTransportations());
	}

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
