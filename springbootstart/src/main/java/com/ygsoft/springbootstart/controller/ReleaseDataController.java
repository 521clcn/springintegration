package com.ygsoft.springbootstart.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ygsoft.springbootstart.entity.ReleaseData;
import com.ygsoft.springbootstart.entity.RemainData;
import com.ygsoft.springbootstart.service.ReleaseDataService;
import com.ygsoft.springbootstart.util.DateUtil;

@RestController
@RequestMapping("/rest/releaseData")
public class ReleaseDataController {
	@Autowired
	private ReleaseDataService releaseDataService;
	
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Map<String, Object> nodata(
    		@RequestParam("releaseDate") String releaseDate,
    		@RequestParam("flag") Integer flag,
    		@RequestParam("amount") Double amount,
    		@RequestParam("expireDay") Integer expireDay,
    		@RequestParam("rate") Double rate,
    		@RequestParam("type") String type) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
        	Date startDate = format.parse(releaseDate);
    		Date endDate = DateUtil.getCentainDateByNum(startDate, expireDay);
    		if(flag<1) {
    			amount= -amount;
    		}
        	ReleaseData data = new ReleaseData();
        	data.setReleaseDate(startDate);
        	data.setExpireDay(expireDay);
        	data.setAmount(new BigDecimal(amount));
        	data.setExpireDate(endDate);
        	data.setFlag(flag);
        	data.setRate(rate);
        	data.setType(type);
        	releaseDataService.save(data);
        	
        	releaseDataService.saveDailySum(startDate);
        	
            result.put("suc", "yes");
            result.put("data", "success");
        } catch (Exception e) {
            result.put("suc", "no");
            result.put("msg", "error");
            e.printStackTrace();
        }

        return result;
    }
    
    @RequestMapping(value = "/analyse", method = RequestMethod.POST)
    public Map<String, Object> analyse() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
        	List<RemainData> list = releaseDataService.analyse();
        	List<String> dateList = new ArrayList<String>();
        	List<BigDecimal> amountList = new ArrayList<BigDecimal>();
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        	for(RemainData data : list) {
        		dateList.add(format.format(data.getDate()));
        		amountList.add(data.getAmount());
        	}
        	
            result.put("suc", "yes");
            result.put("dateList", dateList);
            result.put("amountList", amountList);
        } catch (Exception e) {
            result.put("suc", "no");
            result.put("msg", "error");
            e.printStackTrace();
        }

        return result;
    }
    
    @RequestMapping(value = "/init")
    public Map<String, Object> init(@RequestParam("date") String date) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
        	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        	releaseDataService.init(format.parse(date));
            result.put("suc", "yes");
            result.put("data", "success");
        } catch (Exception e) {
            result.put("suc", "no");
            result.put("msg", "error");
            e.printStackTrace();
        }

        return result;
    }

}
