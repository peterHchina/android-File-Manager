package com.example.managerfile.cache.wrap;

public abstract class Wrap<T> {

	private int hit = 0;

	private long used = Long.MIN_VALUE;

	private boolean isLocalCached = false;

	public int getHit() {
		return hit;
	}

	public abstract T getWrap();

	public void hit() {
		hit++;
		used = System.currentTimeMillis();
	}

	public boolean isLocalCached() {
		return isLocalCached;
	}

	public void setHit(int paramInt) {
		hit = paramInt;
	}

	public void setLocalCached(boolean paramBoolean) {
		isLocalCached = paramBoolean;
	}

	public long getLastUsed() {
		return used;
	}

	public abstract void setWrap(T paramT);
}
