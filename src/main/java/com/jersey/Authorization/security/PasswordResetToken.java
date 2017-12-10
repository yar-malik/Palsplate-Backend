package com.jersey.Authorization.security;

import java.util.Calendar;
import java.util.Date;

public class PasswordResetToken{

    private static final int EXPIRATION_TIME = 60 * 24;

    private String principal;
    private String resetToken;
    private Date expiryDate;


    public PasswordResetToken(String principal, String resetToken) {

        this.principal = principal;
        this.resetToken = resetToken;
        this.expiryDate = calculateExpiryDate();
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    private Date calculateExpiryDate() {

        final Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(new Date().getTime());

        cal.add(Calendar.MINUTE, EXPIRATION_TIME);

        return new Date(cal.getTime().getTime());

    }

    @Override

    public String toString() {

        final StringBuilder builder = new StringBuilder();

        builder.append("Token [String=").append(resetToken).append("]").append("[Expires").append(expiryDate).append("]");

        return builder.toString();

    }
}