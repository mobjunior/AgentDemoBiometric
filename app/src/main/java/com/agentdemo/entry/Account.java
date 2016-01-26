package com.agentdemo.entry;

import java.io.Serializable;

public class Account
  implements Serializable
{
  private String account = null;
  private double amount = 0.0D;
  private String fingerprint = null;
  private String name = null;
  private String password = null;
  private String phoneNumber = null;
  
  public Account() {}
  
  public Account(String paramString1, String paramString2, String paramString3, double paramDouble, String paramString4, String paramString5)
  {
    this.account = paramString1;
    this.name = paramString2;
    this.phoneNumber = paramString3;
    this.amount = paramDouble;
    this.password = paramString4;
    this.fingerprint = paramString5;
  }
  
  public String getAccount()
  {
    return this.account;
  }
  
  public double getAmount()
  {
    return this.amount;
  }
  
  public String getFingerprint()
  {
    return this.fingerprint;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public String getPhoneNumber()
  {
    return this.phoneNumber;
  }
  
  public void setAccount(String paramString)
  {
    this.account = paramString;
  }
  
  public void setAmount(double paramDouble)
  {
    this.amount = paramDouble;
  }
  
  public void setFingerprint(String paramString)
  {
    this.fingerprint = paramString;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public void setPhoneNumber(String paramString)
  {
    this.phoneNumber = paramString;
  }
}


/* Location:              E:\backup\App\dex2jar-2.0(1)\dex2jar-2.0\classes-dex2jar.jar!\com\nbbse\printerdemo\entry\Account.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */