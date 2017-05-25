package com.bigdeep.dandelion.transformers;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by weiliyang on 25/05/2017.
 */
@Service(value = "date-transformer")
public class DateTransformer implements Transformer {
    private static final Logger logger = LoggerFactory.getLogger(DateTransformer.class);

    @Override
    public String perform(final String input) {
        String[] patterns = new String[]{
                "yyyy-MM-dd",
                "yyyy.MM.dd",
                "yyyy/MM/dd",
                "yyyyMMdd",
                "yyyy年MM月dd日",
                "yyyy年MM月dd",
                "yy年MM月dd日"
        };

        Date date = null;
        try {
            date = DateUtils.parseDate(input, patterns);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date == null ? null : DateFormatUtils.format(date, "yyyy-MM-dd");
    }
}
