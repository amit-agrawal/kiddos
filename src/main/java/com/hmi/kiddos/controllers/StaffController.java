package com.hmi.kiddos.controllers;
import com.hmi.kiddos.model.Staff;
import org.springframework.roo.addon.web.mvc.controller.annotations.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/staffs")
@Controller
@RooWebScaffold(path = "staffs", formBackingObject = Staff.class)
public class StaffController {
}
