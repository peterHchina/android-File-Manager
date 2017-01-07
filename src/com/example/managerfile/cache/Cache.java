package com.example.managerfile.cache;



public abstract interface Cache {
	/**
	 * @description Clear memory cache
	 */
    public abstract void clear();
    
    /**
     * @description Flush memory cache to local disk
     */
    public abstract void flush();

    /**
     * @description Reclaim memory cache, this is often called when low
     * 				memory happened
     * @param paramReclaimLevel
     * @return true if cache is reclaimed successfully, otherwise false
     */
    public abstract boolean reclaim(ReclaimLevel paramReclaimLevel);
    
    /**
     * @description Destroy native disk cache
     */
    public abstract void destroy();
}
