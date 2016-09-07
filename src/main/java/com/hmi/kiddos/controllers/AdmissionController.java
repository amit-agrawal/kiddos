package com.hmi.kiddos.controllers;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.Program;
import com.hmi.kiddos.model.Transportation;

@RequestMapping("/admissions")
@Controller
public class AdmissionController {
	
	@Autowired
	private ChildDao childDao;

	@Autowired
	private AdmissionDao admissionDao;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Admission admission, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, admission);
            return "admissions/create";
        }
        uiModel.asMap().clear();
        admission.persist();
        return "redirect:/admissions/" + encodeUrlPathSegment(admission.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Admission());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (childDao.countChildren() == 0) {
            dependencies.add(new String[] { "transportArrival", "children" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "admissions/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("admission", admissionDao.findAdmission(id));
        uiModel.addAttribute("itemId", id);
        return "admissions/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
/*        if (page != null || size != null) {
            int sizeNo = size == null ? 1000 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("admissions", admissionDao.findAdmissionEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) admissionDao.countAdmissions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
*/ 
		uiModel.addAttribute("admissions", admissionDao.findAllAdmissions(sortFieldName, sortOrder));
//        }
        addDateTimeFormatPatterns(uiModel);
        return "admissions/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Admission admission, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, admission);
            return "admissions/update";
        }
        uiModel.asMap().clear();
        admission.merge();
        return "redirect:/admissions/" + encodeUrlPathSegment(admission.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, admissionDao.findAdmission(id));
        return "admissions/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Admission admission = admissionDao.findAdmission(id);
        admission.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "1000" : size.toString());
        return "redirect:/admissions";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("admission_admissiondate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("admission_joiningdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Admission admission) {
        uiModel.addAttribute("admission", admission);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("children", childDao.findAllChildren());
        uiModel.addAttribute("programs", Program.findAllPrograms());
        uiModel.addAttribute("activePrograms", Program.findAllActivePrograms());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
