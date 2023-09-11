package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.SmsCode;
import com.example.MyBookShopApp.data.storage.SmsCodeRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class SmsService {

    @Value("${twilio.ACCOUNT_SID}")
    private String ACCOUNT_SID;

    @Value("${twilio.ACCOUNT_SID}")
    private String AUTH_TOKEN;

    @Value("${twilio.ACCOUNT_SID}")
    private String TWILIO_NUMBER;

    @Value("${sms-ru.Account.id}")
    private String SMS_ID;
    private final SmsCodeRepository smsCodeRepository;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository) {
        this.smsCodeRepository = smsCodeRepository;
    }

    public String sendSecretCodeSMS(String  contact) throws URISyntaxException {
        String code = generateCode();
        String requestString = "https://sms.ru/sms/send?api_id=" +SMS_ID
                +"&to="+contact
                +"&msg=You+secret+code+is+"+code+"&json=1";
        RestTemplate template = new RestTemplate();
        URI uri = new URI(requestString);
        String result = template.getForObject(uri, String.class);
        return code;
    }
    public String sendTwilioSecretCodeSms(String contact) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = contact.replaceAll("[()-]]", "");
        String generatedCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(TWILIO_NUMBER),
                "Your secret code is: " + generatedCode
        ).create();
        return generatedCode;
    }

    public String generateCode() {
        //nnn nnn - pattern
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

    public void saveNewCode(SmsCode smsCode) {
        if (smsCodeRepository.findByCode(smsCode.getCode()) == null) {
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        Logger.getLogger(SmsService.class.getName()).info("verification sms code "+smsCode.toString());
        return (smsCode != null && !smsCode.isExpired());
    }
}