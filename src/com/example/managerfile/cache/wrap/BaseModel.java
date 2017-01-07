
package com.example.managerfile.cache.wrap;
import java.io.Serializable;

public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 988287172603888683L;


    protected int snsTypeId;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseModel)) return false;

        BaseModel that = (BaseModel) o;

        if (snsTypeId != that.snsTypeId) return false;


        return true;
    }



    public int hashCode() {
        return snsTypeId + snsTypeId * 31;
    }

 
}
