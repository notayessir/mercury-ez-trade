package com.notayessir.quote.api.spot.mq;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class QuoteUpdatedEvent implements Serializable {

    private Long requestId;

    private QTickRecordDTO qTickRecord;

    private List<QOrderBookDTO> qOrderBooks;

    private List<QKLineDTO> qKLines;


}
