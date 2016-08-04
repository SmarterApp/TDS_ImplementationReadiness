package org.cresst.sb.irp.domain.automation;

import java.net.URI;
import java.net.URL;

public class AutomationRequest {
    public AutomationRequest() {}

    private String vendorName;
    private String tenantName;
    private String stateAbbreviation;
    private String district;
    private String institution;
    private URL oAuthUrl;
    private URL programManagementUrl;
    private String programManagementClientId;
    private String programManagementClientSecret;
    private String programManagementUserId;
    private String programManagementUserPassword;
    private URL testSpecBankUrl;
    private String testSpecBankUserId;
    private String testSpecBankPassword;
    private URL artUrl;
    private String artUserId;
    private String artPassword;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public URL getoAuthUrl() {
        return oAuthUrl;
    }

    public void setoAuthUrl(URL oAuthUrl) {
        this.oAuthUrl = oAuthUrl;
    }

    public URL getProgramManagementUrl() {
        return programManagementUrl;
    }

    public void setProgramManagementUrl(URL programManagementUrl) {
        this.programManagementUrl = programManagementUrl;
    }

    public String getProgramManagementClientId() {
        return programManagementClientId;
    }

    public void setProgramManagementClientId(String programManagementClientId) {
        this.programManagementClientId = programManagementClientId;
    }

    public String getProgramManagementClientSecret() {
        return programManagementClientSecret;
    }

    public void setProgramManagementClientSecret(String programManagementClientSecret) {
        this.programManagementClientSecret = programManagementClientSecret;
    }

    public String getProgramManagementUserId() {
        return programManagementUserId;
    }

    public void setProgramManagementUserId(String programManagementUserId) {
        this.programManagementUserId = programManagementUserId;
    }

    public String getProgramManagementUserPassword() {
        return programManagementUserPassword;
    }

    public void setProgramManagementUserPassword(String programManagementUserPassword) {
        this.programManagementUserPassword = programManagementUserPassword;
    }

    public URL getTestSpecBankUrl() {
        return testSpecBankUrl;
    }

    public void setTestSpecBankUrl(URL testSpecBankUrl) {
        this.testSpecBankUrl = testSpecBankUrl;
    }

    public String getTestSpecBankUserId() {
        return testSpecBankUserId;
    }

    public void setTestSpecBankUserId(String testSpecBankUserId) {
        this.testSpecBankUserId = testSpecBankUserId;
    }

    public String getTestSpecBankPassword() {
        return testSpecBankPassword;
    }

    public void setTestSpecBankPassword(String testSpecBankPassword) {
        this.testSpecBankPassword = testSpecBankPassword;
    }

    public URL getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(URL artUrl) {
        this.artUrl = artUrl;
    }

    public String getArtUserId() {
        return artUserId;
    }

    public void setArtUserId(String artUserId) {
        this.artUserId = artUserId;
    }

    public String getArtPassword() {
        return artPassword;
    }

    public void setArtPassword(String artPassword) {
        this.artPassword = artPassword;
    }
}
