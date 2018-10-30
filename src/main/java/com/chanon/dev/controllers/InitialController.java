package com.chanon.dev.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chanon.dev.beans.ReqCurrentRate;
import com.chanon.dev.daos.ExRateRepository;
import com.chanon.dev.services.ExRateService;


@Controller
public class InitialController {
	private static final Logger logger = LogManager.getLogger();
	@Autowired
	private ExRateService exRateService;
	@RequestMapping("/")
	public String index() {
		ReqCurrentRate req = new ReqCurrentRate();
		req.setDate("05012017");
		req.setRound("1");
		String result = exRateService.currentRate(req);
		
		logger.debug("Debugging log: "+result);
		logger.info("Info log");
		logger.warn("Hey, This is a warning!");
		logger.error("Oops! We have an Error. OK");
		logger.fatal("Damn! Fatal error. Please fix me.");
		return "index";
	}
}
