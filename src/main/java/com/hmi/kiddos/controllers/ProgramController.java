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
import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Centers;
import com.hmi.kiddos.model.Program;
import com.hmi.kiddos.model.Staff;

@RequestMapping("/programs")
@Controller
public class ProgramController {
	@Autowired
	private AdmissionDao admissionDao;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Program program, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, program);
			return "programs/create";
		}
		uiModel.asMap().clear();
		program.persist();
		return "redirect:/programs/" + encodeUrlPathSegment(program.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Program());
		return "programs/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("program", Program.findProgram(id));
		uiModel.addAttribute("itemId", id);
		return "programs/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "type", required = false) String types, Model uiModel) {
		if (types != null) {
			uiModel.addAttribute("programs", Program.findAllPrograms(types));
		} else if (page != null || size != null) {
			int sizeNo = size == null ? 100 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("programs", Program.findProgramEntries(firstResult, sizeNo, sortFieldName, sortOrder));
			float nrOfPages = (float) Program.countPrograms() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("programs", Program.findAllPrograms(sortFieldName, sortOrder));
		}
		addDateTimeFormatPatterns(uiModel);
		return "programs/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Program program, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, program);
			return "programs/update";
		}
		uiModel.asMap().clear();
		program.merge();
		return "redirect:/programs/" + encodeUrlPathSegment(program.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, Program.findProgram(id));
		return "programs/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Program program = Program.findProgram(id);
		program.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/programs";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("program_duedate_date_format",
				DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, Program program) {
		uiModel.addAttribute("program", program);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("admissions", admissionDao.findAllAdmissions());
		uiModel.addAttribute("centerses", Arrays.asList(Centers.values()));
		uiModel.addAttribute("staffs", Staff.findAllStaffs());
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
