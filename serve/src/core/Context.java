package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
	private List<Entity> entitys = null;
	private List<Mapping> mappings = null;
	
	private Map<String, String> entityMap = new HashMap<String ,String>();
	private Map<String, String> mappingMap = new HashMap<String ,String>();
	public Context(List<Entity> entitys, List<Mapping> mappings) {
		this.entitys = entitys;
		this.mappings = mappings;
		for(Entity entity : entitys) {
			entityMap.put(entity.getName(), entity.getClz());
		}
		for(Mapping mapping : mappings) {
			for(String pattern : mapping.getPattern()) {
				mappingMap.put(pattern, mapping.getName());
			}
		}
	}
	
	public String getClz(String pattern) {
		
		String name = mappingMap.get(pattern);
		String Clz = entityMap.get(name);
		return Clz;
	}
}
