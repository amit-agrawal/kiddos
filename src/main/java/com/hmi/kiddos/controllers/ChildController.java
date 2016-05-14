package com.hmi.kiddos.controllers;
import com.hmi.kiddos.model.Child;
import org.springframework.roo.addon.web.mvc.controller.annotations.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/children")
@Controller
@RooWebScaffold(path = "children", formBackingObject = Child.class)
public class ChildController {
}
