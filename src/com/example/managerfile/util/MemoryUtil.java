
package com.example.managerfile.util;


/**
 * @author yangchun
 * @date 2012-3-24
 * @description Utility for look up and trace memory information
 *
 */
public class MemoryUtil {
	
    public static final int REMAIN_MEMORY_LEVEL_1 = 1024*1024; //1M

    public static long getAvailableNativeMemorySize() {
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        return maxMem - totalMem;
    }

    public static long getJavaAvaiableMemorySize() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getJavaFreeMemorySize() {
        return Runtime.getRuntime().freeMemory();
    }

    public static boolean isNeedReclaim() {
        if (getJavaFreeMemorySize()>REMAIN_MEMORY_LEVEL_1) {
			return true;
		}
        return false;
    }
    
    public static void gc(){
    	Runtime.getRuntime().gc();
    }

    public static void trace() {
    	StringBuilder builder = new StringBuilder();
    	builder.delete(0, builder.length());
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        builder.append("SNS memory information{");
        builder.append("MaxMemorySize:");
        builder.append(maxMem/1024);
        builder.append("KB-----");
        builder.append("TotalMemorySize:");
        builder.append(totalMem/1024);
        builder.append("KB-----");
        builder.append("FreeMemorySize:");
        builder.append(freeMem/1024);
        builder.append("KB");
        builder.append("}");
     
    }
}
