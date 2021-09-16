package com.superhao.base.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * @Auther: super
 * @Date: 2019/10/17 18:44
 * @email:
 */
public class PhoneUtils {
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();

    private static final Integer CN = 86;
    /**
     * check phone
     *
     * @param phoneNumber eg" 18012345678"
     * @param countryCode eg "86"
     * @return the result "true" or "false"
     */
    public static boolean checkPhoneNumber(String phoneNumber, Integer countryCode) {
        if(StringUtils.isEmpty(countryCode)){
            countryCode = CN;
        }
        long phone = Long.parseLong(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phone);
        return phoneNumberUtil.isValidNumber(pn);
     }

    /**
     * check phone that it brings country code
     *
     * @param phoneNumber eg" +8618012345678"
     * @return the result "true" or "false"
     * @throws NumberParseException handle phone that it can't resolve
     */
    public static boolean checkPhoneNumberBringCountryCode(String phoneNumber) throws NumberParseException {
        Phonenumber.PhoneNumber cn = phoneNumberUtil.parse(phoneNumber, "CN");
        return phoneNumberUtil.isValidNumber(cn);
    }


    /**
     * 根据国家代码和手机号  判断手机运营商
     * @date 2017-4-26 上午11:30:18
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static String getCarrier(String phoneNumber, Integer countryCode){

        long phone = Long.parseLong(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }


    /**
     *
     * @Description: 根据国家代码和手机号  手机归属地
     * @date 2017-4-26 上午11:33:18
     * @param phoneNumber
     * @param countryCode
     * @return    参数
     */
    public static String getGeo(String phoneNumber, Integer countryCode){

        long phone = Long.parseLong(phoneNumber);

        Phonenumber.PhoneNumber pn = new  Phonenumber.PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phone);

        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }

    public static void main(String[] args) {
      // System.out.println(""+PhoneUtils.checkPhoneNumber("12345678910",null));
    }
}
