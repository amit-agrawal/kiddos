package com.hmi.kiddos.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Program;
import com.hmi.kiddos.model.Transportation;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.annotations.scaffold.RooWebScaffold;

@RequestMapping("/transportations")
@Controller
@RooWebScaffold(path = "transportations", formBackingObject = Transportation.class)
public class TransportationController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Transportation transportation, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, transportation);
            return "transportations/create";
        }
        uiModel.asMap().clear();
        transportation.persist();
        return "redirect:/transportations/" + encodeUrlPathSegment(transportation.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Transportation());
        return "transportations/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("transportation", Transportation.findTransportation(id));
        uiModel.addAttribute("itemId", id);
        return "transportations/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, 
    		@RequestParam(value = "size", required = false) Integer size, 
    		@RequestParam(value = "sortFieldName", required = false) String sortFieldName, 
    		@RequestParam(value = "sortOrder", required = false) String sortOrder, 
			@RequestParam(value = "type", required = false) String types, Model uiModel) {
		if (types != null) {
			uiModel.addAttribute("transportations", Transportation.findAllTransports(types));
		} else if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("transportations", Transportation.findTransportationEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Transportation.countTransportations() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("transportations", Transportation.findAllTransportations(sortFieldName, sortOrder));
        }
        return "transportations/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Transportation transportation, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, transportation);
            return "transportations/update";
        }
        uiModel.asMap().clear();
        transportation.merge();
        return "redirect:/transportations/" + encodeUrlPathSegment(transportation.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Transportation.findTransportation(id));
        return "transportations/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Transportation transportation = Transportation.findTransportation(id);
        transportation.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/transportations";
    }

	void populateEditForm(Model uiModel, Transportation transportation) {
        uiModel.addAttribute("transportation", transportation);
        uiModel.addAttribute("admissions", Admission.findAllAdmissions());
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
