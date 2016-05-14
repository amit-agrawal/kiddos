package com.hmi.kiddos.controllers;
import com.hmi.kiddos.model.Program;
import org.springframework.roo.addon.web.mvc.controller.annotations.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/programs")
@Controller
@RooWebScaffold(path = "programs", formBackingObject = Program.class)
public class ProgramController {
}
