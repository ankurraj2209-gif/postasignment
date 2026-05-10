package com.gaurav.postserviceassignment.service;

public interface GuardrailService {

    public void validateDepth(Integer depthlevel);

    public void validateHorizonalCap(Long postId);

    public void validateCoolDownCaplCap(Long botId, Long humanId);
}
