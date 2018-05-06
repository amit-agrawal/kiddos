package com.hmi.kiddos.controllers;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
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

import com.hmi.kiddos.model.Staff;
import com.hmi.kiddos.model.UserRole;
import com.hmi.kiddos.model.enums.Centers;
import com.hmi.kiddos.model.enums.Department;
import com.hmi.kiddos.model.enums.Gender;

@RequestMapping("/staffs")
@Controller
public class StaffController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Staff staff, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, staff);
            return "staffs/create";
        }
        uiModel.asMap().clear();
        staff.persist();
        return "redirect:/staffs/" + encodeUrlPathSegment(staff.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Staff());
        return "staffs/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("staff", Staff.findStaff(id));
        uiModel.addAttribute("itemId", id);
        return "staffs/show";
    }

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "type", required = false) String types, Model uiModel) {
		if (types != null && types.equals("all")) {
            uiModel.addAttribute("staffs", Staff.findAllStaffs());
        }
		else {
            uiModel.addAttribute("staffs", Staff.findAllActiveStaffs());			
		}
        addDateTimeFormatPatterns(uiModel);
        return "staffs/list";
    }


	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Staff staff, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, staff);
            return "staffs/update";
        }
        uiModel.asMap().clear();
        staff.merge();
        return "redirect:/staffs/" + encodeUrlPathSegment(staff.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Staff.findStaff(id));
        return "staffs/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Staff staff = Staff.findStaff(id);
        staff.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "100" : size.toString());
        return "redirect:/staffs";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("staff_dob_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Staff staff) {
        uiModel.addAttribute("staff", staff);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("centerses", Arrays.asList(Centers.values()));
        uiModel.addAttribute("departments", Arrays.asList(Department.values()));
        uiModel.addAttribute("genders", Arrays.asList(Gender.values()));
        uiModel.addAttribute("userroles", UserRole.findAllUserRoles());
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
