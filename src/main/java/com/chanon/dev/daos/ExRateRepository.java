package com.chanon.dev.daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chanon.dev.beans.ExCounterRate;
import com.chanon.dev.beans.ExCounterRateItem;
import com.chanon.dev.beans.ExRoundItem;
import com.chanon.dev.beans.ReqCurrentRate;
import com.chanon.dev.response.ResCurrentRate;
import com.google.gson.Gson;

@Repository
public class ExRateRepository {
	private static final Logger logger = LogManager.getLogger();

	private SimpleDateFormat ddMMyyyy = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat ddMMyyyyhh = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public ExRateRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String currentRate(ReqCurrentRate req) {
		String result = null;
		List<ExCounterRateItem> itemList = new ArrayList<>();
		List<ExRoundItem> roundList = new ArrayList<>();

		ExCounterRate exCounterRate = new ExCounterRate();
		ResCurrentRate res = new ResCurrentRate();

		String date_str = "";
		String round_str = "";
		String listround_str = "";

		if (req != null && req.getDate() != null && req.getDate().trim().length() > 0) {
			date_str += " and ex_date  >=  to_date( '" + req.getDate() + "' ,'ddmmyyyy') " + " and ex_date  <  to_date( '" + req.getDate() + "' ,'ddmmyyyy')+1 ";

			listround_str = date_str;

			if (req.getRound() != null && req.getRound().trim().length() > 0) {
				round_str += " and ex_round = " + req.getRound();
			} else {
				round_str += " and ex_round = (select  max(ex_round) as ex_round  " + " from excdba.counter_rate  where 1=1   " + " and ex_date  >=  to_date('" + req.getDate() + "','ddmmyyyy') and ex_date  < to_date('" + req.getDate() + "','ddmmyyyy')+1 " + " and ex_type != 'B' )";
			}

		} else {
			date_str += " and ex_date  = (select max(ex_date) as ex_date  from excdba.counter_rate  where 1=1  and ex_type != 'B' )  ";
			listround_str = " and to_char(ex_date,'ddmmyyyy')  = ( select to_char(max(ex_date),'ddmmyyyy') as ex_date  from excdba.counter_rate  where 1=1  and ex_type != 'B' )  ";
		}

		String sql = " select  " + "    to_char(ex_date,'dd/mm/yyyy HH24:MI') exdate , to_char(ex_date,'HH24:MI') time " + "    ,EX_CURR currencyCode " + "    ,currency_name currencyName " + "    ,EX_TYPE extype" + "    ,EX_ROUND round " + "    ,case when instr(set3.cfg_value1 ,set1.EX_CURR,1)  = 0  then BANKNOTE_DENOM else  '' end  denomRate " + "    ,trim(decode(SIGHT_BILL_RATE,null,'Unq',   case when  instr(SIGHT_BILL_RATE,'.',1) = 1 then '0'||SIGHT_BILL_RATE else SIGHT_BILL_RATE end  ) ) sightBillRate   "
				+ "    ,trim(decode(TT_RATE,null,'Unq',  case when  instr(TT_RATE,'.',1) = 1 then '0'||TT_RATE else  TT_RATE end  ))   ttRate " + "    ,trim(decode(SELLING_RATE,null,'Unq',  case when  instr(SELLING_RATE,'.',1) = 1 then '0'||SELLING_RATE else  SELLING_RATE end   ))   sellingRate  " + "    ,trim(decode(BANKNOTE_BUYING,null,'Unq', case when  instr(BANKNOTE_BUYING,'.',1) = 1 then '0'||BANKNOTE_BUYING else BANKNOTE_BUYING end   )) bnBuyingRate "
				+ "    ,trim(decode(BANKNOTE_SELLING,null,'Unq', case when  instr(BANKNOTE_SELLING,'.',1) = 1 then '0'||BANKNOTE_SELLING else BANKNOTE_SELLING end   )) bnSellingRate  " + "    ,AVE_EXC_RATE " + "   , UPDATE_DATE " + "   ,ORDERNO " + "    " + "  from ( " + "      select ex_date, ex_curr,  ex_type,   ex_round,   banknote_denom " + "      ,   char(sight_bill_rate) sight_bill_rate,         char(tt_rate) tt_rate,         char(selling_rate) selling_rate "
				+ "      ,   char(banknote_buying) banknote_buying,         char(banknote_selling) banknote_selling,         char(ave_exc_rate) ave_exc_rate " + "      ,   cr.update_date    " + "      ,   cc.currency_name " + "      , (select instr(cor.cfg_value1,ex_curr,1) from SYSIBM.SYSDUMMY1 ) as orderno " + "        " + "      from excdba.counter_rate   cr " + "      inner join excdba.currency cc on cc.currency_code = cr.ex_curr " + "      left join ( " + "               select cfg_value1 from excdba.config_exchrate  where cfg_grp = 'CCY_ORDER' " + "                )  cor on 1 = 1 "
				+ "       " + "      where 1=1   " +

				" " + date_str + round_str +

				"      and ex_type != 'B' " + "      and cc.flag_ct = 'Y' " + "       " + "      order by trunc(ex_date) desc, ex_date, ex_round, ex_curr, ex_type  " + "    ) set1 "

				+ "  left join (select cfg_value1 from excdba.config_exchrate  where CFG_KEY = 'EXC_WS_NO_DISPLAY') set3 on 1=1  " + "  where  instr(set3.cfg_value1 ,set1.EX_CURR||set1.EX_TYPE,1)  = 0 "

				+ " order by ORDERNO ,ex_date, ex_round, ex_curr, ex_type ";

		try {

			// get exchange list
			itemList = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<ExCounterRateItem>(ExCounterRateItem.class));

			// get round List
			sql = "  select  distinct to_char(ex_date,'dd/mm/yyyy HH24:MI') datetime , to_char(ex_date,'HH24:MI') time ,ex_round round " + " from   excdba.counter_rate where 1=1 " + listround_str + " and ex_type != 'B'  " + " order by ex_round  desc";

			roundList = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<ExRoundItem>(ExRoundItem.class));

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		// set title
		ExCounterRateItem title = new ExCounterRateItem();
		if (itemList != null && itemList.size() > 0) {
			title = itemList.get(0);
			exCounterRate.setDate(title.getExdate());
			exCounterRate.setTime(title.getTime());
			exCounterRate.setRound(title.getRound());
		} else {
			if (req.getDate() != null) {
				try {
					exCounterRate.setDate(ddMMyyyyhh.format(ddMMyyyy.parse(req.getDate())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		}

		exCounterRate.setItemList(itemList);
		exCounterRate.setRoundList(roundList);

		res.setExCounterRate(exCounterRate);

		Gson gson = new Gson();
		result = gson.toJson(res);
		result = StringEscapeUtils.escapeHtml(result);

		return result;
	}
}
