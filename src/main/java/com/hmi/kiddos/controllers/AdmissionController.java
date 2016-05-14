package com.hmi.kiddos.controllers;
import com.hmi.kiddos.model.Admission;
import org.springframework.roo.addon.web.mvc.controller.annotations.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admissions")
@Controller
@RooWebScaffold(path = "admissions", formBackingObject = Admission.class)
public class AdmissionController {
}
