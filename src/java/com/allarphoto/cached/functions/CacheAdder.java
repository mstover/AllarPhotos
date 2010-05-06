package com.lazerinc.cached.functions;

public interface CacheAdder<T> {
	public void add(T obj);

	public void update(T obj);

	public void delete(T obj);
}
