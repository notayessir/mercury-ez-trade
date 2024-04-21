package com.notayessir.user.order.service;

import com.notayessir.user.order.entity.ClinchSequence;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IClinchSequenceService extends IService<ClinchSequence> {

    void createClinchSequence(ClinchSequence clinchSequence);


    long countClinchSequence(Long globalSequence);
}
