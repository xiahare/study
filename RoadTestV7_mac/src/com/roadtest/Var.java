package com.roadtest;

public class Var {
    String url = "https://icbc.com/roadtest";
    String loginUrl = "https://onlinebusiness.icbc.com/webdeas-ui/login;type=driver";
    String improve_xpath = "//a[@class='ssPopup_dialog_cancel']";
    String book_online_xpath = "//a[text()='Book online']";
    String next_button_xpath = "//span[contains(text(),'Next')]/..";
    String last_name_xpath = "//input[@formcontrolname='drvrLastName']";
    String licence_Number_xpath = "//input[@formcontrolname='licenceNumber']";
    String keyword_xpath = "//input[@formcontrolname='keyword']";
    String agree_checkbox_xpath = "//div/input[@type='checkbox']/..";
    String signin_button_xpath = "//span[contains(text(),'Sign in')]/..";
    String reschedule_appointment_xpath = "//span[contains(text(),'Reschedule appointment')]/..";
    String reschedule_confirm_xpath = "//span[contains(text(),'Yes')]/..";
    String location_xpath = "//div/input[@type='text']";
    String address_xpath = "//span[normalize-space(text())='address']/..";
    String search_xpath = "//span[contains(.,'Search')]/..";
    String nearby_location_xpath = "//div[normalize-space(text())='nearby_location']/..";
    String date_title_xpath = "//div[@class='date-title']";
    String appoinment_xpath = "//div[@class='appointment-listings']/mat-button-toggle[1]";
    String review_appoinment_xpath = "//span[normalize-space(text())='Review Appointment']/..";
    String review_next_button_xpath = "//span[normalize-space(text())='Next']/..";
    //String sms_verification_xpath = "//label[contains(text(),'SMS (send verification code to')]/..";
    String sms_verification_xpath = "//input[@value='SNS']";
    String email_verification_xpath = "//input[@value='Email']";
    String send_button_xpath = "//span[normalize-space(text())='Send']/..";
}
