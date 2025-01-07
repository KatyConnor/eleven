package hx.nine.eleven.domain.obj.po;

import com.github.f4b6a3.ulid.UlidCreator;
import hx.nine.eleven.commons.utils.DateUtils;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description 数据库映射类 Project Object
 * @Author wml
 * @Date 2019-02-15
 */
public abstract class BasePO<E> implements Serializable {
    // 公共信息部分
    private E id;
    private String createTime;
    private String updateTime;
    private Long version;
    private Boolean effective;

    public BasePO(){
        try{
            String typeName = ((ParameterizedTypeImpl)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            if ("java.lang.String".equals(typeName)){
                this.id = (E) UlidCreator.getUlid().toString();
            }
        }catch (Exception e){
            ElevenLoggerFactory.build(this).error("生成[id]异常",e);
        }
        this.createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.version = 1L;
        this.effective = true;

    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getEffective() {
        return effective;
    }

    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

    public void update(){
        this.updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.version += 1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public  boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null){
            return false;
        }

        if (getClass() != obj.getClass()){
            return false;
        }

        if (this.id == null && ((BasePO)obj).id != null) {
            return false;
        } else if (this.id != null && !this.id.equals(((BasePO)obj).id)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
