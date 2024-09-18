package com.cqrcb.cims.common.db.entity;

import com.cqrcb.cims.core.impl.dao.BaseEntity;

public class BaseControlledEntity extends BaseEntity {

    private boolean myself;

    private boolean myOrg;

    private boolean myOrgAndBranch;

    public boolean getMyself() {
        return myself;
    }

    public void setMyself(boolean myself) {
        this.myself = myself;
    }

    public boolean getMyOrg() {
        return myOrg;
    }

    public void setMyOrg(boolean myOrg) {
        this.myOrg = myOrg;
    }

    public boolean getMyOrgAndBranch() {
        return myOrgAndBranch;
    }

    public void setMyOrgAndBranch(boolean myOrgAndBranch) {
        this.myOrgAndBranch = myOrgAndBranch;
    }
}
