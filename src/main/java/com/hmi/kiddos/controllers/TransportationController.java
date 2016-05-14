package com.hmi.kiddos.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hmi.kiddos.model.Transportation;
import org.springframework.roo.addon.web.mvc.controller.annotations.scaffold.RooWebScaffold;

@RequestMapping("/transportations")
@Controller
@RooWebScaffold(path = "transportations", formBackingObject = Transportation.class)
public class TransportationController {
}
