package ru.neoflex.gateway.client.deal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;

import java.util.UUID;

@FeignClient(name = "deal", url = "${rest.deal.host}")
public interface DealApi {

    @PostMapping(value = "${rest.deal.method.finish-registration}")
    void finishRegistration(@RequestBody FinishRegistrationRequestDto dto,
                            @PathVariable String statementId);

    @PostMapping(value = "${rest.deal.method.send-document}")
    void sendDocument(@PathVariable UUID statementId);

    @PostMapping(value = "${rest.deal.method.sign-document}")
    void signDocument(@PathVariable UUID statementId, @RequestParam boolean isSigned);

    @PostMapping(value = "${rest.deal.method.verify-ses-code}")
    void verifyCode(@PathVariable UUID statementId, @RequestParam String code);
}