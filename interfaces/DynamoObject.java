package com.cavegaming.tournamastercore.interfaces;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DynamoObject {	
	
	public DynamoObject() {}
	
	public DynamoObject(JSONObject jo) {
		Class<?> act = this.getClass();
		Field[] class_fields = act.getDeclaredFields();
		for(Field field: class_fields) {
			String name = field.getName();
			if(jo.has(name)){
				Object o = null;
				try {
					o = jo.get(name);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(o instanceof JSONObject) {
					o = processJSONObject((JSONObject) o);
				}else if (o instanceof JSONArray) {
					o = processJSONArray((JSONArray) o);
				}
				try {
					field.setAccessible(true);
					field.set(this, o);
					field.setAccessible(false);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private Map<Object, Object> processJSONObject(JSONObject jo){
		Map<Object, Object> map = new HashMap<Object, Object>();
		Iterator keys = ((JSONObject)jo).keys();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			Object value = null;
			try {
				value = jo.get(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(value instanceof JSONObject) {
				map.put(key, processJSONObject((JSONObject)value));
			}else if(value instanceof JSONArray){
				map.put(key, processJSONArray((JSONArray)value));
			}else {
				try {
					map.put(key, jo.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	private Set<Object> processJSONArray(JSONArray ja){
		Set<Object> set = new HashSet<Object>();
		
		for(int i = 0; i < ja.length(); i++) {
			Object o = null;
			try {
				o = ja.get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(o instanceof JSONObject) {
				set.add(processJSONObject((JSONObject) o));
			}else if(o instanceof JSONArray) {
				set.add(processJSONArray((JSONArray) o));
			}else {
				set.add(o);
			}
		}
		
		return set;
	}
	
	public JSONObject toJSONObject() {
		Map<String, Object> values = new HashMap<String, Object>();
		Class<?> act = this.getClass();
		Field[] class_fields = act.getDeclaredFields();
		for(Field field: class_fields) {
			String name = field.getName();
			Object val = null;
			field.setAccessible(true);
			try {
				val = field.get(this);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(val != null) {
				values.put(name, val);
			}
			field.setAccessible(false);
		}
		return new JSONObject(values);
	} 
	/*
	Comparator<ObjectInterface> compareById = (ObjectInterface t1, ObjectInterface t2) -> 
    new Long(t1.getId()).compareTo( new Long(t2.getId()) );
	*/
	
    //compares two ObjectInterfaces by converting them to JSONObjects and matching them
	public <T extends DynamoObject> boolean equals(T compare) {
		JSONObject jo1 = this.toJSONObject();
		JSONObject jo2 = compare.toJSONObject();
		if(jo1.equals(jo2)) return true;
		
		return false;
	}

	public String toString() {
		return this.toJSONObject().toString();
	}
	
	/*
	//Generates a DBMap for this class
	default <T extends ObjectInterface> void generateDBMap(){
		Class<?> act = this.getClass();
	
		Field[] class_fields = act.getDeclaredFields();
	    
        String fileName = "src/main/java/com/cavegaming/tournamasterbackend/db_map/" + act.getSimpleName() + ".xml";
        FileWriter fw = null;
        try {
        	fw = new FileWriter(new File(fileName));

        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    try {
			//headers
	    	fw.write("<?xml version = \"1.0\" encoding = \"UTF-8\"?>" + "\n\n");
			fw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">" + "\n\n");
			
			fw.write("<mapper namespace = \""+ act.getSimpleName() + "\">\n\n");			
			
			//insert statement
			fw.write("\t<insert id = \"insert\" parameterType = \"" + act.getSimpleName() + "\">\n");
			fw.write("\t\tINSERT INTO " + English.plural(act.getSimpleName()).toLowerCase() + " (");
			boolean first = true;
			for(Field f : class_fields) {
				if(first) {
					first = false;
				}else {
					fw.write(", ");
				}
				fw.write(f.getName());
			}
			fw.write(")\n");
			fw.write("\t\tVALUES (");
			first = true;
			for(Field f : class_fields) {
				if(first) {
					first = false;
				}else {
					fw.write(", ");
				}
				fw.write("#{");
				fw.write(f.getName());
				fw.write("}");
			}
			fw.write(")\n");
			fw.write("\t</insert>\n\n");
			
			//update statement
			fw.write("\t<update id = \"update\" parameterType = \""+ act.getSimpleName() + "\">\n");
			fw.write("\t\tUPDATE "+ English.plural(act.getSimpleName()).toLowerCase() + " SET ");
			first = true;
			for(Field f : class_fields) {
				if(f.getName().equals("id")) continue;
				if(first) {
					first = false;
				}else {
					fw.write(", ");
				}
				fw.write(f.getName());
				fw.write(" = ");
				fw.write("#{");
				fw.write(f.getName());
				fw.write("}");
			}
			fw.write("\n");
			fw.write("\t\tWHERE id = #{id}\n");
			fw.write("\t</update>\n\n");
			
			//delete statement
			fw.write("\t<delete id = \"delete\" parameterType = \"long\">\n");
			fw.write("\t\tDELETE FROM " + English.plural(act.getSimpleName()).toLowerCase() + " WHERE id = #{id}\n");
			fw.write("\t</delete>\n\n");
			
			//singleton select statement
			fw.write("\t<select id=\"get\" parameterType = \"long\" resultMap=\""+ act.getSimpleName().toLowerCase() + "Map\">\n");
			fw.write("\t\tSELECT * FROM " + English.plural(act.getSimpleName()).toLowerCase() + " where id = #{id};\n");
			fw.write("\t</select>\n\n");			
			
			//set select statement
			fw.write("\t<select id=\"getSet\" parameterType = \"long\" resultMap=\""+ act.getSimpleName().toLowerCase() + "Map\">\n");
			fw.write("\t\tSELECT * FROM " + English.plural(act.getSimpleName()).toLowerCase() + " where id IN\n");
			fw.write("\t\t<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\n");
			fw.write("\t\t\t#{item}\n");
			fw.write("\t\t</foreach>\n");
			fw.write("\t</select>\n\n");

			//select all statement
			fw.write("\t<select id=\"getAll\" resultMap=\"" + act.getSimpleName().toLowerCase() + "Map\">\n");
			fw.write("\t\tSELECT * FROM " + English.plural(act.getSimpleName()).toLowerCase() + "\n" );
			fw.write("\t</select>\n\n");
			
			//result mapping
			fw.write("\t<resultMap id = \"" + act.getSimpleName().toLowerCase() + "Map\" type = \""+ act.getSimpleName() + "\">\n");
			for(Field f: class_fields) {
				fw.write("\t\t<result property = ");
				fw.write("\"" + f.getName().toLowerCase() + "\"");
				fw.write(" column = ");
				fw.write("\"" + f.getName().toUpperCase() + "\"");
				fw.write("/>\n");
			}
			fw.write("\t</resultMap>\n\n");
			
			
			fw.write("</mapper>");
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	//Generates a junction DB Map for this class
	default <T extends ObjectInterface> void generateDBJunctionMap(){
		Class<?> act = this.getClass();
		
		Field[] class_fields = act.getDeclaredFields();
        
        String[] comps = fetchJunctionComponents();

        String fileName = "src/main/java/com/cavegaming/tournamasterbackend/db_map/" + act.getSimpleName() + ".xml";
        FileWriter fw = null;
        try {
        	fw = new FileWriter(new File(fileName));

			//headers
	    	fw.write("<?xml version = \"1.0\" encoding = \"UTF-8\"?>" + "\n\n");
			fw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">" + "\n\n");
			
			fw.write("<mapper namespace = \""+ act.getSimpleName() + "\">\n\n");			
			
			//insert statement
			fw.write("\t<insert id = \"insert\" parameterType = \"" + act.getSimpleName() + "\">\n");
			fw.write("\t\tINSERT INTO " + fetchTableName());
			
			
			fw.write(" (");
			boolean is_first = true;
			for(Field f : class_fields) {
				if(is_first) {
					is_first = false;
				}else {
					fw.write(", ");
				}
				fw.write(f.getName());
			}
			fw.write(")\n");
			fw.write("\t\tVALUES ("); 
			is_first = true;
			for(Field f : class_fields) {
				if(is_first) {
					is_first = false;
				}else {
					fw.write(", ");
				}
				fw.write("#{");
				fw.write(f.getName());
				fw.write("}");
			}
			fw.write(")\n");
			fw.write("\t</insert>\n\n");
			
			//delete statement
			fw.write("\t<delete id = \"delete\" parameterType = \"" + act.getSimpleName() + "\">\n");
			fw.write("\t\tDELETE FROM " + fetchTableName() + " WHERE ");
			is_first = true;
			for(int i = 0; i < comps.length; i ++) {
				if(is_first) {
					is_first = false;
				}else {
					fw.write(" AND ");
				}
				
				fw.write(comps[i].toLowerCase() + " = #{" + comps[i].toLowerCase() + "}");
			}
			fw.write("\n");
			
			fw.write("\t</delete>\n\n");
			
			//select statement by each criteria
			for(int i = 0; i < comps.length; i++) {
				fw.write("\t<select id=\"getBy" + comps[i] + "\" parameterType = \"long\" resultMap=\""+ act.getSimpleName() + "Map\">\n");
				fw.write("\t\tSELECT * FROM " + fetchTableName() + " WHERE " + comps[i].toLowerCase() + " = #{" + comps[i].toLowerCase() + "}\n");
				fw.write("\t</select>\n\n");	
			}

			//select all statement
			fw.write("\t<select id=\"getAll\" resultMap=\"" + act.getSimpleName() + "Map\">\n");
			fw.write("\t\tSELECT * FROM " + fetchTableName() + "\n" );
			fw.write("\t</select>\n\n");
			
			//result mapping
			fw.write("\t<resultMap id = \"" + act.getSimpleName() + "Map\" type = \""+ act.getSimpleName() + "\">\n");
			for(Field f: class_fields) {
				fw.write("\t\t<result property = ");
				fw.write("\"" + f.getName().toLowerCase() + "\"");
				fw.write(" column = ");
				fw.write("\"" + f.getName().toUpperCase() + "\"");
				fw.write("/>\n");
			}
			fw.write("\t</resultMap>\n\n");
			
			
			fw.write("</mapper>");
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Grabs the name of the table 
	default String fetchTableName() {
		String[] comps = fetchJunctionComponents();
		
        String tablename = "";
        boolean first = true;
        for(int i = 0; i < comps.length; i++) {
        	if(first) {
        		first = false;
        	}else {
        		tablename += "_";
        	}
        	
        	if(i < comps.length-1) {
        		tablename += comps[i].toLowerCase();
        	}else {
        		tablename += English.plural(comps[i]).toLowerCase();
        	}
        }
        
        return tablename;
	}
	
	//Gets the names of the junction components of the object
	default String[] fetchJunctionComponents() {
		Class<?> act = this.getClass();
		
        char[] charArray = act.getSimpleName().toCharArray();
        ArrayList<Integer> upArr = new ArrayList<Integer>();
        
        for(int i=0; i < charArray.length; i++){          
            if(Character.isUpperCase( charArray[i] )) {
            	upArr.add(i);
            }
        }
		
        int[] up = new int[upArr.size()];
        for(int i=0; i < up.length; i++) {
        	up[i] = upArr.get(i);
        }
        
        String[] comps = new String[up.length];
        for(int i=0; i < up.length; i++) {
        	if(i < up.length-1) {
        		comps[i] = act.getSimpleName().substring(up[i], up[i+1]);
        	}else {
        		comps[i] = act.getSimpleName().substring(up[i]);
        	}
        }
        
        return comps;
	}
	*/
	
	/*
	public String getId();
	
	public void setId(String id);
	*/
}
