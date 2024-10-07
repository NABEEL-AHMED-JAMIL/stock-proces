package com.stock.process.service.impl;

import com.stock.process.repository.FileInfoRepository;
import com.stock.process.repository.StockPriceRepository;
import com.stock.process.service.StockPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Ahmed
 */
@Service
public class StockPriceServiceImpl implements StockPriceService {

    private Logger logger = LoggerFactory.getLogger(StockPriceServiceImpl.class);

    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private StockPriceRepository stockPriceRepository;

    public StockPriceServiceImpl() {}

}
