package com.hmi.kiddos.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.hmi.kiddos.dao.ChildDao;
import com.hmi.kiddos.dao.ProgramDao;
import com.hmi.kiddos.model.Admission;
import com.hmi.kiddos.model.Child;
import com.hmi.kiddos.model.Payment;
import com.hmi.kiddos.model.PaymentMedium;
import com.hmi.kiddos.model.Program;
import com.hmi.kiddos.util.MailingAspect;

@RequestMapping("/payments")
@Controller
public class PaymentController {
	@Autowired
	private ChildDao childDao;

	@Autowired
	private ProgramDao programDao;

	@RequestMapping(value = "/getPrograms/{id}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, String> getPrograms(@PathVariable("id") Long id, Model uiModel) {
		HashMap<String, String> stateMap = new HashMap<String, String>();
		// put your logic to add state on basis of country
		Child child = childDao.findChild(id);
		Set<Program> programs = new TreeSet<Program>();
		if (child != null)
			programs = child.getCurrentOrFuturePrograms();
		uiModel.addAttribute("childPrograms", programs);
		return stateMap;
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Payment payment, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, payment);
			return "payments/create";
		}
		uiModel.asMap().clear();
		payment.persist();

		createPaymentRelatedAdmissions(payment);

		return "redirect:/payments/" + encodeUrlPathSegment(payment.getId().toString(), httpServletRequest);
	}

	private void createPaymentRelatedAdmissions(Payment payment) {
		Set<Program> programs = new TreeSet<Program>();
		programs.addAll(payment.getDaycarePrograms());
		programs.addAll(payment.getOtherPrograms());
		programs.addAll(payment.getPreschoolPrograms());
		programs.addAll(payment.getCharges());
		
		payment.setPrograms(programs);
		
		for (Program program : programs) {
			if (!program.isCharge()) {
				Child child = payment.getChild();
				Admission admission = new Admission();
				admission.setChild(child);
				admission.setProgram(program);
				admission.setAdmissionDate(Calendar.getInstance());

				try {
					admission.persist();
				} catch (Exception ex) {
					Logger.getLogger(PaymentController.class).error("Exception while creating admission: " + admission,
							ex);
				}
			}
		}
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Payment());
		return "payments/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("payment", Payment.findPayment(id));
		uiModel.addAttribute("itemId", id);
		return "payments/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "sortFieldName", required = false) String sortFieldName,
			@RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("payments", Payment.findPaymentEntries(firstResult, sizeNo, sortFieldName, sortOrder));
			float nrOfPages = (float) Payment.countPayments() / sizeNo;
			uiModel.addAttribute("maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("payments", Payment.findAllPayments(sortFieldName, sortOrder));
		}
		addDateTimeFormatPatterns(uiModel);
		return "payments/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Payment payment, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, payment);
			return "payments/update";
		}
		uiModel.asMap().clear();
		payment.merge();

		createPaymentRelatedAdmissions(payment);

		return "redirect:/payments/" + encodeUrlPathSegment(payment.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, Payment.findPayment(id));
		return "payments/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Payment payment = Payment.findPayment(id);
		payment.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/payments";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("payment_paymentdate_date_format",
				DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, Payment payment) {
		uiModel.addAttribute("payment", payment);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("children", childDao.findAllChildren());
		uiModel.addAttribute("programs", programDao.findAllPrograms());
		uiModel.addAttribute("activePreschoolPrograms", programDao.findCurrentFuturePreschoolPrograms());
		uiModel.addAttribute("activeDaycarePrograms", programDao.findCurrentFutureDaycarePrograms());
		uiModel.addAttribute("activeCharges", programDao.findCurrentFutureCharges());
		uiModel.addAttribute("activeOtherPrograms", programDao.findCurrentFutureOtherPrograms());
		uiModel.addAttribute("paymentmediums", Arrays.asList(PaymentMedium.values()));
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
