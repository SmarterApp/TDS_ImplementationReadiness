package org.cresst.sb.irp.domain.automation;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URL;
import java.util.Objects;

public class AutomationRequest {

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
    private URL proctorUrl;
    private String proctorUserId;
    private String proctorPassword;

    public AutomationRequest() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutomationRequest that = (AutomationRequest) o;
        return Objects.equals(vendorName, that.vendorName) &&
                Objects.equals(tenantName, that.tenantName) &&
                Objects.equals(stateAbbreviation, that.stateAbbreviation) &&
                Objects.equals(district, that.district) &&
                Objects.equals(institution, that.institution) &&
                Objects.equals(oAuthUrl, that.oAuthUrl) &&
                Objects.equals(programManagementUrl, that.programManagementUrl) &&
                Objects.equals(programManagementClientId, that.programManagementClientId) &&
                Objects.equals(programManagementClientSecret, that.programManagementClientSecret) &&
                Objects.equals(programManagementUserId, that.programManagementUserId) &&
                Objects.equals(programManagementUserPassword, that.programManagementUserPassword) &&
                Objects.equals(testSpecBankUrl, that.testSpecBankUrl) &&
                Objects.equals(testSpecBankUserId, that.testSpecBankUserId) &&
                Objects.equals(testSpecBankPassword, that.testSpecBankPassword) &&
                Objects.equals(artUrl, that.artUrl) &&
                Objects.equals(artUserId, that.artUserId) &&
                Objects.equals(artPassword, that.artPassword) &&
                Objects.equals(proctorUrl, that.proctorUrl) &&
                Objects.equals(proctorUserId, that.proctorUserId) &&
                Objects.equals(proctorPassword, that.proctorPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorName,
                tenantName,
                stateAbbreviation,
                district,
                institution,
                oAuthUrl,
                programManagementUrl,
                programManagementClientId,
                programManagementClientSecret,
                programManagementUserId,
                programManagementUserPassword,
                testSpecBankUrl,
                testSpecBankUserId,
                testSpecBankPassword,
                artUrl,
                artUserId,
                artPassword,
                proctorUrl,
                proctorUserId,
                proctorPassword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("vendorName", vendorName)
                .append("oAuthUrl", oAuthUrl)
                .toString();
    }

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

    public URL getProctorUrl() {
        return proctorUrl;
    }

    public void setProctorUrl(URL proctorUrl) {
        this.proctorUrl = proctorUrl;
    }

    public String getProctorUserId() {
        return proctorUserId;
    }

    public void setProctorUserId(String proctorUserId) {
        this.proctorUserId = proctorUserId;
    }

    public String getProctorPassword() {
        return proctorPassword;
    }

    public void setProctorPassword(String proctorPassword) {
        this.proctorPassword = proctorPassword;
    }
}
