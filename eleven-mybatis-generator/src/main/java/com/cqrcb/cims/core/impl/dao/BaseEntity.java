package com.cqrcb.cims.core.impl.dao;

public class BaseEntity
{
  private boolean forUpdate;
  
  public boolean isForUpdate()
  {
    return this.forUpdate;
  }
  
  public void setForUpdate(boolean forUpdate)
  {
    this.forUpdate = forUpdate;
  }
}
