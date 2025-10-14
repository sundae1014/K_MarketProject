package com.example.demo.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COUPON")
public class COUPONEntity {

	@Id
	@Column(name="COUPON_NO")
	private int couponNo;

	@Column(name="COUPON_TYPE")
	private int couponType;

	@Column(name="BENEFIT")
	private String benefit;

	@Column(name="USE_PERIOD")
	private String usePeriod;

	@Column(name="ISSUER")
	private String issuer;

	@Column(name="ISSUE_DATE")
	private Date issueDate;

	@Column(name="USE_COUNT")
	private int useCount;

	@Column(name="STATUS")
	private String status;

	@Column(name="MANAGE")
	private String manage;

	@Column(name="BENEFIT2")
	private String benefit2;

	
	public COUPONEntity() {}
	
	public int getCouponNo() { return couponNo; }
	public void setCouponNo(int couponNo) { this.couponNo = couponNo; }

	public int getCouponType() { return couponType; }
	public void setCouponType(int couponType) { this.couponType = couponType; }

	public String getBenefit() { return benefit; }
	public void setBenefit(String benefit) { this.benefit = benefit; }

	public String getUsePeriod() { return usePeriod; }
	public void setUsePeriod(String usePeriod) { this.usePeriod = usePeriod; }

	public String getIssuer() { return issuer; }
	public void setIssuer(String issuer) { this.issuer = issuer; }

	public Date getIssueDate() { return issueDate; }
	public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

	public int getUseCount() { return useCount; }
	public void setUseCount(int useCount) { this.useCount = useCount; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getManage() { return manage; }
	public void setManage(String manage) { this.manage = manage; }

	public String getBenefit2() { return benefit2; }
	public void setBenefit2(String benefit2) { this.benefit2 = benefit2; }
	
	
}
