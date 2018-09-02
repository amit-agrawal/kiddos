package com.hmi.kiddos.service;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.hmi.kiddos.dao.AdmissionDao;
import com.hmi.kiddos.model.Admission;

@Path("admission")
public class AdmissionService extends RestServiceBase {

    @GET
    @Path("getAll")	
    public Response getAll() {
		return resourceResponse(admissionDao.findAllAdmissions());
    }
    
    @GET
    @Path("getFirst")	
	@Produces(MediaType.APPLICATION_JSON)
    public Admission getFirst() {
		return admissionDao.findAdmission(1L);
    }

    @GET
    @Path("get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    public Admission get(@PathParam("id") long id) {
        return admissionDao.findAdmission(id);
    }

/*	@Autowired
	private ChildDao childDao;

	@Autowired
	private ProgramDao programDao;
*/
	@Autowired
	private AdmissionDao admissionDao;

	/*
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Admission admission, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
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
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "type", required = false) String types, Model uiModel) {
		if (types != null) {
			uiModel.addAttribute("admissions", admissionDao.findAllAdmissions(types));
		} else {
			uiModel.addAttribute("admissions", admissionDao.findAllAdmissions(sortFieldName, sortOrder));
		}
		addDateTimeFormatPatterns(uiModel);
		return "admissions/list";
	}

	@RequestMapping(produces = "text/json")
	public List<Admission> getAll() {
		return admissionDao.findAllAdmissions();
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Admission admission, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
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
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Admission admission = admissionDao.findAdmission(id);
		admission.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "1000" : size.toString());
		return "redirect:/admissions";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("admission_admissiondate_date_format",
				DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
		uiModel.addAttribute("admission_joiningdate_date_format",
				DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, Admission admission) {
		uiModel.addAttribute("admission", admission);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("children", childDao.findAllChildren());
		uiModel.addAttribute("programs", programDao.findAllPrograms());
		uiModel.addAttribute("activePrograms", programDao.findCurrentFuturePrograms());
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
	*/

}
