package com.chanon.dev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chanon.dev.beans.ReqCurrentRate;
import com.chanon.dev.daos.ExRateRepository;

@Service
public class ExRateServiceImpl implements ExRateService {

    private ExRateRepository exRateRepository;

    @Autowired
    public ExRateServiceImpl(ExRateRepository exRateRepository) {
		this.exRateRepository = exRateRepository;
	}

	@Override
	public String currentRate(ReqCurrentRate req) {
		return exRateRepository.currentRate(req);
	}
}
