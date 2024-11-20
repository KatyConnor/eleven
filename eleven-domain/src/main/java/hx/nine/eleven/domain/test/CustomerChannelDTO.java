package hx.nine.eleven.domain.test;

import hx.nine.eleven.domain.obj.dto.HeaderDTO;

public class CustomerChannelDTO extends HeaderDTO {
    /**
     * 处理步骤
     */
    private String procedureStep;
    /**
     * 源用户ID
     */
    private boolean sourceUserId;
    /**
     * 源用户ID
     */
    private int sourceAppId;

    private DemoDTO demoDTO;

    public CustomerChannelDTO() {
    }

    public CustomerChannelDTO(String procedureStep, boolean sourceUserId, int sourceAppId) {
        this.procedureStep = procedureStep;
        this.sourceUserId = sourceUserId;
        this.sourceAppId = sourceAppId;
    }

    public String getProcedureStep() {
        return procedureStep;
    }

    public void setProcedureStep(String procedureStep) {
        this.procedureStep = procedureStep;
    }

    public boolean isSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(boolean sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public int getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(int sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public DemoDTO getDemoDTO() {
        return demoDTO;
    }

    public void setDemoDTO(DemoDTO demoDTO) {
        this.demoDTO = demoDTO;
    }
}
